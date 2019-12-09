package demo.lucius.baselib.api.adapter;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import demo.lucius.baselib.bean.SearchRepoBean;
import demo.lucius.utilslib.log.LogUtils;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;

public class LiveDataCallAdapterFactory extends CallAdapter.Factory {

    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {

//        if (returnType)
        Type observerType = getParameterUpperBound(0, (ParameterizedType) returnType);

        Class<?> rawObserverType = getRawType(observerType);

//        if (observerType != SearchRepoBean.class) {
////            throw ;
//            LogUtils.printInfo("+++++++++++");
//            return null;
//        }
//
//        if (!rawObserverType.isAssignableFrom(ParameterizedType.class)) {
//            LogUtils.printInfo("+++++++++++");
//            return null;
//        }

        Type bodyType = getParameterUpperBound(0, (ParameterizedType) observerType);

        LiveDataCallAdapter<Object> liveDataCallAdapter = new LiveDataCallAdapter<>(bodyType);

        return liveDataCallAdapter;
    }
}
