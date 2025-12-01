package com.milsabores.ventas.service.impl;

import com.milsabores.ventas.config.TransbankProperties;
import com.milsabores.ventas.dto.transbank.*;
import com.milsabores.ventas.entity.Order;
import com.milsabores.ventas.entity.OrderStatus;
import com.milsabores.ventas.exception.OrderNotFoundException;
import com.milsabores.ventas.exception.TransbankException;
import com.milsabores.ventas.repository.OrderRepository;
import com.milsabores.ventas.service.TransbankService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementación del servicio de Transbank WebPay usando HTTP directo
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TransbankServiceImpl implements TransbankService {

    private final RestTemplate restTemplate;
    private final TransbankProperties transbankProperties;
    private final OrderRepository orderRepository;

    @Value("${transbank.environment:INTEGRATION}")
    private String environment;

    // Headers requeridos por la API de Transbank
    private static final String HEADER_COMMERCE_CODE = "Tbk-Api-Key-Id";
    private static final String HEADER_API_KEY = "Tbk-Api-Key-Secret";

    // Endpoints de la API WebPay Plus
    private static final String ENDPOINT_CREATE = "/rswebpaytransaction/api/webpay/v1.2/transactions";
    private static final String ENDPOINT_COMMIT = "/rswebpaytransaction/api/webpay/v1.2/transactions/{token}";
    private static final String ENDPOINT_STATUS = "/rswebpaytransaction/api/webpay/v1.2/transactions/{token}";
    private static final String ENDPOINT_REFUND = "/rswebpaytransaction/api/webpay/v1.2/transactions/{token}/refunds";

    @Override
    public InitPaymentResponseDTO initTransaction(String buyOrder, int amount, String sessionId) {
        log.info("Iniciando transacción WebPay - Orden: {}, Monto: {}", buyOrder, amount);

        try {
            // Construir el request
            WebPayCreateRequest request = WebPayCreateRequest.builder()
                    .buy_order(buyOrder)
                    .session_id(sessionId != null ? sessionId : "session-" + System.currentTimeMillis())
                    .amount(amount)
                    .return_url(transbankProperties.getReturnUrl())
                    .build();

            // Hacer la llamada HTTP
            HttpEntity<WebPayCreateRequest> entity = new HttpEntity<>(request, createHeaders());
            String url = getBaseUrl() + ENDPOINT_CREATE;

            log.debug("Llamando a Transbank: {} con body: {}", url, request);

            ResponseEntity<WebPayCreateResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    WebPayCreateResponse.class
            );

            WebPayCreateResponse body = response.getBody();
            if (body == null || body.getToken() == null) {
                throw new TransbankException("Respuesta inválida de Transbank");
            }

            log.info("Transacción iniciada exitosamente - Token: {}", body.getToken());

            // Construir la URL de redirección
            String redirectUrl = body.getUrl() + "?token_ws=" + body.getToken();

            return InitPaymentResponseDTO.builder()
                    .token(body.getToken())
                    .url(body.getUrl())
                    .redirectUrl(redirectUrl)
                    .buyOrder(buyOrder)
                    .build();

        } catch (HttpClientErrorException e) {
            log.error("Error HTTP al crear transacción: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new TransbankException("Error al comunicarse con Transbank: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error al iniciar transacción WebPay", e);
            throw new TransbankException("Error al iniciar pago: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public PaymentResultDTO commitTransaction(String token) {
        log.info("Confirmando transacción WebPay - Token: {}", token);

        try {
            // Hacer la llamada HTTP para confirmar
            HttpEntity<Void> entity = new HttpEntity<>(createHeaders());
            String url = getBaseUrl() + ENDPOINT_COMMIT.replace("{token}", token);

            ResponseEntity<WebPayCommitResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    entity,
                    WebPayCommitResponse.class
            );

            WebPayCommitResponse commitResponse = response.getBody();
            if (commitResponse == null) {
                throw new TransbankException("Respuesta vacía de Transbank");
            }

            log.info("Respuesta de confirmación - Orden: {}, Código: {}, Estado: {}",
                    commitResponse.getBuyOrder(),
                    commitResponse.getResponseCode(),
                    commitResponse.getStatus());

            // Actualizar la orden en la base de datos
            updateOrderFromCommit(commitResponse);

            // Construir respuesta
            return buildPaymentResult(commitResponse);

        } catch (HttpClientErrorException e) {
            log.error("Error HTTP al confirmar transacción: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            
            // Si es 422, la transacción ya fue confirmada o expiró
            if (e.getStatusCode() == HttpStatus.UNPROCESSABLE_ENTITY) {
                throw new TransbankException("La transacción ya fue procesada o ha expirado");
            }
            throw new TransbankException("Error al confirmar pago: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error al confirmar transacción WebPay", e);
            throw new TransbankException("Error al confirmar pago: " + e.getMessage());
        }
    }

    @Override
    public WebPayCommitResponse getTransactionStatus(String token) {
        log.info("Consultando estado de transacción - Token: {}", token);

        try {
            HttpEntity<Void> entity = new HttpEntity<>(createHeaders());
            String url = getBaseUrl() + ENDPOINT_STATUS.replace("{token}", token);

            ResponseEntity<WebPayCommitResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    WebPayCommitResponse.class
            );

            return response.getBody();

        } catch (HttpClientErrorException e) {
            log.error("Error al consultar estado: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new TransbankException("Error al consultar estado: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean refundTransaction(String token, int amount) {
        log.info("Reversando transacción - Token: {}, Monto: {}", token, amount);

        try {
            Map<String, Integer> requestBody = new HashMap<>();
            requestBody.put("amount", amount);

            HttpEntity<Map<String, Integer>> entity = new HttpEntity<>(requestBody, createHeaders());
            String url = getBaseUrl() + ENDPOINT_REFUND.replace("{token}", token);

            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            log.info("Reversión exitosa - Respuesta: {}", response.getBody());
            return response.getStatusCode().is2xxSuccessful();

        } catch (HttpClientErrorException e) {
            log.error("Error al reversar transacción: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            return false;
        }
    }

    /**
     * Crea los headers HTTP requeridos por Transbank
     */
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HEADER_COMMERCE_CODE, transbankProperties.getCommerceCode());
        headers.set(HEADER_API_KEY, transbankProperties.getApiKey());
        return headers;
    }

    /**
     * Obtiene la URL base según el ambiente configurado
     */
    private String getBaseUrl() {
        if ("PRODUCTION".equalsIgnoreCase(environment)) {
            return transbankProperties.getApiUrlProduction();
        }
        return transbankProperties.getApiUrlIntegration();
    }

    /**
     * Actualiza la orden en la base de datos con el resultado del pago
     */
    private void updateOrderFromCommit(WebPayCommitResponse response) {
        try {
            Order order = orderRepository.findByBuyOrder(response.getBuyOrder())
                    .orElseThrow(() -> new OrderNotFoundException("Orden no encontrada: " + response.getBuyOrder()));

            if (response.isApproved()) {
                order.setStatus(OrderStatus.APPROVED);
                order.setAuthorizationCode(response.getAuthorizationCode());
                if (response.getCardDetail() != null) {
                    order.setCardNumber(response.getCardDetail().getCardNumber());
                }
            } else {
                order.setStatus(OrderStatus.REJECTED);
                order.setErrorMessage("Código de respuesta: " + response.getResponseCode());
            }

            order.setTransactionDate(parseTransactionDate(response.getTransactionDate()));
            orderRepository.save(order);

            log.info("Orden actualizada - BuyOrder: {}, Estado: {}", response.getBuyOrder(), order.getStatus());

        } catch (OrderNotFoundException e) {
            log.warn("No se encontró la orden para actualizar: {}", response.getBuyOrder());
        }
    }

    /**
     * Construye el DTO de resultado del pago
     */
    private PaymentResultDTO buildPaymentResult(WebPayCommitResponse response) {
        boolean success = response.isApproved();

        return PaymentResultDTO.builder()
                .success(success)
                .message(success ? "Pago aprobado exitosamente" : "Pago rechazado")
                .buyOrder(response.getBuyOrder())
                .amount(response.getAmount())
                .authorizationCode(response.getAuthorizationCode())
                .cardNumber(response.getCardDetail() != null ? response.getCardDetail().getCardNumber() : null)
                .paymentType(response.getPaymentTypeCode())
                .installments(response.getInstallmentsNumber())
                .transactionDate(parseTransactionDate(response.getTransactionDate()))
                .responseCode(response.getResponseCode())
                .orderStatus(success ? "APPROVED" : "REJECTED")
                .build();
    }

    /**
     * Parsea la fecha de transacción de Transbank
     */
    private LocalDateTime parseTransactionDate(String dateString) {
        if (dateString == null) return LocalDateTime.now();
        try {
            return LocalDateTime.parse(dateString, DateTimeFormatter.ISO_DATE_TIME);
        } catch (Exception e) {
            log.warn("No se pudo parsear fecha de transacción: {}", dateString);
            return LocalDateTime.now();
        }
    }
}
