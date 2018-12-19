package com.lougw.learning.widget;

import android.text.TextUtils;
import android.webkit.WebSettings;

import java.net.URI;
import java.util.List;

import static android.webkit.WebSettings.LOAD_DEFAULT;

/**
 * Created by pangbingxin on 2018/12/7.
 */

public class CMWebConfigManager {
    //主机名白名单
    private List<CMWebConfig> mConfigList;
    //默认配置
    private CMWebConfig mDefaultConfig = new CMWebConfig();

    //单例
    private static CMWebConfigManager mInstance = null;
    public static final CMWebConfigManager sharedInstance() {
        if( null == mInstance ) {
            synchronized (CMWebConfigManager.class) {
                if (null == mInstance) {
                    mInstance = new CMWebConfigManager();
                }
            }
        }
        return mInstance;
    }

    private CMWebConfigManager() {
        mDefaultConfig.mAllowFileAccessFromFileURLs = false;
    }

    /**
     * @brief 获取默认权限配置信息
     * @return
     */
    public CMWebConfig getDefaultWebConfig() {
        return mDefaultConfig;
    }

    public static String getProtocolByUrl(String url) {
        if( TextUtils.isEmpty(url) ) {
            return null;
        }
        URI uri = URI.create(url);
        if( null == uri ) {
            return null;
        }
        String scheme = uri.getScheme();
        if( TextUtils.isEmpty(scheme) ) {
            return null;
        }
        return scheme;
    }

    public static String getHostByUrl(String url) {
        if( TextUtils.isEmpty(url) ) {
            return null;
        }
        URI uri = URI.create(url);
        if( null == uri ) {
            return null;
        }
        String host = uri.getHost();
        if( TextUtils.isEmpty(host) ) {
            return url;
        }
        return host;
    }

    /**
     * @brief 根据主机名获取配置信息
     * @param host 主机名
     * @return
     */
    public CMWebConfig getHostWebConfig(String host) {
        if( TextUtils.isEmpty(host) ) {
            return getDefaultWebConfig();
        }
        if( null == mConfigList || mConfigList.isEmpty() ) {
            return getDefaultWebConfig();
        }
        for( int i = 0; i < mConfigList.size(); i++ ) {
            CMWebConfig p = mConfigList.get(i);
            if( null != p ) {
                if( host.equalsIgnoreCase(p.mHost) ) {
                    return p;
                }
            }
        }
        return getDefaultWebConfig();
    }

    /**
     * @brief 根据地址获取配置
     * @param url 地址
     * @return
     */
    public CMWebConfig getUrlWebConfig(String url) {
        if( TextUtils.isEmpty(url) ) {
            return getDefaultWebConfig();
        }
        String host = getHostByUrl(url);
        CMWebConfig config = getHostWebConfig(host);
        if( null == config ) {
            return getDefaultWebConfig();
        }
        return config;
    }

    /**
     * @brief 修改配置信息
     * @param url 主机名
     * @return
     */
    public boolean fixUrlSetting(String url, WebSettings settings) {
        if( TextUtils.isEmpty(url) || null == settings ) {
            return false;
        }
        String host = getHostByUrl(url);
        String protocol = getProtocolByUrl(url);
        return fixHostSetting(protocol, host, settings);
    }

