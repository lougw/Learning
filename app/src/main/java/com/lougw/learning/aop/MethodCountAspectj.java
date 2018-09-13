package com.lougw.learning.aop;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
public class MethodCountAspectj {
    private static final String TAG = MethodCountAspectj.class.getSimpleName();

    @Pointcut("execution(* *(..))")
    public void invokeMethod() {

    }

    @Pointcut("execution(* com.lougw.learning.aop.StopWatch.*(..))")
    public void invokeWatch() {

    }

    @Around("invokeMethod() && !invokeWatch()")
    public void aroundMethodExecution(final ProceedingJoinPoint joinPoint) {
        try {
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            String className = methodSignature.getDeclaringType().getSimpleName();
            String methodName = methodSignature.getName();
            final StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            joinPoint.proceed();
            stopWatch.stop();
            Log.d(TAG, buildLogMessage(className, methodName, stopWatch.getTotalTimeMillis()));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }

    public static String buildLogMessage(String className, String methodName, long methodDuration) {
        StringBuilder message = new StringBuilder();
        message.append(" --> ");
        message.append(className);
        message.append(" --> ");
        message.append(methodName);
        message.append(" --> ");
        message.append("[  ");
        message.append(methodDuration);
        message.append("ms");
        message.append("  ]");
        return message.toString();
    }
}
