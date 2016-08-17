package com.gank.io.presenter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;

import com.facebook.cache.common.CacheKey;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.gank.io.R;
import com.gank.io.ui.view.IFragmentView;
import com.gank.io.util.CommonUtils;
import com.gank.io.util.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Lucifer on 2016/8/17.
 */
public class GirlPreviewPresenter extends BasePresenter<IFragmentView> {

    private static final String LOG_TAG = GirlPreviewPresenter.class.getSimpleName();
    private static final String ERROR_FILE_EXISTED = "file existed";

    protected Subscription subscription;

    public GirlPreviewPresenter(Activity activity, IFragmentView view) {
        super(activity, view);
    }

    /**
     * 保存图片
     * @param mUrl
     */
    public void saveImg(final String mUrl, final SaveImgCallback callback) {

        // create an observer
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (e.getMessage().equals(ERROR_FILE_EXISTED))
                    callback.onFailed(e.getMessage());
            }

            @Override
            public void onNext(String s) {
                if (TextUtils.isEmpty(s)) {
                    callback.onFailed(mActivity.getResources().getString(R.string.save_image_failed));
                } else {
                    callback.onSuccess(s);
                }
            }
        };

        subscription = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                Logger.i(LOG_TAG, "call");
                CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(ImageRequest.fromUri(Uri.parse(mUrl)));
                File localFile = CommonUtils.getCachedImageOnDisk(cacheKey);
                if (localFile == null) {
                    ImageRequest imageRequest = ImageRequestBuilder
                            .newBuilderWithSource(Uri.parse(mUrl))
                            .setProgressiveRenderingEnabled(true)
                            .build();
                    final ImagePipeline imagePipeline = Fresco.getImagePipeline();
                    DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchImageFromBitmapCache(imageRequest, mActivity);
                    dataSource.subscribe(new BaseBitmapDataSubscriber() {
                        @Override
                        protected void onNewResultImpl(Bitmap bitmap) {
                            if (bitmap == null) {
                                Logger.i(LOG_TAG, "bitmap lost.");
                                subscriber.onNext(null);
                                subscriber.onCompleted();
                                return;
                            }
                            String dirName = Environment.getExternalStorageDirectory() + "/Gank.io/";
                            File dirFile = new File(dirName);
                            if (!dirFile.exists()) {
                                dirFile.mkdir();
                            }
                            File imgFile = new File(dirFile, getImgName(mUrl));
                            if (imgFile.exists()) {
                                subscriber.onError(new Throwable(ERROR_FILE_EXISTED));
                            } else {
                                try {
                                    FileOutputStream fos = new FileOutputStream(imgFile);
                                    assert bitmap != null;
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                                    fos.flush();
                                    fos.close();
                                    subscriber.onNext(getImgName(mUrl));
                                    subscriber.onCompleted();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    subscriber.onError(e);
                                }
                            }
                        }

                        @Override
                        protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                            Logger.i(LOG_TAG, "onFailureImpl");
                        }
                    }, CallerThreadExecutor.getInstance());
                } else {
                    String dirName = Environment.getExternalStorageDirectory() + "/Gank.io/";
                    File dirFile = new File(dirName);
                    if (!dirFile.exists()) {
                        dirFile.mkdir();
                    }
                    File imgFile = new File(dirFile, getImgName(mUrl));
                    if (imgFile.exists()) {
                        subscriber.onError(new Throwable(ERROR_FILE_EXISTED));
                    } else {
                        // copy file return boolean
                        if (CommonUtils.copyFile(localFile, imgFile)) {
                            subscriber.onNext(getImgName(mUrl));
                            subscriber.onCompleted();
                        } else {
                            subscriber.onNext(null);
                            subscriber.onCompleted();
                        }
                    }
                }
            }
        }).observeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread()).subscribe(observer);

    }

    public void unSubscribe() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    private String getImgName(String url) {
        String[] parts =  url.split("/");
        return parts[parts.length - 1];
    }

    public interface SaveImgCallback{
        void onSuccess(String path);
        void onFailed(String errorMsg);
    }
}
