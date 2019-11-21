package demo.lucius.javatestlib.time;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class AESUtil {


    private static final String EncryptAlg = "AES";

    private static final String Ciper_Mode = "AES/ECB/PKCS5PADDING";

    private static final String Encode = "UTF-8";

    private static final int Secret_Key_Size = 32;

    private static final String Key_Encode = "UTF-8";

    //用于表示秘钥种子
    private static final String Key_Seed="PADESBLE";

    //用于根据生成的秘钥对明文plainText进行加密
    public static final String encrypt(String plainText) {
        Key secretKey = getKey(Key_Seed);
//          byte[] raw   = secretKey.getEncoded();   //获得原始对称秘钥字节数组
//        SecretKey key=new SecretKeySpec(raw,"AES");  //根据字节数组生成AES秘钥

        try {
            //依据指定算法AES自成密码器
            Cipher cipher = Cipher.getInstance(Ciper_Mode);
            //初始化密码器
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            //获取加密内容的字节数组，设置模式
            byte[] p = plainText.getBytes("UTF-8");
            //根据密码器的初始化方式，将数据加密
            byte[] result = cipher.doFinal(p);

            //在Android中，不能直接使用BASE64Encoder，在javalib中可以使用  import sun.misc.BASE64Encoder;
            //解决办在项目的Build path中先移除JRE System Library，再添加库JRE System Library，重新编译后就一切正常了，需要测试是否是Android  Studio
            BASE64Encoder encoder = new BASE64Encoder();
            System.out.println(TimeControl.byteToString(result));
            String encoded = encoder.encode(result);
            return encoded;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    //用于对字符串进行解密
    public static final String decrypt(String cipherText) {
        Key secretKey = getKey(Key_Seed);
        try {

            BASE64Decoder decoder = new BASE64Decoder();
            byte[] decoded = decoder.decodeBuffer(cipherText);
            Cipher cipher = Cipher.getInstance(Ciper_Mode);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] result = cipher.doFinal(decoded);
            String plainText = new String(result, "UTF-8");
            return plainText;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }


    //用于获取秘钥
    public static Key getKey(String keySeed) {
        if (keySeed == null) {
            keySeed = System.getenv("AES_SYS_KEY");
        }

        if (keySeed == null) {
            keySeed = System.getProperty("AES_SYS_KEY");
        }

        if (keySeed == null || keySeed.trim().length() == 0) {
            //设置默认种子
            keySeed = "abcd1234!@#$";
        }

        try {
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(keySeed.getBytes());
            //构造秘钥生成器，指定为AES算法，不区分大小写
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(secureRandom);

            //产生原始对称秘钥
            return generator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


}
