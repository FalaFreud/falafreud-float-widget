package com.falafreud.floatwidget;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;

public class FloatWidgetManagerModule extends ReactContextBaseJavaModule
{
    private final ReactApplicationContext reactContext;
    private static final int SYSTEM_ALERT_WINDOW_PERMISSION = 2048;

    public FloatWidgetManagerModule(ReactApplicationContext reactContext)
    {
        super(reactContext);
        this.reactContext = reactContext;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(reactContext))
        {
            askPermission();
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
        {
            reactContext.startService(new Intent(reactContext, FloatIconService.class));
        }
        else if (Settings.canDrawOverlays(reactContext))
        {
            reactContext.startService(new Intent(reactContext, FloatIconService.class));
        }
        else
        {
            askPermission();
        }
    }

    @Override
    public String getName()
    {
        return "FloatWidgetManagerModule";
    }

    private void askPermission()
    {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + reactContext.getPackageName()));
        getCurrentActivity().startActivityForResult(intent, SYSTEM_ALERT_WINDOW_PERMISSION);
    }
}