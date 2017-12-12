package com.lougw.learning.utils;

import com.lougw.net.BeanFactory;
import com.lougw.net.HttpSenderProxy;

/**
 * <pre>
 *     author : lougw
 *     e-mail : gaoweilou@pptv.com
 *     time   : 2017/12/12
 *     desc   :
 * </pre>
 */

public class RequestManager {

    public static RequestSender getRequestSender() {
        return BeanFactory.getInstance4Interface(RequestSender.class, new HttpSenderProxy());
    }
}
