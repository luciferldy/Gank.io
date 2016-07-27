package com.gank.io.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gank.io.R;
import com.gank.io.model.ContentItem;
import com.gank.io.ui.view.IFragmentView;
import com.gank.io.util.FragmentUtils;

import java.util.List;

/**
 * Created by Lucifer on 2016/7/27.
 */
public class WebFragment extends Fragment implements IFragmentView {

    private WebView mWvContent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.web_content, container, false);
        mWvContent = (WebView) root.findViewById(R.id.web_content);
        mWvContent.getSettings().setJavaScriptEnabled(true);
        mWvContent.setWebViewClient(new CustomWebViewClient());
        Bundle bundle = getArguments();
        if (bundle.isEmpty()) {
            String url = bundle.getString(ContentItem.URL);
            mWvContent.loadUrl(url);
        }
        return root;
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
