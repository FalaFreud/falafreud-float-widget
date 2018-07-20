package com.falafreud.floatwidget.message.service;

import android.content.Intent;
import android.util.Log;

import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationReceivedResult;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Haroldo Shigueaki Teruya on 20/07/18.
 */
public class MessageNotificationExtenderService extends NotificationExtenderService
{
    // GLOBAL VARIABLES ============================================================================
    // =============================================================================================

    public static final String TAG = "FloatWidget";

    public static final class Constant {
        public static final String DATA = "data";
        public static final String TYPE = "type";
        public static final String NEW_MESSAGE = "new_message";
        public static final String ACTION = "com.falafreud.floatwidget.unread_message_received";
    }

    // METHODS =====================================================================================
    // =============================================================================================

    /**
     * This method is called when the device receive an push notification using the OneSignal SDK.
     *
     * @param receivedResult
     * @return
     */
    @Override
    protected boolean onNotificationProcessing(OSNotificationReceivedResult receivedResult) {

        JSONObject additionalData = receivedResult.payload.additionalData;

        Log.d(TAG, "MessageNotificationExtenderService onNotificationProcessing content: " + additionalData.toString());
        try {
            String response = additionalData.getString(Constant.TYPE);
            Boolean isUnreadMessage = response.equals(Constant.NEW_MESSAGE);
            Log.d(TAG, "MessageNotificationExtenderService onNotificationProcessing is new message: " + isUnreadMessage);
            if (isUnreadMessage) {
                this.unreadMessageReceived();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "MessageNotificationExtenderService onNotificationProcessing error: " + e.toString());
        }

        // Return true to stop the notification from displaying.
        return false;
    }

    /**
     * This method is responsible to start an ACTION.
     * The receiver is the FloatWidgetManagerModule instance.
     */
    private void unreadMessageReceived() {

        Intent intent = new Intent();
        intent.setAction(Constant.ACTION);
        Log.d(TAG, "MessageNotificationExtenderService unreadMessageReceived");
        MessageNotificationExtenderService.this.sendBroadcast(intent);
    }
}
