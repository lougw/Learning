package com.lougw.net;

/**
 * <pre>
 *     author : lougw
 *     e-mail : gaoweilou@pptv.com
 *     time   : 2017/12/12
 *     desc   :
 * </pre>
 */

public class ResponseModel {
    public static final int NetworkError = 0x10;
    public static final int NoConnectionError = 0x11;
    public static final int ServerError = 0x12;
    public static final int AuthFailureError = 0x13;
    public static final int TimeoutError = 0x14;
    public static final int NoDataError = 0x15;
    public static final int OtherError = 0x16;
    public static final int NoLogin = 0x17;

    public int code;
    public String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
