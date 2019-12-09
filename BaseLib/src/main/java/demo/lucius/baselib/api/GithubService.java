package demo.lucius.baselib.api;

import androidx.lifecycle.LiveData;

import demo.lucius.baselib.bean.SearchRepoBean;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GithubService {
    @GET("search/repositories")
    LiveData<ApiResponse<SearchRepoBean>> getRepoInfo(@Query("q") String query);
}
