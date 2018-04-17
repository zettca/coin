package ist.sec.coin.server.security;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

public final class CryptoUtils {
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

    public static boolean verifySignature(PublicKey publicKey, byte[] data, byte[] cipher)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature sig = Signature.getInstance(SIGN_ALGORITHM);
        sig.initVerify(publicKey);
        sig.update(data);
        return sig.verify(cipher);
    }

    public static byte[] digest(byte[] data) throws NoSuchAlgorithmException {
        return MessageDigest.getInstance(HASH_ALGORITHM).digest(data);
    }

    public static String getPublicKeyFingerprint(PublicKey publicKey) throws NoSuchAlgorithmException {
        return DatatypeConverter.printHexBinary(digest(publicKey.getEncoded()));
    }

    public static Certificate getCertificateFromString(String certString) throws CertificateException {
        CertificateFactory cf = CertificateFactory.getInstance(CERTIFICATE_FORMAT);
        InputStream is = new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(certString));
        return cf.generateCertificate(is);
    }
}
