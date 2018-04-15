package ist.sec.coin.server.ws;

import ist.sec.coin.server.domain.AccountAddress;
import ist.sec.coin.server.domain.AccountStatus;
import ist.sec.coin.server.domain.PKey;
import ist.sec.coin.server.domain.Transaction;
import ist.sec.coin.server.domain.exception.InvalidAccountAddressException;
import ist.sec.coin.server.domain.exception.InvalidAmountException;
import ist.sec.coin.server.domain.exception.InvalidPublicKeyException;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.ArrayList;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC, use = SOAPBinding.Use.ENCODED)
public interface CoinService {

    @WebMethod
    String echo(@WebParam(name = "message") String message);

    @WebMethod
    void register(
            @WebParam(name = "publicKey") PKey publicKey
    ) throws InvalidPublicKeyException;

    @WebMethod
    void sendAmount(
            @WebParam(name = "source") AccountAddress source,
            @WebParam(name = "destination") AccountAddress destination,
            @WebParam(name = "amount") int amount
    ) throws InvalidAccountAddressException, InvalidAmountException;

    @WebMethod
    AccountStatus checkAccount(
            @WebParam(name = "address") AccountAddress address
    ) throws InvalidAccountAddressException;

    @WebMethod
    void receiveAmount(
            @WebParam(name = "address") AccountAddress address
    ) throws InvalidAccountAddressException;

    @WebMethod
    ArrayList<Transaction> audit(
            @WebParam(name = "address") AccountAddress address
    ) throws InvalidAccountAddressException;
}
