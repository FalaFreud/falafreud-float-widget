package com.falafreud.floatwidget;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.falafreud.floatwidget.R;
import com.falafreud.floatwidget.icon.IconCallback;
import com.falafreud.floatwidget.icon.Magnet;

class FloatIconService extends Service implements IconCallback
{
    // GLOBAL VARIABLES ============================================================================
    // =============================================================================================

    private static final String TAG = "FloatIcon";
    private Magnet magnet;

    // METHODS =====================================================================================
    // =============================================================================================

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        Log.d(TAG, "onCreate");
        this.startMagnet();
    }

    void destroy()
    {
        if (magnet != null)
        {
            magnet.destroy();
        }
        this.stopSelf();
    }

    /**
     * This method instantiate the magnet object.
     * See {@link Magnet}
     */
    public void startMagnet()
    {
        if (magnet == null)
        {
            magnet = Magnet.newBuilder(FloatIconService.this)
                    .setIconView(R.layout.logo_float_layout)
                    .setIconCallback(this)
                    .setHideFactor(0.2f)
                    .setShouldShowRemoveView(true)
                    .setRemoveIconResId(R.drawable.ic_close)
                    .setRemoveIconShadow(R.drawable.bottom_shadow)
                    .setShouldStickToWall(true)
                    .setRemoveIconShouldBeResponsive(true)
                    .setInitialPosition(100, 200)
                    .build();
            magnet.show();
        }
    }

    /**
     * This method is called when the user moves the icon.
     *
     * @param x x coordinate on the screen in pixels
     * @param y y coordinate on the screen in pixels
     */
    @Override
    public void onMove(float x, float y)
    {
        Log.i(TAG, "onMove(" + x + "," + y + ")");
    }

    /**
     * This method is called when the user single click the icon.
     * This method open the "com.fw2" application.
     *
     * @param icon the view holding the icon. Get context from this view
     * @param x current icon position
     * @param y current icon position
     */
    @Override
    public void onIconClick(View icon, float x, float y)
    {
        Log.i(TAG, "onIconClick");
        Toast.makeText(FloatIconService.this, "GO GO GO!", Toast.LENGTH_SHORT).show();

        PackageManager packageManager = FloatIconService.this.getPackageManager();
        Intent launchIntent = packageManager.getLaunchIntentForPackage("magnet.com.magnet");
        FloatIconService.this.startActivity(launchIntent);

        FloatIconService.this.destroy();
    }

    /**
     * This method is called when the user remove the icon.
     * This method destroy the icon.
     */
    @Override
    public void onFlingAway()
    {
        Log.i(TAG, "onFlingAway");
        if (magnet != null)
        {
            magnet.destroy();
            magnet = null;
        }
    }

    /**
     * This method is called after the {@code onFlingAway}.
     * This method finish the service.
     */
    @Override
    public void onIconDestroyed()
    {
        Log.i(TAG, "onIconDestroyed()");
        this.stopSelf();
    }
}

