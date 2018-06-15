package com.lougw.learning.net;

import com.lougw.net.BeanFactory;
import com.lougw.net.HttpSenderProxy;

/**
 * <pre>
 *     author : lougw
 *     e-mail : gaoweilou@pptv.com
 *     time   : 2017/12/29
 *     desc   :
 * </pre>
 */

public class RequestHelper {

    public static ISender getSender() {
        return BeanFactory.getInstance4Interface(ISender.class, new HttpSenderProxy());
    }
}

