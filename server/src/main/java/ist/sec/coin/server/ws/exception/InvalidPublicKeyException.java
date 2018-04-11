package ist.sec.coin.server.ws.exception;

import javax.xml.soap.SOAPException;

public class InvalidPublicKeyException extends SOAPException {
    public InvalidPublicKeyException() {
        super();
    }

    public InvalidPublicKeyException(String s) {
        super(s);
    }
}
