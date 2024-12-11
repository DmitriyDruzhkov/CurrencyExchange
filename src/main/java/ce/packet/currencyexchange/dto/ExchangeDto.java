package ce.packet.currencyexchange.dto;

import ce.packet.currencyexchange.model.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ExchangeDto {
    private Currency baseCurrency;
    private Currency targetCurrency;
    private double rate;
    private double amount;
    private double convertedAmount;
}
