package com.gank.io.ui.fragment;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.image.QualityInfo;
import com.gank.io.R;
import com.gank.io.model.ContentItem;
import com.gank.io.presenter.MeizhiPreviewPresenter;
import com.gank.io.ui.view.IFragmentView;
import com.gank.io.util.CommonUtils;
import com.gank.io.util.FragmentUtils;
import com.gank.io.util.Logger;
import com.gank.io.util.WechatUtils;

import java.io.File;
import java.util.List;

/**
 * Created by lucifer on 16-1-4.
 */
public class MeizhiPreviewFragment extends Fragment implements IFragmentView{

    private static final int SAVE_PIC_ID = 1;
    private static final int SHARE_TO_TIMELINE_ID = 2;
    private static final int SHARE_TO_FRIEND = 3;
    private static final String LOG_TAG = MeizhiPreviewFragment.class.getSimpleName();

    private SimpleDraweeView meizhiimg;
    private View mContainer;

    private MeizhiPreviewPresenter mPresenter;
    private String mUrl;
    private String mLocalPath;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mUrl = bundle.getString(ContentItem.URL);
            initLocalPath(mUrl);
        }
        mContainer = inflater.inflate(R.layout.meizhi_preview, container, false);
        mContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        meizhiimg = (SimpleDraweeView) mContainer.findViewById(R.id.meizhi_preview_img);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setControllerListener(mControllerListener)
                .setUri(Uri.parse(mUrl))
                .build();
        meizhiimg.setController(controller);
        initPresenter();
        return mContainer;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.clear();
        menu.add(0, SAVE_PIC_ID, 0, "保存图片到本地");
        menu.add(0, SHARE_TO_FRIEND, 0, "分享给朋友");
        menu.add(0, SHARE_TO_TIMELINE_ID, 0, "分享到朋友圈");
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case SAVE_PIC_ID:
                mPresenter.saveImg(mUrl, new MeizhiPreviewPresenter.SaveImgCallback() {
                    @Override
                    public void onSuccess(final String path) {
                        mLocalPath = path;
                        Logger.i(LOG_TAG, "save image success path = " + path);
                        Snackbar.make(mContainer, R.string.save_image_success, Snackbar.LENGTH_SHORT)
                                .setAction(R.string.open_cn, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.setDataAndType(Uri.parse(path), "image/*");
                                        startActivity(intent);
                                    }
                                }).show();
                    }

                    @Override
                    public void onFailed(String errorMsg) {
                        Logger.i(LOG_TAG, "save image failed error msg = " + errorMsg);
                        if (errorMsg.equals(MeizhiPreviewPresenter.ERROR_FILE_EXISTED))
                            Snackbar.make(mContainer, R.string.save_image_existed, Snackbar.LENGTH_SHORT)
                                    .show();
                    }
                });
                return true;
            case SHARE_TO_FRIEND:
                if (!TextUtils.isEmpty(mLocalPath)) {
                    WechatUtils.shareToFriend(getContext(), mLocalPath);
                    return true;
                } else {
                    Snackbar.make(mContainer, R.string.image_not_existed, Snackbar.LENGTH_SHORT)
                            .show();
                }
                break;
            case SHARE_TO_TIMELINE_ID:
                if (!TextUtils.isEmpty(mLocalPath)) {
                    WechatUtils.shareToTimeLine(getContext(), "来自 Gank.io 的妹纸", mLocalPath);
                    return true;
                } else {
                    Snackbar.make(mContainer, R.string.image_not_existed, Snackbar.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                break;

        }

        return super.onContextItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.unSubscribe();
    }

    @Override
    public void onBackPressed() {
        FragmentUtils.popBackStack(getActivity());
    }


    @Override
    public void initPresenter() {
        mPresenter = new MeizhiPreviewPresenter(getActivity(), this);
    }

    @Override
    public void fillData(List data) {

    }

    /**
     * 初始化本地路径，分享到微信或者朋友圈的时候用
     * @param url
     */
    private void initLocalPath(String url) {
        String dirName = Environment.getExternalStorageDirectory() + "/Gank.io/";
        File dirFile = new File(dirName);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File imgFile = new File(dirFile, getImgNameFromURL(url));
        if (imgFile.exists()) {
            mLocalPath = imgFile.getPath();
        }
    }

    /**
     * 通过 url 获得图片的名字
     * @param url
     * @return
     */
    private String getImgNameFromURL(String url) {
        String[] parts =  url.split("/");
        return parts[parts.length - 1];
    }

    /**
     *  监听 drawee 图片加载
     */
    private ControllerListener mControllerListener = new BaseControllerListener<ImageInfo>() {

        @Override
        /**
         * 图片下载成功时
         */
        public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
            if (imageInfo == null) {
                return;
            }
            // 图片加载成功之后可以使用 ContextMenu
            registerForContextMenu(meizhiimg);
            QualityInfo qualityInfo = imageInfo.getQualityInfo();
            FLog.i(LOG_TAG, "Final image received! Size %d x %d Quality level %d, good enough: %s, full quality: %s",
                    imageInfo.getWidth(),
                    imageInfo.getHeight(),
                    qualityInfo.getQuality(),
                    qualityInfo.isOfGoodEnoughQuality(),
                    qualityInfo.isOfFullQuality());
            Logger.i(String.format("Final image received! Size %d x %d Quality level %d, good enough: %s, full quality: %s",
                    imageInfo.getWidth(),
                    imageInfo.getHeight(),
                    qualityInfo.getQuality(),
                    qualityInfo.isOfGoodEnoughQuality(),
                    qualityInfo.isOfFullQuality()));
        }

        @Override
        public void onIntermediateImageSet(String id, ImageInfo imageInfo) {
            super.onIntermediateImageSet(id, imageInfo);
        }

        @Override
        /**
         * 图片下载失败时
         */
        public void onFailure(String id, Throwable throwable) {
            super.onFailure(id, throwable);
            Toast.makeText(getContext(), "貌似出了点问题..", Toast.LENGTH_SHORT).show();
        }
    };
}
