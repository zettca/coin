package ist.sec.coin.server.domain;

import ist.sec.coin.server.security.CoinSignature;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.util.UUID;

public class Transaction {
    private UUID id;
    private String source;
    private String destination;
    private int amount;
    private byte[] signature;

    public Transaction(UUID uuid, String source, String destination, int amount) {
        this.id = uuid;
        this.source = source;
        this.destination = destination;
        this.amount = amount;
    }

    public UUID getId() {
        return id;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public int getAmount() {
        return amount;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public byte[] sign(PrivateKey privateKey)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        String dataToSign = this.id.toString() + this.source + this.destination + String.valueOf(this.amount);

        this.signature = CoinSignature.sign(privateKey, dataToSign.getBytes());

        return this.signature;
    }

}
