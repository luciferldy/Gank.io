package com.gank.io.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gank.io.R;
import com.gank.io.model.ContentItem;
import com.gank.io.presenter.BasePresenter;
import com.gank.io.presenter.NewsPresenter;
import com.gank.io.ui.view.IFragmentView;
import com.gank.io.util.FragmentUtils;
import com.gank.io.util.GetRss;
import com.gank.io.util.ParseRss;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lucifer on 16-1-5.
 */
public class NewsFragment extends Fragment implements IFragmentView{

    private static final String LOG_TAG = NewsFragment.class.getSimpleName();
    private HashMap<String, ArrayList<ContentItem>> mContents;
    private BasePresenter presenter;
    private LinearLayout rssContent;
    private SimpleDraweeView rssImg;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.news_content, container, false);
        rssContent = (LinearLayout) root.findViewById(R.id.content_meizhi_rss);
        rssImg = (SimpleDraweeView) root.findViewById(R.id.content_meizhi_img);
        Bundle bundle = getArguments();
        String year, month, day;
        if (bundle != null) {
            year = bundle.getString("year");
            month = bundle.getString("month");
            day = bundle.getString("day");
            ((NewsPresenter) presenter).loadNews(year + "/" + month + "/" + day);
        }
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void initPresenter() {
        presenter = new NewsPresenter(getActivity(), this);
    }

    @Override
    public void onBackPressed() {
        FragmentUtils.popBackStack(getActivity());
    }

    @Override
    public void fillData(final List data) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mContents = (HashMap<String, ArrayList<ContentItem>>) data;
                if (mContents.get(ContentItem.MEI_ZHI) != null) {
                    mContents.get(ContentItem.MEI_ZHI).get(0);
                    Uri imgUri = Uri.parse(mContents.get(ContentItem.MEI_ZHI).get(0).getUrl());
                    rssImg.setImageURI(imgUri);
                }
                for (Map.Entry<String, ArrayList<ContentItem>> entry : mContents.entrySet()) {
                    Log.d(LOG_TAG, "key: " + entry.getKey() + " value： " + entry.getValue());
                    if (entry.getKey().equals(ContentItem.MEI_ZHI)) {
                        continue;
                    }
                    TextView keyTv = new TextView(getContext());
                    keyTv.setText(entry.getKey());
                    rssContent.addView(keyTv);
                    RecyclerView itemRv = new RecyclerView(getContext());
                    itemRv.setLayoutManager(new WrappingLinearLayoutManager(getContext()));
                    itemRv.setNestedScrollingEnabled(false);
                    itemRv.setHasFixedSize(false);
                    RssViewAdapter adapter = new RssViewAdapter(entry.getValue());
                    itemRv.setAdapter(adapter);
                    rssContent.addView(itemRv);
                }
            }
        });

    }

    // recycleView 的适配器
    private class RssViewAdapter extends RecyclerView.Adapter<RssItemViewHolder>{

        private ArrayList<ContentItem> rssItem;

        public RssViewAdapter(ArrayList<ContentItem> rssItem) {
            this.rssItem = rssItem;
        }

        @Override
        public void onBindViewHolder(RssItemViewHolder holder, final int position) {
            holder.rssItemDesc.setText(rssItem.get(position).getDesc());
            holder.rssItemDesc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(LOG_TAG, "click recycle view position " + position);
                }
            });
        }

        @Override
        public RssItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View root = layoutInflater.inflate(R.layout.rss_item, parent, false);
            RssItemViewHolder holder = new RssItemViewHolder(root);
            return holder;
        }

        @Override
        public int getItemCount() {
            return rssItem.size();
        }

    }

    private class RssItemViewHolder extends RecyclerView.ViewHolder{

        public TextView rssItemDesc;

        public RssItemViewHolder(View itemView) {
            super(itemView);
            rssItemDesc = (TextView) itemView.findViewById(R.id.rss_item_desc);
        }
    }

    // 自定义的 LinearLayoutManager, 用作 NestedScrollView 中嵌套 RecycleView
    public class WrappingLinearLayoutManager extends LinearLayoutManager {

        public WrappingLinearLayoutManager(Context context) {
            super(context);
        }

        private int[] mMeasuredDimension = new int[2];

        @Override
        public boolean canScrollVertically() {
            return false;
        }

        @Override
        public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state,
                              int widthSpec, int heightSpec) {
            final int widthMode = View.MeasureSpec.getMode(widthSpec);
            final int heightMode = View.MeasureSpec.getMode(heightSpec);

            final int widthSize = View.MeasureSpec.getSize(widthSpec);
            final int heightSize = View.MeasureSpec.getSize(heightSpec);

            int width = 0;
            int height = 0;
            for (int i = 0; i < getItemCount(); i++) {
                if (getOrientation() == HORIZONTAL) {
                    measureScrapChild(recycler, i,
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                            heightSpec,
                            mMeasuredDimension);

                    width = width + mMeasuredDimension[0];
                    if (i == 0) {
                        height = mMeasuredDimension[1];
                    }
                } else {
                    measureScrapChild(recycler, i,
                            widthSpec,
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                            mMeasuredDimension);

                    height = height + mMeasuredDimension[1];
                    if (i == 0) {
                        width = mMeasuredDimension[0];
                    }
                }
            }

            switch (widthMode) {
                case View.MeasureSpec.EXACTLY:
                    width = widthSize;
                case View.MeasureSpec.AT_MOST:
                case View.MeasureSpec.UNSPECIFIED:
            }

            switch (heightMode) {
                case View.MeasureSpec.EXACTLY:
                    height = heightSize;
                case View.MeasureSpec.AT_MOST:
                case View.MeasureSpec.UNSPECIFIED:
            }

            setMeasuredDimension(width, height);
        }

        private void measureScrapChild(RecyclerView.Recycler recycler, int position, int widthSpec,
                                       int heightSpec, int[] measuredDimension) {

            View view = recycler.getViewForPosition(position);
            if (view.getVisibility() == View.GONE) {
                measuredDimension[0] = 0;
                measuredDimension[1] = 0;
                return;
            }
            // For adding Item Decor Insets to view
            super.measureChildWithMargins(view, 0, 0);
            RecyclerView.LayoutParams p = (RecyclerView.LayoutParams) view.getLayoutParams();
            int childWidthSpec = ViewGroup.getChildMeasureSpec(
                    widthSpec,
                    getPaddingLeft() + getPaddingRight() + getDecoratedLeft(view) + getDecoratedRight(view),
                    p.width);
            int childHeightSpec = ViewGroup.getChildMeasureSpec(
                    heightSpec,
                    getPaddingTop() + getPaddingBottom() + getDecoratedTop(view) + getDecoratedBottom(view),
                    p.height);
            view.measure(childWidthSpec, childHeightSpec);

            // Get decorated measurements
            measuredDimension[0] = getDecoratedMeasuredWidth(view) + p.leftMargin + p.rightMargin;
            measuredDimension[1] = getDecoratedMeasuredHeight(view) + p.bottomMargin + p.topMargin;
            recycler.recycleView(view);
        }
    }
}
