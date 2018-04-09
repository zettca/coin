package ist.sec.coin.server.ws;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.security.PublicKey;

@WebService
@SOAPBinding(
        style = SOAPBinding.Style.RPC,
        use = SOAPBinding.Use.ENCODED)
public interface CoinService {

    @WebMethod
    void register(PublicKey key);

    @WebMethod
    void sendAmount(PublicKey source, PublicKey destination, int amount);

    @WebMethod
    void checkAccount(PublicKey key);

    @WebMethod
    void receiveAmount(PublicKey key);

    @WebMethod
    void audit(PublicKey key);
}

