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

import norakomi.sovietposterart.Adapters.GridItem;
import norakomi.sovietposterart.data.pojo.Poster;
import norakomi.sovietposterart.data.prefs.SovietArtMePrefs;
import norakomi.sovietposterart.helpers.App;

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
public abstract class BaseDataManager {

    private SovietArtMePrefs sovietArtMePrefs;
    private RequestQueue requestQue;
    private ArrayList<Poster> listPosters = new ArrayList<>();

    public BaseDataManager(Context context) {
        // setup the API access objects
        sovietArtMePrefs = SovietArtMePrefs.get(context);
//        createSovietArtMeData();
    }

    public abstract void onDataLoaded(List<? extends GridItem> data);

    private void createSovietArtMeData() {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                App.JSON_SOVIET_ART_ME,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // parse JSON response on pass returned poster object to recycler adapter
                        ArrayList<Poster> posters = parseJSONResponse(response);
                        App.log("number of posters after json request" + posters.size());
                        onDataLoaded(posters);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                App.toast("ERROR!");
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
                    {author = posterData.getString(AUTHOR);}
                } else {
                    author = "no author";
                }
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
                listPosters.add(poster);

            }

            return listPosters;
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return null;
    }
}
