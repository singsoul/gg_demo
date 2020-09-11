package com.boniu.ad.aes;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class AESUtil {

    public static String encrypt(String data, String key) {
//        return data;
        try {
            SecretKeySpec spec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, spec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
            byte[] bs = cipher.doFinal(data.getBytes("UTF-8"));
            return JukeBase64.encode(bs);
        } catch (Exception e) {
            return "";
//            throw new RuntimeException("AES加密异常", e);
        }
    }


    public static String decrypt(String data, String key) {
//        return data;
        try {
            SecretKeySpec spec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, spec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
            byte[] originBytes = JukeBase64.decode(data);
            byte[] result = cipher.doFinal(originBytes);
            return new String(result, "UTF-8");
        } catch (Exception e) {
            return "";
//            throw new RuntimeException("AES解密异常", e);
        }
    }
}