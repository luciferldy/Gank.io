package com.gank.io.api;

import com.gank.io.model.ContentItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Lucifer on 2016/8/7.
 */
public interface GankApiService {

    @GET("{requestCount}/{pageNumber}")
    Call<List<ContentItem>> getGank(@Path("requestCount") String requestCount, @Path("pageNumber") String pageNumber);

}
