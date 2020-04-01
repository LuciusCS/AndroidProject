package demo.lucius.blemodule.protocol.utils;


import java.math.BigInteger;

public class ProtocolUtil {

    /**
     * 将Byte 转换为相应进制的字符串
     *
     * @param bytes 传入的byte数组
     * @param radix 需要转换的进制
     * @return
     */
    public static String binary(byte[] bytes, int radix) {

        String result = new BigInteger(1, bytes).toString(radix);

        return new BigInteger(1, bytes).toString(radix);    //1表示正整数
    }

    /**
     * 用于从byte数组指定位置截取信息，返回int
     *
     * @param data  用于传入的byte数组
     * @param start 用于标记开始位
     * @param end   用于标记结束位
     * @return
     */
    public static int getCode(byte data[], int start, int end) {
        int num = 0;
        //基数
        long cardinal = 1;
        for (int i = 0; i < end - start + 1; i++) {
            //将字节数组转化为16进制字符串
            String s = Integer.toHexString(data[start + i] & 0xFF);
            //将16进制字符串转换为10进制
            num += Integer.parseInt(s, 16) * cardinal;
            cardinal *= 16 * 16;
        }
        return num;
    }

    /**
     * 从Byte数组指定位置截取相应的数组
     *
     * @param data
     * @param start
     * @param end
     * @return
     */
    public static byte[] cutOutByte(byte data[], int start, int end) {
        byte[] result = new byte[end - start + 1];
        System.arraycopy(data, start, result, 0, end - start + 1);
        return result;
    }

    //将Byte数组逆序
    public static byte[] reverseByteArray(byte data[]) {

        byte[] result = new byte[data.length];
        int length = data.length - 1;
        for (int i = data.length - 1; i >= 0; i--) {
            result[i] = data[length - i];
        }

        return data;
    }


    /**
     * String最高位补零，补充到指定长度
     *
     * @param code   需要补零的字符串
     * @param length 指定的长度
     * @return
     */
    public static String appendCode(String code, int length) {
        for (int i = length - code.length(); i > 0; i--) {
            code = "0" + code;
        }
        return code;
    }

    public static byte[] appendCode(byte[] code,int length){
        byte[] tmp=new byte[]{0};
        for (int i=length-code.length;i>0;i--){
            code=byteMerge(tmp,code);
        }
        return code;
    }

    /**
     * 将16进制字符串转换为字节数组
     *
     * @param code
     * @return
     */
    public static byte[] hexStringToByte(String code) {
        byte[] result = new byte[code.length() / 2];
        for (int i = 0; i < result.length; i++) {
            String subStr = code.substring(2 * i, 2 * i + 2);
            result[i] = ((byte) Integer.parseInt(subStr, 16));
        }
        return result;
    }

    public static byte[] hexStringToByteAdd33(String code) {
        byte[] result = new byte[code.length() / 2];
        for (int i = 0; i < result.length; i++) {
            String subStr = code.substring(2 * i, 2 * i + 2);
            result[i] = ((byte) (Integer.parseInt(subStr, 16) + 0x33));
        }
        return result;
    }

    /**
     * 将字节数组转换为16进制字符串
     *
     * @param bytes
     * @return
     */
    public static String byteToHexString(byte[] bytes) {
        StringBuilder buf = new StringBuilder();
        //
        int length = bytes.length;
        for (int i = 0; i <= length - 1; i++) {
            int tmp = bytes[i] & 0xff;
            buf.append(appendCode(Integer.toHexString(tmp), 2));
        }
        return buf.toString();
    }



    public static String byteToHexStringSpace(byte[] bytes) {
        StringBuilder buf = new StringBuilder();
        //
        int length = bytes.length;
        for (int i = 0; i <= length - 1; i++) {
            int tmp = bytes[i] & 0xff;
            buf.append(appendCode(Integer.toHexString(tmp), 2)+" ");
        }
        return buf.toString();
    }

    /**
     * 用于转换16进制减33
     *
     * @param bytes
     * @return
     */
    public static String byteToHexStringCut33(byte[] bytes) {
        StringBuilder buf = new StringBuilder();
        //
        int length = bytes.length;
        for (int i = 0; i <= length - 1; i++) {
            int tmp = (bytes[i] & 0xff) - 0x33;
            buf.append(appendCode(Integer.toHexString(tmp), 2));
        }
        return buf.toString();
    }

    //将指定位置的字节数组转换为16进制字符串
    public static String cutByteToHexString(byte[] bytes, int start, int end) {
        String info = "";

        info = byteToHexString(cutOutByte(bytes, start, end));

        return info;

    }



    /**
     * 用于合并两个byte数组
     * @param byte1
     * @param byte2
     * @return
     */
    public static  byte[] combineByte(byte[] byte1,byte[] byte2){
        byte[] tmp=new byte[byte1.length+byte2.length];
        System.arraycopy(byte1,0,tmp,0,byte1.length);
        System.arraycopy(byte2,0,tmp,byte1.length,byte2.length);

        return  tmp;
    }

