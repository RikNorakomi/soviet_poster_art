package norakomi.sovietposterart.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Created by MEDION on 16-11-2015.
 */
public class SovietArtMePrefs {

    private static final String SOVIET_ART_ME_PREFS = "SOVIET_ART_ME_PREFS";
    private static final String CATEGORY_PREFS = "CATEGORY_PREFS";

    private static volatile SovietArtMePrefs singleton;
    private SharedPreferences prefs;
    private Set<String> categories;

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
                getSharedPreferences(SOVIET_ART_ME_PREFS, Context.MODE_PRIVATE);
        categories = prefs.getStringSet(CATEGORY_PREFS, null);
    }

    public void setCategoryPrefs(Set<String> categories){
        this.categories = categories;
        prefs.edit().putStringSet(CATEGORY_PREFS, categories).apply();
    }

    public boolean isCategorySet() {return categories != null;
    }
}
