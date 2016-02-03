package com.gank.io.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gank.io.R;
import com.gank.io.util.ContentItem;

/**
 * Created by lucifer on 16-1-4.
 */
public class MeizhiPreview extends Fragment {

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
}
