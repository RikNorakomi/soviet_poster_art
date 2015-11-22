package norakomi.sovietposterart.data;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import norakomi.sovietposterart.Adapters.GridItem;
import norakomi.sovietposterart.data.pojo.Poster;
import norakomi.sovietposterart.helpers.App;
import norakomi.sovietposterart.networking.VolleySingleton;

import static norakomi.sovietposterart.helpers.Keys.PosterKeys.AUTHOR;
import static norakomi.sovietposterart.helpers.Keys.PosterKeys.CATEGORY;
import static norakomi.sovietposterart.helpers.Keys.PosterKeys.FILENAME;
import static norakomi.sovietposterart.helpers.Keys.PosterKeys.FILEPATH;
import static norakomi.sovietposterart.helpers.Keys.PosterKeys.POSTERS;
import static norakomi.sovietposterart.helpers.Keys.PosterKeys.TITLE;
import static norakomi.sovietposterart.helpers.Keys.PosterKeys.YEAR;

/**
 * Created by MEDION on 16-11-2015.
 */
public class DataManager extends BaseDataManager {

    private AtomicInteger loadingCount;
    private RequestQueue requestQue;
    private ArrayList<Poster> posters = new ArrayList<>();

    public DataManager(Context context) {
        super(context);
        loadingCount = new AtomicInteger(0);
        requestQue = VolleySingleton.getInstance().getRequestQueue();

        loadSovietPosterArtMeData();
    }


    @Override
    public void onDataLoaded(List<? extends GridItem> data) {

    }


    public boolean isDataLoading() {
        // some logic to go here whether data is loading
        return false;
    }

    private void loadSovietPosterArtMeData() {
        String url = App.URL_JSON_SOVIET_ART_ME;
        doJSONRequest(url);

    }

    public void doJSONRequest(String url) {
        App.log("doJsonRequest");
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // parse JSON response on pass returned poster object to recycler adapter
                        ArrayList<Poster> posters = parseJSONResponse(response);
                        App.log("in onResponse with posters.size="+posters.size());
                        onDataLoaded(posters);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                App.log("ERROR!");
            }
        }
        );
        requestQue.add(request);
    }

    public ArrayList<Poster> parseJSONResponse(JSONObject response) {
        if (response == null || response.length() == 0) {
            return null;
        }
        try {
            StringBuilder data = new StringBuilder();
            JSONArray posterArray = response.getJSONArray(POSTERS);
            for (int i = 0; i < posterArray.length(); i++) {
                JSONObject posterData = posterArray.getJSONObject(i);
                String title = null;
                String author = null;
                String filepath = null;
                String filename = null;
                String category = null;
                String year = null;

                if (posterData.has(TITLE)) {
                    title = posterData.getString(TITLE);
                } else
                    title = "no title";

                if (posterData.has(AUTHOR)) {
                    author = posterData.getString(AUTHOR);
                } else
                    author = "no author";
                if (posterData.has(FILEPATH)) {
                    filepath = posterData.getString(FILEPATH);
                } else
                    filepath = "no path";
                if (posterData.has(FILENAME)) {
                    filename = posterData.getString(FILENAME);
                } else
                    filename = "no name";
                if (posterData.has(CATEGORY)) {
                    category = posterData.getString(CATEGORY);
                } else
                    category = "no category";
                if (posterData.has(YEAR)) {
                    category = posterData.getString(CATEGORY);
                } else
                    category = "no year";

                Poster poster = new Poster(
                        title,
                        author,
                        filepath,
                        filename,
                        category,
                        year);
                posters.add(poster);

                // gebleven by lect 38 slidenerd

            }

            return posters;
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return null;
    }
}
