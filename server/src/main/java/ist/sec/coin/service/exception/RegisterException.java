package ist.sec.coin.service.exception;

import javax.xml.soap.SOAPException;

public class RegisterException extends SOAPException {
    public RegisterException() {
    }

    public RegisterException(String reason) {
        super(reason);
    }
}
