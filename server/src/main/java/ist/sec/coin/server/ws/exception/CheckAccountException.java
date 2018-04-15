package ist.sec.coin.server.ws.exception;

import javax.xml.soap.SOAPException;

public class CheckAccountException extends SOAPException {
    public CheckAccountException() {
    }

    public CheckAccountException(String reason) {
        super(reason);
    }
}
