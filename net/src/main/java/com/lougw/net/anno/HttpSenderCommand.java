package com.lougw.net.anno;

import com.lougw.net.BeanFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;

/**
 * <pre>
 *     author : lougw
 *     e-mail : gaoweilou@pptv.com
 *     time   : 2017/12/12
 *     desc   :
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface HttpSenderCommand {
    String url();

    String url_br();

    boolean shouldCache() default false;

    String cacheKey();

    int method() default BeanFactory.GET;

    Class responseBean();

    boolean postByJson() default false;
}
