package com.gank.io.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.gank.io.R;
import com.gank.io.model.ContentItem;
import com.gank.io.ui.adapter.MainListAdapter;
import com.gank.io.presenter.MainPresenter;
import com.gank.io.ui.fragment.MeizhiPreviewFragment;
import com.gank.io.ui.fragment.NewsFragment;
import com.gank.io.ui.view.IFragmentView;
import com.gank.io.ui.view.IMainView;
import com.gank.io.util.CommonUtils;
import com.gank.io.util.DateUtils;
import com.gank.io.util.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends ISwipeRefreshActivity implements IMainView {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRvMeizhi;
    private MainPresenter mPresenter;
    private MainListAdapter mAdapter;
    private MainListAdapter.IClickMainItem mClickItem;
    private MainPresenter.LoadCallback mLoadCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.i(LOG_TAG, "onCreate");
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View view = findViewById(R.id.status_bar_holder);
            AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) view.getLayoutParams();
            params.height = CommonUtils.getStatusbarHeight(getBaseContext());
            view.setLayoutParams(params);
            view.setVisibility(View.VISIBLE);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.inflateMenu(R.menu.menu_main);

        setSupportActionBar(toolbar);

        initRefreshLayout((SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout));

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Do u want know me ?", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mRvMeizhi = (RecyclerView)findViewById(R.id.rv_meizhi);
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL);
        mRvMeizhi.setLayoutManager(layoutManager);
        mAdapter = new MainListAdapter(getBaseContext());
        mRvMeizhi.setAdapter(mAdapter);
        mRvMeizhi.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // 测试时使用的 log, lastVisibleItemPositions 返回的最后一个可见的 item 的位置, lastCompletelyVisibleItemPositions 返回的是最后一个完全可见的 item 的位置.
//                    int[] visiblePos = layoutManager.findLastVisibleItemPositions(new int[2]);
//                    Logger.i(LOG_TAG, "last visible position is "  + visiblePos[0] + ", " + visiblePos[1]);
//                    int[] compVisPoss = layoutManager.findLastCompletelyVisibleItemPositions(new int[2]);
//                    Logger.i(LOG_TAG, "last completely visible position is " + compVisPoss[0] + ", " + compVisPoss[1]);
                    boolean isBottom = layoutManager.findLastCompletelyVisibleItemPositions(new int[2])[1] >= mAdapter.getItemCount() - 3;
                    if (!mSwipeRefreshLayout.isRefreshing() && isBottom) {
                        Logger.i(LOG_TAG, "slide to the bottom and no refreshing, ready to load more data.");
                        showRefresh();
                        mPresenter.getMeizhiRetrofit(true);
//                        mPresenter.loadMeizhi(true, mLoadCallback);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                // 在滚动过程中会不停的判断，会影响性能
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        mClickItem = new MainListAdapter.IClickMainItem() {
            @Override
            public void onClickGankItem(ContentItem item) {
                try {
                    Date date = item.getPublishedAt();
                    String year = (new SimpleDateFormat("yyyy", Locale.US)).format(date);
                    String month = (new SimpleDateFormat("MM", Locale.US)).format(date);
                    String day = (new SimpleDateFormat("dd", Locale.US)).format(date);
                    Log.d(LOG_TAG, DateUtils.YEAR + "=" + year + " " + DateUtils.MONTH + "=" + month + " " + DateUtils.DAY + "=" + day);
                    FragmentManager manager = getSupportFragmentManager();
                    NewsFragment newsItem = new NewsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(DateUtils.YEAR, year);
                    bundle.putString(DateUtils.MONTH, month);
                    bundle.putString(DateUtils.DAY, day);
                    newsItem.setArguments(bundle);
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.add(android.R.id.content, newsItem);
                    transaction.addToBackStack(NewsFragment.class.getSimpleName() + System.currentTimeMillis());
                    transaction.commit();
                } catch (Exception e) {
                    Logger.i(LOG_TAG, "publish date is ");
                    e.printStackTrace();
                }
            }

            @Override
            public void onClickGankItemGirl(ContentItem item) {
                FragmentManager manager = getSupportFragmentManager();
                MeizhiPreviewFragment preview = new MeizhiPreviewFragment();
                Bundle bundle = new Bundle();
                bundle.putString(ContentItem.URL, item.getUrl());
                preview.setArguments(bundle);
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(android.R.id.content, preview);
                transaction.addToBackStack(MeizhiPreviewFragment.class.getSimpleName() + System.currentTimeMillis());
                transaction.commit();
            }
        };
        mAdapter.setClickItem(mClickItem);
        mPresenter = new MainPresenter(this, this);
        mLoadCallback = new MainPresenter.LoadCallback() {
            @Override
            public void onLoadSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideRefresh();
                    }
                });
            }

            @Override
            public void onLoadFailed() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideRefresh();
                    }
                });
            }
        };
//        mPresenter.loadMeizhi(false, mLoadCallback);

        showRefresh();
        mPresenter.getMeizhiRetrofit(false);
    }

    /**
     * handle the key back event
     */
    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();
        int count = manager.getBackStackEntryCount();
        if (count > 0) {
            String fName = manager.getBackStackEntryAt(count - 1).getName();
            Fragment fragment = (Fragment) manager.findFragmentByTag(fName);
            if (null != fragment && fragment instanceof IFragmentView) {
                ((IFragmentView) fragment).onBackPressed();
                return;
            }
        }
        super.onBackPressed();
    }

    @Override
    public void fillData(final List data) {
        Logger.i(LOG_TAG, "fillData");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // adapter 监控的是 meiZhis 的这个内存地址，如果使用 meiZhis = data 的话不会显示图片效果
                mAdapter.update(data);
            }
        });
    }

    @Override
    public void appendMoreData(final List data) {
        Logger.i(LOG_TAG, "appendMoreDate");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.appendData(data);
            }
        });
    }

    @Override
    protected boolean prepareRefresh() {
        return !mPresenter.isLoadingData();
    }

    @Override
    protected void onRefreshStart() {
        super.onRefreshStart();
        Logger.i(LOG_TAG, "onRefresh");
        mPresenter.getMeizhiRetrofit(false);
//        mPresenter.loadMeizhi(false, mLoadCallback);
    }
}
