package com.gank.io.ui.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.gank.io.ui.view.IRefreshView;

public abstract class ISwipeRefreshActivity extends AppCompatActivity implements IRefreshView {

    protected SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 4.4 translucent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        // 5.0 transparent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
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
                    // no showRefresh()
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
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (mSwipeRefreshLayout != null)
                    mSwipeRefreshLayout.setRefreshing(true);
            }
        });
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
        }, 500);
    }

    @Override
    public void onComplete() {
        hideRefresh();
    }
}