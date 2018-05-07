package ist.sec.coin.server.domain;

import ist.sec.coin.server.security.CryptoUtils;

import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

public class AccountAddress {
    private final String fingerprint;

    public AccountAddress(String fingerprint) {
        if (fingerprint == null || fingerprint.trim().isEmpty()) {
            throw new IllegalArgumentException("invalid argument");
        }
        this.fingerprint = fingerprint;
    }

    public AccountAddress(PublicKey key) throws NoSuchAlgorithmException {
        if (key == null) {
            throw new IllegalArgumentException("invalid argument");
        }
        this.fingerprint = CryptoUtils.getPublicKeyFingerprint(key);
    }

    public String getFingerprint() {
        return fingerprint;
    }

    @Override
    public String toString() {
        return fingerprint;
    }

    @Override
    public boolean equals(Object o) {
        return fingerprint.equals(o.toString());
    }
}
