package com.lougw.learning.aop;

import android.util.Log;
import android.view.View;

import com.lougw.learning.utils.UIUtils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
public class ViewOnClickListenerAspectj {
    private static final String TAG = ViewOnClickListenerAspectj.class.getSimpleName();

    @Before("execution(* android.view.View.OnClickListener.onClick(android.view.View))")
    public void onClickMethodExecution(final JoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        String viewId = "";

        if (joinPoint != null && joinPoint.getArgs() != null) {
            for (int i = 0; i < joinPoint.getArgs().length; i++) {
                Object obj = joinPoint.getArgs()[i];
                if (obj != null && obj instanceof View) {
                    viewId = UIUtils.getResourceEntryName(((View) obj).getId());
                }
            }
        }
        Log.d(TAG, buildLogMessage(className, methodName, viewId));
    }


    public static String buildLogMessage(String className, String methodName, String viewId) {
        StringBuilder message = new StringBuilder();
        message.append(" --> ");
        message.append(className);
        message.append(" --> ");
        message.append(methodName);
        message.append(" --> ");
        message.append(" viewId");
        message.append(viewId);
        message.append("ms");
        return message.toString();
    }
}
