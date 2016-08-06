package com.gank.io.ui.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;

import com.gank.io.ui.view.IFragmentView;
import com.gank.io.ui.view.IRefreshView;
import com.gank.io.util.Logger;

import java.util.List;

/**
 * Created by Lucifer on 2016/8/6.
 */
public class ISwipeRefreshFragment extends Fragment implements IRefreshView, IFragmentView {

    private static final String LOG_TAG = ISwipeRefreshFragment.class.getSimpleName();
    private SwipeRefreshLayout mISwipeRefreshLayout;

    protected void initRefreshLayout(SwipeRefreshLayout layout) {
        this.mISwipeRefreshLayout = layout;
        mISwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Logger.i(LOG_TAG, "onRefresh");
                if (prepareRefresh()) {
                    onRefreshStart();
                } else {
                    hideRefresh();
                }
            }
        });
    }

    /**
     * 刷新条件
     * @return
     */
    protected boolean prepareRefresh() {
        return true;
    }

    /**
     * 刷新开始标志
     */
    protected void onRefreshStart() {

    }

    @Override
    public void showRefresh() {
        mISwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideRefresh() {
        mISwipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mISwipeRefreshLayout.setRefreshing(false);
            }
        }, 700);
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void fillData(List data) {

    }
}
