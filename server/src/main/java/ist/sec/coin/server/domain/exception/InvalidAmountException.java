package ist.sec.coin.server.domain.exception;

public class InvalidAmountException extends Exception {
    public InvalidAmountException() {
        super();
    }

    public InvalidAmountException(String s) {
        super(s);
    }
}
