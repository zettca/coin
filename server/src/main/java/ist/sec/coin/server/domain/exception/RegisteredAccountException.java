package ist.sec.coin.server.domain.exception;

public class RegisteredAccountException extends CoinException {
    public RegisteredAccountException() {
        super();
    }

    public RegisteredAccountException(String s) {
        super(s);
    }
}
