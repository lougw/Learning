package com.lougw.learning.utils;

import android.util.Log;
import android.view.View;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

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


    @Pointcut("execution(@com.lougw.learning.utils.CheckLogin * *(..))")
    public void testAspect() {

    }

    @Around("testAspect()")
    public void testTestAspect(final ProceedingJoinPoint joinPoint) {
        if (joinPoint == null || joinPoint.getTarget() == null) {
            return;
        }
        long lastTime = System.currentTimeMillis();
        Log.e("654321", "around-> 333 " + joinPoint.getTarget().toString() + "#" + joinPoint.getSignature().getName());
        try {
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            String className = methodSignature.getDeclaringType().getSimpleName();
            String methodName = methodSignature.getName();
            final StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            joinPoint.proceed();
            stopWatch.stop();
            Log.d("7654321", buildLogMessage(className, methodName, stopWatch.getTotalTimeMillis()));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        Log.e("654321", "around-> 444 " + joinPoint.getTarget().toString() + "#" + joinPoint.getSignature().getName() + " " + (System.currentTimeMillis() - lastTime));
    }

    @Pointcut("execution(* com.lougw.learning.MainActivity.getName(..))")
    public void invokeReturning() {
    }

    @Pointcut("execution(* com.lougw.learning.MainActivity.isHigh(..))")
    public void invokeBooleanReturning() {
    }

    @AfterReturning(value = "invokeReturning() ||  invokeBooleanReturning()", returning = "result")
    public void callMethod(JoinPoint joinPoint, Object result) {
        Log.d("654321", "AfterReturning  : " + joinPoint.getSignature() + "-------" + joinPoint.getTarget() + "-------returnValue:" + result);
    }

    private static String buildLogMessage(String className, String methodName, long methodDuration) {
        StringBuilder message = new StringBuilder();
        message.append("Gintonic --> ");
        message.append(className);
        message.append(" --> ");
        message.append(methodName);
        message.append(" --> ");
        message.append("[");
        message.append(methodDuration);
        message.append("ms");
        message.append("]");
        return message.toString();
    }
}
