package com.lougw.net;

import android.text.TextUtils;

import com.lougw.net.anno.HttpProtocolParam;
import com.lougw.net.anno.HttpSenderCommand;
import com.lougw.net.anno.PostParam;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

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
    public Object invoke(Object proxy, Method method, Object[] args) {
        try {
            HttpSenderCommand command = method.getAnnotation(HttpSenderCommand.class);
            String url = command.url();
            String api = command.api();
            int httpMethod = command.method();
            Class responseClz = command.responseBean();
            boolean isPostByJson = command.postByJson();
            Map<String, String> postParams = null;
            if (httpMethod == RequestMethod.GET) {
                url = generateUrlNoEntry(url, api, method, args);
            } else {
                HashMap<String, String> map = new HashMap<String, String>();
                url = generateUrlHasEntry(url, api, method, args, map);
                postParams = map;
            }
            sendService(httpMethod, url, responseClz, postParams, isPostByJson, getCallback(args));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private void sendService(final int httpMethod, final String url, final Class responseClz, final Map<String, String> postParams, boolean isPostByJson, final
    HttpSenderCallback callBack) {

        OkHttpClient client = OkHttpFactory.getInstance().getClient();
        final okhttp3.Request request;
        if (RequestMethod.GET == httpMethod) {
            request = createOkHttpGetRequest(url);
        } else {
            request = createOKHttpPostRequest(url, postParams, isPostByJson);
        }

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                if (callBack != null) {


                    Observable.empty().observeOn(AndroidSchedulers.mainThread()).doOnComplete(new Action() {
                        @Override
                        public void run() throws Exception {
                            final ResponseModel model = new ResponseModel();
                            model.message = e.toString();
                            callBack.onFail(model);
                        }
                    }).subscribe();

                }
            }

            @Override
            public void onResponse(Call call, final okhttp3.Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    final String result = response.body().string();
                    if (callBack != null) {
                        if (responseClz == String.class) {

                            Observable.empty().observeOn(AndroidSchedulers.mainThread()).doOnComplete(new Action() {
                                @Override
                                public void run() throws Exception {
                                    callBack.onSuccess(result);
                                }
                            }).subscribe();


                        } else  {

                        }
                    }
                } else {

                    Observable.empty().observeOn(AndroidSchedulers.mainThread()).doOnComplete(new Action() {
                        @Override
                        public void run() throws Exception {
                            final ResponseModel model = new ResponseModel();
                            if (response != null) {
                                model.message = response.message();
                                model.code = response.code();
                            } else {

                            }
                            callBack.onFail(model);
                        }
                    }).subscribe();


                }
            }
        });

    }


    private static okhttp3.Request createOkHttpGetRequest(String url) {
        return new okhttp3.Request.Builder()
                .url(url)
                .get()
                .build();
    }

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static okhttp3.Request createOKHttpPostRequest(String url, Map<String, String> postParams, boolean isPostByJson) {
        if (isPostByJson) {
            JSONObject jsonObject = new JSONObject();
            if (postParams != null) {
                for (String key : postParams.keySet()) {
                    try {
                        jsonObject.put(key, postParams.get(key));
                    } catch (JSONException e) {
                    }
                }
            }
            RequestBody body = RequestBody.create(JSON, jsonObject.toString());
            return new okhttp3.Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
        } else {
            FormBody.Builder formBodyBuilder = new FormBody.Builder();
            if (postParams != null) {
                for (String key : postParams.keySet()) {
                    formBodyBuilder.add(key, postParams.get(key));
                }
            }
            return new okhttp3.Request.Builder()
                    .url(url)
                    .post(formBodyBuilder.build())
                    .build();
        }
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

    private String generateUrlHasEntry(String url, String api, Method method, Object[] args, HashMap<String, String> map) {
        Annotation[][] ParameterAnnotations = method.getParameterAnnotations();
        String result = generateUrlNoEntry(url, api, method, args);

        for (int i = 0; i < args.length; i++) {
            Annotation[] annotations = ParameterAnnotations[i];
            if (annotations.length <= 0) {
                continue;
            }
            Annotation annotation = annotations[0];
            if (annotation instanceof PostParam && args[i] != null) {
                PostParam paramAnnotation = (PostParam) annotation;
                String fieldName = paramAnnotation.fieldName();
                map.put(fieldName, args[i] + "");
            }
        }

        return result;
    }

    private String generateUrlNoEntry(String url, String api, Method method, Object[] args) {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        StringBuffer tempUrl_br = new StringBuffer(api);
        boolean containsParamInfo = containsParamInfo(api);
        if (!api.endsWith("?") && !containsParamInfo) {
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
