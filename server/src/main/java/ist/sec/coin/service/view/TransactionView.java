package ist.sec.coin.service.view;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TransactionView")
public class TransactionView {
    @XmlElement(name = "uid")
    private String uid;
    @XmlElement(name = "source")
    private String source;
    @XmlElement(name = "destination")
    private String destination;
    @XmlElement(name = "amount")
    private int amount;
    @XmlElement(name = "sourceSignature")
    private byte[] sourceSignature;
    @XmlElement(name = "destinationSignature")
    private byte[] destinationSignature;

    public TransactionView() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public byte[] getSourceSignature() {
        return sourceSignature;
    }

    public void setSourceSignature(byte[] sourceSignature) {
        this.sourceSignature = sourceSignature;
    }

    public byte[] getDestinationSignature() {
        return destinationSignature;
    }

    public void setDestinationSignature(byte[] destinationSignature) {
        this.destinationSignature = destinationSignature;
    }
}
