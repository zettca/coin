package ist.sec.coin.service.view;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AuditView")
public class AuditView {
    @XmlElement(name = "transactions")
    private List<TransactionView> transactions;

    public AuditView() {
    }

    public List<TransactionView> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionView> transactions) {
        this.transactions = transactions;
    }
}
