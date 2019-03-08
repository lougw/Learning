package com.lougw.learning;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class TestService extends Service {
    public TestService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        String action = intent.getAction();
        if ("two".equals(action)) {
            return two;
        }
        return one;
    }

    private MyBinderOne one = new MyBinderOne();
    private MyBinderTwo two = new MyBinderTwo();

    public class MyBinderOne extends Binder {
        public void onOne() {
            Log.d("LgwTag", "one");
        }
    }

    public class MyBinderTwo extends Binder {
        public void onTwo() {
            Log.d("LgwTag", "two");
        }
    }
}
