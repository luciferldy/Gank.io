package com.gank.io.presenter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gank.io.ui.fragment.ISwipeRefreshFragment;
import com.gank.io.ui.view.IBaseView;

/**
 * Created by Lucifer on 2016/8/6.
 */
public class WebPresenter extends BasePresenter<IBaseView> {

    private boolean isLoading = false;
    private WebView mWebView;

    public WebPresenter(Activity activity, IBaseView view) {
        super(activity, view);
    }

    public void setUpWebView(WebView webView) {
        this.mWebView = webView;
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new CustomWebViewClient());
    }

    public boolean isLoading() {
        return isLoading;
    }

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            isLoading = true;
            ((ISwipeRefreshFragment)mView).showRefresh();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            isLoading = false;
            ((ISwipeRefreshFragment)mView).hideRefresh();
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            isLoading = false;
        }
    }

}
