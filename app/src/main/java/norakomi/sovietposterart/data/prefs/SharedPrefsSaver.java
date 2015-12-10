package norakomi.sovietposterart.data.prefs;

import android.content.SharedPreferences;

import java.util.Map;

import norakomi.sovietposterart.helpers.App;

/**
 * Created by MEDION on 10-12-2015.
 */
public class SharedPrefsSaver {

    public final String TAG = getClass().getSimpleName();
    public static final String SOVIET_ART_ME_PREFS = "sovietArtMePrefs";

    public static final int STRING = 1;
    public static final int BOOLEAN = 2;
    public static final int INTEGER = 3;

    public SharedPreferences prefs;
    public SharedPreferences.Editor editor;

    /* GENERIC PREFS SAVER */
    public void savePrefs(String key, boolean value, int type) {
        savePrefs(key, String.valueOf(value), type);
    }

    public void savePrefs(String key, int value, int type) {
        savePrefs(key, Integer.toString(value), type);
    }

    public void savePrefs(String key, String value, int type) {
        if (editor == null) {
            App.log(TAG, "error saving to SharedPreferences!!");
            return;
        }
        switch (type) {
            case BOOLEAN:
                boolean boolValue = false;
                if (value.equals("true")) {
                    boolValue = true;
                }
                editor.putBoolean(key, boolValue);
                break;
            case STRING:
                editor.putString(key, value);
                break;
            case INTEGER:
                editor.putInt(key, Integer.parseInt(value));
                break;
            default:
                App.log(TAG, "Error saving prefs; wrong type!");
                break;
        }

        editor.apply();
    }

    public void logAllEntries() {
        Map<String, ?> allEntries = prefs.getAll();
        App.log(TAG, "in logAllEntries*** met mapsize=" + allEntries.size());
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            App.log("Prefs map values", entry.getKey() + ": " + entry.getValue().toString());
        }
    }
}
