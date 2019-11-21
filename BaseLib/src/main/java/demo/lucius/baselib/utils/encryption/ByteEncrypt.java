package demo.lucius.baselib.utils.encryption;

import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 *   aes -128位  ECB  加解密，
 */
public class ByteEncrypt {

    //用于表示加密秘钥，用于表示解密秘钥
    public static final String KEY = "EASTSOFT8848ESAI";


    // 加密
    public static byte[]  Encrypt(byte[]  payload) throws Exception {

        byte[] raw = KEY.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("PKCS5Padding");//"不需要补位"
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(payload);
        return encrypted;


    }


    // 解密
    public static byte[] Decrypt(byte[] sSrc) throws Exception {
        try {

            byte[] raw = KEY.getBytes();
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            try {
                byte[] original = cipher.doFinal(sSrc);
                return  original;
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }

}
