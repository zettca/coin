package ist.sec.coin.server.ws;

import ist.sec.coin.server.domain.AccountAddress;
import ist.sec.coin.server.domain.PKey;

import javax.jws.WebService;

@WebService(endpointInterface = "ist.sec.coin.server.ws.CoinService")
public class CoinServiceImpl implements CoinService {

    public String echo(String message) {
        return "<" + message + ">";
    }

    public void register(PKey publicKey) {

    }

    public void sendAmount(AccountAddress source, AccountAddress destination, int amount) {

    }

    public void checkAccount(AccountAddress key) {

    }

    public void receiveAmount(AccountAddress key) {

    }

    public void audit(AccountAddress key) {

    }
}
