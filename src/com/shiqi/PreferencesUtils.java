package com.shiqi;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.R.integer;
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
	
	public static void addObject2List(Context context, Object object) {
		Object obj = ReflectHelper.newInstance(object.getClass());
		int i = 0;
		while (true) {
			if (initFromSharePreference(context, obj, obj.getClass().getSimpleName() + i)) {
				i++;
			} else {
				break;
			}
		}
		
		HashMap<String, Field> fields = ReflectHelper.getFields(object);
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				object.getClass().getSimpleName() + i, Context.MODE_PRIVATE);
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

	public static <T> List<T> initFromSharePreferenceList(Context context,
			Class<T> cls) {

		List<T> list = new ArrayList<T>();
		int i = 0;
		while (true) {
			T obj = ReflectHelper.newInstance(cls);
			if (initFromSharePreference(context, obj, obj.getClass().getSimpleName() + i)) {
				list.add(obj);
			} else {
				break;
			}
			i++;
		}
		return list;
	}

	public static <T> T initFromSharePreference(Context context, Class<T> cls) {

		T obj = ReflectHelper.newInstance(cls);
		initFromSharePreference(context, obj);
		return obj;
	}

	public static boolean initFromSharePreference(Context context, Object obj,
			String name) {
		HashMap<String, Field> fields = ReflectHelper.getFields(obj);
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				name, Context.MODE_PRIVATE);

		for (String key : fields.keySet()) {
			if (sharedPreferences.contains(key)) {
				Field field = fields.get(key);
				TypeActionManager.getInstance().getTypeAction(field.getType())
						.getFromPreference(sharedPreferences, key, obj, field);
			} else {
				return false;
			}
		}
		return true;
	}

	public static boolean initFromSharePreference(Context context, Object obj) {
		return initFromSharePreference(context, obj, obj.getClass()
				.getSimpleName());
	}
}
