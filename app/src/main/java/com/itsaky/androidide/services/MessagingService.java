/************************************************************************************
 * This file is part of AndroidIDE.
 *
 * Copyright (C) 2021 Akash Yadav
 *
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 *
**************************************************************************************/


package com.itsaky.androidide.services;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.core.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.BuildConfig;
import com.itsaky.androidide.SplashActivity;
import java.util.Map;
import android.provider.Settings;
import androidx.core.app.NotificationManagerCompat;
import android.annotation.SuppressLint;

public class MessagingService extends FirebaseMessagingService {
    
	public static final String TOPIC_UPDATE= "app_update";
	public static final String TOPIC_DEV_MSGS = "developer_notifications";
	
	private final String KEY_TITLE = "title";
	private final String KEY_MESSAGE = "message";
	private final String KEY_VERSION = "version";
	private final String KEY_CLICK_ACTION = "click_action";
	
	private final String ACTION_OPEN_APP = "open_app";
	private final String ACTION_OPEN_URL = "open_url:";
	
    @Override
	public void onMessageReceived(RemoteMessage message) {
		if(message != null && message.getData() != null)
			processNotification(message.getFrom(), message.getData());
	}

	@Override
	public void onNewToken(String token) {
		super.onNewToken(token);
	}
	
	private void processNotification(String topic, Map<String, String> data) {
		if(topic != null && topic.contains("/")) {
			topic = topic.substring(topic.lastIndexOf("/") + 1);
			if(topic.equals(TOPIC_UPDATE) && checkHasUpdateData(data)) {
				final String title = data.get(KEY_TITLE);
				final String msg = data.get(KEY_MESSAGE);
				final String version = data.get(KEY_VERSION);
				final String action = data.get(KEY_CLICK_ACTION);
				final int l = stringAsInt(version);
				if(l > BuildConfig.VERSION_CODE) {
					showNotificationWithAction(action, title, msg);
				}
			} else if(topic.equals(TOPIC_DEV_MSGS) && checkHasTitleAndMessage(data)) {
				final String title = data.get(KEY_TITLE);
				final String msg = data.get(KEY_MESSAGE);
				final String action = data.get(KEY_CLICK_ACTION);
				showNotificationWithAction(action, title, msg);
			}
		}
	}
    
	private void showNotificationWithAction(String action, String title, String msg) {
		Intent i = null;
		int flag = PendingIntent.FLAG_IMMUTABLE;
		if (action.equals(ACTION_OPEN_APP)) {
			i = StudioApp.getInstance().getPackageManager().getLaunchIntentForPackage(StudioApp.getInstance().getPackageName());
		} else if (action.startsWith(ACTION_OPEN_URL)) {
			i = createOpenUrlIntent(action.substring(ACTION_OPEN_URL.length()));
			flag |= Intent.FLAG_ACTIVITY_NEW_TASK;
		}

		if (i != null) {
			PendingIntent intent = PendingIntent.getActivity(this, 0, i, flag);
			showNotification(title, msg, intent, StudioApp.NOTIFICATION_ID_UPDATE);
		}
	}
	
	private void showNotification(String title, String msg, PendingIntent i, String channelId) {
		NotificationCompat.Builder b = new NotificationCompat.Builder(this, channelId);
		b.setContentTitle(title)
			.setContentText(msg)
			.setAutoCancel(true)
			.setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
			.setContentIntent(i);
			
		NotificationManagerCompat.from(StudioApp.getInstance()).notify(0, b.build());
	}
	
	private Intent createOpenAppIntent() {
		Intent i = new Intent(this, SplashActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
		return i;
	}
	
	private Intent createOpenUrlIntent(String url) {
		return new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(url));
	}
	
	private int stringAsInt(String str) {
		try {
			return Integer.valueOf(str);
		} catch (Throwable th) {
			return -1;
		}
	}
	
	private boolean checkHasUpdateData(Map<String, String> data) {
		return checkHasTitleAndMessage(data)
		&& data.containsKey(KEY_VERSION)
		&& data.containsKey(KEY_CLICK_ACTION);
	}
	
	private boolean checkHasTitleAndMessage(Map<String, String> data) {
		return data.containsKey(KEY_TITLE)
			&& data.containsKey(KEY_MESSAGE);
	}
}
