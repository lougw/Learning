package com.lougw.learning.utils;


import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class ViewOnClickListenerAspectj {
    @Before("execution(* *(..))")
    public void onViewClickAOP(final JoinPoint joinPoint) throws Throwable {
        Log.d("654321", "11111111"+joinPoint.getSignature().toString());
    }
}
