package com.gank.io.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gank.io.R;
import com.gank.io.api.GankService;
import com.gank.io.model.ContentItem;
import com.gank.io.util.CallableHashMap;
import com.gank.io.util.GetRss;
import com.gank.io.util.Logger;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by lucifer on 16-1-4.
 */
public class MainRvAdapter extends RecyclerView.Adapter<MainRvAdapter.MainAdapterViewHolder> {

    private static final String LOG_TAG = MainRvAdapter.class.getSimpleName();
    private ArrayList<ContentItem> mMeizhis;
    private IClickMainItem mIClickItem;
    private Retrofit mRetrofit;
    private GankService mGankService;
    private CallableHashMap mDesMap;

    public MainRvAdapter(Context context) {
        this.mMeizhis = new ArrayList<>();
        mDesMap = new CallableHashMap();
    }

    @Override
    public MainAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mLayoutInflater = LayoutInflater.from(parent.getContext());
        View root = mLayoutInflater.inflate(R.layout.main_item, parent, false);
        MainAdapterViewHolder navh = new MainAdapterViewHolder(root);
        return navh;
    }

    @Override
    public void onBindViewHolder(final MainAdapterViewHolder holder, final int position) {
        final ContentItem item = mMeizhis.get(position);
        // 改成发布时间
        Date date = item.getPublishedAt();
        String des;
        try {
            DateFormat format = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
            des = format.format(date);
            if (!mDesMap.keySet().contains(des)) {
                getTitle(des, new CallableHashMap.Callback() {
                    TextView tv = holder.newsDes;
                    @Override
                    public void update(String key, String value) {
                        tv.setText(key + " 推荐：" + value);
                        tv = null; // 消除 tv 的引用
                    }

                    @Override
                    public void failed() {
                        tv = null; // 同样消除 tv 的引用
                    }
                });
            } else {
                des = des + " 推荐：" + mDesMap.get(des);
            }
        } catch (Exception e) {
            Logger.i(LOG_TAG, "Date cast to String occur exception.");
            e.printStackTrace();
            des = item.getDesc();
        }
        holder.newsDes.setText(des);
        holder.newsDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIClickItem.onClickGankItem(item);
            }
        });
        Uri uri = Uri.parse(item.getUrl());
        holder.newsImg.setImageURI(uri);
        holder.newsImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIClickItem.onClickGankItemGirl(item);
            }
        });
    }

        @Override
    public int getItemCount() {
        return mMeizhis == null ? 0 : mMeizhis.size();
    }

    /**
     * 更新数据
     * @param data
     */
    public void update(List data) {
        mMeizhis.clear();
        try {
            mMeizhis.addAll(data);
            notifyDataSetChanged();
        } catch (ClassCastException e) {
            Logger.i(LOG_TAG, "update meet class cast error.");
            e.printStackTrace();
        }
    }

    /**
     * 填充新的数据
     * @param data
     */
    public void appendData(List data) {
        try {
            mMeizhis.addAll(data);
            notifyDataSetChanged();
        } catch (Exception e) {
            Logger.i(LOG_TAG, "appendData occur an error.");
            e.printStackTrace();
        }
    }

    public void setClickItem(IClickMainItem clickItem) {
        this.mIClickItem = clickItem;
    }

    static class MainAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView newsDes;
        SimpleDraweeView newsImg;

        MainAdapterViewHolder(View view) {
            super(view);
            newsDes = (TextView) view.findViewById(R.id.tv_meizhi);
            newsImg = (SimpleDraweeView) view.findViewById(R.id.iv_meizhi);
        }
    }

    /**
     * click the recycle view interface
     */
    public interface IClickMainItem{

        /**
         * click the pic
         */
        void onClickGankItemGirl(ContentItem item);

        /**
         * click the item
         */
        void onClickGankItem(ContentItem item);
    }

    /**
     * 获得某天的 html 的 title.
     * @param date 格式需要为 yyyy/MM/dd
     */
    private void getTitle(final String date, final CallableHashMap.Callback callback) {
        if (TextUtils.isEmpty(date))
            return;
        if (mRetrofit == null)
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(GetRss.API_GANK)
                    .build();
        if (mGankService == null)
            mGankService = mRetrofit.create(GankService.class);
        Call<ResponseBody> call = mGankService.getGank(date);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response == null ) {
                    callback.failed();
                    return;
                }
                try {
                    String result = response.body().string();
                    Pattern pattern = Pattern.compile("<title>(.*?)</title>");
                    Matcher matcher = pattern.matcher(result);
                    if (matcher.find()) {
                        Logger.i(LOG_TAG, "match date: " + date + ", " + matcher.group(1));
                        mDesMap.put(date, matcher.group(1).replace("今日力推：", ""), callback);
                    } else {
                        Logger.i(LOG_TAG, "response.body: " + result);
                        callback.failed();
                    }
                } catch (IOException e) {
                    Logger.e(LOG_TAG, "get title failed", e);
                    callback.failed();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Logger.i(LOG_TAG, "getTitle onFailure, throwable = " + t.getMessage());
                callback.failed();
            }
        });
    }
}

