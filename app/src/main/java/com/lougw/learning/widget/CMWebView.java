package com.lougw.learning.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslCertificate;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.webkit.ClientCertRequest;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by pangbingxin on 2018/12/7.
 */

public class CMWebView extends WebView {
    //指纹SHA256
    private static final String[] DOMAIN_CHECKSUM_CODE_SHA256 = {
            //*.ksmobile.net(2018/07/16 - 2020/07/16)
            "DF51593E2879EF6BA929E7EA8754C02112D9333151B0B1C741ED58064710BF91",
            //*.ksmobile.com(2018/05/02 - 2020/04/13)
            "26EECD738F81237A94C026B978DD9544ABBF0FF71FF309F9C113E5FBDA26D72A",
            //*.liveme.com(2018/05/02 - 2019/12/15)
            "596DA21AD08E6EC1B54CC91A0A2F57B95FFE8D42C05DB5D29388720D8DA6BA30",
            //*.cmcm.com(2018/05/03 - 2020/06/15)
            "91F4253489B291063B9651DC771E9487735F8873E2A494E38ABAE222A49669B9"
    };

    //指纹SHA1
    private static final String[] DOMAIN_CHECKSUM_SHA1 = {
            //*.ksmobile.net(2018/07/16 - 2020/07/16)
            "5981037B7570AA003A3E95C71CD5883C0E87849B",
            //*.ksmobile.com(2018/05/02 - 2020/04/13)
            "316DBDAE7020110B2F44F581B8935A52B330F7FC",
            //*.liveme.com(2018/05/02 - 2019/12/15)
            "BDD03B6BD1F61722B9608264BA6EC36D6705A05C",
            //*.cmcm.com(2018/05/03 - 2020/06/15)
            "5E32F03D3BFFF5F6CF7911F97ADC870BA758004C"
    };


    //是否已经初始化过了
    private boolean mSetuped = false;
    //事件监听器
    private CMWebViewListener mListener = null;
    //当前使用的配置信息
    private CMWebConfigManager.CMWebConfig mCurrentWebConfig;

    public CMWebView(Context context) {
        super(context);
    }

    public CMWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CMWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CMWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * @brief 设置事件监听器
     * @param listener
     */
    public void setListener(CMWebViewListener listener) {
        mListener = listener;
        return;
    }

    /**
     * @brief 重置设置
     */
    private void resetUrlSettings(String url) {
        mCurrentWebConfig = CMWebConfigManager.sharedInstance().getUrlWebConfig(url);
        if( null != mCurrentWebConfig ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                //是否允许调试
                setWebContentsDebuggingEnabled(mCurrentWebConfig.mWebContentsDebuggingEnabled);
            }
        }

