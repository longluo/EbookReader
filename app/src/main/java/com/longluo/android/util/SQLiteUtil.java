package com.longluo.android.util;

import java.util.Date;

import android.database.sqlite.SQLiteStatement;
import android.database.Cursor;

public abstract class SQLiteUtil {
	public static void bindString(SQLiteStatement statement, int index, String value) {
		if (value != null) {
			statement.bindString(index, value);
		} else {
			statement.bindNull(index);
		}
	}

	public static void bindLong(SQLiteStatement statement, int index, Long value) {
		if (value != null) {
			statement.bindLong(index, value);
		} else {
			statement.bindNull(index);
		}
	}

	public static void bindDate(SQLiteStatement statement, int index, Date value) {
		if (value != null) {
			statement.bindLong(index, value.getTime());
		} else {
			statement.bindNull(index);
		}
	}

	public static Date getDate(Cursor cursor, int index) {
		if (cursor.isNull(index)) {
			return null;
		}
		return new Date(cursor.getLong(index));
	}
}
