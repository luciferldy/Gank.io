package com.gank.io.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gank.io.R;
import com.gank.io.model.ContentItem;
import com.gank.io.util.Logger;
import com.gank.io.util.StringStyleUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Lucifer on 2016/7/18.
 */
public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolderItem> {

    private static final String TAG = NewsListAdapter.class.getSimpleName();
    private Context mContext;
    private List<ContentItem> items;
    private static IClickNewsItem mIClickItem;

    /**
     * the type of recycle view item
     */
    private enum EItemType {
        ITEM_TYPE_GIRL,
        ITEM_TYPE_CATEGORY,
        ITEM_TYPE_NORMAL
    }

    public NewsListAdapter(Context context) {
        this.mContext = context;
        items = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        ContentItem item = items.get(position);
        if (item.isCategory()) {
            return EItemType.ITEM_TYPE_CATEGORY.ordinal();
        }  else if (item.getType().equals(ContentItem.MEI_ZHI) && !item.isCategory()) {
            return EItemType.ITEM_TYPE_GIRL.ordinal();
        } else {
            return EItemType.ITEM_TYPE_NORMAL.ordinal();
        }
    }

    @Override
    public ViewHolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == EItemType.ITEM_TYPE_GIRL.ordinal()) {
            view = LayoutInflater.from(mContext).inflate(R.layout.gank_item_meizhi, parent, false);
            return new ViewHolderItemGirl(view);
        } else if (viewType == EItemType.ITEM_TYPE_CATEGORY.ordinal()) {
            view = LayoutInflater.from(mContext).inflate(R.layout.gank_item_category, parent, false);
            return new ViewHolderItemCategory(view);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.gank_item_normal, parent, false);
            return new ViewHolderItemNormal(view);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolderItem holder, int position) {
        holder.bindItem(mContext, items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * 外部调用，刷新数据
     * @param list
     */
    public void updateData(ArrayList<ContentItem> list) {

        items.clear();
        items.addAll(list);
        notifyDataSetChanged();

    }


    /**
     * abstract class as parent class of three kind of items
     */
    abstract static class ViewHolderItem extends RecyclerView.ViewHolder {

        public ViewHolderItem(View itemView) {
            super(itemView);
        }

        abstract void bindItem(Context context, ContentItem item);

    }

    static class ViewHolderItemNormal extends ViewHolderItem {

        private TextView mTvTitle;
        private RelativeLayout mGankParent;

        public ViewHolderItemNormal(View itemView) {
            super(itemView);
            mGankParent = (RelativeLayout) itemView.findViewById(R.id.gank_normal_layout);
            mTvTitle = (TextView) itemView.findViewById(R.id.tv_gank_title);
        }

        @Override
        void bindItem(Context context, final ContentItem item) {
            mTvTitle.setText(StringStyleUtils.getGankInfoSequence(context, item));
            mGankParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mIClickItem.onClickGankItemNormal(item, view);
                }
            });
        }
    }

    static class ViewHolderItemCategory extends ViewHolderItem {

        private TextView mTvCategory;

        public ViewHolderItemCategory(View itemView) {
            super(itemView);
            mTvCategory = (TextView) itemView.findViewById(R.id.tv_category);
        }

        @Override
        void bindItem(Context context, ContentItem item) {
            mTvCategory.setText(item.getType());
        }
    }

    static class ViewHolderItemGirl extends ViewHolderItem {

        private TextView mTvTime;
        private SimpleDraweeView mGirlView;

        public ViewHolderItemGirl(View itemView) {
            super(itemView);
            mTvTime = (TextView) itemView.findViewById(R.id.tv_girl_tag);
            mGirlView = (SimpleDraweeView) itemView.findViewById(R.id.iv_girl);
        }

        @Override
        void bindItem(Context context, final ContentItem item) {
            Logger.i(TAG, "view holder bindItem item=" + item.toString());
            Date date = item.getPublishedAt();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            mTvTime.setText(format.format(date));
            Uri uri = Uri.parse(item.getUrl());
            mGirlView.setImageURI(uri);
            mGirlView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mIClickItem.onClickGankItemGirl(item, mGirlView, mTvTime);
                }
            });
        }
    }

    public void setIClickItem(IClickNewsItem item) {
        this.mIClickItem = item;
    }

    public interface IClickNewsItem {

        /**
         * click the girl pic
         * @param item
         * @param viewImg
         * @param viewText
         */
        void onClickGankItemGirl(ContentItem item, View viewImg, View viewText);

        /**
         * click the normal content
         * @param item
         * @param view
         */
        void onClickGankItemNormal(ContentItem item, View view);

    }
}
