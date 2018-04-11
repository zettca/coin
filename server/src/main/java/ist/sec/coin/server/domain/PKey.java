package ist.sec.coin.server.domain;

public class PKey {
    private String key;

    public PKey() {
    }

    public PKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Override
    public boolean equals(Object obj) {
        PKey key2 = (PKey) obj;
        return key.equals(key2.getKey());
    }
}
