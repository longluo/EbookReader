package com.longluo.android.ebookreader.network.auth;

import java.net.URI;
import java.util.Map;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import androidx.core.app.NotificationCompat;

import com.longluo.zlibrary.core.resources.ZLResource;

import com.longluo.ebookreader.R;

public class ServiceNetworkContext extends AndroidNetworkContext {
	private final Service myService;

	public ServiceNetworkContext(Service service) {
		myService = service;
	}

	public Context getContext() {
		return myService;
	}

	@Override
	protected Map<String,String> authenticateWeb(URI uri, String realm, String authUrl, String completeUrl, String verificationUrl) {
		final NotificationManager notificationManager =
			(NotificationManager)myService.getSystemService(Context.NOTIFICATION_SERVICE);
		final Intent intent = new Intent(myService, WebAuthorisationScreen.class);
		intent.setData(Uri.parse(authUrl));
		intent.putExtra(WebAuthorisationScreen.COMPLETE_URL_KEY, completeUrl);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_FROM_BACKGROUND);
		final PendingIntent pendingIntent = PendingIntent.getActivity(myService, 0, intent, 0);
		final String text =
			ZLResource.resource("dialog")
				.getResource("backgroundAuthentication")
				.getResource("message")
				.getValue();
		final Notification notification = new NotificationCompat.Builder(myService)
			.setSmallIcon(R.drawable.fbreader)
			.setTicker(text)
			.setContentTitle(realm)
			.setContentText(text)
			.setContentIntent(pendingIntent)
			.setAutoCancel(true)
			.build();
		notificationManager.notify(0, notification);
		return errorMap("Notification sent");
	}
}
