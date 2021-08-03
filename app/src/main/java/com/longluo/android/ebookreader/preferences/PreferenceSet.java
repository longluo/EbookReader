package com.longluo.android.ebookreader.preferences;

import java.util.LinkedList;

import android.preference.Preference;

abstract class PreferenceSet<T> implements Runnable {
	private final LinkedList<Preference> myPreferences = new LinkedList<Preference>();

	final void add(Preference preference) {
		myPreferences.add(preference);
	}

	public final void run() {
		final T state = detectState();
		for (Preference preference : myPreferences) {
			update(preference, state);
		}
	}

	protected abstract T detectState();
	protected abstract void update(Preference preference, T state);

	static abstract class Enabler extends PreferenceSet<Boolean> {
		protected void update(Preference preference, Boolean state) {
			preference.setEnabled(state);
		}
	}

	static class Reloader extends PreferenceSet<Void> {
		protected Void detectState() {
			return null;
		}

		protected void update(Preference preference, Void state) {
			((ReloadablePreference)preference).reload();
		}
	}
}
