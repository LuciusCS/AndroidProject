package project.lucius.aspectjlib.trace;

import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

//用于对方法调用跟踪结果的操作
@Aspect
public class MethodTraceInfo {

    private static final String TAG="METHOD_TRACE";

    //对方法的执行添加切入点，并进行跟踪
    @Pointcut("execution(@project.lucius.aspectjlib.trace.MethodTrace * *(..))")
    public void traceMethod(){

    }

    //用于对方法调用跟踪的执行
    @Before("traceMethod()")
    public void beforeTraceMethod(JoinPoint joinPoint){
        String info=joinPoint.getSignature().toString();
        Log.e("METHOD",info);
    }


}
