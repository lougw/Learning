package com.lougw.learning.aop;

import android.util.Log;

//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;

//@Aspect
public class MethodCountAspectj {
    private static final String TAG = MethodCountAspectj.class.getSimpleName();

//    @Pointcut("execution(* *(..))")
//    public void invokeMethod() {
//
//    }
//
//    @Pointcut("execution(* com.lougw.learning.aop..*(..))")
//    public void invokeWatch() {
//
//    }

    //       @Around("invokeMethod() && !invokeWatch()")
//    public void aroundMethodExecution(final ProceedingJoinPoint joinPoint) {
//        try {
//            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
//            String className = methodSignature.getDeclaringType().getSimpleName();
//            String methodName = methodSignature.getName();
//            String stackTrace = getStackTrace();
//            final StopWatch stopWatch = new StopWatch();
//            stopWatch.start();
//            joinPoint.proceed();
//            stopWatch.stop();
//            Log.d(TAG, buildLogMessage(stackTrace, className, methodName, stopWatch.getTotalTimeMillis()));
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
//
//    }

//    @Pointcut("execution(* com.lougw.learning.modle.VideoDataInfo.get*(..))")
//    public void invokeClassMethod() {
//
//    }
//
//
//    @Before("invokeClassMethod()")
//    public void beforeMethodExecution(final JoinPoint joinPoint) {
//        try {
//            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
//            String className = methodSignature.getDeclaringType().getSimpleName();
//            String methodName = methodSignature.getName();
//            String stackTrace = getStackTrace();
//            final StopWatch stopWatch = new StopWatch();
//            Log.d(TAG, buildLogMessage(stackTrace, className, methodName, stopWatch.getTotalTimeMillis()));
//            getStackTrace();
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
//
//    }


    public static String buildLogMessage(String stackTrace, String className, String methodName, long methodDuration) {
        StringBuilder message = new StringBuilder();
        message.append(" --> ");
        message.append(stackTrace);
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

    public static String getStackTrace() {
        StringBuilder sb = new StringBuilder();
        StackTraceElement[] stackTraceElements = (new Throwable()).getStackTrace();
        for (int i = 0; i < stackTraceElements.length; i++) {
            if (i == 3) {
                StackTraceElement stackTraceElement = stackTraceElements[i];
                if (stackTraceElement != null) {
                    sb.append(stackTraceElement.getClassName());
                    sb.append(".");
                    sb.append(stackTraceElement.getMethodName());
                    sb.append("( )");
                    sb.append("-->( ");
                    sb.append(stackTraceElement.getLineNumber());
                    sb.append(" )");
                }
                break;
            }
            continue;
        }
        return sb.toString();
    }


}
