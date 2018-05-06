package ist.sec.coin.server.ws;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "TransactionData")
public class TransactionData {
    private String uid, source, destination;
    private int amount;
    private byte[] sourceSignature, destinationSignature;

    public TransactionData() {
    }

    @XmlElement(name = "uid")
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @XmlElement(name = "source")
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @XmlElement(name = "destination")
    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    @XmlElement(name = "amount")
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @XmlElement(name = "sourceSignature")
    public byte[] getSourceSignature() {
        return sourceSignature;
    }

    public void setSourceSignature(byte[] sourceSignature) {
        this.sourceSignature = sourceSignature;
    }

    @XmlElement(name = "destinationSignature")
    public byte[] getDestinationSignature() {
        return destinationSignature;
    }

    public void setDestinationSignature(byte[] destinationSignature) {
        this.destinationSignature = destinationSignature;
    }
}
