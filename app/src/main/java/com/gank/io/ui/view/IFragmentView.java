package com.gank.io.ui.view;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Lucifer on 2016/7/16.
 */
public interface IFragmentView extends IBaseView{

    void initPresenter();

    /**
     * handle the back event
     */
    void onBackPressed();

    /**
     * fill the data
     * @param data
     */
    void fillData(List data);
}
