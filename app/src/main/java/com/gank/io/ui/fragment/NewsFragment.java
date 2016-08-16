package com.gank.io.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.gank.io.R;
import com.gank.io.model.ContentItem;
import com.gank.io.presenter.BasePresenter;
import com.gank.io.presenter.NewsPresenter;
import com.gank.io.ui.adapter.NewsListAdapter;
import com.gank.io.ui.view.IFragmentView;
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
        // 在 4.4 之上的 Fragment 需要对此进行适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                AppBarLayout layout = (AppBarLayout) root.findViewById(R.id.toolbar_layout);
//                int top = root.getPaddingTop() == 0 ? CommonUtils.getStatusbarHeight(getContext()) : root.getPaddingTop() + CommonUtils.getStatusbarHeight(getContext());
//                CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) layout.getLayoutParams();
//                params.topMargin = top;
//                layout.setLayoutParams(params);
            AppBarLayout appBarLayout = (AppBarLayout) root.findViewById(R.id.toolbar_layout);
            appBarLayout.setPadding(appBarLayout.getLeft(), CommonUtils.getStatusbarHeight(getContext()),
                    appBarLayout.getRight(), appBarLayout.getBottom());
        }

        setHasOptionsMenu(true);
        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        mRvGank = (RecyclerView) root.findViewById(R.id.rv_gank);
        mRvGank.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvAdapter = new NewsListAdapter(getContext());
        NewsListAdapter.IClickNewsItem clickNewsItem = new NewsListAdapter.IClickNewsItem() {
            @Override
            public void onClickGankItemGirl(ContentItem item, View viewImg, View viewText) {

            }

            @Override
            public void onClickGankItemNormal(ContentItem item, View view) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                WebFragment fragment = new WebFragment();
                Bundle bundle = new Bundle();
                bundle.putString(ContentItem.URL, item.getUrl());
                Logger.i(LOG_TAG, ContentItem.URL + "=" + item.getUrl());
                fragment.setArguments(bundle);
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(android.R.id.content, fragment);
                transaction.addToBackStack(WebFragment.class.getSimpleName() + System.currentTimeMillis());
                transaction.commit();
            }
        };
        mRvAdapter.setIClickItem(clickNewsItem);
        mRvGank.setAdapter(mRvAdapter);
        initRefreshLayout((SwipeRefreshLayout) root.findViewById(R.id.swipe_refresh_layout));
        Bundle bundle = getArguments();
        String year, month, day;
        if (bundle != null) {
            year = bundle.getString(DateUtils.YEAR);
            month = bundle.getString(DateUtils.MONTH);
            day = bundle.getString(DateUtils.DAY);
            mPublishDate = year + "/" + month + "/" + day;
            if (presenter instanceof NewsPresenter) {
                showRefresh();
                ((NewsPresenter) presenter).loadNews(mPublishDate);
            }
        }

        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        return root;
    }

    @Override
    protected void onRefreshStart() {
        super.onRefreshStart();
        ((NewsPresenter) presenter).loadNews(mPublishDate);
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
                mRvAdapter.updateData((ArrayList<ContentItem>) data);
                hideRefresh();
            }
        });
    }

    @Override
    protected boolean prepareRefresh() {
        return ((NewsPresenter)presenter).isLoading();
    }
}
