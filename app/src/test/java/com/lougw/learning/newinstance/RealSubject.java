package com.lougw.learning.newinstance;

public class RealSubject implements ISubject{
    @Override
    public void request(String str) {
        System.out.println("requestrequestrequestrequest");
    }

    @Override
    public String test() {
        return "test";
    }
}
