package ist.sec.coin.server.ws.exception;

import javax.xml.soap.SOAPException;

public class InvalidAccountAddressException extends SOAPException {
    public InvalidAccountAddressException() {
        super();
    }

    public InvalidAccountAddressException(String reason) {
        super(reason);
    }
}
