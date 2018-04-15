package ist.sec.coin.server.ws.exception;

import javax.xml.soap.SOAPException;

public class SendAmountException extends SOAPException {
    public SendAmountException() {
    }

    public SendAmountException(String reason) {
        super(reason);
    }
}
