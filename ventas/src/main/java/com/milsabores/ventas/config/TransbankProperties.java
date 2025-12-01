package com.milsabores.ventas.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de propiedades de Transbank WebPay
 */
@Configuration
@ConfigurationProperties(prefix = "transbank.webpay")
@Getter
@Setter
public class TransbankProperties {

    /**
     * Código de comercio asignado por Transbank
     */
    private String commerceCode;

    /**
     * API Key para autenticación
     */
    private String apiKey;

    /**
     * URL de retorno después del pago (donde Transbank redirige)
     */
    private String returnUrl;

    /**
     * URL final después de confirmar el pago
     */
    private String finalUrl;

    /**
     * URL base de la API en ambiente de integración
     */
    private String apiUrlIntegration;

    /**
     * URL base de la API en ambiente de producción
     */
    private String apiUrlProduction;
}
