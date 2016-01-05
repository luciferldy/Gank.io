package com.gank.io.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gank.io.R;
import com.gank.io.task.LoadMeizhiTask;
import com.gank.io.util.ContentItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by lucifer on 16-1-4.
 */
public class NewsAdapter  extends RecyclerView.Adapter<NewsAdapter.NewsAdapterViewHolder> {

    private static final String LOG_TAG = NewsAdapter.class.getSimpleName();
    private ArrayList<HashMap<String, String>> meiZhis;

    public NewsAdapter(ArrayList<HashMap<String, String>> meiZhis) {
        this.meiZhis = meiZhis;
    }

    @Override
    public NewsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mLayoutInflater = LayoutInflater.from(parent.getContext());
        View root = mLayoutInflater.inflate(R.layout.news_item, parent, false);
        TextView newsDesc = (TextView)root.findViewById(R.id.news_desc);
        SimpleDraweeView newsImg = (SimpleDraweeView)root.findViewById(R.id.news_img);
        NewsAdapterViewHolder navh = new NewsAdapterViewHolder(root);
        navh.newsDes = newsDesc;
        navh.newsImg = newsImg;
        return navh;
    }

    @Override
    public void onBindViewHolder(NewsAdapterViewHolder holder, final int position) {
        holder.newsDes.setText(meiZhis.get(position).get(ContentItem.DESC));
        holder.newsDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String publishDate = meiZhis.get(position).get(ContentItem.PUBLISHED_AT);
                // 下面方法总是抛出异常,2016-01-04T02:49:48.409Z最后一位Z解析出错
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                try {
                    Date date = dateFormat.parse(publishDate);
                    String year = (new SimpleDateFormat("yyyy")).format(date);
                    String month = (new SimpleDateFormat("MM")).format(date);
                    String day = (new SimpleDateFormat("dd")).format(date);
                    Log.d(LOG_TAG, "year:" + year + " month:" + month + " day" + day);
                } catch (ParseException e) {
                    Log.d(LOG_TAG, "publish date is " + publishDate);
                    e.printStackTrace();
                }
            }});
            Uri uri = Uri.parse(meiZhis.get(position).get(ContentItem.URL));
            holder.newsImg.setImageURI(uri);
        }

        @Override
    public int getItemCount() {
        return meiZhis == null ? 0 : meiZhis.size();
    }

    public static class NewsAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView newsDes;
        SimpleDraweeView newsImg;

        NewsAdapterViewHolder(View view) {
            super(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("NormalTextViewHolder", "onClick--> position = " + getPosition());
                }
            });
        }
    }
}

