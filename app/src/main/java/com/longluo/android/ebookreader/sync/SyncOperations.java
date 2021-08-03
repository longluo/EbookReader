package com.longluo.android.ebookreader.sync;

import android.content.Context;
import android.content.Intent;

import com.longluo.ebookreader.fbreader.options.SyncOptions;
import com.longluo.android.ebookreader.api.FBReaderIntents;

public abstract class SyncOperations {
	public static void enableSync(Context context, SyncOptions options) {
		final String action = options.Enabled.getValue()
			? FBReaderIntents.Action.SYNC_START : FBReaderIntents.Action.SYNC_STOP;
		context.startService(new Intent(context, SyncService.class).setAction(action));
	}

	public static void quickSync(Context context, SyncOptions options) {
		if (options.Enabled.getValue()) {
			context.startService(
				new Intent(context, SyncService.class)
					.setAction(FBReaderIntents.Action.SYNC_QUICK_SYNC)
			);
		}
	}
}
