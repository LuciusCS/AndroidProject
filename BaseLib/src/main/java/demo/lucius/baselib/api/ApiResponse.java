package demo.lucius.baselib.api;

import java.util.Map;

import retrofit2.Response;

public class ApiResponse<T> {

    public ApiResponseSuccess<T> create(Response<T> response) {
        if (response.isSuccessful()) {
            return new ApiResponseSuccess<T>(response.body());
        } else {
            return null;
        }
    }

    //用于表示返回成功的值
    public static class ApiResponseSuccess<T> extends ApiResponse<T> {

        private T body;

        public ApiResponseSuccess(T body) {
            this.body = body;
        }

        public T getBody() {
            return body;
        }
    }
}
