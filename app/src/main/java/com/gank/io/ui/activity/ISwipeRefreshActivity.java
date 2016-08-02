package com.gank.io.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;

import com.gank.io.ui.view.IRefreshView;

public abstract class ISwipeRefreshActivity extends AppCompatActivity implements IRefreshView {

    protected SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     *  初始化刷新控件
     * @param view
     */
    public void initRefreshLayout(SwipeRefreshLayout view) {
        mSwipeRefreshLayout = view;
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (prepareRefresh()) {
                    // 下拉刷新时滚动条一直会存在，只可以手动关闭，这里不需要进行 showRefresh() 操作
                    onRefreshStart();
                } else {
                    hideRefresh();
                }
            }
        });
    }

    /**
     * 刷新的条件
     * @return
     */
    protected boolean prepareRefresh() {return true;}

    protected void onRefreshStart(){}

    @Override
    public void showRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideRefresh() {
        // 防止刷新消失太快，设置一个 delay
        mSwipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mSwipeRefreshLayout != null)
                    mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 700);
    }

    @Override
    public void showEmptyView() {

    }

    @Override
    public void getDataFinish() {

    }

    @Override
    public void showErrorView(Throwable throwable) {

    }
}