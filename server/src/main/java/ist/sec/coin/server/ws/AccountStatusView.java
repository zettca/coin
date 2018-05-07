package ist.sec.coin.server.ws;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@XmlType(name = "AccountStatusView")
public class AccountStatusView {
    private int balance;
    private List<TransactionView> pendingTransactions;

    public AccountStatusView() {
    }

    @XmlElement(name = "balance")
    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    @XmlElement(name = "transaction")
    public List<TransactionView> getPendingTransactions() {
        return pendingTransactions;
    }

    public void setPendingTransactions(List<TransactionView> pendingTransactions) {
        this.pendingTransactions = pendingTransactions;
    }
}
