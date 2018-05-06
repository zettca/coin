package ist.sec.coin.server.ws;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@XmlType(name = "AccountStatusData")
public class AccountStatusData {
    private int balance;
    private List<TransactionData> pendingTransactions;

    public AccountStatusData() {
    }

    @XmlElement(name = "balance")
    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    @XmlElement(name = "transaction")
    public List<TransactionData> getPendingTransactions() {
        return pendingTransactions;
    }

    public void setPendingTransactions(List<TransactionData> pendingTransactions) {
        this.pendingTransactions = pendingTransactions;
    }
}
