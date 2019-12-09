package demo.lucius.javatestlib.pattern.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyHandler implements InvocationHandler {

    private Object tar;

    //绑定委托对象，并返回代理类
    public Object bind(Object tar){
        this.tar=tar;
        return Proxy.newProxyInstance(tar.getClass().getClassLoader(),tar.getClass().getInterfaces(),this);
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {

        Object result=null;
        //这里可以进行所谓的AOP编程
        //在调用具体函数方法项之前，执行功能处理
        result=method.invoke(tar,objects);
        //在调用具体函数方法项之后，执行功能处理
        return result;
    }
}
