package demo.lucius.javatestlib.time;


import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 用于AESUtil加密解密测试
 */
public class AESUtilControl {

    public static void main(String[] args){
        DateFormat formatter = new SimpleDateFormat("yyMMddhhmmss");
        long time = System.currentTimeMillis();
        long stopTimeStamp = time + 1000l * 60l * 60l * 24l * 15l;
        System.out.println("输出停止日期：" + formatter.format(new Date(stopTimeStamp)));
        System.out.println("输出停止日期时间戳：" + stopTimeStamp);

       String enText=AESUtil.encrypt(String.valueOf(stopTimeStamp));

       System.out.println("输出加密后的字符串"+enText);
       //用于表示解密的字符串
        String deText=AESUtil.decrypt("zyDB/j/55OP/4kv5xpgNXQ==");
        System.out.println("输出解密后的字符串"+deText);
        System.out.println("输出日期："+(Long.valueOf(deText)-time)/1000l/60l/60l/24l);

        Integer integer = null;
        if (integer==null){
            System.out.println("abc"+integer);
        }else {
            System.out.println("cde"+"=");
        }


        integer=1;

        int i ;
        if (integer==null){
            System.out.println(integer);
            System.out.println("cde"+"456465");
        }else {
            System.out.println(integer);
            System.out.println("cde"+"4565");
        }

    }
}
