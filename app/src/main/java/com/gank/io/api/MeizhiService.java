package com.gank.io.api;

import com.gank.io.model.MeizhiGson;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Lucifer on 2016/8/7.
 * Call<T> get() retrofit 2.0 style
 * 所使用的 url 为 {@link com.gank.io.util.GetRss} 中的
 * http://gank.io/api/data/%E7%A6%8F%E5%88%A9/
 */
public interface MeizhiService {

    @GET("{requestCount}/{pageNumber}")
    Call<MeizhiGson> getMeizhi(@Path("requestCount") int requestCount, @Path("pageNumber") int pageNumber);

}
