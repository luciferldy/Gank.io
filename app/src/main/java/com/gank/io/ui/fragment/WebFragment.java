package com.gank.io.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Toast;

import com.gank.io.R;
import com.gank.io.model.ContentItem;
import com.gank.io.presenter.WebPresenter;
import com.gank.io.util.CommonUtils;
import com.gank.io.util.FragmentUtils;
import com.gank.io.util.Logger;

import java.util.List;

/**
 * Created by Lucifer on 2016/7/27.
 */
public class WebFragment extends ISwipeRefreshFragment {

    private static final String LOG_TAG = WebFragment.class.getSimpleName();
    private String mUrl;
    private WebView mWvContent;
    private WebPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.web_content, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View view = root.findViewById(R.id.status_bar_holder);
            AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) view.getLayoutParams();
            params.height = CommonUtils.getStatusbarHeight(getContext());
            view.setLayoutParams(params);
            view.setVisibility(View.VISIBLE);
        }

        // init toolbar
        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.resource_cn));
        toolbar.inflateMenu(R.menu.menu_web);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.open_in_browser) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mUrl));
                    startActivity(intent);
                    return true;
                } else if (item.getItemId() == R.id.copy_url) {
                    CommonUtils.copyText(getContext(), mUrl);
                    Toast.makeText(getContext(), "已经复制到剪贴板", Toast.LENGTH_SHORT).show();
                    return true;
                } else {
                    return false;
                }
            }
        });

        mWvContent = (WebView) root.findViewById(R.id.web_content);
        initRefreshLayout((SwipeRefreshLayout) root.findViewById(R.id.swipe_refresh_layout));
        initPresenter();
        presenter.setUpWebView(mWvContent);
        Bundle bundle = getArguments();
        if (bundle != null && !bundle.isEmpty()) {
            mUrl = bundle.getString(ContentItem.URL);
            mWvContent.loadUrl(mUrl);
        } else {
            Logger.i(LOG_TAG, "bundle is null or bundle is empty.");
        }
        return root;
    }

    @Override
    public void initPresenter() {
        this.presenter = new WebPresenter(getActivity(), this);
    }

    @Override
    public void onBackPressed() {
        if (mWvContent.canGoBack()) {
            mWvContent.goBack();
            return;
        } else {
            FragmentUtils.popBackStack(getActivity());
        }
    }

    @Override
    public void fillData(List data) {
    }

    @Override
    protected void onRefreshStart() {
        mWvContent.reload();
    }

    @Override
    protected boolean prepareRefresh() {
        return !presenter.isLoading();
    }
}
