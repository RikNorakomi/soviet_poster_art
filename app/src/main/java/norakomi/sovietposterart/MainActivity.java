package norakomi.sovietposterart;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import norakomi.sovietposterart.Adapters.PosterAdapter;
import norakomi.sovietposterart.helpers.App;
import norakomi.sovietposterart.networking.VolleySingleton;
import norakomi.sovietposterart.pojo.Poster;

import static norakomi.sovietposterart.helpers.Keys.PosterKeys.AUTHOR;
import static norakomi.sovietposterart.helpers.Keys.PosterKeys.CATEGORY;
import static norakomi.sovietposterart.helpers.Keys.PosterKeys.FILENAME;
import static norakomi.sovietposterart.helpers.Keys.PosterKeys.FILEPATH;
import static norakomi.sovietposterart.helpers.Keys.PosterKeys.POSTERS;
import static norakomi.sovietposterart.helpers.Keys.PosterKeys.TITLE;
import static norakomi.sovietposterart.helpers.Keys.PosterKeys.YEAR;

public class MainActivity extends AppCompatActivity {


    private RequestQueue mRequestQue;
    private ArrayList<Poster> listPosters = new ArrayList<>();
    private CardView mCardView;
    private ImageView mPosterView;
    private VolleySingleton mVolley;
    private RecyclerView mRecyclerView;
    private PosterAdapter mPosterAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_posters);

        mRecyclerView = (RecyclerView) findViewById(R.id.poster_overview_recycler);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        mPosterAdapter = new PosterAdapter(listPosters);
        mRecyclerView.setAdapter(mPosterAdapter);

        int width = getWindowManager().getDefaultDisplay().getWidth();
        App.log("screenwidth = " + width);
        App.log("screenheight = " + getWindowManager().getDefaultDisplay().getHeight());


        mPosterView = (ImageView) findViewById(R.id.poster_imageview);
        // JSON download request: see Lecture 36 Slidenerd
        mVolley = VolleySingleton.getInstance();
        mRequestQue = mVolley.getRequestQueue();

        doJSONRequest(App.URL);

        // JSON PARSING: see lecture 37 Slidenerd

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
                        App.log("number of posters after json request" + posters.size());
                        mPosterAdapter.refreshPosters(posters);


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

                Poster poster = new Poster();
                poster.setAuthor(author);
                poster.setCategory(category);
                poster.setFilename(filename);
                poster.setFilepath(filepath);
                poster.setTitle(title);
                poster.setYear(year);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
