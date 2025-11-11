package com.progressoft.fx.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DealRequest {
    @NotBlank
    @Size(max = 128)
    private String dealUniqueId;
    @NotBlank
    @Pattern(regexp = "^[A-Z]{3}$", message = "fromCurrency must be 3 uppercase letters")
    private String fromCurrency;
    @NotBlank
    @Pattern(regexp = "^[A-Z]{3}$", message = "toCurrency must be 3 uppercase letters")
    private String toCurrency;
    @NotBlank
    private String dealTimestamp;
    @NotNull
    @DecimalMin(value = "0.000001")
    private BigDecimal amount;
}
