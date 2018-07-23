package com.falafreud.floatwidget;

public final class Constant {

    // used to read the pushnotification  payload
    public static final String DATA = "data";
    public static final String TYPE = "type";
    public static final String NEW_MESSAGE = "new_message";

    // used for BroadcastReceiver
    public static final String ACTION = "com.falafreud.floatwidget.unread_message_received";

    // used to send data to the FloatIconService
    public static final String ON_UNREAD_MESSAGE_RECEIVED = "on_unread_message_received";
}