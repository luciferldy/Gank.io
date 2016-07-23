package com.gank.io.ui.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gank.io.R;
import com.gank.io.model.ContentItem;
import com.gank.io.presenter.BasePresenter;
import com.gank.io.presenter.NewsPresenter;
import com.gank.io.ui.adapter.NewsListAdapter;
import com.gank.io.ui.view.IFragmentView;
import com.gank.io.util.FragmentUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lucifer on 16-1-5.
 */
public class NewsFragment extends Fragment implements IFragmentView{

    private static final String LOG_TAG = NewsFragment.class.getSimpleName();
    private HashMap<String, ArrayList<ContentItem>> mContents;
    private BasePresenter presenter;
    private RecyclerView mRvGank;
    private NewsListAdapter mRvAdapter;

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

            }
        };
        mRvAdapter.setIClickItem(clickNewsItem);
        mRvGank.setAdapter(mRvAdapter);
        Bundle bundle = getArguments();
        String year, month, day;
        if (bundle != null) {
            year = bundle.getString("year");
            month = bundle.getString("month");
            day = bundle.getString("day");
            if (presenter instanceof NewsPresenter)
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
                mRvAdapter.updateData((ArrayList<ContentItem>) data);
            }
        });

    }
}
