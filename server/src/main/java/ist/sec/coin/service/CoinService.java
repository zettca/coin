package ist.sec.coin.service;

import ist.sec.coin.service.view.*;
import ist.sec.coin.service.exception.*;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(name = "CoinServicePortType")
@SOAPBinding(style = SOAPBinding.Style.RPC, use = SOAPBinding.Use.ENCODED)
public interface CoinService {

    // ===== CoinService Main Methods

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

    // ===== CoinService Aux/Testing Methods

    @WebMethod
    void clean();

    // ===== CoinService Sync/Broadcast Methods

    @WebMethod
    void noticeMeSenpai(String wsURL);
}
