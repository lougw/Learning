package com.pptv.imageloader.glide;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.GifRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.pptv.imageloader.ILoader;
import com.pptv.imageloader.ImageLoaderOptions;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * <pre>
 *     author : lougw
 *     e-mail : gaoweilou@pptv.com
 *     time   : 2017/12/18
 *     desc   :
 * </pre>
 */

public class GlideImageLoader implements ILoader {
    @Override
    public void load(ImageLoaderOptions options) {
        GenericRequestBuilder mGenericRequestBuilder = init(options);
        if (mGenericRequestBuilder != null) {
            showImageLast(mGenericRequestBuilder, options);
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

    private void showImageLast(GenericRequestBuilder mDrawableTypeRequest, ImageLoaderOptions options) {
        final ImageView img = (ImageView) options.getViewContainer();
        // 是否展示一个gif
        if (options.isAsGif()) {
            ((GifRequestBuilder) mDrawableTypeRequest).dontAnimate().into(new SimpleTarget<GifDrawable>() {
                @Override
                public void onResourceReady(GifDrawable resource, GlideAnimation<? super GifDrawable> glideAnimation) {
                    img.setImageDrawable(resource);
                    resource.start();
                }
            });
            return;
        }
        if (options.getTarget() != null) {
            mDrawableTypeRequest.into(options.getTarget());
            return;
        }
        mDrawableTypeRequest.into(img);
    }

    @Override
    public void init(Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
        Glide.get(context).register(GlideUrl.class, InputStream.class, new MOkHttpUrlLoader.Factory(client));
    }

    public GenericRequestBuilder init(ImageLoaderOptions options) {
        View v = options.getViewContainer();

        //存在问题
        // java.lang.IllegalArgumentException You cannot start a load for a destroyed activity
        //RequestManager manager=getRequestManager(v.getContext());
        RequestManager manager = getRequestManager(v.getContext());
        if (v instanceof ImageView) {
            GenericRequestBuilder mDrawableTypeRequest = getGenericRequestBuilder(manager, options).asBitmap();
            if (options.isAsGif()) {
                mDrawableTypeRequest = getGenericRequestBuilder(manager, options);
            }
            //装载参数
            mDrawableTypeRequest = loadGenericParams(mDrawableTypeRequest, options);
            return mDrawableTypeRequest;
        }
        return null;
    }

    private GenericRequestBuilder loadGenericParams(GenericRequestBuilder mGenericRequestBuilder, final ImageLoaderOptions options) {
        final View view = options.getViewContainer();
        GenericRequestBuilder builder = mGenericRequestBuilder;
        if (mGenericRequestBuilder instanceof DrawableTypeRequest) {
            if (options.isCrossFade()) {
                ((DrawableTypeRequest) mGenericRequestBuilder).crossFade();
            }
            if (options.isAsGif()) {
                builder = ((DrawableTypeRequest) mGenericRequestBuilder).asGif();
            }
        }
        builder.skipMemoryCache(options.isSkipMemoryCache());
        if (options.getImageSize() != null) {
            int width = options.getImageSize().getWidth();
            int height = options.getImageSize().getHeight();
            Log.i("tag ", "load params " + options.getImageSize().getWidth() + "  : " + options.getImageSize().getHeight());
            builder.override(width, height);
        }
        if (options.getHolderDrawable() != -1) {
            builder.placeholder(options.getHolderDrawable());
        }
        if (options.getErrorDrawable() != -1) {
            builder.error(options.getErrorDrawable());
        }

        if (options.getDiskCacheStrategy() != ImageLoaderOptions.DiskCacheStrategy.DEFAULT) {
            switch (options.getDiskCacheStrategy()) {
                case NONE:
                    builder.diskCacheStrategy(DiskCacheStrategy.NONE);
                    break;
                case All:
                    builder.diskCacheStrategy(DiskCacheStrategy.ALL);
                    break;
                case SOURCE:
                    builder.diskCacheStrategy(DiskCacheStrategy.SOURCE);
                    break;
                case RESULT:
                    builder.diskCacheStrategy(DiskCacheStrategy.RESULT);
                    break;
                default:
                    break;
            }
        }


        return builder;

    }

    public RequestManager getRequestManager(Context context) {
        return Glide.with(context);

    }

    public DrawableTypeRequest getGenericRequestBuilder(RequestManager manager, ImageLoaderOptions options) {

        if (!TextUtils.isEmpty(options.getUrl())) {
            return manager.load(options.getUrl());
        }
        return manager.load(options.getResource());
    }
}
