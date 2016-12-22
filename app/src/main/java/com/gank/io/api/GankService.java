package com.gank.io.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Lucifer on 2016/12/21.
 * 请求的 Api 为 {@link com.gank.io.util.GetRss} 中的
 * http://gank.io/
 */

public interface GankService {
    @GET("{date}")
    Call<ResponseBody> getGank(@Path("date") String date);
}
