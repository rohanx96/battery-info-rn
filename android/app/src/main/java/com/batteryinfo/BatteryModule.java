package com.batteryinfo;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import javax.annotation.Nonnull;

/**
 * Created by rohanx96 on 28/05/19.
 */
public class BatteryModule extends ReactContextBaseJavaModule {
    private ReactApplicationContext reactContext;

    public BatteryModule(@Nonnull ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Nonnull
    @Override
    public String getName() {
        return "BatteryInfo";
    }

    @ReactMethod
    public void getBatteryPercentage(Callback onSuccess) {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = this.reactContext.getApplicationContext().registerReceiver(null, ifilter);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        float batteryLevel = Math.round((level / (float) scale) * 100);
        onSuccess.invoke(batteryLevel);
    }

    @ReactMethod
    public void getBatteryChargingStatus(Callback onSuccess) {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = this.reactContext.getApplicationContext().registerReceiver(null, ifilter);
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;
        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
        if (isCharging) {
            if (usbCharge) {
                onSuccess.invoke("Charging over USB");
                return;
            } else if (acCharge) {
                onSuccess.invoke("Charging on AC");
                return;
            } else {
                onSuccess.invoke("Charging");
                return;
            }
        }
        onSuccess.invoke("Discharging");
    }
}
