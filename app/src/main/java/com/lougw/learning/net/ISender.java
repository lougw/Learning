package com.lougw.learning.net;

import com.lougw.net.HttpSenderCallback;
import com.lougw.net.RequestMethod;
import com.lougw.net.anno.HttpProtocolParam;
import com.lougw.net.anno.HttpSenderCommand;

public interface ISender {

    @HttpSenderCommand(url = "", api = "/topic/getTopicSet", responseBean = String.class)
    void test(HttpSenderCallback callback,
              @HttpProtocolParam(fieldName = "test") String test);


    @HttpSenderCommand(url = "", api = "/topic/getTopicSet", responseBean = String.class,method = RequestMethod.POST)
    void test2(HttpSenderCallback callback,
              @HttpProtocolParam(fieldName = "test") String test);
}
