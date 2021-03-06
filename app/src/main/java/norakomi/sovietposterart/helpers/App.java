package norakomi.sovietposterart.helpers;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by MEDION on 11-10-2015.
 */
public class App extends Application {
    public static final String URL_JSON_SOVIET_ART_ME = "http://www.norakomi.com/assets/json/soviet_art.json";
    public static final String URL_SOVIET_ART = "http://sovietart.me";
    private static App instance;
    private static boolean DEBUG = true;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static App getInstance() {
        return instance;
    }

    public static Context getAppContext() {
        return instance.getApplicationContext();
    }

    public static void toast(String string) {
        Toast.makeText(getAppContext(), string, Toast.LENGTH_SHORT).show();
    }

    public static void toastLong(String string) {
        Toast.makeText(getAppContext(), string, Toast.LENGTH_LONG).show();
    }

    public static void log(String string) {
        log("SovietArt", string);
    }

    public static void log(String tag, String msg) {
        // turn off debug to prevent writing log statements
        if (DEBUG)
            Log.e(tag, msg);

    }

}
