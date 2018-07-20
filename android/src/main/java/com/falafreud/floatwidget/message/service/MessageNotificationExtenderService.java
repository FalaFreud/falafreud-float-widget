package com.falafreud.floatwidget.message.service;

import android.util.Log;

import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationReceivedResult;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Haroldo Shigueaki Teruya on 15/04/18.
 */
public class MessageNotificationExtenderService extends NotificationExtenderService
{
    public static final String TAG = "FloatWidget";

    @Override
    public void onCreate() {

        super.onCreate();
        Log.d(TAG, "MessageNotificationExtenderService onCreate");
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        Log.d(TAG, "MessageNotificationExtenderService onDestroy");
    }

    @Override
    protected boolean onNotificationProcessing(OSNotificationReceivedResult receivedResult) {

        JSONObject additionalData = receivedResult.payload.additionalData;

        Log.d(TAG, "MessageNotificationExtenderService onNotificationProcessing content: " + additionalData.toString());
        try {
            String response = additionalData.getString("type");
            Log.d(TAG, "MessageNotificationExtenderService onNotificationProcessing is new message: " + response.equals("new_message"));
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "MessageNotificationExtenderService onNotificationProcessing error: " + e.toString());
        }

        // Return true to stop the notification from displaying.
        return false;
    }
}
