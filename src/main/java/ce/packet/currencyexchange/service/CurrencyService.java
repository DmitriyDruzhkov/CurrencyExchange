package ce.packet.currencyexchange.service;

import ce.packet.currencyexchange.dto.CurrencyDto;
import ce.packet.currencyexchange.model.Currency;
import ce.packet.currencyexchange.repository.CurrencyRepository;
import ce.packet.currencyexchange.validator.CurrencyValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CurrencyService {
    private final CurrencyRepository currencyRepository;
    private final CurrencyValidator currencyValidator;

    @Autowired // лишняя аннотация
    public CurrencyService(CurrencyRepository currencyRepository, CurrencyValidator currencyValidator) {//форматирование
        this.currencyRepository = currencyRepository;
        this.currencyValidator = currencyValidator;
    }

    public Currency addNewCurrency(CurrencyDto currencyDto) {
        currencyValidator.validate(currencyDto);
        Currency currency = new Currency();
        currency.setCode(currencyDto.getCode());
        currency.setFullName(currencyDto.getFullName());
        currency.setSign(currencyDto.getSign());

        currencyRepository.save(currency);
        return currency;
    }

    public List<Currency> findAll() {
        return currencyRepository.findAll();
    }

    public Optional<Currency> findByCode(String code) {
        return currencyRepository.findByCode(code);
    }
}
