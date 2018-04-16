package ist.sec.coin.server.ws;

import ist.sec.coin.server.domain.Transaction;
import ist.sec.coin.server.ws.exception.*;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.ArrayList;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC, use = SOAPBinding.Use.ENCODED)
public interface CoinService {

    @WebMethod
    String echo(
            @WebParam(name = "message") String message)
            throws EchoException;

    @WebMethod
    String register(
            @WebParam(name = "certificate") String certString
    ) throws RegisterException;

    @WebMethod
    void sendAmount(
            @WebParam(name = "source") String source,
            @WebParam(name = "destination") String destination,
            @WebParam(name = "amount") int amount)
            throws SendAmountException;

    @WebMethod
    AccountStatus checkAccount(
            @WebParam(name = "address") String address)
            throws CheckAccountException;

    @WebMethod
    void receiveAmount(
            @WebParam(name = "address") String address)
            throws ReceiveAmountException;

    @WebMethod
    ArrayList<Transaction> audit(
            @WebParam(name = "address") String address)
            throws AuditException;
}
