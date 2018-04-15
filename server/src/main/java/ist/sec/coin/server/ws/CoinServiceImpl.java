package ist.sec.coin.server.ws;

import ist.sec.coin.server.domain.*;
import ist.sec.coin.server.domain.exception.InvalidAccountAddressException;
import ist.sec.coin.server.domain.exception.InvalidPublicKeyException;

import javax.jws.WebService;
import java.util.ArrayList;

@WebService(endpointInterface = "ist.sec.coin.server.ws.CoinService")
public class CoinServiceImpl implements CoinService {
    private Coin coin;

    public CoinServiceImpl() {
        coin = Coin.getInstance();
    }

    public String echo(String message) {
        return "<" + message + ">";
    }

    public void register(PKey publicKey) throws InvalidPublicKeyException {
        coin.registerAccount(publicKey);
    }

    public void sendAmount(AccountAddress source, AccountAddress destination, int amount) {
        coin.sendAmount(source, destination, amount);
    }

    public AccountStatus checkAccount(AccountAddress address) throws InvalidAccountAddressException {
        return coin.getAccountStatus(address);
    }

    public void receiveAmount(AccountAddress address) {
        coin.receiveAmount(address);

    }

    public ArrayList<Transaction> audit(AccountAddress address) throws InvalidAccountAddressException {
        return coin.getAccountTransactions(address);
    }
}
