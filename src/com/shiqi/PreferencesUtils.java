package com.shiqi;

import java.lang.reflect.Field;
import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.shiqi.reflect.ReflectHelper;
import com.shiqi.reflect.TypeActionManager;


public class PreferencesUtils {

	public static void saveObject(Context context, Object object) {
		HashMap<String, Field> fields = ReflectHelper.getFields(object);
		SharedPreferences sharedPreferences = context.getSharedPreferences(
		        object.getClass().getSimpleName(), Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		for (String key : fields.keySet()) {
			Field field = fields.get(key);
			try {
				TypeActionManager.getInstance().getTypeAction(field.getType())
				        .putToSharePreference(editor, key, field.get(object));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
		editor.commit();
	}

	public static <T> T initFromSharePreference(Context context, Class<T> cls) {

		T obj = ReflectHelper.newInstance(cls);
		initFromSharePreference(context, obj);
		return obj;
	}

	public static void initFromSharePreference(Context context, Object obj) {
		HashMap<String, Field> fields = ReflectHelper.getFields(obj);
		SharedPreferences sharedPreferences = context.getSharedPreferences(
		        obj.getClass().getSimpleName(), Context.MODE_PRIVATE);
		for (String key : fields.keySet()) {
			Field field = fields.get(key);
			TypeActionManager.getInstance().getTypeAction(field.getType()).getFromPreference(sharedPreferences, key, obj, field);
		}

	}
}
