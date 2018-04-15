package ist.sec.coin.server.domain.exception;

import javax.xml.soap.SOAPException;

public class InvalidAmountException extends SOAPException {
    public InvalidAmountException() {
        super();
    }

    public InvalidAmountException(String s) {
        super(s);
    }
}
