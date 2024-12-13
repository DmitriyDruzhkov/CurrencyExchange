package ce.packet.currencyexchange.service;

import ce.packet.currencyexchange.dto.ExchangeDto;
import ce.packet.currencyexchange.exception.IllegalOperationException;
import ce.packet.currencyexchange.model.Currency;
import ce.packet.currencyexchange.repository.ExchangeRatesRepository;
import org.springframework.stereotype.Service;

@Service
public class ExchangeService {
    private final ExchangeRatesRepository exchangeRatesRepository;

    public ExchangeService(ExchangeRatesRepository exchangeRatesRepository) {
        this.exchangeRatesRepository = exchangeRatesRepository;
    }

    public ExchangeDto convert(String from, String to, double amount) {
        var maybeExchangeRate = exchangeRatesRepository.findByBaseCurrencyCodeAndTargetCurrencyCode(from, to);
        if (maybeExchangeRate.isEmpty()) {
            maybeExchangeRate = exchangeRatesRepository.findByBaseCurrencyCodeAndTargetCurrencyCode(to, from);
        }
        if (maybeExchangeRate.isPresent()) {
            var exchangeRates = maybeExchangeRate.get();
            return calculate(
                    amount,
                    exchangeRates.getBaseCurrency().getCode().equals(from) ? exchangeRates.getBaseCurrency() : exchangeRates.getTargetCurrency(),
                    exchangeRates.getTargetCurrency().getCode().equals(to) ? exchangeRates.getTargetCurrency() : exchangeRates.getBaseCurrency(),
                    exchangeRates.getRate()
            );
        } else {
            return tryCalculateByUSD(from, to, amount);
        }
    }

    private ExchangeDto calculate(double amount,
                                  Currency baseCurrency,
                                  Currency targetCurrency,
                                  Double rate) {
        return new ExchangeDto(
                baseCurrency,
                targetCurrency,
                rate,
                amount,
                amount * rate
        );
    }

    private ExchangeDto tryCalculateByUSD(String from, String to, double amount) { // добавить варианты USD/from и from/USD (для to тоже)
        var maybeExchangeRateUSDfrom = exchangeRatesRepository.findByBaseCurrencyCodeAndTargetCurrencyCode(
                "USD", from);
        var maybeExchangeRateUSDto = exchangeRatesRepository.findByBaseCurrencyCodeAndTargetCurrencyCode(
                "USD", to);
        if (amount <= 0) throw new IllegalOperationException("error.currency.not.found", "Неверное количество средств"); // не писать if в одну строку; проверку вынести перед запросов в бд
        if (maybeExchangeRateUSDfrom.isPresent() && maybeExchangeRateUSDto.isPresent()) {
            double newRate = maybeExchangeRateUSDfrom.get().getRate() / maybeExchangeRateUSDto.get().getRate();
            return new ExchangeDto(maybeExchangeRateUSDfrom.get().getBaseCurrency(), // поправить форматирование
                    maybeExchangeRateUSDto.get().getTargetCurrency(),
                    newRate,
                    amount,
                    amount * newRate);
        } else {
            throw new IllegalOperationException("error.currency.not.found", "Валюта не найдена");
        }
    }

}
