package com.falafreud.floatwidget;

import android.content.Context;
import android.content.Intent;
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

public class FloatWidgetManagerModule extends ReactContextBaseJavaModule implements LifecycleEventListener {

    private final ReactApplicationContext reactContext;
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

    private void startService() {

        Log.d(TAG, getName() + " startService");

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            this.reactContext.startService(new Intent(this.reactContext, FloatIconService.class));
        } else if (Settings.canDrawOverlays(this.reactContext)) {
            this.reactContext.startService(new Intent(this.reactContext, FloatIconService.class));
        } else {
            askPermission();
        }
    }

    // this line invoke the FloatIconService "onDestroy".
    private void stopService() {
        reactContext.stopService(new Intent(reactContext, FloatIconService.class));
    }

    private void askPermission() {

        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + this.reactContext.getPackageName()));
        getCurrentActivity().startActivityForResult(intent, SYSTEM_ALERT_WINDOW_PERMISSION);
    }

    @Override
    public void onHostResume() {

        Log.d(TAG, getName() + " onHostResume");
        this.stopService();
    }

    @Override
    public void onHostPause() {

        Log.d(TAG, getName() + " onHostPause");
        this.startService();
    }

    @Override
    public void onHostDestroy() {

    }

    @ReactMethod
    public void showWhenApplicationInactive(boolean enable) {

        Log.d(TAG,getName() + " showWhenApplicationInactive: " + enable);
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
