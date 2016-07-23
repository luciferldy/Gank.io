package com.gank.io.ui.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gank.io.R;
import com.gank.io.model.ContentItem;
import com.gank.io.ui.view.IFragmentView;
import com.gank.io.util.FragmentUtils;

import java.util.HashMap;
import java.util.List;

/**
 * Created by lucifer on 16-1-4.
 */
public class ImgPreviewFragment extends Fragment implements IFragmentView{

    private SimpleDraweeView meizhiimg;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        String url = "";
        if (bundle != null) {
            url = bundle.getString(ContentItem.URL);
        }
        View root = inflater.inflate(R.layout.meizhi_preview, container, false);
        meizhiimg = (SimpleDraweeView) root.findViewById(R.id.meizhi_preview_img);
        Uri uri = Uri.parse(url);
        meizhiimg.setImageURI(uri);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onBackPressed() {
        FragmentUtils.popBackStack(getActivity());
    }


    @Override
    public void initPresenter() {

    }

    @Override
    public void fillData(HashMap data) {

    }
}
