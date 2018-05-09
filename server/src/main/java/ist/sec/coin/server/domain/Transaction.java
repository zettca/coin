package ist.sec.coin.server.domain;

import ist.sec.coin.server.security.CryptoUtils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;

public class Transaction {
    private final String uid;
    private final AccountAddress source, destination;
    private final int amount;
    private byte[] sourceSignature, destinationSignature;

    public Transaction(String id, AccountAddress source, AccountAddress destination, int amount) {
        this.uid = id;
        this.source = source;
        this.destination = destination;
        this.amount = amount;
    }

    private static byte[] mergeBytes(byte[] a, byte[] b) {
        byte[] result = new byte[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
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

    @Override
    public String toString() {
        return uid + source.getFingerprint() + destination.getFingerprint() + String.valueOf(amount);
    }

    private byte[] getData() {
        return toString().getBytes();
    }

    boolean validateSource(PublicKey sourceKey)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        return CryptoUtils.verifySignature(sourceKey, getData(), sourceSignature);
    }

    boolean validateDestination(PublicKey destinationKey)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        return CryptoUtils.verifySignature(destinationKey, getData(), destinationSignature);
    }

}
