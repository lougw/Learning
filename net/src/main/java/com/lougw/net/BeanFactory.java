package com.lougw.net;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *     author : lougw
 *     e-mail : gaoweilou@pptv.com
 *     time   : 2017/12/12
 *     desc   :
 * </pre>
 */

public class BeanFactory {

    private static Map<String, Object> beanMap = new HashMap<String, Object>();

    @SuppressWarnings("unchecked")
    public static <T> T getInstance4Interface(Class<T> interClazz, InvocationHandler handler) {
        String key = interClazz.getName();
        if (beanMap.containsKey(key)) {
            return (T) beanMap.get(key);
        }
        T bean = getInterfaceInstance(interClazz, handler);
        beanMap.put(key, bean);
        return bean;
    }

    @SuppressWarnings("unchecked")
    private static <T> T getInterfaceInstance(Class<T> clazz, InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, handler);
    }


}
