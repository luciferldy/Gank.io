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

/**
 * Created by lucifer on 16-1-4.
 */
public class NewsAdapter  extends RecyclerView.Adapter<NewsAdapter.NewsAdapterViewHolder> {
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private String[] mTitles;

    public NewsAdapter(Context context) {
        mTitles = new String[]{"Android", "IOS", "Java", "C", "Objective C", "Python", "Ruby", "JavaScript"};
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public NewsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = mLayoutInflater.inflate(R.layout.news_item, parent, false);
        TextView newsDesc = (TextView)root.findViewById(R.id.news_desc);
        SimpleDraweeView newsImg = (SimpleDraweeView)root.findViewById(R.id.news_img);
        NewsAdapterViewHolder navh = new NewsAdapterViewHolder(root);
        navh.newsDes = newsDesc;
        navh.newsDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        navh.newsImg = newsImg;
        navh.newsImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Uri uri = Uri.parse("http://ww1.sinaimg.cn/large/7a8aed7bjw1ezn79ievhzj20p00odwhr.jpg");
        navh.newsImg.setImageURI(uri);
        return navh;
    }

    @Override
    public void onBindViewHolder(NewsAdapterViewHolder holder, int position) {
        holder.newsDes.setText(mTitles[position]);
    }

    @Override
    public int getItemCount() {
        return mTitles == null ? 0 : mTitles.length;
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

