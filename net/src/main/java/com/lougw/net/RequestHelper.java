package com.lougw.net;

/**
 * <pre>
 *     author : lougw
 *     e-mail : gaoweilou@pptv.com
 *     time   : 2017/12/29
 *     desc   :
 * </pre>
 */

public class RequestHelper {
    public static final int DEFAULTITMEOUT = 5;


    private RequestHelper() {

    }

    public RequestHelper getRequestHelper() {
        return RequestHelperHolder.INSTANCE;
    }

    private static class RequestHelperHolder {
        public static RequestHelper INSTANCE = new RequestHelper();
    }
}

