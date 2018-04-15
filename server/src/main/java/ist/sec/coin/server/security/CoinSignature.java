package ist.sec.coin.server.security;

import javax.xml.bind.DatatypeConverter;
import java.security.*;

public final class CoinSignature {
    private static final String HASH_ALGORITHM = "SHA-256";
    private static final String SIGN_ALGORITHM = "SHA256WITHRSA";

    public static String getHashAlgorithm() {
        return HASH_ALGORITHM;
    }

    public static String getSignAlgorithm() {
        return SIGN_ALGORITHM;
    }

    public static byte[] sign(PrivateKey privateKey, byte[] data)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature sig = Signature.getInstance(SIGN_ALGORITHM);
        sig.initSign(privateKey);
        sig.update(data);
        return sig.sign();
    }

    public static String signToBase64(PrivateKey privateKey, byte[] data)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        return DatatypeConverter.printBase64Binary(sign(privateKey, data));
    }

    public static byte[] signText(PrivateKey privateKey, String data)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature sig = Signature.getInstance(SIGN_ALGORITHM);
        sig.initSign(privateKey);
        sig.update(DatatypeConverter.parseBase64Binary(data));
        return sig.sign();
    }

    public static String signTextToBase64(PrivateKey privateKey, String data)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature sig = Signature.getInstance(SIGN_ALGORITHM);
        sig.initSign(privateKey);
        sig.update(DatatypeConverter.parseBase64Binary(data));
        return DatatypeConverter.printBase64Binary(sig.sign());
    }


    public static boolean verify(PublicKey publicKey, byte[] cipher, byte[] data)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature sig = Signature.getInstance(SIGN_ALGORITHM);
        sig.initVerify(publicKey);
        sig.update(data);
        return sig.verify(cipher);
    }

    public static byte[] digest(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
        return digest.digest(data);
    }

    public static String digestToBase64(byte[] data) throws NoSuchAlgorithmException {
        return DatatypeConverter.printBase64Binary(digest(data));
    }

}
