package ist.sec.coin.server.ws;

import ist.sec.coin.server.ws.exception.*;
import ist.sec.coin.server.ws.view.*;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC, use = SOAPBinding.Use.ENCODED)
public interface CoinService {

    @WebMethod
    String echo(
            @WebParam(name = "message") String message)
            throws EchoException;

    @WebMethod
    String register(
            @WebParam(name = "publickey") byte[] publicKeyBytes)
            throws RegisterException;

    @WebMethod
    void sendAmount(
            @WebParam(name = "transaction") TransactionView transaction)
            throws SendAmountException;

    @WebMethod
    AccountStatusView checkAccount(
            @WebParam(name = "fingerprint") String fingerprint)
            throws CheckAccountException;

    @WebMethod
    void receiveAmount(
            @WebParam(name = "transaction") TransactionView transaction)
            throws ReceiveAmountException;

    @WebMethod
    AuditView audit(
            @WebParam(name = "address") String address)
            throws AuditException;

    // ===== Used for testing

    @WebMethod
    void clean();
}
