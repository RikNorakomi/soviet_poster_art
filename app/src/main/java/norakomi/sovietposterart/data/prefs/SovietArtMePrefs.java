package norakomi.sovietposterart.data.prefs;

import android.app.Activity;
import android.content.Context;

/**
 * Created by MEDION on 16-11-2015.
 */
public class SovietArtMePrefs extends SharedPrefsSaver{

    private static volatile SovietArtMePrefs singleton;

    public static SovietArtMePrefs get(Context context) {
        if (singleton == null) {
            synchronized (SovietArtMePrefs.class) {
                singleton = new SovietArtMePrefs(context);
            }
        }
        return singleton;
    }

    private SovietArtMePrefs(Context context) {
        prefs = context.getApplicationContext().
                getSharedPreferences(SOVIET_ART_ME_PREFS, Activity.MODE_PRIVATE);
        editor = prefs.edit();
    }

    // when a category's state is not stored in Shared Preferences we set it's default value to true
    public boolean isCategoryChecked(String category) {
        return prefs.getBoolean(category, true);
    }

    public void setCategoryChecked(String category, boolean checked) {
        savePrefs(category, checked, BOOLEAN);
    }

}
