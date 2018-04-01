package com.edricchan.studybuddy.interfaces;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import static com.edricchan.studybuddy.SharedHelper.ACTION_NOTIFICATIONS_SETTINGS;
import static com.edricchan.studybuddy.SharedHelper.ACTION_SETTINGS_ICON;

public class Notification {
	private String mUsername;
	private String mMessage;
	private String mBody;
	private NotificationData mNotificationData;

	public Notification() {

	}

	public Notification(String username, String message, String body, NotificationData notificationData) {
		this.mUsername = username;
		this.mMessage = message;
		this.mBody = body;
		this.mNotificationData = notificationData;
	}
	public Notification(String username, String message, String body, String notificationChannelId, List<NotificationAction> notificationActions) {
		this.mUsername = username;
		this.mMessage = message;
		this.mBody = body;
		this.mNotificationData = new NotificationData(notificationChannelId, notificationActions);
	}

	public Notification(Context context, int username, int message, int body, int notificationChannelId, List<NotificationAction> notificationActions) {
		this.mUsername = context.getString(username);
		this.mMessage = context.getString(message);
		this.mBody = context.getString(body);
		this.mNotificationData = new NotificationData(context.getString(notificationChannelId), notificationActions);
	}

	public Notification(String username, String message) {
		this.mUsername = username;
		this.mMessage = message;
		this.mBody = "";
		// Assume default is "uncategorised"
		List<NotificationAction> notificationActions = new ArrayList<NotificationAction>();
		notificationActions.add(new NotificationAction(ACTION_SETTINGS_ICON, "Notification settings", ACTION_NOTIFICATIONS_SETTINGS));
		this.mNotificationData = new NotificationData("uncategorised", notificationActions);
	}

	public Notification(String username, String message, String body) {
		this.mUsername = username;
		this.mMessage = message;
		this.mBody = body;
		List<NotificationAction> notificationActions = new ArrayList<NotificationAction>();
		notificationActions.add(new NotificationAction(ACTION_SETTINGS_ICON, "Notification settings", ACTION_NOTIFICATIONS_SETTINGS));
		this.mNotificationData = new NotificationData("uncategorised", notificationActions);
	}

	public String getUsername() {
		return this.mUsername;
	}

	public String getMessage() {
		return this.mMessage;
	}

	public String getBody() {
		return this.mBody;
	}

	public NotificationData getData() {
		return this.mNotificationData;
	}
}
