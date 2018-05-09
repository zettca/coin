package ist.sec.coin.service.exception;

import javax.xml.soap.SOAPException;

public class ReceiveAmountException extends SOAPException {
    public ReceiveAmountException() {
    }

    public ReceiveAmountException(String reason) {
        super(reason);
    }
}
