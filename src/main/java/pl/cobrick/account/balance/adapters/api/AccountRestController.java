package pl.cobrick.account.balance.adapters.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.cobrick.account.balance.domain.AccountCommandHandler;
import pl.cobrick.account.balance.domain.AccountQueryHandler;
import pl.cobrick.account.balance.domain.exception.InvalidDomainStateException;
import pl.cobrick.account.balance.domain.model.Balance;
import pl.cobrick.account.balance.domain.model.Currency;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
class AccountRestController {

    private final AccountCommandHandler accountCommandHandler;
    private final AccountQueryHandler accountQueryHandler;

    @PostMapping
    ResponseEntity<String> createAccount(@RequestBody CreateAccountRequest request) {
        var command = CreateAccountRestCommand.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .plnAmount(Optional.ofNullable(request.plnAmount()).map(BigDecimal::new).orElse(BigDecimal.ZERO))
                .usdAmount(Optional.ofNullable(request.usdAmount()).map(BigDecimal::new).orElse(BigDecimal.ZERO))
                .build();
        return ResponseEntity.ok(accountCommandHandler.handle(command));
    }

    @PatchMapping("/{id}")
    ResponseEntity<Void> transferBalance(@PathVariable String id, @RequestBody TransferBalanceRequest request) {
        var command = TransferBalanceRestCommand.builder()
                .id(id)
                .source(Optional.ofNullable(request.sourceCurrency())
                        .map(Currency::valueOf)
                        .orElseThrow(() -> new InvalidDomainStateException("Invalid source currency.")))
                .target(Optional.ofNullable(request.targetCurrency())
                        .map(Currency::valueOf)
                        .orElseThrow(() -> new InvalidDomainStateException("Invalid target currency.")))
                .amount(Optional.ofNullable(request.amount()).map(BigDecimal::new).orElse(BigDecimal.ZERO))
                .build();
        accountCommandHandler.handle(command);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    ResponseEntity<AccountResponse> findById(@PathVariable String id) {
        var account = accountQueryHandler.findById(id);
        return ResponseEntity.ok(AccountResponse.builder()
                .id(account.getId())
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .plnBalance(Optional.ofNullable(account.getPlnBalance())
                        .map(Balance::amount)
                        .map(BigDecimal::toPlainString)
                        .orElse(null))
                .usdBalance(Optional.ofNullable(account.getUsdBalance())
                        .map(Balance::amount)
                        .map(BigDecimal::toPlainString)
                        .orElse(null))
                .build());
    }
}
