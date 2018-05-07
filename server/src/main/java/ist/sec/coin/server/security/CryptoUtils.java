package ist.sec.coin.server.security;

import javax.xml.bind.DatatypeConverter;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public final class CryptoUtils {
    public static final String KEY_GEN_ALGORITHM = "RSA";
    public static final int KEY_SIZE = 2048;

    public static final String HASH_ALGORITHM = "SHA-256";
    public static final String SIGN_ALGORITHM = "SHA256WITHRSA";
    public static final String CERTIFICATE_FORMAT = "X.509";

    public static byte[] sign(PrivateKey privateKey, byte[] data)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature sig = Signature.getInstance(SIGN_ALGORITHM);
        sig.initSign(privateKey);
        sig.update(data);
        return sig.sign();
    }

    public static boolean verifySignature(PublicKey publicKey, byte[] data, byte[] signature)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature sig = Signature.getInstance(SIGN_ALGORITHM);
        sig.initVerify(publicKey);
        sig.update(data);
        return sig.verify(signature);
    }

    public static byte[] digest(byte[] data) throws NoSuchAlgorithmException {
        return MessageDigest.getInstance(HASH_ALGORITHM).digest(data);
    }

    public static String getPublicKeyFingerprint(PublicKey publicKey) throws NoSuchAlgorithmException {
        return DatatypeConverter.printHexBinary(digest(publicKey.getEncoded()));
    }

    public static PublicKey getPublicKeyFromString(byte[] publicKeyBytes) throws NoSuchAlgorithmException,
            InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_GEN_ALGORITHM);
        EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        return keyFactory.generatePublic(keySpec);
    }

    public static byte[] mergeByteArray(final byte[] a, final byte[] b) {
        byte[] c = new byte[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }
}
