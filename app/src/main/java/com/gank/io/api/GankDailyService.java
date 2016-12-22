package com.gank.io.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Lucifer on 2016/8/7.
 * 使用 retrofit 需定义的类
 * 对应的 url 为 {@link com.gank.io.util.GetRss} 中的
 * http://gank.io/api/day/
 */
public interface GankDailyService {

    @GET("{date}")
    Call<ResponseBody> getGankDaily(@Path("date") String date);

}
