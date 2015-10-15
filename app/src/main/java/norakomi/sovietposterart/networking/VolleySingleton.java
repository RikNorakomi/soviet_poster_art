package norakomi.sovietposterart.networking;

import android.graphics.drawable.BitmapDrawable;
import android.support.v4.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.cache.plus.ImageCache;
import com.android.volley.cache.plus.ImageLoader;
import com.android.volley.toolbox.Volley;

import norakomi.sovietposterart.helpers.App;

/**
 * Created by MEDION on 8-10-2015.
 * Info on VolleyPlus library
 * https://github.com/DWorkS/VolleyPlus
 */

public class VolleySingleton {
    private static VolleySingleton mInstance = null;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private VolleySingleton() {
        // the context you need to pass in is the ApplicationContext
        mRequestQueue = Volley.newRequestQueue(App.getAppContext());
        mImageLoader= new ImageLoader(mRequestQueue, new ImageCache() {
            /** For more info n caching using totalMemory() & freeMemory() check:
             * http://stackoverflow.com/questions/3571203/what-is-the-exact-meaning-of-runtime-getruntime-totalmemory-and-freememory
             */
            private LruCache<String, BitmapDrawable> cache =
                    new LruCache<>((int) (Runtime.getRuntime().maxMemory() / 1024 / 8));

            @Override
            public BitmapDrawable getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, BitmapDrawable bitmap) {
                cache.put(url, bitmap);
            }

            @Override
            public void invalidateBitmap(String url) {

            }

            @Override
            public void clear() {

            }
        });
    }

    public static VolleySingleton getInstance() {
        if (mInstance == null) {
            mInstance = new VolleySingleton();
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
}
