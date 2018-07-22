package com.falafreud.floatwidget.icon;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.falafreud.floatwidget.R;
import com.falafreud.floatwidget.icon.magnet.IconCallback;
import com.falafreud.floatwidget.icon.magnet.Magnet;
import com.falafreud.floatwidget.message.service.MessageNotificationExtenderService;

/**
 * Created by Haroldo Shigueaki Teruya on 18/07/18.
 */
public class FloatIconService extends Service implements IconCallback
{
    // GLOBAL VARIABLES ============================================================================
    // =============================================================================================

    private static final String TAG = "FloatWidget";
    private Magnet magnet;

    // METHODS =====================================================================================
    // =============================================================================================

    @Nullable
    @Override
    public IBinder onBind(@NonNull Intent intent) {

        return new LocalBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "FloatIconService onStartCommand");
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        Log.d(TAG, "FloatIconService onCreate");
        this.startMagnet();
    }

    /**
     * This event can be called by "...stopService(new Intent(...))".
     * Event this event been called, must implement the methods that destroy the UI and the service
     * itself.
     */
    @Override
    public void onDestroy() {

        Log.d(TAG, "FloatIconService onDestroy");
        this.destroy();
        super.onDestroy();
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
                    .setRemoveIconResId(R.drawable.remove_shape)
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
     * This method open the "FalaFreud application" application.
     *
     * @param icon the view holding the icon. Get context from this view
     * @param x current icon position
     * @param y current icon position
     */
    @Override
    public void onIconClick(View icon, float x, float y)
    {
        Log.i(TAG, "FloatIconService onIconClick");
        Toast.makeText(FloatIconService.this, "GO GO GO!", Toast.LENGTH_SHORT).show();

        PackageManager packageManager = FloatIconService.this.getPackageManager();
        Intent launchIntent = packageManager.getLaunchIntentForPackage("com.falafreud.falafreud");
        FloatIconService.this.startActivity(launchIntent);

//        FloatIconService.this.destroy();
    }

    /**
     * This method is called when the user remove the icon.
     * This method destroy the icon.
     */
    @Override
    public void onFlingAway()
    {
        Log.i(TAG, "FloatIconService onFlingAway");
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
        Log.i(TAG, "FloatIconService onIconDestroyed()");
        this.stopSelf();
    }

    // CLASS =======================================================================================
    // =============================================================================================

    public class LocalBinder extends Binder
    {
        public FloatIconService getService()
        {
            return FloatIconService.this;
        }
    }
}

