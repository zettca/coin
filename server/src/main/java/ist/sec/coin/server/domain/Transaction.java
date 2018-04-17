package ist.sec.coin.server.domain;

import ist.sec.coin.server.security.CryptoUtils;

import java.security.*;

public class Transaction {
    private String uid;
    private AccountAddress source;
    private AccountAddress destination;
    private int amount;
    private byte[] signature;

    public Transaction(String id, AccountAddress source, AccountAddress destination, int amount) {
        this.uid = id;
        this.source = source;
        this.destination = destination;
        this.amount = amount;
    }

    public Transaction(String uid, AccountAddress source, AccountAddress destination, int amount, byte[] signature) {
        this(uid, source, destination, amount);
        this.signature = signature;
    }

    public String getId() {
        return uid;
    }

    public AccountAddress getSource() {
        return source;
    }

    public AccountAddress getDestination() {
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

    @Override
    public String toString() {
        return uid + source.getFingerprint() + destination.getFingerprint() + String.valueOf(amount);
    }

    public void sign(PrivateKey privateKey)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        this.signature = CryptoUtils.sign(privateKey, this.toString().getBytes());
    }

    public boolean validate(PublicKey publicKey)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        return CryptoUtils.verifySignature(publicKey, this.toString().getBytes(), signature);
    }

}
