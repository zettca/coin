package ist.sec.coin.server.ws;

import javax.jws.WebService;
import java.security.PublicKey;

@WebService(endpointInterface = "ist.sec.coin.server.ws.CoinService")
public class CoinServiceImpl implements CoinService {
    public CoinServiceImpl() {
    }

    public void register(PublicKey key) {

    }

    public void sendAmount(PublicKey source, PublicKey destination, int amount) {

    }

    public void checkAccount(PublicKey key) {

    }

    public void receiveAmount(PublicKey key) {

    }

    public void audit(PublicKey key) {

    }
}
