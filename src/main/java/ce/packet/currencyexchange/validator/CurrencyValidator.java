package ce.packet.currencyexchange.validator;

import ce.packet.currencyexchange.dto.CurrencyDto;
import ce.packet.currencyexchange.exception.IllegalOperationException;
import ce.packet.currencyexchange.repository.CurrencyRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class CurrencyValidator {
    private final CurrencyRepository currencyRepository;

    public CurrencyValidator(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    public void validate(CurrencyDto currencyDto) {
        if (StringUtils.isEmpty(currencyDto.getCode())
                || StringUtils.isEmpty(currencyDto.getFullName())
                || StringUtils.isEmpty(currencyDto.getSign())) {
            throw new IllegalOperationException("error.field.not.filed", "Not enough parameters for currency");
        }
        var maybeCurrency = currencyRepository.findByCode(currencyDto.getCode());
        if (maybeCurrency.isPresent()) {
            throw new IllegalOperationException("error.currency.already.exists", "Currency code already exists");
        }
    }

}
