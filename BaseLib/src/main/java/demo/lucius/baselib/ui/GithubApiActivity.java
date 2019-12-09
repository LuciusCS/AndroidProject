package demo.lucius.baselib.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.List;

import demo.lucius.baselib.R;
import demo.lucius.baselib.api.ApiResponse;
import demo.lucius.baselib.api.GithubService;
import demo.lucius.baselib.api.adapter.LiveDataCallAdapterFactory;
import demo.lucius.baselib.bean.RepoBean;
import demo.lucius.baselib.bean.SearchRepoBean;
import demo.lucius.baselib.databinding.ActivityGithubServiceBinding;
import demo.lucius.baselib.module.DBBaseActivity;
import demo.lucius.baselib.viewmodel.BaseViewModel;
import demo.lucius.utilslib.log.LogUtils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GithubApiActivity extends DBBaseActivity<ActivityGithubServiceBinding, BaseViewModel> {

    ActivityGithubServiceBinding databinding;

    GithubService githubService;

    LiveData<ApiResponse<SearchRepoBean>> repoBeanLiveData = new LiveData<ApiResponse<SearchRepoBean>>() {
    };

    MediatorLiveData<SearchRepoBean> result = new MediatorLiveData<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databinding = putContentView(R.layout.activity_github_service);
        initRetrofit();


        databinding.getInfoTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                repoBeanLiveData = githubService.getRepoInfo("Android");
//                result.addSource(repoBeanLiveData, new Observer<SearchRepoBean>() {
//                    @Override
//                    public void onChanged(SearchRepoBean searchRepoBean) {
//                        LogUtils.printInfo(searchRepoBean.getItems().size()+"+++++++++++++++");
//                        LogUtils.printInfo("++++++++++++++++");
//                    }
//                });
                LogUtils.printInfo("++++++++++++++++");
                repoBeanLiveData.observe(GithubApiActivity.this, new Observer<ApiResponse<SearchRepoBean>>() {
                    @Override
                    public void onChanged(ApiResponse<SearchRepoBean> searchRepoBeanApiResponse) {
                        SearchRepoBean searchRepoBean = (SearchRepoBean) ((ApiResponse.ApiResponseSuccess) searchRepoBeanApiResponse).getBody();
                        LogUtils.printInfo("++++++++++++++++");
                    }
                });

//                Call<SearchRepoBean> call = githubService.getRepoInfo("Android");
////                LogUtils.printInfo(call.toString());
//                //同步请求方式
//                call.request();
//                //异步请求方式
//                call.enqueue(new Callback<SearchRepoBean>() {
//                    @Override
//                    public void onResponse(Call<SearchRepoBean> call, Response<SearchRepoBean> response) {
//                        LogUtils.printInfo("搜索到的仓库的数量：" + response.body().getItems().size());
//                        List<RepoBean>repoBeans=new ArrayList<>();
//                        repoBeans.addAll(response.body().getItems());
//                        for (int i=repoBeans.size()-1;i>=0;i--){
//                            LogUtils.printInfo(repoBeans.get(i).getFullName());
//                        }
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<SearchRepoBean> call, Throwable t) {
//
//                    }
//                });

            }
        });
    }


    //用于初始化Retrofit
    private void initRetrofit() {
        //使用OkHttp官方拦截器，也可以自定义拦截器，可设置只在Debug模式下进行
//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);//这里可以选择拦截级别
//
//        //设置 Debug Log 模式
//        builder.addInterceptor(loggingInterceptor);
//        OkHttpClient client = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build();



        githubService = retrofit.create(GithubService.class);


    }


    @Override
    protected int getBindingVariable() {
        return 0;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected BaseViewModel getViewModel() {
        return null;
    }
}
