package pl.cobrick.account.balance.adapters.rest;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.cobrick.account.balance.domain.model.Currency;
import pl.cobrick.account.balance.domain.ports.ExchangeRateClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
class ExchangeRateRestClientAdapter implements ExchangeRateClient {

    private final ExchangeRateRestClient exchangeRateRestClient;

    @Override
    public BigDecimal getForCurrency(Currency currency) {
        var currencySymbol = getCurrencySymbol(currency);
        var nbpResponse = tryToGetNbpExchangeRates(currencySymbol);
        if (nbpResponse.rates().size() != 1) {
            throw new IllegalStateException("There should be only one rate for given currency symbol: %s.".formatted(currencySymbol));
        }
        return nbpResponse.rates().stream()
                .map(NbpExchangeRateResponse::ask)
                .map(BigDecimal::valueOf)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Error while parsing NbpExchangeRatesResponse."));
    }

    private String getCurrencySymbol(Currency currency) {
        return switch (currency) {
            case USD -> "usd";
            case PLN -> "pln";
        };
    }

    private NbpExchangeRatesResponse tryToGetNbpExchangeRates(String currencySymbol) {
        var currentLocalDate = LocalDate.now();
        try {
            return exchangeRateRestClient.getNbpExchangeRates(currencySymbol, DateTimeFormatter.ISO_DATE.format(currentLocalDate));
        } catch (FeignException.FeignClientException exception) {
            log.warn("Retrying getNbpExchangeRates rest call for exception: ", exception);
            if (exception instanceof FeignException.NotFound) {
                return exchangeRateRestClient.getNbpExchangeRates(currencySymbol, DateTimeFormatter.ISO_DATE.format(currentLocalDate.minusDays(1)));
            }
            throw exception;
        }
    }
}
