package ce.packet.currencyexchange.validator;

import ce.packet.currencyexchange.dto.ExchangeRateDto;
import ce.packet.currencyexchange.exception.IllegalOperationException;
import org.springframework.stereotype.Component;

@Component
public class ExchangeRatesValidator {
    public void validate(ExchangeRateDto exchangeRateDto) throws IllegalOperationException {
        if (exchangeRateDto.getBaseCurrencyCode() == null
                || exchangeRateDto.getTargetCurrencyCode() == null
                || exchangeRateDto.getRate() == null) {
            throw new IllegalOperationException("error.field.not.filed", "Not enough parameters for currency");
        }
    }
}
