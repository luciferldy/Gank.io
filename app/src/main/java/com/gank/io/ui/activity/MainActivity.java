package com.gank.io.ui.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.gank.io.R;
import com.gank.io.adapter.NewsAdapter;
import com.gank.io.presenter.MainPresenter;
import com.gank.io.ui.view.IFragmentView;
import com.gank.io.ui.view.IMainView;
import com.gank.io.util.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IMainView {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView mNewsCon;
    private ArrayList<HashMap<String, String>> meiZhis;
    private MainPresenter presenter;
    private NewsAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.i(TAG, "onCreate");
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

        mNewsCon = (RecyclerView)findViewById(R.id.news_container);
        mNewsCon.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));
        meiZhis = new ArrayList<>();
        newsAdapter = new NewsAdapter(meiZhis, this);
        mNewsCon.setAdapter(newsAdapter);
        presenter = new MainPresenter(this, this);
        presenter.loadMeizhi();
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
        Logger.i(TAG, "fillData");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // adapter 监控的是 meiZhis 的这个内存地址，如果使用 meiZhis = data 的话不会显示图片效果
                for (HashMap<String, String> map: (ArrayList<HashMap<String, String>>) data) {
                    meiZhis.add(map);
                }
                Logger.i(TAG, "meiZhis has " + meiZhis.size());
                newsAdapter.notifyDataSetChanged();
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
