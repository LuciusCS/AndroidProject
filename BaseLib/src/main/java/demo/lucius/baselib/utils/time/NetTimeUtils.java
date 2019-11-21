package demo.lucius.baselib.utils.time;


import android.net.UrlQuerySanitizer;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import demo.lucius.utilslib.log.LogUtils;

/**
 * 用于获取网络时间工具
 */
public class NetTimeUtils {

    public static Long getNetTime() throws ExecutionException, InterruptedException {

        //多线程的callable必须通过ExecutorService submit()调用来执行，搭配Future来使用
        ExecutorService exec = Executors.newCachedThreadPool();

        //用于获取时间的资源对象
        URL url= null;
        ArrayList<Future<Long>> results= new ArrayList<>();
        results.add(exec.submit(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                try {
                    URL url= null;
                    url=new URL("http://www.ntsc.ac.cn");//中国科学院国家授时中心
                    URLConnection urlConnection=url.openConnection();//生成连接对象
                    urlConnection.connect();//发出连接
                    long ld=urlConnection.getDate(); //用于获取网站的日期时间
                    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    String   time=formatter.format(ld);


                    return ld;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return (long)99999999;
            }
        }));

        return results.get(0).get();
    }

}
