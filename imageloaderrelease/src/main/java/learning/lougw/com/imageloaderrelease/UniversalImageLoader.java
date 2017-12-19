package learning.lougw.com.imageloaderrelease;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.View;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.pptv.imageloader.ILoader;
import com.pptv.imageloader.ImageLoaderOptions;

/**
 * <pre>
 *     author : lougw
 *     e-mail : gaoweilou@pptv.com
 *     time   : 2017/12/18
 *     desc   :
 * </pre>
 */

public class UniversalImageLoader implements ILoader {

    @Override
    public void load(ImageLoaderOptions options) {
        DisplayImageOptions defaultOptions = init(options);
        if (defaultOptions != null) {
            ImageLoader.getInstance().displayImage(options.getUrl(), (ImageAware) options.getViewContainer(), defaultOptions);
        }

    }

    private DisplayImageOptions init(ImageLoaderOptions options) {

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().
                cacheOnDisk(true)
                .delayBeforeLoading(50).bitmapConfig(Bitmap.Config.RGB_565)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
        return defaultOptions;
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

    @Override
    public void init(Context context) {
        ImageLoaderConfiguration config =
                new ImageLoaderConfiguration.Builder(context).threadPoolSize(Thread.NORM_PRIORITY - 2)
                        .denyCacheImageMultipleSizesInMemory()
                        .discCacheFileNameGenerator(new Md5FileNameGenerator())
                        .tasksProcessingOrder(QueueProcessingType.LIFO)
                        .memoryCacheSize(10 * 1024 * 1024)
                        .diskCacheSize(100 * 1024 * 1024)
                        .build();
        ImageLoader.getInstance().init(config);

    }
}