        //修改配置
        WebSettings ws = getSettings();
        if( null != ws ) {
            CMWebConfigManager.sharedInstance().fixUrlSetting(url, ws);
        }
        return;
    }

    /**
     * @brief 初始化
     */
    public void setup() {
        if( mSetuped ) {
            return;
        }
        mSetuped = true;

        final WebViewClient wvc = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if( null != mCurrentWebConfig ) {
                    //检测是否是同一个主机
                    if( !mCurrentWebConfig.isSameHostUrl(url) ) {
                        //不是同一个主机要更换配置
                        resetUrlSettings(url);
                    }
                } else {
                    //不是同一个主机要更换配置
                    resetUrlSettings(url);
                }

                if( mCurrentWebConfig.mShouldOverrideUrlLoading ) {
                    //允许跳转到其它浏览器
                    return super.shouldOverrideUrlLoading(view, url);
                }

                //不能识别的scheme不处理
                if( !url.startsWith("http://") ) {
                    if( !url.startsWith("https://") ) {
                        if( !url.startsWith("liveme://") ) {
                            return true;
                        }
                    }
                }

                view.loadUrl(url);
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = view.getUrl();
                if( null != mCurrentWebConfig ) {
                    //检测是否是同一个主机
                    if( !mCurrentWebConfig.isSameHostUrl(url) ) {
                        //不是同一个主机要更换配置
                        resetUrlSettings(url);
                    }
                } else {
                    //不是同一个主机要更换配置
                    resetUrlSettings(url);
                }

                if( mCurrentWebConfig.mShouldOverrideUrlLoading ) {
                    //允许跳转到其它浏览器
                    return super.shouldOverrideUrlLoading(view, request);
                }

                //不能识别的scheme不处理
                if( !url.startsWith("http://") ) {
                    if( !url.startsWith("https://") ) {
                        if( !url.startsWith("liveme://") ) {
                            return true;
                        }
                    }
                }

                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if( null != mListener ) {
                    mListener.onPageStarted(view, url, favicon);
                }

                if( null != mCurrentWebConfig ) {
                    if( mCurrentWebConfig.mIsImagesAfterContent ) {
                        if(Build.VERSION.SDK_INT >= 19) {
                            view.getSettings().setLoadsImagesAutomatically(false);
                        } else {
                            view.getSettings().setLoadsImagesAutomatically(false);
                        }
                    }
                }
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if( null != mListener ) {
                    mListener.onPageFinished(view, url);
                }
                if(!view.getSettings().getLoadsImagesAutomatically()) {
                    view.getSettings().setLoadsImagesAutomatically(true);
                }
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                if( null != mCurrentWebConfig && mCurrentWebConfig.mManualCheckSSLError ) {
                    //手动校验sha256成功就允许加载页面
                    //有些时候由于Android系统的bug或者其他的原因, 导致我们的webview不能验证通过我们的https证书,
                    //最明显的例子就是华为手机mate7升级到Android7.0后, 手机有些网站打不开了, 而更新了webview的补丁后就没问题了
                    for( int i = 0; i < DOMAIN_CHECKSUM_CODE_SHA256.length; i++ ) {
                        String code = DOMAIN_CHECKSUM_CODE_SHA256[i];
                        if( isSSLCertOk(error.getCertificate(), code)) {
                            handler.proceed();
                            return;
                        }
                    }

                    //展示白屏
                    handler.cancel();

                    //显示证书错误
                    showSSLErrorDialog(view.getContext(), error);
                    return;
                }
                //展示白屏
                handler.cancel();
                return;
            }

            @Override
            public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
                super.onReceivedClientCertRequest(view, request);
            }
        };

        setWebViewClient(wvc);

        final WebChromeClient wcc = new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if( null != mListener ) {
                    mListener.onProgressChanged(view, newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        };
        setWebChromeClient(wcc);
        return;
    }

    @Override
    public void loadUrl(String url) {
        //重置配置
        resetUrlSettings(url);
        //初始化
        setup();

        super.loadUrl(url);
        return;
    }

    @Override
    public void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
        //重置配置
        resetUrlSettings(url);
        //初始化
        setup();

        super.loadUrl(url, additionalHttpHeaders);
    }

    @Override
    public void loadData(String data, String mimeType, String encoding) {
        //重置配置
        resetUrlSettings(null);
        //初始化
        setup();

        super.loadData(data, mimeType, encoding);
    }

    @Override
    public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding, String historyUrl) {
        //重置配置
        resetUrlSettings(baseUrl);
        //初始化
        setup();

        super.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
    }


    /**
     * @brief 显示证书验证错误
     * @param context
     * @param error
     */
    public static void showSSLErrorDialog(Context context, SslError error) {
        if( null == context || null == error ) {
            return;
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("警告");
        switch( error.getPrimaryError() ) {
            case SslError.SSL_INVALID:
                builder.setMessage("证书无效-1!");
                break;
            case SslError.SSL_DATE_INVALID:
                builder.setMessage("证书日期无效!");
                break;
            case SslError.SSL_EXPIRED:
                builder.setMessage("证书过期!");
                break;
            case SslError.SSL_NOTYETVALID:
                builder.setMessage("证书无效-2!");
                break;
            case SslError.SSL_UNTRUSTED:
                builder.setMessage("证书不可信任!");
                break;
            default:
                builder.setMessage("证书校验失败:" + error.getPrimaryError() + "!");
                break;
        }
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dlg, int i) {
                dlg.dismiss();
                return;
            }
        });
        builder.show();
        return;
    }


    /**
     * SSL证书错误，手动校验https证书
     * @param cert      https证书
     * @param sha256Str sha256值
     * @return true通过，false失败
     */
    public static boolean isSSLCertOk(SslCertificate cert, String sha256Str) {
        byte[] SSLSHA256 = hexToBytes(sha256Str);
        Bundle bundle = SslCertificate.saveState(cert);
        if (bundle != null) {
            byte[] bytes = bundle.getByteArray("x509-certificate");
            if (bytes != null) {
                try {
                    CertificateFactory cf = CertificateFactory.getInstance("X.509");
                    Certificate ca = cf.generateCertificate(new ByteArrayInputStream(bytes));
                    MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
                    byte[] key = sha256.digest(((X509Certificate) ca).getEncoded());
                    return Arrays.equals(key, SSLSHA256);
                } catch (Exception e) {
                    e.printStackTrace();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * @brief hexString转byteArray
     * @param hexString
     * @return 字节数组
     */
    public static byte[] hexToBytes(String hexString) {
        if( TextUtils.isEmpty(hexString) ) {
            return null;
        }
        if( hexString.trim().length() <= 0 ) {
            return null;
        }
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] bytes = new byte[length];
        String hexDigits = "0123456789abcdef";
        for (int i = 0; i < length; i++) {
            int pos = i * 2; // 两个字符对应一个byte
            int h = hexDigits.indexOf(hexChars[pos]) << 4; // 注1
            int l = hexDigits.indexOf(hexChars[pos + 1]); // 注2
            if (h == -1 || l == -1) { // 非16进制字符
                return null;
            }
            bytes[i] = (byte) (h | l);
        }
        return bytes;
    }


    public interface CMWebViewListener {
        /**
         * @brief 页面开始加载
         * @param view
         * @param url
         * @param favicon
         */
        void onPageStarted(WebView view, String url, Bitmap favicon);

        /**
         * @brief 页面结束加载
         * @param view
         * @param url
         */
        void onPageFinished(WebView view, String url);

        /**
         * @brief 加载进度变更
         * @param view
         * @param newProgress
         */
        void onProgressChanged(WebView view, int newProgress);
    }
}
