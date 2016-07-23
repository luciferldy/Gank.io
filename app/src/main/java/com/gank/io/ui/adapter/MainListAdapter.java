package com.gank.io.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.gank.io.R;
import com.gank.io.model.ContentItem;
import com.gank.io.util.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucifer on 16-1-4.
 */
public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.MainAdapterViewHolder> {

    private static final String LOG_TAG = MainListAdapter.class.getSimpleName();
    private ArrayList<ContentItem> mMeizhis;
    private Context mContext;
    private IClickMainItem mIClickItem;

    public MainListAdapter(Context context) {
        this.mMeizhis = new ArrayList<>();
        this.mContext = context;
    }

    @Override
    public MainAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mLayoutInflater = LayoutInflater.from(parent.getContext());
        View root = mLayoutInflater.inflate(R.layout.main_item, parent, false);
        MainAdapterViewHolder navh = new MainAdapterViewHolder(root);
        return navh;
    }

    @Override
    public void onBindViewHolder(MainAdapterViewHolder holder, final int position) {
        final ContentItem item = mMeizhis.get(position);
        holder.newsDes.setText(item.getDesc());
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

    public void setClickItem(IClickMainItem clickItem) {
        this.mIClickItem = clickItem;
    }

    public static class MainAdapterViewHolder extends RecyclerView.ViewHolder {

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
}

