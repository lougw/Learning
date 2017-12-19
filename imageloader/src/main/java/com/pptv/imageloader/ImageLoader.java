package com.pptv.imageloader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * <pre>
 *     author : lougw
 *     e-mail : gaoweilou@pptv.com
 *     time   : 2017/12/14
 *     desc   :
 * </pre>
 */

public class ImageLoader implements com.pptv.imageloader.ILoader {

    private Context mContext;

    private ImageLoader() {
    }

    private static class Instance {
        private static final ImageLoader instance = new ImageLoader();
    }

    public static ImageLoader getInstance() {
        return Instance.instance;
    }

    private com.pptv.imageloader.ILoader mImageLoader;

    public void init(Context context) {
        mContext = context;
        try {
            Class clazz = null;
            try {
                clazz = Class.forName("com.pptv.imageloader.glide.GlideImageLoader");
            } catch (ClassNotFoundException e) {

            }
            if (clazz == null) {
                try {
                    clazz = Class.forName("learning.lougw.com.imageloaderrelease.UniversalImageLoader");
                } catch (ClassNotFoundException e) {

                }
            }
            mImageLoader = (ILoader) clazz.newInstance();
            mImageLoader.init(mContext);

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void load(ImageLoaderOptions options) {
        if (getImageLoader() != null) {
            getImageLoader().load(options);
        }

    }

    @Override
    public void hideImage(@NonNull View view, int visiable) {

    }

    @Override
    public void cleanMemory(Context context) {

    }

    @Override
    public void pause(Context context) {

    }

    @Override
    public void resume(Context context) {

    }

    @Override
    public void clearDiskCache() {

    }

    @Override
    public void clearMemoryCache(View view) {

    }

    @Override
    public void trimMemory(int level) {

    }

    @Override
    public void clearAllMemoryCaches() {

    }

    private ILoader getImageLoader() {
        if (mImageLoader == null && mContext != null) {
            init(mContext);
        }
        return mImageLoader;
    }
}
