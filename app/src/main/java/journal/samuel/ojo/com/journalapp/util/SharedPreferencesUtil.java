package journal.samuel.ojo.com.journalapp.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;


public class SharedPreferencesUtil {

    private static final String APP_SHARED_PREFS_NAME = "samuel.ojo.sharedpreferences";

    public static void putString(Activity activity, String key, String value) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(APP_SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getString(Activity activity, String key) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(APP_SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    public static void purge(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(APP_SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
    }
}
