package demo.lucius.javatestlib.time;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 *   aes -128位  ECB  加解密，
 */
public class ByteEncrypt {

    //用于表示加密秘钥，用于表示解密秘钥
    public static final String KEY = "EASTSOFT8848ESAI";


    // 加密
    public static String  Encrypt(String string) throws Exception {
        byte[]  info=TimeControl.stringToByte(string);
        byte[] raw = KEY.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding ");//"算法/模式/补码方式"
        byte[] iv = { 3, 5, 3, 0, 0, 0, 4, 0, 8, 0, 0, 2,5, 3, 9, 7 };
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec,ivspec);
        byte[] encrypted = cipher.doFinal(info);
//        return encrypted;
        System.out.println("base64加密前："+TimeControl.byteToString(encrypted));
        return new BASE64Encoder().encode(encrypted);
    }


    // 解密
    public static String Decrypt(String string) throws Exception {
        try {
            byte[] sSrc= new BASE64Decoder().decodeBuffer(string);//TimeControl.stringToByte(string);
            System.out.println("base64解密："+TimeControl.byteToString(sSrc));
            byte[] raw = KEY.getBytes();
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding ");
            byte[] iv = { 3, 5, 3, 0, 0, 0, 4, 0, 8, 0, 0, 2,5, 3, 9, 7 };
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec,ivspec);
            try {
                byte[] original = cipher.doFinal(sSrc);
//                return  original;
                return TimeControl.byteToString(original);
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
