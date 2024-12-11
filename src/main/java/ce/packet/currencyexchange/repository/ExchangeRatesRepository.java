package ce.packet.currencyexchange.repository;

import ce.packet.currencyexchange.model.ExchangeRates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExchangeRatesRepository extends JpaRepository<ExchangeRates, Long> {
    Optional<ExchangeRates> findByBaseCurrencyCodeAndTargetCurrencyCode(String baseCurrencyCode, String targetCurrencyCode);
}


