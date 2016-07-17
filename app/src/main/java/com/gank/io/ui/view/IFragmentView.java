package com.gank.io.ui.view;

import com.gank.io.model.ContentItem;

import java.util.List;

/**
 * Created by Lucifer on 2016/7/16.
 */
public interface IFragmentView<I extends ContentItem>  extends IBaseView{

    void initPresenter();

    /**
     * handle the back event
     */
    void onBackPressed();

    void fillData(List<I> data);
}
