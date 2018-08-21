package com.lougw.learning.utils;

import android.util.Log;
import android.view.View;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class AspectjTest {
    @Pointcut("execution(* onCreate(android.os.Bundle))")
    public void invokeCreate() {
    }

    @Pointcut("execution(* onResume())")
    public void invokeResume() {
    }

    @Before("execution(* android.view.View.OnClickListener.onClick(android.view.View))")
    public void beforeMethodExecution(final JoinPoint joinPoint) throws Throwable {

        if (joinPoint != null && joinPoint.getArgs() != null) {
            for (int i = 0; i < joinPoint.getArgs().length; i++) {
                Object obj = joinPoint.getArgs()[i];
                if (obj != null && obj instanceof View) {
                    Report.getInstance().report((View) obj);
                }
            }
        }

    }


//    @After("execution(* onCreate(android.os.Bundle))")
//    public void afterMethodExecution(final JoinPoint joinPoint) throws Throwable {
//        Log.e("654321", "After->" + joinPoint.getTarget().toString() + "#" + joinPoint.getSignature().getName());
//
//    }


    @After("invokeCreate()|| invokeResume()")
    public void afterPointcutMethodExecution(final JoinPoint joinPoint) throws Throwable {
//        Log.e("654321", "After-> 123  " + joinPoint.getTarget().toString() + "#" + joinPoint.getSignature().getName());

    }


    @Around("execution(* *(..))")
    public void aroundMethodExecution(final ProceedingJoinPoint joinPoint) throws Throwable {
        if (joinPoint == null || joinPoint.getTarget() == null) {
            return;
        }
        long lastTime = System.currentTimeMillis();
        Log.e("654321", "around-> 111 " + joinPoint.getTarget().toString() + "#" + joinPoint.getSignature().getName());
        joinPoint.proceed();
        Log.e("654321", "around-> 222 " + joinPoint.getTarget().toString() + "#" + joinPoint.getSignature().getName() + " " + (System.currentTimeMillis() - lastTime));
    }

}
