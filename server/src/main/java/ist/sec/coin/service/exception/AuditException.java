package ist.sec.coin.service.exception;

import javax.xml.soap.SOAPException;

public class AuditException extends SOAPException {
    public AuditException() {
    }

    public AuditException(String reason) {
        super(reason);
    }
}
