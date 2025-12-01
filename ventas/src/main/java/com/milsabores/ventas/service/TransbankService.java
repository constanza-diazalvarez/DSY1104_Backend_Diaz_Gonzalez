package com.milsabores.ventas.service;

import com.milsabores.ventas.dto.transbank.*;

/**
 * Interfaz del servicio de pagos con Transbank WebPay
 */
public interface TransbankService {

    /**
     * Inicia una transacción de pago en WebPay
     * @param buyOrder Número de orden
     * @param amount Monto a pagar
     * @param sessionId ID de sesión (opcional)
     * @return Datos para redirigir al formulario de pago
     */
    InitPaymentResponseDTO initTransaction(String buyOrder, int amount, String sessionId);

    /**
     * Confirma una transacción después del retorno de WebPay
     * @param token Token de la transacción
     * @return Resultado del pago
     */
    PaymentResultDTO commitTransaction(String token);

    /**
     * Obtiene el estado de una transacción
     * @param token Token de la transacción
     * @return Estado de la transacción
     */
    WebPayCommitResponse getTransactionStatus(String token);

    /**
     * Reversa una transacción (anulación)
     * @param token Token de la transacción
     * @param amount Monto a reversar
     * @return true si se reversó exitosamente
     */
    boolean refundTransaction(String token, int amount);
}
