package pl.cobrick.account.balance.domain.ports;

import pl.cobrick.account.balance.domain.model.Currency;

import java.math.BigDecimal;

public interface ExchangeRateClient {
    BigDecimal getForCurrency(Currency currency);
}
