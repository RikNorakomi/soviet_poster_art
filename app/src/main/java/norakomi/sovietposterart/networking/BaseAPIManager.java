package norakomi.sovietposterart.networking;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import norakomi.sovietposterart.Adapters.GridItem;
import norakomi.sovietposterart.data.pojo.Poster;

import static norakomi.sovietposterart.helpers.Keys.PosterKeys.AUTHOR;
import static norakomi.sovietposterart.helpers.Keys.PosterKeys.CATEGORY;
import static norakomi.sovietposterart.helpers.Keys.PosterKeys.FILENAME;
import static norakomi.sovietposterart.helpers.Keys.PosterKeys.FILEPATH;
import static norakomi.sovietposterart.helpers.Keys.PosterKeys.POSTERS;
import static norakomi.sovietposterart.helpers.Keys.PosterKeys.TITLE;
import static norakomi.sovietposterart.helpers.Keys.PosterKeys.YEAR;

/**
 * Created by MEDION on 22-11-2015.
 */
public abstract class BaseAPIManager {


    public BaseAPIManager(Context context) {
        // setup the API access objects


        // see later Plaid/Retrofit code on how to create API's
    }

    public abstract void onDataLoaded(List<? extends GridItem> data);

    // todo abstract even further to parse return GridItem
    public ArrayList<Poster> parseJSONResponse(JSONObject response) {
        if (response == null || response.length() == 0) {
            return null;
        }

        ArrayList<Poster> listPosters = new ArrayList<>();
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
                listPosters.add(poster);

                // gebleven by lect 38 slidenerd

            }
//            App.toastLong(listPosters.toString());
            return listPosters;
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return null;
    }
}
