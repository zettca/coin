package ist.sec.coin.server.domain;

public class AccountAddress {
    private String fingerprint;

    public AccountAddress() {
    }

    public AccountAddress(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public String getFingerprint() {
        return fingerprint;
    }
}
