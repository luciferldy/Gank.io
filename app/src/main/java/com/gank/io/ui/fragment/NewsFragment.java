package com.gank.io.ui.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.gank.io.R;
import com.gank.io.model.ContentItem;
import com.gank.io.presenter.BasePresenter;
import com.gank.io.presenter.NewsPresenter;
import com.gank.io.ui.adapter.NewsListAdapter;
import com.gank.io.ui.view.IFragmentView;
import com.gank.io.util.DateUtils;
import com.gank.io.util.FragmentUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lucifer on 16-1-5.
 */
public class NewsFragment extends Fragment implements IFragmentView{

    private static final String LOG_TAG = NewsFragment.class.getSimpleName();
    private BasePresenter presenter;
    private RecyclerView mRvGank;
    private NewsListAdapter mRvAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.news_content, container, false);
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
                fragment.setArguments(bundle);
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(android.R.id.content, fragment);
                transaction.addToBackStack(WebFragment.class.getSimpleName() + System.currentTimeMillis());
                transaction.commit();
            }
        };
        mRvAdapter.setIClickItem(clickNewsItem);
        mRvGank.setAdapter(mRvAdapter);
        Bundle bundle = getArguments();
        String year, month, day;
        if (bundle != null) {
            year = bundle.getString(DateUtils.YEAR);
            month = bundle.getString(DateUtils.MONTH);
            day = bundle.getString(DateUtils.DAY);
            if (presenter instanceof NewsPresenter)
                ((NewsPresenter) presenter).loadNews(year + "/" + month + "/" + day);
        }

        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setEnabled(false);

        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
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
                mRvAdapter.updateData((ArrayList<ContentItem>) data);
            }
        });

    }
}
