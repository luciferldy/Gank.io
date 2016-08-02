package com.gank.io.ui.view;

/**
 * Created by Lucifer on 2016/7/30.
 */
public interface IRefreshView extends IBaseView{

    void getDataFinish();

    void showEmptyView();

    void showErrorView(Throwable throwable);

    void showRefresh();

    void hideRefresh();
}
