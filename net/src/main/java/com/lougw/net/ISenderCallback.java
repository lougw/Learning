package com.lougw.net;

/**
 * <pre>
 *     author : lougw
 *     e-mail : gaoweilou@pptv.com
 *     time   : 2017/12/12
 *     desc   :
 * </pre>
 */

public interface ISenderCallback<T> {

    void onSuccess(T result);

    void onFail(ErrorResponseModel error);
}
