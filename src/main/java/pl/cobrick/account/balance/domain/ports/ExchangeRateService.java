package pl.cobrick.account.balance.domain.ports;

import pl.cobrick.account.balance.domain.model.Currency;

import java.math.BigDecimal;

public interface ExchangeRateService {

    BigDecimal getForCurrency(Currency currency);
}
