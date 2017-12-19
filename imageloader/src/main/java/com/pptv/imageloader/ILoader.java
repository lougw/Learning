package com.pptv.imageloader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * <pre>
 *     author : lougw
 *     e-mail : gaoweilou@pptv.com
 *     time   : 2017/12/18
 *     desc   :
 * </pre>
 */

public interface ILoader {
    // 在application的oncreate中初始化
    void init(Context context);

    void load(ImageLoaderOptions options);

    void hideImage(@NonNull View view, int visiable);

    void cleanMemory(Context context);

    void pause(Context context);

    void resume(Context context);

    void clearDiskCache();

    void clearMemoryCache(View view);

    void trimMemory(int level);

    void clearAllMemoryCaches();


}
