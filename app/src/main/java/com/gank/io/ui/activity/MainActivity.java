package com.gank.io.ui.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.gank.io.R;
import com.gank.io.model.ContentItem;
import com.gank.io.ui.adapter.MainListAdapter;
import com.gank.io.presenter.MainPresenter;
import com.gank.io.ui.fragment.ImgPreviewFragment;
import com.gank.io.ui.fragment.NewsFragment;
import com.gank.io.ui.view.IFragmentView;
import com.gank.io.ui.view.IMainView;
import com.gank.io.util.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IMainView {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRvMeizhi;
    private MainPresenter mPresenter;
    private MainListAdapter mAdapter;
    private MainListAdapter.IClickMainItem mClickItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.i(LOG_TAG, "onCreate");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mRvMeizhi = (RecyclerView)findViewById(R.id.rv_meizhi);
        mRvMeizhi.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));
        mAdapter = new MainListAdapter(getBaseContext());
        mRvMeizhi.setAdapter(mAdapter);
        mClickItem = new MainListAdapter.IClickMainItem() {
            @Override
            public void onClickGankItem(ContentItem item) {
                try {
                    Date date = item.getPublishedAt();
                    String year = (new SimpleDateFormat("yyyy")).format(date);
                    String month = (new SimpleDateFormat("MM")).format(date);
                    String day = (new SimpleDateFormat("dd")).format(date);
                    Log.d(LOG_TAG, "year=" + year + " month=" + month + " day=" + day);
                    FragmentManager manager = getSupportFragmentManager();
                    NewsFragment newsItem = new NewsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("year", year);
                    bundle.putString("month", month);
                    bundle.putString("day", day);
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
                ImgPreviewFragment preview = new ImgPreviewFragment();
                Bundle bundle = new Bundle();
                bundle.putString(ContentItem.URL, item.getUrl());
                preview.setArguments(bundle);
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(android.R.id.content, preview);
                transaction.addToBackStack(ImgPreviewFragment.class.getSimpleName() + System.currentTimeMillis());
                transaction.commit();
            }
        };
        mAdapter.setClickItem(mClickItem);
        mPresenter = new MainPresenter(this, this);
        mPresenter.loadMeizhi();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
    public void appendMoreData(List data) {

    }

    @Override
    public void hasNoMoreData() {

    }


}
