package ist.sec.coin.server.domain.exception;

public class RegisteredAccountException extends Exception {
    public RegisteredAccountException() {
        super();
    }

    public RegisteredAccountException(String s) {
        super(s);
    }
}
