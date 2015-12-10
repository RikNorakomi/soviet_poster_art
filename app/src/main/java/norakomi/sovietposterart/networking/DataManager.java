package norakomi.sovietposterart.networking;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;

import norakomi.sovietposterart.data.pojo.Poster;
import norakomi.sovietposterart.helpers.App;
import norakomi.sovietposterart.helpers.DataLoadingSubject;

/**
 * Created by MEDION on 22-11-2015.
 */
public abstract class DataManager extends BaseAPIManager implements DataLoadingSubject {

    private AtomicInteger loadingCount;
    private RequestQueue mRequestQue;
    private ArrayList<String> mCategories = new ArrayList<>();

    public DataManager(Context context) {
        super(context);
        loadingCount = new AtomicInteger(0);
        mRequestQue = VolleySingleton.getInstance().getRequestQueue();
        loadSovietArtMeData();
    }

    private void loadSovietArtMeData() {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                App.URL_JSON_SOVIET_ART_ME,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Set<String> categorySet = new TreeSet<>();
                        ArrayList<Poster> posters = parseJSONResponse(response);
                        for (Poster p: posters) {
                            categorySet.add(p.getCategory());
                        }

                        // before sending poster objects through callback set the category list!
                        mCategories.clear();
                        mCategories.addAll(categorySet);
                        onDataLoaded(posters);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                App.toast("ERROR!");
            }
        }
        );
        mRequestQue.add(request);

    }

    @Override
    public boolean isDataLoading() {
        return loadingCount.get() > 0;
    }

    public ArrayList<String> getCategories() {
        return mCategories;
    }
}
