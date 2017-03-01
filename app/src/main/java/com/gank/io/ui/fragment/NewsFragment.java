package com.gank.io.ui.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.gank.io.R;
import com.gank.io.model.ContentItem;
import com.gank.io.presenter.BasePresenter;
import com.gank.io.presenter.NewsPresenter;
import com.gank.io.ui.adapter.NewsListAdapter;
import com.gank.io.util.CommonUtils;
import com.gank.io.util.DateUtils;
import com.gank.io.util.FragmentUtils;
import com.gank.io.util.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucifer on 16-1-5.
 */
public class NewsFragment extends ISwipeRefreshFragment{

    private static final String LOG_TAG = NewsFragment.class.getSimpleName();
    private BasePresenter presenter;
    private RecyclerView mRvGank;
    private NewsListAdapter mRvAdapter;
    private String mPublishDate;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.news_content, container, false);

        // compact device after Android 5.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View view = root.findViewById(R.id.status_bar_holder);
            AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) view.getLayoutParams();
            params.height = CommonUtils.getStatusbarHeight(getContext());
            view.setLayoutParams(params);
            view.setVisibility(View.VISIBLE);
        }

        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        Bundle bundle = getArguments();
        if (bundle != null && !bundle.isEmpty()) {
            mPublishDate = bundle.getString(DateUtils.DATE);
            toolbar.setTitle(mPublishDate);
        }

        mRvGank = (RecyclerView) root.findViewById(R.id.rv_gank);
        mRvGank.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvAdapter = new NewsListAdapter(getContext());
        NewsListAdapter.IClickNewsItem clickNewsItem = new NewsListAdapter.IClickNewsItem() {
            @Override
            public void onClickGankItemGirl(ContentItem item, View viewImg, View viewText) {
                Bundle bundle = new Bundle();
                bundle.putString(ContentItem.URL, item.getUrl());
                Log.d(LOG_TAG, ContentItem.URL + "=" + item.getUrl());
                FragmentUtils.addFragment(new MeizhiPreviewFragment(), getActivity().getSupportFragmentManager()
                        , bundle, FragmentUtils.FragmentAnim.FADE, true);
            }

            @Override
            public void onClickGankItemNormal(ContentItem item, View view) {
                Bundle bundle = new Bundle();
                bundle.putString(ContentItem.URL, item.getUrl());
                Logger.i(LOG_TAG, ContentItem.URL + "=" + item.getUrl());
                FragmentUtils.addFragment(new WebFragment(), getActivity().getSupportFragmentManager()
                , bundle, FragmentUtils.FragmentAnim.SLIDE_RIGHT, true);
            }
        };
        mRvAdapter.setIClickItem(clickNewsItem);
        mRvGank.setAdapter(mRvAdapter);
        initRefreshLayout((SwipeRefreshLayout) root.findViewById(R.id.swipe_refresh_layout));

        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (presenter instanceof NewsPresenter) {
            showRefresh();
            ((NewsPresenter) presenter).getNewsRetrofit(mPublishDate);
        }
    }

    @Override
    protected void onRefreshStart() {
        super.onRefreshStart();
        ((NewsPresenter) presenter).getNewsRetrofit(mPublishDate);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
    }

    @Override
    public void onDetach() {
        // Called after the onDestroyView
        super.onDetach();
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
                mRvAdapter.updateData((ArrayList<ContentItem>) data);
            }
        });
    }

    @Override
    protected boolean prepareRefresh() {
        return !((NewsPresenter)presenter).isLoading();
    }
}
