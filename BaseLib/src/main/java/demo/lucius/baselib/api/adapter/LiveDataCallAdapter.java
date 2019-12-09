package demo.lucius.baselib.api.adapter;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.lang.reflect.Type;

import demo.lucius.baselib.api.ApiResponse;
import demo.lucius.baselib.bean.SearchRepoBean;
import demo.lucius.utilslib.log.LogUtils;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveDataCallAdapter<R> implements CallAdapter<R,LiveData<ApiResponse<R>>> {

    private Type responseType;

    public LiveDataCallAdapter(Type responseType){
        this.responseType=responseType;
    }


    @Override
    public Type responseType() {
        return responseType;
    }

    @Override
    public LiveData<ApiResponse<R>> adapt(final Call<R> call) {
        LiveData<ApiResponse<R>> result=new LiveData<ApiResponse<R>>() {
            @Override
            protected void onActive() {
                super.onActive();
                call.enqueue(new Callback<R>() {
                    @Override
                    public void onResponse(Call<R> call, Response<R> response) {
                        Object object =response.body();
                       postValue(new ApiResponse<R>().create(response));
                    }

                    @Override
                    public void onFailure(Call<R> call, Throwable t) {
                        LogUtils.printInfo("+++++!2312qwqweww31");
                    }
                });
            }
        };

        //
        return result;
    }

//    @Override
//    public LiveData<SearchRepoBean> adapt(final Call<R> call) {
//
//         LiveData<SearchRepoBean>searchRepoBeanLiveData=new LiveData<SearchRepoBean>() {
//
//            @Override
//            protected void onActive() {
//                super.onActive();
//                call.enqueue(new Callback<R>() {
//
//                    @Override
//                    public void onResponse(Call<R> call, Response<R> response) {
//                        postValue((SearchRepoBean) response.body());
//                    }
//
//                    @Override
//                    public void onFailure(Call<R> call, Throwable t) {
//                        LogUtils.printInfo("error");
//                    }
//                });
//            }
//        };
//       return searchRepoBeanLiveData;
//    }
}
