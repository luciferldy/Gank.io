package com.gank.io.ui.view;

import com.gank.io.model.ContentItem;
import com.gank.io.ui.view.IBaseView;

import java.util.List;

/**
 * Created by Lucifer on 2016/7/13.
 */
public interface IMainView<T extends ContentItem> extends IBaseView{

    void fillData(List<T> data);

    void appendMoreData(List<T> data);

    void hasNoMoreData();

}
