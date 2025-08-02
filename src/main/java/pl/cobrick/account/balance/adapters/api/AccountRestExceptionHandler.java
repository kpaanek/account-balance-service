package pl.cobrick.account.balance.adapters.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.cobrick.account.balance.domain.exception.InvalidDomainStateException;

@ControllerAdvice
class AccountRestExceptionHandler {

    @ExceptionHandler(value = InvalidDomainStateException.class)
    ResponseEntity<String> invalidDomainStateException(InvalidDomainStateException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}