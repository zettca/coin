package ist.sec.coin.server.domain.exception;

public class InvalidAccountAddressException extends Exception {
    public InvalidAccountAddressException() {
        super();
    }

    public InvalidAccountAddressException(String reason) {
        super(reason);
    }
}
