package com.lougw.learning.utils;

import android.content.Context;
import android.widget.ImageView;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * <pre>
 *     author : lougw
 *     e-mail : gaoweilou@pptv.com
 *     time   : 2017/12/23
 *     desc   :
 * </pre>
 */

public class ImageLoader {

    private ImageLoader() {
    }

    private static class Instance {
        private static ImageLoader instance = new ImageLoader();
    }

    public static ImageLoader getInstance() {
        return Instance.instance;
    }

    public void init(Context context) {
        OkHttpClient builder = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
        //Glide.get(context).register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(builder));
    }

    public void display(String uri, ImageView view) {
        this.display(uri, view, 0, false);
    }

    public void display(String uri, ImageView view, final int defaultImage, boolean skip) {
//        Glide.with(UIUtils.getContext()).load(uri).asBitmap().dontAnimate().format(
//                DecodeFormat.PREFER_RGB_565).placeholder(defaultImage).error(defaultImage).skipMemoryCache(skip).into(view);
    }
}


