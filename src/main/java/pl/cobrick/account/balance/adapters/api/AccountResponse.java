package pl.cobrick.account.balance.adapters.api;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PACKAGE)
record AccountResponse(String id,
                       String firstName,
                       String lastName,
                       String plnBalance,
                       String usdBalance) {
}
