package ce.packet.currencyexchange.service;

import ce.packet.currencyexchange.dto.ExchangeRateDto;
import ce.packet.currencyexchange.dto.ExchangeRateChangeDto;
import ce.packet.currencyexchange.exception.IllegalOperationException;
import ce.packet.currencyexchange.model.ExchangeRates;
import ce.packet.currencyexchange.repository.CurrencyRepository;
import ce.packet.currencyexchange.repository.ExchangeRatesRepository;
import ce.packet.currencyexchange.validator.ExchangeRatesValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExchangeRatesService {
    private final ExchangeRatesRepository exchangeRatesRepository;
    private final ExchangeRatesValidator exchangeRatesValidator;
    private final CurrencyRepository currencyRepository;

    public ExchangeRatesService(ExchangeRatesRepository exchangeRatesRepository,
                                ExchangeRatesValidator exchangeRatesValidator,
                                CurrencyRepository currencyRepository) {
        this.exchangeRatesRepository = exchangeRatesRepository;
        this.exchangeRatesValidator = exchangeRatesValidator;
        this.currencyRepository = currencyRepository;
    }

    public ExchangeRates addNewExchangeRates(ExchangeRateDto exchangeRateDto) {
        exchangeRatesValidator.validate(exchangeRateDto);

        var mayBeBaseCurrency = currencyRepository.findByCode(exchangeRateDto.getBaseCurrencyCode());
        var targetCurrencyId = currencyRepository.findByCode(exchangeRateDto.getTargetCurrencyCode());
        if (mayBeBaseCurrency.isEmpty() || targetCurrencyId.isEmpty()) {
            throw new IllegalOperationException(
                    "error.base.or.currency.it.not.found",
                    "По переданным кодам не найдены валюты"
            );
        }
        ExchangeRates exchangeRate = new ExchangeRates();
        exchangeRate.setRate(exchangeRateDto.getRate());
        exchangeRate.setBaseCurrency(mayBeBaseCurrency.get());
        exchangeRate.setTargetCurrency(targetCurrencyId.get());


        exchangeRatesRepository.save(exchangeRate);

        return exchangeRate;
    }

    public void updateExchangeRate(String pairOfCode, ExchangeRateChangeDto exchangeRateChangeDto) {
        String baseCurrencyCode = pairOfCode.substring(0, 3);
        String targetCurrencyCode = pairOfCode.substring(3);
        var mayBeExchangeRates = exchangeRatesRepository.findByBaseCurrencyCodeAndTargetCurrencyCode(baseCurrencyCode, targetCurrencyCode);
        mayBeExchangeRates.ifPresentOrElse(
                exchangeRate -> exchangeRate.setRate(exchangeRateChangeDto.getRate()),
                () -> {
                    throw new IllegalOperationException("error.exchange.rate.not.found",
                            "По переданным кодам валютная пара не найдена");
                }
        );
        exchangeRatesRepository.flush();
    }

    public List<ExchangeRates> findAllExchangeRates() {
        return exchangeRatesRepository.findAll();
    }

    public Optional<ExchangeRates> findExchangeRate(String pairOfCode) {
        String baseCurrencyCode = pairOfCode.substring(0, 3);
        String targetCurrencyCode = pairOfCode.substring(3);
        return exchangeRatesRepository.findByBaseCurrencyCodeAndTargetCurrencyCode(baseCurrencyCode, targetCurrencyCode);
    }

}
