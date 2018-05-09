package ist.sec.coin.service.view;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AccountStatusView")
public class AccountStatusView {
    @XmlElement(name = "balance")
    private int balance;
    @XmlElement(name = "transactions")
    private List<TransactionView> pendingTransactions;

    public AccountStatusView() {
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public List<TransactionView> getPendingTransactions() {
        return pendingTransactions;
    }

    public void setPendingTransactions(List<TransactionView> pendingTransactions) {
        this.pendingTransactions = pendingTransactions;
    }
}