    /**
     * 用于Cspg16
     * 校验位生工具类
     * 用于16进制字符串生成校验位,返回值为 String 一个字节 “00” 格式的16进制，算术和校验
     *
     * @param code
     */
    public static String generateCheckSum(String code) {
        int checkSum = 0;
        for (int i = 0; i < code.length() / 2; i++) {
            String subStr = code.substring(2 * i, 2 * i + 2);
            checkSum += Integer.parseInt(subStr, 16);
        }
        checkSum %= 127;
        return appendCode(Integer.toHexString(checkSum), 2);
        //return appendCode( Hex.toHexString(result),2);
    }

    /**
     * 用于Cspg16
     * 确定收到报文校验位是否正确
     *
     * @param code
     * @param cs
     * @return
     */
    public static Boolean checkCs(String code, String cs) {
        return generateCheckSum(code).equals(cs);

    }

    /**
     * 用于将一个字节格式进行逆序，如"E8000301" -》"010300E8"
     *
     * @param code
     * @return
     */
    public static String reverseCode(String code) {
        StringBuilder tmp = new StringBuilder();
        for (int i = code.length() - 2; i >= 0; i -= 2) {
            tmp.append(code.substring(i, i + 2));
        }
        return tmp.toString();
    }

    /***
     * 用于信息类数据DT即Fn的转化,返回值为String
     * @param fn
     * @return
     */
    public static String getDt(int fn) {
        int dt = (fn - 1) / 8 + (1 << ((fn - 1) % 8)) * 256;
        String s1 = Integer.toHexString(dt).toUpperCase();
        for (int i = 4 - s1.length(); i > 0; i--) {
            s1 = "0".concat(s1).toUpperCase();
        }
        return s1;
    }


    /**
     * 将指定位置的byte[] 转换为减 33十进制
     *
     * @param bytes
     * @param start
     * @param end
     * @return
     */
    public static String cutByteToBCDString(byte[] bytes, int start, int end) {
        return byteToBCDString(cutOutByte(bytes, start, end));
    }


    /**
     * 用于将byte转为BCD码 减16进制 33
     *
     * @param bytes
     * @return
     */
    public static String byteToBCDString(byte[] bytes) {
        StringBuilder buf = new StringBuilder();
        //
        int length = bytes.length;
        for (int i = 0; i <= length - 1; i++) {
            int tmp = ((bytes[i] & 0xff + 0x100) - 0x33) & 0xff;
            buf.append(appendCode(Integer.toHexString(tmp), 2));
        }
        return buf.toString();

    }

    /**
     * 给BCD码添加小数点，正序
     *
     * @param bytes
     * @param dot
     * @return
     */
    public static String byteToBCDDotInfo(byte[] bytes, int dot) {
        return stringBCDDotInfo(byteToBCDString(bytes), dot);
    }

    /**
     * 给BCD码添加小数点倒叙
     *
     * @param bytes
     * @param dot
     * @return
     */
    public static String byteToBCDReverseDotInfo(byte[] bytes, int dot) {
        return stringBCDDotInfo(reverseCode(byteToBCDString(bytes)), dot);
    }

    /**
     * 用于获取带小数点的BCD码
     *
     * @param s
     * @param dot 表示小数点位置
     * @return
     */
    public static String stringBCDDotInfo(String s, int dot) {
        if (dot == 0) {
            return s;
        }
        StringBuilder stringBuilder = new StringBuilder(s);
        stringBuilder.insert(s.length() - dot, '.');
        return stringBuilder.toString();
    }


    /**
     * 将byte转为Ascii
     *
     * @param bytes
     * @return
     */
    public static String byteToAscii(byte[] bytes) {
        StringBuilder buf = new StringBuilder();
        int length = bytes.length;
        for (int i = 0; i <= length - 1; i++) {
            char tmp = (char) (bytes[i] - 0x33);
            buf.append(tmp);
        }
        return buf.toString();
    }

    //用于添加年月日单位
    public static String addPerUnitToString(String data, String perUnit) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < perUnit.length() - 1; i++) {
            buf.append(data.substring(2 * i, 2 * i + 2));
            buf.append(perUnit.subSequence(i, i + 1));
        }
        if ((perUnit.length()) * 2 < data.length()) {
            buf.append(data.substring((perUnit.length() - 1) * 2, data.length() - 1));
        }
        return buf.toString();
    }


    //用于合并byte[]数组
    public static byte[] byteMerge(byte[] bt1, byte[] bt2) {

        byte[] bt3=new byte[bt1.length+bt2.length];
        System.arraycopy(bt1,0,bt3,0,bt1.length);
        System.arraycopy(bt2,0,bt3,bt1.length,bt2.length);

        return bt3;
    }

    //用于微断将整个ASSCII码进行倒叙
    public static String reverseAscii(String ascii){
        StringBuilder tmp = new StringBuilder();
        for (int i = ascii.length() - 1; i >= 0; i --) {
            tmp.append(ascii.substring(i, i +1));
        }
        return tmp.toString();
    }

}