    /**
     * @brief 修改配置信息
     * @param protocol 协议
     * @param host 主机名
     * @return
     */
    public boolean fixHostSetting(String protocol, String host, WebSettings settings) {
        if( TextUtils.isEmpty(host) || null == settings ) {
            return false;
        }

        CMWebConfig config = getHostWebConfig(host);
        if( null == config ) {
            config = getDefaultWebConfig();
            if( null == config ) {
                return false;
            }
        }

        //是否允许执行JavaScript脚本
        settings.setJavaScriptEnabled(config.mJavaScriptEnable);

        //是否允许使用File协议: 即允许在File域下执行任意JavaScript代码
        //如果禁用则不能加载本地的html文件
        //禁用文件系统访问,但仍然可以使用(默认值true)
        //file:///android_asset,file:///android_res等访问资源.
        settings.setAllowFileAccess(config.mAllowFileAccess);

        //否允许通过file url加载的Javascript读取其他的本地文件
        //这项设置只影响对file schema资源的JavaScript访问
        settings.setAllowFileAccessFromFileURLs(config.mAllowFileAccessFromFileURLs);

        //是否允许通过file url加载的Javascript可以访问其他的源
        //注意这项设置只影响对file schema资源的JavaScript访问
        settings.setAllowUniversalAccessFromFileURLs(config.mAllowUniversalAccessFromFileURLs);

        //是否让JavaScript可以自动打开窗口
        settings.setJavaScriptCanOpenWindowsAutomatically(config.mJavaScriptCanOpenWindowsAutomatically);

        if( config.mAppCacheEnabled ) {
            //是否开启缓存(默认值false)
            settings.setAppCacheEnabled(config.mAppCacheEnabled);
            //本地缓存路径(该方法只会执行一次)
            settings.setAppCachePath(config.mAppCachePath);
            //缓存模式(默认为LOAD_DEFAULT)
            settings.setCacheMode(config.mCacheMode);
        }


        //是否支持缩放
        settings.setSupportZoom(config.mSupportZoom);
        //是否展示缩放工具
        settings.setBuiltInZoomControls(config.mBuiltInZoomControls);
        //不显示webview缩放按钮
        settings.setDisplayZoomControls(config.mDisplayZoomControls);
        //是否支持扩大比例的缩放
        settings.setUseWideViewPort(config.mUseWideViewPort);

        //是否自适应屏幕
        //if( config.mLoadWithOverviewMode ) {
            //settings.setLayoutAlgorithm(config.mtLayoutAlgorithm);
            //settings.setLoadWithOverviewMode(true);
        //}

        if( config.mAllowFileAccess ) {
            //在允许使用File协议时,关闭js载入
            if( !TextUtils.isEmpty(protocol) ) {
                if( "File".equalsIgnoreCase(protocol) ) {
                    //使用File协议访问本地文件时,不允许载入js,否则有漏洞
                    settings.setJavaScriptEnabled(false);
                    settings.setAllowFileAccessFromFileURLs(false);
                    settings.setAllowUniversalAccessFromFileURLs(false);
                }
            }
        }

        return true;
    }

    public static class CMWebConfig {
        //对应的主机
        public String mHost;

        //是否先加载内容后加载图片
        public boolean mIsImagesAfterContent = true;
        //是否允许跳转(不会跳出到系统浏览器)
        public boolean mShouldOverrideUrlLoading = false;
        //是否允许调试
        public boolean mWebContentsDebuggingEnabled = false;
        //是否手动校验证书
        public boolean mManualCheckSSLError = false;

        //----------------------------------------------------------------------
        //WebSettings
        //是否允许执行JavaScript脚本
        public boolean mJavaScriptEnable = true;

        //是否允许使用File协议: 即允许在File域下执行任意JavaScript代码
        //如果禁用则不能加载本地的html文件
        //禁用文件系统访问,但仍然可以使用(默认值true)
        //file:///android_asset,file:///android_res等访问资源.
        public boolean mAllowFileAccess = false;

        //是否允许通过file url加载的Js代码读取其他的本地文件
        //这项设置只影响对file schema资源的JavaScript访问
        public boolean mAllowFileAccessFromFileURLs = false;

        //设置是否允许通过file url加载的js可以访问其他的源(包括http,https等源)
        //注意这项设置只影响对file schema资源的JavaScript访问
        public boolean mAllowUniversalAccessFromFileURLs = false;

        //是否让JavaScript可以自动打开窗口
        public boolean mJavaScriptCanOpenWindowsAutomatically = false;

        //是否开启缓存(默认值false)
        public boolean mAppCacheEnabled = false;
        //本地缓存路径(该方法只会执行一次)
        public String mAppCachePath = null;
        //缓存模式(默认为LOAD_DEFAULT)
        public int mCacheMode = LOAD_DEFAULT;
        //----------------------------------------------------------------------
        //是否支持缩放
        public boolean mSupportZoom = true;
        //是否展示缩放工具
        public boolean mBuiltInZoomControls = true;
        //不显示webview缩放按钮
        public boolean mDisplayZoomControls = false;
        //是否支持扩大比例的缩放
        public boolean mUseWideViewPort = true;
        //是否自适应屏幕
        public boolean mLoadWithOverviewMode = true;
        //自适应屏幕模式
        public WebSettings.LayoutAlgorithm mtLayoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN;
        //----------------------------------------------------------------------

        //是否可以访问文件
        public CMWebConfig() {

        }

        public boolean isSameHostUrl(String url) {
            if( TextUtils.isEmpty(url) ) {
                return false;
            }
            if( TextUtils.isEmpty(mHost) ) {
                return false;
            }
            String host = CMWebConfigManager.getHostByUrl(url);
            if( TextUtils.isEmpty(host) ) {
                return false;
            }
            if( mHost.equalsIgnoreCase(host) ) {
                return true;
            }
            return false;
        }
    }
}
