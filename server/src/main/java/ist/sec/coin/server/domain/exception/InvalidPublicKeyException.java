package ist.sec.coin.server.domain.exception;

public class InvalidPublicKeyException extends Exception {
    public InvalidPublicKeyException() {
        super();
    }

    public InvalidPublicKeyException(String s) {
        super(s);
    }
}
