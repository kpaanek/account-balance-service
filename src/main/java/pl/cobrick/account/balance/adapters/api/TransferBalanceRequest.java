package pl.cobrick.account.balance.adapters.api;

record TransferBalanceRequest(String sourceCurrency,
                              String targetCurrency,
                              String amount) {
}
