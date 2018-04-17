package ist.sec.coin.server.domain;

import ist.sec.coin.server.security.CryptoUtils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.cert.Certificate;

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

    @Override
    public String toString() {
        return uid + source.getFingerprint() + destination.getFingerprint() + String.valueOf(amount);
    }

    public boolean validate(Certificate cert)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        return CryptoUtils.verifySignature(cert.getPublicKey(), this.signature, this.toString().getBytes());
    }

}
