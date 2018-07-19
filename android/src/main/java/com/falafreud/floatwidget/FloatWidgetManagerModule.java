package com.falafreud.floatwidget;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;

public class FloatWidgetManagerModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;
    private static final int SYSTEM_ALERT_WINDOW_PERMISSION = 2048;
    private static final String TAG = "FloatWidget";

    public FloatWidgetManagerModule(ReactApplicationContext reactContext) {

        super(reactContext);
        this.reactContext = reactContext;

        Context context = getReactApplicationContext().getApplicationContext();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(context)) {
            this.askPermission();
        }
        this.startService();
    }

    @Override
    public String getName() {

        return "FloatWidgetManagerModule";
    }

    private void startService() {

        Log.d(TAG, "FloatWidgetManagerModule startService");

        Context context = getReactApplicationContext().getApplicationContext();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            context.startService(new Intent(context, FloatIconService.class));
        } else if (Settings.canDrawOverlays(context)) {
            context.startService(new Intent(context, FloatIconService.class));
        } else {
            askPermission();
        }
    }

    private void askPermission() {

        Context context = getReactApplicationContext().getApplicationContext();
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName()));
        getCurrentActivity().startActivityForResult(intent, SYSTEM_ALERT_WINDOW_PERMISSION);
    }
}