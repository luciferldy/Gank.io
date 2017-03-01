package com.gank.io.presenter;

import android.app.Activity;
import android.text.TextUtils;

import com.gank.io.api.MeizhiService;
import com.gank.io.model.ContentItem;
import com.gank.io.model.MeizhiGson;
import com.gank.io.ui.activity.ISwipeRefreshActivity;
import com.gank.io.ui.view.IBaseView;
import com.gank.io.ui.view.IMainView;
import com.gank.io.util.GetRss;
import com.gank.io.util.Logger;
import com.gank.io.util.ParseRss;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Lucifer on 2016/7/13.
 */
public class MainPresenter extends BasePresenter {

    private static final String LOG_TAG = MainPresenter.class.getSimpleName();
    private static final int REQUEST_COUNT = 10; // 请求次数
    private static int mPageNumber = 1; // 第几页，初始值为1，自增，可以大于请求次数
    private static boolean isLoadingData = false;
    Retrofit retrofit;
    MeizhiService mZService;
    Call<MeizhiGson> mMeizhiCall;


    public MainPresenter(Activity context, IBaseView view) {
        super(context, view);
    }

    /**
     * load meizhi information by UrlConnection
     * @boolean loadMore 是否加载更多
     * @LoadCallback callback 请求妹纸信息的回调接口，用来更新 swiperefreshlayout
     */
    @Deprecated
    public synchronized void loadMeizhi(final boolean loadMore, final LoadCallback callback) {
        // 网络访问请求妹纸图片
        new Thread(new Runnable() {
            @Override
            public void run() {
                Logger.i(LOG_TAG, "loadMeizhi");
                isLoadingData = true;
                if (loadMore) {
                    mPageNumber++;
                } else {
                    mPageNumber = 1;
                }
                String result = GetRss.getMeizhiImg(REQUEST_COUNT + "/" + mPageNumber);
                if (TextUtils.isEmpty(result)) {
                    Logger.i(LOG_TAG, "get meizhi img but no response.");
                    callback.onLoadFailed();
                    isLoadingData = false;
                    return;
                }
//                Logger.i(LOG_TAG, "getMeizhiImg response=" + result);
                ArrayList<ContentItem> items = ParseRss.parseMeizhi(result);
                if (items == null || items.isEmpty()) {
                    Logger.i(LOG_TAG, "parse meizhi but no result.");
                    callback.onLoadFailed();
                    isLoadingData = false;
                    return;
                }
                if (mView instanceof IMainView) {
                    callback.onLoadSuccess();
                    if (!loadMore)
                        ((IMainView) mView).fillData(items);
                    else {
                        ((IMainView) mView).appendMoreData(items);
                    }
                } else {
                    callback.onLoadFailed();
                }
                isLoadingData = false;
            }
        }).start();
    }

    /**
     * 使用 Retrofit 加载资源
     * @param loadMore 是否加载更多
     */
    public synchronized void getMeizhiRetrofit(final boolean loadMore) {
        isLoadingData = true;
        if (retrofit == null)
            retrofit = new Retrofit.Builder()
                .baseUrl(GetRss.API_MEIZHI_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        if (loadMore) {
            mPageNumber++;
        } else {
            mPageNumber = 1;
        }
        if (mZService == null)
            mZService = retrofit.create(MeizhiService.class);
        if (mMeizhiCall != null && mMeizhiCall.isExecuted())
            mMeizhiCall.cancel();  // Cancel 掉上一次的 Call
        mMeizhiCall = mZService.getMeizhi(REQUEST_COUNT, mPageNumber);
        mMeizhiCall.enqueue(new Callback<MeizhiGson>() {
            @Override
            public void onResponse(Call<MeizhiGson> call, Response<MeizhiGson> response) {
                List<MeizhiGson.ResultsBean> girls  = response.body().getResults();
                if (girls == null || girls.isEmpty()) {
                    Logger.i(LOG_TAG, "onResponse result is empty.");
                    isLoadingData = false;
                    ((ISwipeRefreshActivity) mView).onComplete();
                    return;
                }
                ArrayList<ContentItem> items = new ArrayList<>();
                for (MeizhiGson.ResultsBean bean : girls) {
                    Logger.i(LOG_TAG, bean.toString());
                    ContentItem item = new ContentItem();
                    item.setJsonBean(bean);
                    items.add(item);
                }
                if (!loadMore)
                    ((IMainView) mView).fillData(items);
                else {
                    ((IMainView) mView).appendMoreData(items);
                }
                isLoadingData = false;
                ((ISwipeRefreshActivity) mView).onComplete();
            }

            @Override
            public void onFailure(Call<MeizhiGson> call, Throwable t) {
                Logger.e(LOG_TAG, "onFailure", t);
                isLoadingData = false;
                ((ISwipeRefreshActivity) mView).onComplete();
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMeizhiCall != null && !mMeizhiCall.isCanceled()) {
            mMeizhiCall.cancel();
        }
    }

    public synchronized boolean isLoadingData() {
        return isLoadingData;
    }

    /**
     * 请求妹纸信息的回调
     * 更新 swiperefreshlayout
     */
    @Deprecated
    public interface LoadCallback {

        void onLoadSuccess();

        void onLoadFailed();
    }

}
