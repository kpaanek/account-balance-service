package pl.cobrick.account.balance.adapters.rest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "exchangeRateRestClient", url = "https://api.nbp.pl/api")
interface ExchangeRateRestClient {

    @GetMapping("/exchangerates/rates/c/{currencySymbol}/{isoFormatDate}/?format=json")
    NbpExchangeRatesResponse getNbpExchangeRates(@PathVariable("currencySymbol") String currencySymbol,
                                                 @PathVariable("isoFormatDate") String isoFormatDate);
}
