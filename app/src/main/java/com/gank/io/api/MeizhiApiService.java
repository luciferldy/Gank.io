package com.gank.io.api;

import com.gank.io.model.MeizhiGson;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Lucifer on 2016/8/7.
 * Call<T> get() retrofit 2.0 style
 */
public interface MeizhiApiService {

    @GET("{requestCount}/{pageNumber}")
    Call<MeizhiGson> getMeizhi(@Path("requestCount") int requestCount, @Path("pageNumber") int pageNumber);

}
