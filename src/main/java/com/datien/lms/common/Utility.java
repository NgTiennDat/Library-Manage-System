package com.datien.lms.common;

import jakarta.servlet.http.HttpServletRequest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.xml.bind.DatatypeConverter;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.text.DecimalFormat;
import java.util.Base64;
import java.util.Random;

public class Utility {
    public static String getHeaderParam(String headerKey) {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes == null) return null;
        HttpServletRequest request
                = ((ServletRequestAttributes) attributes).getRequest();
        return request.getHeader(headerKey);
    }

    public static String getSessionId(String... defaults) {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes == null) return null;
        HttpServletRequest request
                = ((ServletRequestAttributes) attributes).getRequest();
        String sessionId = (String) request.getAttribute("sessionId");
        return sessionId == null ? ((defaults == null || defaults.length == 0) ? null : defaults[0]) : sessionId;
    }

    public static String convertPhoneNumber(String phoneNumber) {
        return phoneNumber.replaceFirst("0", "84");
    }

    public static String generateOTP(int length) {
        String numbers = "0123456789";
        Random random = new Random();
        char[] otp = new char[length];
        for (int i = 0; i < length; i++) {
            otp[i] = numbers.charAt(random.nextInt(numbers.length()));
        }
        return new String(otp);
    }

    public static String md5(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(data.getBytes());
            byte[] digest = md.digest();
            String value = DatatypeConverter
                    .printHexBinary(digest).toUpperCase();
            return value;
        } catch (Exception ex) {
            ex.printStackTrace();
            return AppConstant.SPECIAL_CHAR.EMPTY;
        }
    }

    public static byte[] sha256(String base64Data) throws NoSuchAlgorithmException {
        byte[] data = Base64.getDecoder().decode(base64Data);
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(data);
    }
    static {
        Security.addProvider(new BouncyCastleProvider()); // BouncyCastle provider cần thiết cho các phép toán mã hóa nâng cao
    }

    public static byte[] verifySignature(String signatureBase64, String publicKeyBase64, String base64Data) throws Exception {
        byte[] signature = Base64.getDecoder().decode(signatureBase64);
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyBase64);
        byte[] data = Base64.getDecoder().decode(base64Data);
        // Convert byte array to PublicKey object
        X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKeyBytes);
        PublicKey publicKey = java.security.KeyFactory.getInstance("RSA").generatePublic(spec);

        // Verify the signature
        Signature verifier = Signature.getInstance("SHA256withRSA");
        verifier.initVerify(publicKey);
        verifier.update(data);

        if (!verifier.verify(signature)) {
            // If verification is successful, return the hash of the data
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(data);
        } else {
            throw new SecurityException("Signature verification failed");
        }
    }

    public static String convertToCurrency(BigDecimal number){
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
        return decimalFormat.format(number);
    }
}