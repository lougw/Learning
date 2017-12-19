package com.lougw.net;

import android.text.TextUtils;

import com.lougw.net.anno.HttpProtocolParam;
import com.lougw.net.anno.HttpSenderCommand;
import com.lougw.net.anno.PostParam;

import org.json.JSONException;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 *     author : lougw
 *     e-mail : gaoweilou@pptv.com
 *     time   : 2017/12/12
 *     desc   :
 * </pre>
 */

public class HttpSenderProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        HttpSenderCommand command = method.getAnnotation(HttpSenderCommand.class);
        String url = command.url();
        String url_br = command.url_br();
        int httpMethod = command.method();
        Class responseClz = command.responseBean();
        boolean isPostByJson = command.postByJson();
        boolean shouldCache = command.shouldCache();
        String cacheKey = command.cacheKey();
        Map<String, Object> postParams = null;
        if (httpMethod == BeanFactory.GET) {
            url = generateUrlNoEntry(url, url_br, method, args);
        } else {
            HashMap<String, Object> map = new HashMap<String, Object>();
            url = generateUrlHasEntry(url, url_br, method, args, map);
            postParams = map;
        }
        sendService(responseClz, httpMethod, url, postParams, getCallback(args), shouldCache, cacheKey);
        return null;
    }


    private void sendService(final Class responseClz, final int httpMethod, final String url, final Map<String, Object> postParams, final
    HttpSenderCallback callBack, boolean shouldCache, String cacheKey) {


    }

    public static HttpSenderCallback getCallback(Object[] args) {
        HttpSenderCallback callback = null;
        for (Object arg : args) {
            if (arg instanceof HttpSenderCallback) {
                if (callback != null) {
                    throw new IllegalStateException("Only one DataEvent argument is allowed in Sender interface");
                }
                callback = (HttpSenderCallback) arg;
            }
        }
        return callback;
    }

    private String generateUrlNoEntry(String url, String url_br, Method method, Object[] args) {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        StringBuffer tempUrl_br = new StringBuffer(url_br);
        boolean containsParamInfo = containsParamInfo(url_br);
        if (!url_br.endsWith("?") && !containsParamInfo) {
            tempUrl_br.append("?");
        }
        for (int i = 0; i < args.length; i++) {
            Annotation[] annotations = parameterAnnotations[i];
            if (annotations.length <= 0) {
                continue;
            }
            String param = args[i] == null ? null : String.valueOf(args[i]);
            if (!TextUtils.isEmpty(param) && isContainChinese(param)) {
                param = URLEncoder.encode(param);
            }

            Annotation annotation = annotations[0];
            if (annotation instanceof HttpProtocolParam) {
                HttpProtocolParam paramAnnotation = (HttpProtocolParam) annotation;
                String fieldName = paramAnnotation.fieldName();
                if (TextUtils.isEmpty(fieldName)) {
                    tempUrl_br.append("&");
                    tempUrl_br.append(param);
                    continue;
                }
                // Pattern pattern = Pattern.compile(new StringBuilder("\\/").append(fieldName).append("(\\/|$|\\?)").toString());//结尾：要么没有要么/要么?
                Pattern pattern = Pattern.compile("\\{" + fieldName + "\\}");//替换url中的{fieldname}类似的字符串

                Matcher matcher = pattern.matcher(tempUrl_br);
                if (matcher.find()) { //如果tempUrl_br里面包含{fieldName}这样的字符串，那么把它的值缓存这里fieldName对应的参数
                    if (param == null) {
                        param = "";
                    }
                    tempUrl_br = new StringBuffer(tempUrl_br.toString().replace("{" + fieldName + "}", param));
                } else { //如果tempUrl_br里面不包含fieldName的字符串，则在后面拼接参数
                    if (param != null && param.length() > 0) {
                        tempUrl_br.append("&");  //把&改到拼fieldname=value之前添加，因为当value是null的时候不拼串，如果在拼fieldname=value后添加，可能错误的多加了&
                        tempUrl_br.append(fieldName).append("=");

                        tempUrl_br.append(param);
                    }
                }
            }
        }

        String resultUrl = url + tempUrl_br;
        resultUrl = resultUrl.replace("?&", "?");
        if (resultUrl.endsWith("?")) {
            resultUrl = resultUrl.substring(0, resultUrl.length() - 1);
        }


        return resultUrl;
    }

    private String generateUrlHasEntry(String url, String url_br, Method method, Object[] args, HashMap<String, Object> map) throws
            IllegalAccessException, InstantiationException, NoSuchFieldException, JSONException {
        Annotation[][] ParameterAnnotations = method.getParameterAnnotations();
        String result = generateUrlNoEntry(url, url_br, method, args);

        for (int i = 0; i < args.length; i++) {
            Annotation[] annotations = ParameterAnnotations[i];
            if (annotations.length <= 0) {
                continue;
            }
            Annotation annotation = annotations[0];
            if (annotation instanceof PostParam && args[i] != null) {
                PostParam paramAnnotation = (PostParam) annotation;
                String fieldName = paramAnnotation.fieldName();
                map.put(fieldName, args[i]);
            }
        }

        return result;
    }

    private boolean containsParamInfo(String url_br) {
        Pattern pattern = Pattern.compile("\\?\\w*=\\w*");
        Matcher matcher = pattern.matcher(url_br);
        return matcher.find();
    }

    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        return m.find();
    }
}
