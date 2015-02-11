package com.shiqi.reflect;

import java.lang.reflect.Field;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public interface TypeAction {
	void putToSharePreference(Editor editor, String key, Object object);

	void getFromPreference(SharedPreferences sharedPreferences, String key, Object dst, Field field);

}
