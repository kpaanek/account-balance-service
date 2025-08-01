package pl.cobrick.account.balance.domain.exception;

public class InvalidDomainStateException extends RuntimeException {

    public InvalidDomainStateException(String message) {
        super(message);
    }
}
