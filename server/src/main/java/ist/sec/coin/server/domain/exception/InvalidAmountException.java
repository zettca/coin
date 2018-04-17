package ist.sec.coin.server.domain.exception;

public class InvalidAmountException extends CoinException {
    public InvalidAmountException() {
        super();
    }

    public InvalidAmountException(String s) {
        super(s);
    }
}
