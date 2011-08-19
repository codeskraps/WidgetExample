package com.codeskraps;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class ButtonWidget extends AppWidgetProvider {
	private static final String TAG = ButtonWidget.class.getSimpleName();

	private static String ACTION_WIDGET_CONFIGURE = "ConfigureWidget";
	private static String ACTION_WIDGET_RECEIVER = "ActionReceiverWidget";

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		Log.d(TAG, "onUpdate started");
		
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.button);
		Intent configIntent = new Intent(context, ClickedActivity.class);

		configIntent.setAction(ACTION_WIDGET_CONFIGURE);
		Intent active = new Intent(context, ButtonWidget.class);
		active.setAction(ACTION_WIDGET_RECEIVER);
		active.putExtra("msg", "Message for Button 1");

		PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);
		PendingIntent configPendingIntent = PendingIntent.getActivity(context, 0, configIntent, 0);
		remoteViews.setOnClickPendingIntent(R.id.button_one, actionPendingIntent);
		remoteViews.setOnClickPendingIntent(R.id.button_two, configPendingIntent);
		appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "onReceive started");
		
		// v1.5 fix that doesn't call onDelete Action
		final String action = intent.getAction();
		if (AppWidgetManager.ACTION_APPWIDGET_DELETED.equals(action)) {
			final int appWidgetId = intent.getExtras().getInt(
					AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
			if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
				this.onDeleted(context, new int[] { appWidgetId });
			}
		} else {
			// check, if our Action was called
			if (intent.getAction().equals(ACTION_WIDGET_RECEIVER)) {
				String msg = "null";
				try {
					msg = intent.getStringExtra("msg");
				} catch (NullPointerException e) {
					Log.e("Error", "msg = null");
				}
				Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
				PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, 0);
				NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
				Notification noty = new Notification(R.drawable.icon, "Button 1 clicked", System.currentTimeMillis());
				noty.setLatestEventInfo(context, "Notice", msg, contentIntent);
				notificationManager.notify(1, noty);
			}
			super.onReceive(context, intent);
		}
	}

}
