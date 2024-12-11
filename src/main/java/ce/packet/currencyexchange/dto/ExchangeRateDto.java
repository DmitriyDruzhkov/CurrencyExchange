package ce.packet.currencyexchange.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExchangeRateDto {
    private String baseCurrencyCode;
    private String targetCurrencyCode;
    private Double rate;

}