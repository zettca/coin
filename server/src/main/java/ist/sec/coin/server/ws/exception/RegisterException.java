package ist.sec.coin.server.ws.exception;

import javax.xml.soap.SOAPException;

public class RegisterException extends SOAPException {
    public RegisterException() {
    }

    public RegisterException(String reason) {
        super(reason);
    }
}
