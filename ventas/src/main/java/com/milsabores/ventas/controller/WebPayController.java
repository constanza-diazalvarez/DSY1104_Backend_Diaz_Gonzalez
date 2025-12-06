package com.milsabores.ventas.controller;

import com.milsabores.ventas.dto.ApiResponseDTO;
import com.milsabores.ventas.dto.transbank.*;
import com.milsabores.ventas.service.TransbankService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para pagos con Transbank WebPay
 */
@RestController
@RequestMapping("/api/payments/webpay")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "WebPay", description = "API para pagos con Transbank WebPay Plus")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000", "https://mil-sabores-puce.vercel.app"})
public class WebPayController {

    private final TransbankService transbankService;

    @PostMapping("/init")
    @Operation(
            summary = "Iniciar pago",
            description = """
                    Inicia una transacción de pago con WebPay Plus.
                    
                    Retorna un token y una URL. El frontend debe redirigir al usuario a:
                    `redirectUrl` o `url + "?token_ws=" + token`
                    
                    Después del pago, Transbank redirigirá al usuario a la URL de retorno configurada
                    con el parámetro `token_ws` o `TBK_TOKEN`.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transacción iniciada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error al comunicarse con Transbank")
    })
    public ResponseEntity<ApiResponseDTO<InitPaymentResponseDTO>> initPayment(
            @Valid @RequestBody InitPaymentRequestDTO request) {

        log.info("Iniciando pago WebPay - Orden: {}, Monto: {}", request.getBuyOrder(), request.getAmount());

        InitPaymentResponseDTO response = transbankService.initTransaction(
                request.getBuyOrder(),
                request.getAmount(),
                request.getSessionId()
        );

        return ResponseEntity.ok(ApiResponseDTO.success("Transacción iniciada", response));
    }

    @PostMapping("/commit")
    @Operation(
            summary = "Confirmar pago",
            description = """
                    Confirma una transacción después de que el usuario regresa de WebPay.
                    
                    El frontend debe llamar a este endpoint con el `token_ws` recibido
                    en la URL de retorno para obtener el resultado del pago.
                    
                    **IMPORTANTE**: Este endpoint debe llamarse solo una vez por transacción.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago procesado (puede ser aprobado o rechazado)"),
            @ApiResponse(responseCode = "400", description = "Token inválido o transacción ya procesada"),
            @ApiResponse(responseCode = "500", description = "Error al comunicarse con Transbank")
    })
    public ResponseEntity<ApiResponseDTO<PaymentResultDTO>> commitPayment(
            @Parameter(description = "Token de la transacción (token_ws)", required = true)
            @RequestParam("token_ws") String token) {

        log.info("Confirmando pago WebPay - Token: {}", token);

        PaymentResultDTO result = transbankService.commitTransaction(token);

        String message = result.isSuccess() ? "Pago aprobado" : "Pago rechazado";
        return ResponseEntity.ok(ApiResponseDTO.success(message, result));
    }

    @GetMapping("/commit")
    @Operation(
            summary = "Confirmar pago (GET)",
            description = """
                    Endpoint alternativo para confirmar pago vía GET.
                    Útil cuando WebPay redirige directamente al backend.
                    """
    )
    public ResponseEntity<ApiResponseDTO<PaymentResultDTO>> commitPaymentGet(
            @Parameter(description = "Token de la transacción")
            @RequestParam(value = "token_ws", required = false) String tokenWs,
            @Parameter(description = "Token alternativo de Transbank")
            @RequestParam(value = "TBK_TOKEN", required = false) String tbkToken) {

        String token = tokenWs != null ? tokenWs : tbkToken;

        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Token no proporcionado"));
        }

        log.info("Confirmando pago WebPay (GET) - Token: {}", token);

        PaymentResultDTO result = transbankService.commitTransaction(token);

        String message = result.isSuccess() ? "Pago aprobado" : "Pago rechazado";
        return ResponseEntity.ok(ApiResponseDTO.success(message, result));
    }

    @GetMapping("/status/{token}")
    @Operation(
            summary = "Consultar estado de transacción",
            description = "Obtiene el estado actual de una transacción por su token"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado obtenido"),
            @ApiResponse(responseCode = "404", description = "Transacción no encontrada")
    })
    public ResponseEntity<ApiResponseDTO<WebPayCommitResponse>> getTransactionStatus(
            @Parameter(description = "Token de la transacción")
            @PathVariable String token) {

        log.info("Consultando estado de transacción - Token: {}", token);

        WebPayCommitResponse status = transbankService.getTransactionStatus(token);
        return ResponseEntity.ok(ApiResponseDTO.success("Estado obtenido", status));
    }

    @PostMapping("/refund/{token}")
    @Operation(
            summary = "Reversar transacción",
            description = """
                    Reversa (anula) una transacción previamente aprobada.
                    
                    Solo se pueden reversar transacciones del mismo día.
                    Para anulaciones posteriores, usar el portal de Transbank.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reversión procesada"),
            @ApiResponse(responseCode = "400", description = "No se pudo reversar la transacción")
    })
    public ResponseEntity<ApiResponseDTO<Void>> refundTransaction(
            @Parameter(description = "Token de la transacción")
            @PathVariable String token,
            @Parameter(description = "Monto a reversar")
            @RequestParam int amount) {

        log.info("Reversando transacción - Token: {}, Monto: {}", token, amount);

        boolean success = transbankService.refundTransaction(token, amount);

        if (success) {
            return ResponseEntity.ok(ApiResponseDTO.success("Transacción reversada exitosamente"));
        } else {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("No se pudo reversar la transacción"));
        }
    }

    /**
     * Endpoint para manejar el retorno de WebPay cuando el usuario cancela
     */
    @PostMapping("/abort")
    @Operation(
            summary = "Manejar pago abortado",
            description = "Endpoint llamado cuando el usuario cancela el pago en WebPay"
    )
    public ResponseEntity<ApiResponseDTO<Void>> handleAbort(
            @RequestParam(value = "TBK_TOKEN", required = false) String tbkToken,
            @RequestParam(value = "TBK_ORDEN_COMPRA", required = false) String buyOrder,
            @RequestParam(value = "TBK_ID_SESION", required = false) String sessionId) {

        log.info("Pago abortado por usuario - Orden: {}, Token: {}", buyOrder, tbkToken);

        return ResponseEntity.ok(ApiResponseDTO.success("Pago cancelado por el usuario"));
    }
}
