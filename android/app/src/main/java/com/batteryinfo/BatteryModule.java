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
        // Module name by which it will be accessible in JavaScript
        return "BatteryInfo";
    }

    @ReactMethod
    public void getBatteryPercentage(Callback onSuccess) {
        // Intents allow us to pass data between different applications.
        // Read more: https://developer.android.com/guide/components/intents-filters
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        // Here we register a receiver with a filter that allows us to listen to battery change event.
        // Upon registering, we receive an intent that contains the battery information.
        Intent batteryStatus = this.reactContext.getApplicationContext().registerReceiver(null, ifilter);
        // Fetch battery level information from intent.
        // BatteryManager exposes constants to read available battery data from intent.
        // Read More: https://developer.android.com/reference/android/os/BatteryManager.html
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        float batteryLevel = Math.round((level / (float) scale) * 100);
        // Invoke Javascript callback to return data
        onSuccess.invoke(batteryLevel);
    }

    @ReactMethod
    public void getBatteryChargingStatus(Callback onSuccess) {
        // Register battery event receiver to receive an intent with battery information
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = this.reactContext.getApplicationContext().registerReceiver(null, ifilter);
        // Get battery status information from intent
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
        // Invoke Javascript callback to return data.
        onSuccess.invoke("Discharging");
    }
}
