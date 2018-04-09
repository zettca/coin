package ist.sec.coin.domain.exception;

public class InvalidAmountException extends Exception {
    public InvalidAmountException() {
        super();
    }

    public InvalidAmountException(String s) {
        super(s);
    }
}
