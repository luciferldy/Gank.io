package com.gank.io.presenter;

import android.app.Activity;
import android.text.TextUtils;

import com.gank.io.api.GankDailyService;
import com.gank.io.model.ContentItem;
import com.gank.io.ui.fragment.ISwipeRefreshFragment;
import com.gank.io.ui.view.IBaseView;
import com.gank.io.ui.view.IFragmentView;
import com.gank.io.util.GetRss;
import com.gank.io.util.Logger;
import com.gank.io.util.ParseRss;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Lucifer on 2016/7/16.
 */
public class NewsPresenter extends BasePresenter {

    private static final String LOG_TAG = NewsPresenter.class.getSimpleName();
    private boolean isLoading = false;

    public NewsPresenter(Activity activity, IBaseView view) {
        super(activity, view);
    }

    /**
     * load the daily news
     * @param date
     */
    @Deprecated
    public void loadNews(final String date) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                isLoading = true;
                String results = GetRss.getRssContent(date);
                if (TextUtils.isEmpty(results)) {
                    Logger.i(LOG_TAG, "getRssContent but no response.");
                    isLoading = false;
                    return;
                }
                ArrayList<ContentItem> mContents  = ParseRss.parseDailyContent(results);
                if (mContents == null || mContents.isEmpty()) {
                    Logger.i(LOG_TAG, "parseDailyContent but no result.");
                    isLoading = false;
                    return;
                }
                if (mView instanceof IFragmentView) {
                    ((IFragmentView) mView).fillData(mContents);
                }
                isLoading = false;
            }
        }).start();

    }


    Retrofit mRetrofit;
    GankDailyService mDailyService;
    Call<ResponseBody> mCall;

    /**
     * 使用 retrofit 加载内容的详细信息
     * @param date 日期
     */
    public synchronized void getNewsRetrofit(final String date) {
        isLoading = true;
        if (mRetrofit == null)
            mRetrofit = new Retrofit.Builder()
                .baseUrl(GetRss.API_DAILY_URL)
                .build();
        if (mDailyService == null)
            mDailyService = mRetrofit.create(GankDailyService.class);
        if (mCall != null && mCall.isExecuted())
            mCall.cancel();  // Cancel 掉上一个 call.
        mCall = mDailyService.getGankDaily(date);
        mCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String result;
                try {
                    result = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                    ((ISwipeRefreshFragment) mView).onComplete();
                    return;
                }

                Logger.i(LOG_TAG, "onResponse result=" + result);
                if (TextUtils.isEmpty(result)) {
                    Logger.i(LOG_TAG, "result is empty.");
                    isLoading = false;
                    ((ISwipeRefreshFragment) mView).onComplete();
                    return;
                }
                ArrayList<ContentItem> contentItems = ParseRss.parseDailyContent(result);
                if (contentItems == null || contentItems.isEmpty()) {
                    Logger.i(LOG_TAG, "parseDailyContent but no result.");
                    isLoading = false;
                    ((ISwipeRefreshFragment) mView).onComplete();
                    return;
                }
                if (mView instanceof IFragmentView) {
                    ((IFragmentView) mView).fillData(contentItems);
                }
                isLoading = false;
                ((ISwipeRefreshFragment) mView).onComplete();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Logger.i(LOG_TAG, "onFailure");
                isLoading = false;
                ((ISwipeRefreshFragment) mView).onComplete();
            }
        });
    }

    @Override
    public synchronized void onDestroy() {
        super.onDestroy();
        if (mCall != null && !mCall.isCanceled()) {
            mCall.cancel();
        }
    }

    public boolean isLoading() {
        return isLoading;
    }
}
