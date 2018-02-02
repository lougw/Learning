package com.lougw.jettyserver;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

/**
 * Created by gaoweilou on 2017/5/17
 * Email.lgw0727@163.com
 */

public class WebService extends Service {
    public static void onStartService(Context context) {
        Intent intent = new Intent(context, WebService.class);
        context.startService(intent);

    }

    public static void onStopService(Context context) {
        Intent intent = new Intent(context, WebService.class);
        context.stopService(intent);
    }

    private Server server;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startServer();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopServer();
    }

    private void startServer() {
        if (server != null) {
            return;
        }
        new Thread(new StartRunnable()).start();
    }

    private void stopServer() {
        if (server != null) {
            new Thread(new StopRunnable()).start();
        }
    }

    class StartRunnable implements Runnable {
        @Override
        public void run() {
            try {
                server = new Server(9090);
                ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
                contextHandler.setContextPath("/");
                server.setHandler(contextHandler);
                ServletConfig.config(contextHandler);
                server.start();
                server.join();

            } catch (Exception e) {
                server = null;
                e.printStackTrace();
            }
        }
    }

    class StopRunnable implements Runnable {
        @Override
        public void run() {
            try {
                if (server != null) {
                    server.stop();
                    server = null;
                }

            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }
}
