package ist.sec.coin.server.ws;

import ist.sec.coin.server.domain.AccountAddress;
import ist.sec.coin.server.domain.Ledger;
import ist.sec.coin.server.domain.PKey;
import ist.sec.coin.server.domain.Transaction;
import ist.sec.coin.server.ws.exception.InvalidAccountAddressException;
import ist.sec.coin.server.ws.exception.InvalidPublicKeyException;

import javax.jws.WebService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebService(endpointInterface = "ist.sec.coin.server.ws.CoinService")
public class CoinServiceImpl implements CoinService {
    private static final int STARTING_BALANCE = 10;

    private Map<AccountAddress, PKey> accounts;
    private Map<AccountAddress, Ledger> ledgers;

    public CoinServiceImpl() {
        this.accounts = new HashMap<AccountAddress, PKey>();
        this.ledgers = new HashMap<AccountAddress, Ledger>();
    }

    public String echo(String message) {
        return "<" + message + ">";
    }

    public void register(PKey publicKey) throws InvalidPublicKeyException {
        AccountAddress address = new AccountAddress(publicKey);

        if (this.accounts.containsKey(address) || this.ledgers.containsKey(address)) {
            throw new InvalidPublicKeyException("Public Key already registered");
        } else {
            this.accounts.put(address, publicKey);
            this.ledgers.put(address, new Ledger(address, STARTING_BALANCE));
        }
    }

    public void sendAmount(AccountAddress source, AccountAddress destination, int amount) {

    }

    public AccountStatus checkAccount(AccountAddress address) {
        Ledger ledger = this.ledgers.get(address);
        // TODO: implement listing pending transactions
        List<Transaction> pendingTransactions = null;

        return new AccountStatus(ledger.getBalance(), pendingTransactions);
    }

    public void receiveAmount(AccountAddress address) {

    }

    public List<Transaction> audit(AccountAddress address) throws InvalidAccountAddressException {
        Ledger ledger = this.getLedger(address);

        return ledger.getTransactions();
    }

    /* ========== HELPING METHODS ========== */

    private Ledger getLedger(AccountAddress address) throws InvalidAccountAddressException {
        Ledger ledger = this.ledgers.get(address);

        if (ledger != null) {
            return ledger;
        } else {
            throw new InvalidAccountAddressException("Address does not match an existing account");
        }
    }

    private PKey getAccountKey(AccountAddress address) throws InvalidAccountAddressException {
        PKey key = this.accounts.get(address);

        if (key != null) {
            return key;
        } else {
            throw new InvalidAccountAddressException("Address does not match an existing account");
        }
    }
}
