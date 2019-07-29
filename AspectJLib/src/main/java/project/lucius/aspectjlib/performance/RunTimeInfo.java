package project.lucius.aspectjlib.performance;


import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

//用于对关键方法的运行时间进行测试
@Aspect
public class RunTimeInfo {

   private static final String TAG="RUN_TIME";

    @Around("execution(@project.lucius.aspectjlib.performance.RunTimeTrace * *(..))")
    public void TraceMethodTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        long startTime=System.currentTimeMillis();
        String key=proceedingJoinPoint.getSignature().toString();

        //@Around会替换原有代码，使用joinPoint.proceed()可以继续执行原有代码
        proceedingJoinPoint.proceed();
        long endTime=System.currentTimeMillis();
        Log.e(TAG,proceedingJoinPoint.getSignature().toString()+"方法用时："+(endTime-startTime));

    }

}
