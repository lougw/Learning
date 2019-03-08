package com.lougw.learning;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ServiceActivity extends AppCompatActivity {
    TestService.MyBinderOne myBinderOne;
    TestService.MyBinderTwo myBinderTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.one, R.id.two})
    public void Click(View v) {
        if (v.getId() == R.id.one) {
            if (myBinderOne == null) {
                Intent intent = new Intent(this, TestService.class);
                intent.setAction("one");
                bindService(intent, connOne, Context.BIND_AUTO_CREATE);
            }else{
                myBinderOne.onOne();
            }


        } else if (v.getId() == R.id.two) {
            if (myBinderTwo == null) {
                Intent intent = new Intent(this, TestService.class);
                intent.setAction("two");
                bindService(intent, connTwo, Context.BIND_AUTO_CREATE);
            }else{
                myBinderTwo.onTwo();
            }

        }
    }


    private ServiceConnection connOne = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            myBinderOne = (TestService.MyBinderOne) binder;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            myBinderOne = null;
        }
    };
    private ServiceConnection connTwo = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            myBinderTwo = (TestService.MyBinderTwo) binder;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            myBinderTwo = null;
        }
    };
}
