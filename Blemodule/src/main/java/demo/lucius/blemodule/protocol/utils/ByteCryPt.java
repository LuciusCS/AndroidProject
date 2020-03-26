package demo.lucius.blemodule.protocol.utils;


import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


public class ByteCryPt {

    public static final String K1 = "";
    public static final String K2 = "";
    public static byte[] RANDOM_K2 = null;
    public static byte[] RANDOM_K2_MCU = null;

    // 加密
    public static byte[] Encrypt(byte[] payload, String sKey) throws Exception {
        if (sKey == null) {
            System.out.print("Key为空null");
            return null;
        }
        // 判断Key是否为16位
        if (sKey.length() != 16) {
            System.out.print("Key长度不是16位");
            return null;
        }
        byte[] raw = sKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");//"算法/模式/补码方式"
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(payload);
        return encrypted;


    }

    public static byte[] Encrypt(byte[] payload, byte[] sKey) throws Exception {
        if (sKey == null) {
            return null;
        }
        // 判断Key是否为16位
        if (sKey.length != 16) {
            return null;
        }
        SecretKeySpec skeySpec = new SecretKeySpec(sKey, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");//"算法/模式/补码方式"
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(payload);
        return encrypted;
    }


    // 解密
    public static byte[] Decrypt(byte[] sSrc, String sKey) throws Exception {
        try {
            // 判断Key是否正确
            if (sKey == null) {
                System.out.print("Key为空null");
                return null;
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                System.out.print("Key长度不是16位");
                return null;
            }
            byte[] raw = sKey.getBytes();
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            try {
                byte[] original = cipher.doFinal(sSrc);
                return original;
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }

    // 解密
    public static byte[] Decrypt(byte[] sSrc, byte[] sKey) throws Exception {
        try {
            // 判断Key是否正确
            if (sKey == null) {
                System.out.print("Key为空null");
                return null;
            }
            // 判断Key是否为16位
            if (sKey.length != 16) {
                System.out.print("Key长度不是16位");
                return null;
            }
            SecretKeySpec skeySpec = new SecretKeySpec(sKey, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            try {
                byte[] original = cipher.doFinal(sSrc);
                return original;
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }

    //生成随机数 (16长度字节数组)
    public static byte[] createRandomByte() {
        Random random = new Random();
        byte[] bytes = new byte[16];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) random.nextInt(256);
        }
        return bytes;
    }


    public static void main(String[] args) throws Exception {
        System.out.println("++++++++++++++++++++");
        byte[] randomByte =createRandomByte();
        RANDOM_K2 = Encrypt(randomByte, ByteCryPt.K2);

        byte[] tmpInfo = ProtocolUtil.hexStringToByte("234000d81277baddcaf7c33fa15e00da");
        byte[] tmpInfoDe = Encrypt(tmpInfo, RANDOM_K2);
        System.out.println(ProtocolUtil.byteToHexString(tmpInfoDe));
        byte[] tmpInfoRe=Decrypt(tmpInfoDe,RANDOM_K2);
        System.out.println(ProtocolUtil.byteToHexString(tmpInfoRe));

    }

}
