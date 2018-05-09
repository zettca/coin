package ist.sec.coin.service.view;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@XmlType(name = "AuditView")
public class AuditView {
    private List<TransactionView> transactions;

    public AuditView() {
    }

    @XmlElement(name = "transactions")
    public List<TransactionView> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionView> transactions) {
        this.transactions = transactions;
    }
}
