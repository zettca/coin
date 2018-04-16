package ist.sec.coin.server.security;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

public final class CoinSignature {
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

    public static String signToBase64(PrivateKey privateKey, byte[] data)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        return DatatypeConverter.printBase64Binary(CoinSignature.sign(privateKey, data));
    }

    public static byte[] signText(PrivateKey privateKey, String data)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        return CoinSignature.sign(privateKey, DatatypeConverter.parseBase64Binary(data));
    }

    public static String signTextToBase64(PrivateKey privateKey, String data)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        return DatatypeConverter.printBase64Binary(CoinSignature.signText(privateKey, data));
    }


    public static boolean verifySignature(PublicKey publicKey, byte[] cipher, byte[] data)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature sig = Signature.getInstance(SIGN_ALGORITHM);
        sig.initVerify(publicKey);
        sig.update(data);
        return sig.verify(cipher);
    }

    public static byte[] digest(byte[] data) throws NoSuchAlgorithmException {
        return MessageDigest.getInstance(HASH_ALGORITHM).digest(data);
    }

    public static String digestToHex(byte[] data) throws NoSuchAlgorithmException {
        return DatatypeConverter.printHexBinary(CoinSignature.digest(data));
    }

    public static String digestToBase64(byte[] data) throws NoSuchAlgorithmException {
        return DatatypeConverter.printBase64Binary(CoinSignature.digest(data));
    }

    public static String getCertificateFingerprint(Certificate cert) throws NoSuchAlgorithmException {
        return digestToHex(cert.getPublicKey().getEncoded());
    }

    public static Certificate getCertificateFromString(String certString) throws CertificateException {
        CertificateFactory cf = CertificateFactory.getInstance(CERTIFICATE_FORMAT);
        InputStream is = new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(certString));
        return cf.generateCertificate(is);
    }

}
