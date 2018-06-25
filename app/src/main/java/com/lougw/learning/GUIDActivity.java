package com.lougw.learning;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class GUIDActivity extends AppCompatActivity {
    private static final String INVALID_DEVICE_ID = "000000000000000";

    private static final String INVALID_BLUETOOTH_ADDRESS = "02:00:00:00:00:00";

    private static final String INVALID_ANDROID_ID = "9774d56d682e549c";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guid);

        String iid = InstanceID.getInstance(this).getId();
        String uniqueID = UUID.randomUUID().toString();
        //359866f5-4e6f-45b5-aeea-113903d4307b
        //dkOKX8HLvVM
        Log.d("GUID123"," iid : "+iid+ "  uniqueID : "+uniqueID);
        final String sn =  android.os.Build.SERIAL;
        Log.d("GUID123"," sn : "+sn);

        Log.d("GUID123"," getMacAddr : "+getMacAddress(this));

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        AdvertisingIdClient.Info info= AdvertisingIdClient.getAdvertisingIdInfo(GUIDActivity.this);

                    Log.d("GUID123"," info.getId() : "+info.getId());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                }
            }).start();
        String mac = null;
        final WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            final WifiInfo info = wifiManager.getConnectionInfo();
            if (info != null) {
                mac = info.getMacAddress();
                Log.d("GUID123"," mac : "+mac);
            }
        }



    }



    public static String getMacAddress(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
                for (NetworkInterface nif : all) {
                    if (!nif.getName().equalsIgnoreCase("wlan0")) {
                        continue;
                    }

                    byte[] macBytes = nif.getHardwareAddress();
                    if (macBytes == null) {
                        return "";
                    }

                    StringBuilder res1 = new StringBuilder();
                    for (byte b : macBytes) {
                        res1.append(String.format("%02X:", b));
                    }

                    if (res1.length() > 0) {
                        res1.deleteCharAt(res1.length() - 1);
                    }
                    return res1.toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {

                WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo info;
                if (wifi != null && (info = wifi.getConnectionInfo()) != null) {
                    return info.getMacAddress();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "02:00:00:00:00:00";
    }
}
