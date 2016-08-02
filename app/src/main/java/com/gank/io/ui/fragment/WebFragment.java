package com.gank.io.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gank.io.R;
import com.gank.io.model.ContentItem;
import com.gank.io.ui.view.IFragmentView;
import com.gank.io.util.FragmentUtils;
import com.gank.io.util.Logger;

import java.util.List;

/**
 * Created by Lucifer on 2016/7/27.
 */
public class WebFragment extends Fragment implements IFragmentView {

    private static final String LOG_TAG = WebFragment.class.getSimpleName();
    private WebView mWvContent;
    private String mUrl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.web_content, container, false);
        mWvContent = (WebView) root.findViewById(R.id.web_content);
        mWvContent.getSettings().setJavaScriptEnabled(true);
        mWvContent.setWebViewClient(new CustomWebViewClient());
        Bundle bundle = getArguments();
        if (bundle != null && bundle.isEmpty()) {
            mUrl = bundle.getString(ContentItem.URL);
            mWvContent.loadUrl(mUrl);
        } else {
            Logger.i(LOG_TAG, "bundle is null or bundle is empty.");
        }
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_web, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.web_content) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mUrl));
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void initPresenter() {

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

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }
    }
}
