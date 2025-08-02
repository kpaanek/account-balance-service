package pl.cobrick.account.balance.adapters.api;

record CreateAccountRequest(String firstName,
                            String lastName,
                            String usdAmount,
                            String plnAmount) {
}
