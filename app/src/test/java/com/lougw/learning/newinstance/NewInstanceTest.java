package com.lougw.learning.newinstance;

import org.junit.Test;

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
    public void  TestNewInstance(){
        try {
            Persion  persion= (Persion) Class.forName("com.lougw.learning.newinstance.OldMan").newInstance();

            persion.work();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
