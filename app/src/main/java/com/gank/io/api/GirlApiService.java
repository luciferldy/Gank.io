package com.gank.io.api;

import com.gank.io.model.GirlJson;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Lucifer on 2016/8/7.
 * Call<T> get() retrofit 2.0 之后的新样式
 */
public interface GirlApiService {

    @GET("{pageCount}/{pageNumber}")
    Call<GirlJson> getGirl(@Path("pageCount") int pageCount, @Path("pageNumber") int pageNumber);

}
