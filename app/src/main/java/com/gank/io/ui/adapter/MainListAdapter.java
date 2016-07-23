package com.gank.io.ui.adapter;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gank.io.R;
import com.gank.io.ui.fragment.ImgPreviewFragment;
import com.gank.io.ui.fragment.NewsFragment;
import com.gank.io.model.ContentItem;
import com.gank.io.util.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by lucifer on 16-1-4.
 */
public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.MainAdapterViewHolder> {

    private static final String LOG_TAG = MainListAdapter.class.getSimpleName();
    private ArrayList<HashMap<String, String>> meiZhis;
    private AppCompatActivity mainActivity;

    public MainListAdapter(ArrayList<HashMap<String, String>> meiZhis, AppCompatActivity activity) {
        this.meiZhis = meiZhis;
        this.mainActivity = activity;
    }

    @Override
    public MainAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mLayoutInflater = LayoutInflater.from(parent.getContext());
        View root = mLayoutInflater.inflate(R.layout.news_item, parent, false);
        TextView newsDesc = (TextView)root.findViewById(R.id.news_desc);
        SimpleDraweeView newsImg = (SimpleDraweeView)root.findViewById(R.id.news_img);
        MainAdapterViewHolder navh = new MainAdapterViewHolder(root);
        navh.newsDes = newsDesc;
        navh.newsImg = newsImg;
        return navh;
    }

    @Override
    public void onBindViewHolder(MainAdapterViewHolder holder, final int position) {
        holder.newsDes.setText(meiZhis.get(position).get(ContentItem.DESC));
        holder.newsDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String publishDate = meiZhis.get(position).get(ContentItem.PUBLISHED_AT);
                // 下面方法总是抛出异常,2016-01-04T02:49:48.409Z最后一位Z解析出错
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date = dateFormat.parse(publishDate);
                    String year = (new SimpleDateFormat("yyyy")).format(date);
                    String month = (new SimpleDateFormat("MM")).format(date);
                    String day = (new SimpleDateFormat("dd")).format(date);
                    Log.d(LOG_TAG, "year=" + year + " month=" + month + " day=" + day);
                    FragmentManager manager = mainActivity.getSupportFragmentManager();
                    NewsFragment newsItem = new NewsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("year", year);
                    bundle.putString("month", month);
                    bundle.putString("day", day);
                    newsItem.setArguments(bundle);
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.add(android.R.id.content, newsItem);
                    transaction.addToBackStack(NewsFragment.class.getSimpleName() + System.currentTimeMillis());
                    transaction.commit();
                } catch (ParseException e) {
                    Logger.i(LOG_TAG, "publish date is " + publishDate);
                    e.printStackTrace();
                }
            }
        });
        Uri uri = Uri.parse(meiZhis.get(position).get(ContentItem.URL));
        holder.newsImg.setImageURI(uri);
        holder.newsImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = mainActivity.getSupportFragmentManager();
                ImgPreviewFragment preview = new ImgPreviewFragment();
                Bundle bundle = new Bundle();
                bundle.putString(ContentItem.URL, meiZhis.get(position).get(ContentItem.URL));
                preview.setArguments(bundle);
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(android.R.id.content, preview);
                transaction.addToBackStack(ImgPreviewFragment.class.getSimpleName() + System.currentTimeMillis());
                transaction.commit();
            }
        });
    }

        @Override
    public int getItemCount() {
        return meiZhis == null ? 0 : meiZhis.size();
    }

    public static class MainAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView newsDes;
        SimpleDraweeView newsImg;

        MainAdapterViewHolder(View view) {
            super(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Logger.i("NormalTextViewHolder", "onClick--> position = " + getPosition());
                }
            });
        }
    }

    /**
     * click the recycle view interface
     */
    public interface IClickMainItem{

        /**
         * click the pic
         */
        void onClickGankItemGirl();

        /**
         * click the item
         */
        void onClickGankItem();
    }
}
