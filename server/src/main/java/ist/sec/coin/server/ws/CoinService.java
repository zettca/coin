package ist.sec.coin.server.ws;

import ist.sec.coin.server.domain.AccountAddress;
import ist.sec.coin.server.domain.PKey;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.*;

@WebService
@SOAPBinding(style = Style.RPC, use = Use.ENCODED)
public interface CoinService {

    @WebMethod
    String echo(
            @WebParam(name = "message") String message);

    @WebMethod
    void register(
            @WebParam(name = "publicKey") PKey publicKey);

    @WebMethod
    void sendAmount(
            @WebParam(name = "source") AccountAddress source,
            @WebParam(name = "destination") AccountAddress destination,
            @WebParam(name = "amount") int amount);

    @WebMethod
    void checkAccount(
            @WebParam(name = "address") AccountAddress address);

    @WebMethod
    void receiveAmount(
            @WebParam(name = "address") AccountAddress address);

    @WebMethod
    void audit(
            @WebParam(name = "address") AccountAddress address);
}
