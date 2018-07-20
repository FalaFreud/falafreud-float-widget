package com.falafreud.floatwidget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;

import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.falafreud.floatwidget.icon.FloatIconService;
import com.falafreud.floatwidget.message.service.MessageNotificationExtenderService;

/**
 * Created by Haroldo Shigueaki Teruya on 18/07/18.
 */
public class FloatWidgetManagerModule extends ReactContextBaseJavaModule implements LifecycleEventListener {

    private final ReactApplicationContext reactContext;
    private BroadcastReceiver broadcastReceiver = null;
    private static final int SYSTEM_ALERT_WINDOW_PERMISSION = 2048;
    private static final String SHOW_FLOAT_WIDGET_WHEN_APPLICATION_INACTIVE = "FLOAT_WIDGET";
    private static final String TAG = "FloatWidget";

    public FloatWidgetManagerModule(ReactApplicationContext reactContext) {

        super(reactContext);

        this.reactContext = reactContext;
        this.getReactApplicationContext().addLifecycleEventListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(reactContext)) {
            this.askPermission();
        }
    }

    @Override
    public String getName() {

        return "FloatWidgetManagerModule";
    }

    /**
     * This method is called "onPaused".
     */
    private void handleStartService() {

        Log.d(TAG, getName() + " startService");

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            this.startService();
        } else if (Settings.canDrawOverlays(this.reactContext)) {
            this.startService();
        } else {
            askPermission();
        }
    }

    /**
     * This method is called from "handleStartService".
     */
    private void startService() {

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MessageNotificationExtenderService.Constant.ACTION);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Log.d(TAG, getName() + " startService onReceive 0");
                if (intent != null) {
                    Log.d(TAG, getName() + " startService onReceive 1");
                }
            }
        };

        getCurrentActivity().registerReceiver(broadcastReceiver, intentFilter);
        this.reactContext.startService(new Intent(this.reactContext, FloatIconService.class));
    }

    /**
     * this line invoke the FloatIconService "onDestroy".
     */
    private void stopService() {

        this.reactContext.unregisterReceiver(broadcastReceiver);
        reactContext.stopService(new Intent(reactContext, FloatIconService.class));
    }

    private void askPermission() {

        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + this.reactContext.getPackageName()));
        getCurrentActivity().startActivityForResult(intent, SYSTEM_ALERT_WINDOW_PERMISSION);
    }

    @Override
    public void onHostResume() {

    }

    @Override
    public void onHostPause() {

        Log.d(TAG, getName() + " onHostPause " + this.isToShowWhenApplicationInactive());
        if (this.isToShowWhenApplicationInactive()) {
            this.handleStartService();
        }
    }

    @Override
    public void onHostDestroy() {

        Log.d(TAG, getName() + " onHostDestroy");
        this.stopService();
    }

    @ReactMethod
    public void showWhenApplicationInactive(boolean enable) {

        Log.d(TAG, getName() + " showWhenApplicationInactive: " + enable);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getReactApplicationContext().getBaseContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SHOW_FLOAT_WIDGET_WHEN_APPLICATION_INACTIVE, enable);
        editor.apply();
    }

    @ReactMethod
    public Boolean isToShowWhenApplicationInactive() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getReactApplicationContext().getBaseContext());
        return preferences.getBoolean(SHOW_FLOAT_WIDGET_WHEN_APPLICATION_INACTIVE, false);
    }
}
