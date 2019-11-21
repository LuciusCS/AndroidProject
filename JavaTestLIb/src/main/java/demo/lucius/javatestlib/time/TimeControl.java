package demo.lucius.javatestlib.time;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/***
 * 用于控制时间戳转化
 */
public class TimeControl {

    public static byte[] stringToByte(String code) {
        byte[] result = new byte[code.length() / 2];
        for (int i = 0; i < result.length; i++) {
            String subStr = code.substring(2 * i, 2 * i + 2);
            result[i] = ((byte) Integer.parseInt(subStr, 10));
        }
        return result;
    }

    public static String byteToString(byte[] bytes) {
        StringBuilder buf = new StringBuilder();
        //
        int length = bytes.length;
        for (int i = 0; i <= length - 1; i++) {
            int tmp = bytes[i] & 0xff;
            buf.append(appendCode(Integer.toString(tmp), 2));
        }
        return buf.toString();
    }

    public static String appendCode(String code, int length) {
        for (int i = length - code.length(); i > 0; i--) {
            code = "0" + code;
        }
        return code;
    }

    //用于乱序


    //


    public static void main(String[] args) throws Exception {

        DateFormat formatter = new SimpleDateFormat("yyMMddhhmmss");
        long time = System.currentTimeMillis();
        String info = formatter.format(new Date(time));
        System.out.println("输出当前日期：" + time);
        System.out.println("输出当前日期：" + info);

        //用于表示停止日期的时间戳
        long stopTimeStamp = time + 1000l * 60l * 60l * 24l * 35l;
        System.out.println("输出停止日期时间戳：" + stopTimeStamp);
        //加密用的时间，非加密的时间戳
        System.out.println("输出停止日期：" + formatter.format(new Date(stopTimeStamp)));


        String stopTimeInfo=formatter.format(new Date(stopTimeStamp));

        //加密Encryption
        //解密Decryption
        String tmpTimeEncryp = ByteEncrypt.Encrypt(String.valueOf(stopTimeInfo));
        System.out.println("截止时间加密：" + tmpTimeEncryp);
        String timeStampdecrypt = ByteEncrypt.Decrypt(tmpTimeEncryp);
        System.out.println("截止时间解密：" + timeStampdecrypt);


        //用于输出累计时间
        long relayTime =( Long.valueOf(formatter.parse(timeStampdecrypt).getTime()) - time);
//        System.out.println(Long.valueOf(timeStampdecrypt)+"输出相差天数：" + relayTime + "++++++" + timeStampdecrypt + "+++" + time);
        System.out.println("输出相差天数：" + relayTime / 1000l / 60l / 60l / 24l);

    }
}
