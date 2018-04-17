package ist.sec.coin.server.domain;

import ist.sec.coin.server.domain.exception.CoinException;
import ist.sec.coin.server.security.CryptoUtils;

import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.Certificate;

public class AccountAddress {
    private final String fingerprint;

    public AccountAddress(String fingerprint) throws CoinException {
        if (fingerprint == null || fingerprint.trim().isEmpty()) {
            throw new CoinException("invalid argument");
        }
        this.fingerprint = fingerprint;
    }

    public AccountAddress(PublicKey publicKey) throws NoSuchAlgorithmException, CoinException {
        if (publicKey == null) {
            throw new CoinException("invalid argument");
        }
        this.fingerprint = CryptoUtils.getPublicKeyFingerprint(publicKey);
    }

    public AccountAddress(Certificate cert) throws NoSuchAlgorithmException, CoinException {
        this(cert.getPublicKey());
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
