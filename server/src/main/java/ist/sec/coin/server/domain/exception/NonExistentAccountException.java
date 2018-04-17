package ist.sec.coin.server.domain.exception;

public class NonExistentAccountException extends Exception {
    public NonExistentAccountException() {
        super();
    }

    public NonExistentAccountException(String reason) {
        super(reason);
    }
}
