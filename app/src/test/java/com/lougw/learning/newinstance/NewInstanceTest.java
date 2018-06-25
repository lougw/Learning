package com.lougw.learning.newinstance;

import org.junit.Test;

import java.lang.reflect.Proxy;

/**
 * <pre>
 *     author : lougw
 *     e-mail : gaoweilou@pptv.com
 *     time   : 2017/12/18
 *     desc   :
 * </pre>
 */

public class NewInstanceTest {
    @Test
    public void TestNewInstance() {
        ISubject subject = (ISubject) Proxy.newProxyInstance(ISubject.class.getClassLoader(),
                new RealSubject().getClass().getInterfaces(), new DynamicProxy());
        subject.request("ss");

//        try {
//            Persion  persion= (Persion) Class.forName("com.lougw.learning.newinstance.OldMan").newInstance();
//
//            persion.work();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
    }
}
