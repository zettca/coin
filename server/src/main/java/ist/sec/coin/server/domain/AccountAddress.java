package ist.sec.coin.server.domain;

public class AccountAddress {
    private String fingerprint;

    public AccountAddress() {
    }

    public AccountAddress(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public AccountAddress(PKey publicKey) {
        // TODO: get fingerprint from crypto hash
        this.fingerprint = String.valueOf(publicKey.getKey().hashCode());
    }

    public String getFingerprint() {
        return fingerprint;
    }
}
