package com.gank.io.ui.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.gank.io.R;
import com.gank.io.model.ContentItem;
import com.gank.io.ui.adapter.MainRvAdapter;
import com.gank.io.presenter.MainPresenter;
import com.gank.io.ui.fragment.MeizhiPreviewFragment;
import com.gank.io.ui.fragment.NewsFragment;
import com.gank.io.ui.view.IFragmentView;
import com.gank.io.ui.view.IMainView;
import com.gank.io.util.CommonUtils;
import com.gank.io.util.DateUtils;
import com.gank.io.util.FragmentUtils;
import com.gank.io.util.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends ISwipeRefreshActivity implements IMainView {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRvMeizhi;
    private MainPresenter mPresenter;
    private MainRvAdapter mAdapter;
    private MainRvAdapter.IClickMainItem mClickItem;
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
//            SwipeRefreshLayout root = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
//            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) root.getLayoutParams();
//            params.topMargin = CommonUtils.getStatusbarHeight(getBaseContext());
//            root.setLayoutParams(params);
        }
//
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);

        initRefreshLayout((SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout));

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.open_source_on_github, Snackbar.LENGTH_SHORT)
                        .setAction(R.string.open_en, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/luciferldy/Gank.io"));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }).show();
            }
        });

        mRvMeizhi = (RecyclerView)findViewById(R.id.rv_meizhi);
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL);
        mRvMeizhi.setLayoutManager(layoutManager);
        mAdapter = new MainRvAdapter(getBaseContext());
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
        mClickItem = new MainRvAdapter.IClickMainItem() {
            @Override
            public void onClickGankItem(ContentItem item) {
                try {
                    Date date = item.getPublishedAt();
                    DateFormat format = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
                    String dateStr = format.format(date);
                    Log.d(LOG_TAG, "date=" + dateStr);
                    Bundle bundle = new Bundle();
                    bundle.putString(DateUtils.DATE, dateStr);
                    FragmentUtils.addFragment(new NewsFragment(), getSupportFragmentManager(), bundle, FragmentUtils.FragmentAnim.SLIDE_RIGHT, true);
                } catch (Exception e) {
                    Logger.i(LOG_TAG, "publish date is ");
                    e.printStackTrace();
                }
            }

            @Override
            public void onClickGankItemGirl(ContentItem item) {
                Bundle bundle = new Bundle();
                bundle.putString(ContentItem.URL, item.getUrl());
                FragmentUtils.addFragment(new MeizhiPreviewFragment(), getSupportFragmentManager(), bundle, FragmentUtils.FragmentAnim.FADE, true);
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
    }

    @Override
    protected void onResume() {
        super.onResume();
//        hideSystemUI();
    }

    /**
     * when call onPostCreate, the activity is start-up
     * @param savedInstanceState
     */
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
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
            Fragment fragment = manager.findFragmentByTag(fName);
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

    // This snippet hides the system bars.
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void hideSystemUI() {
        // Set the immersive flag
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hie and show.
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    // This snippet shows the system bars. It does this by removing all the flags
    // except for the ones that make the content appear under the system bars.
    // Android 4.1 可以使用 SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void showSystemUi() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        Logger.i(LOG_TAG, "onWindowFocusChanged hasFocus = " + hasFocus);
        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus) {
//            hideSystemUI();
//        }
    }
}
