package norakomi.sovietposterart;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import norakomi.sovietposterart.data.pojo.Poster;

/**
 * Activity gets run when user taps on Image from NavigationViewActivity's RecyclerView
 */
public class PosterViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster_view);
        ImageView posterView = (ImageView) findViewById(R.id.poster_view_image);

        Poster p = (Poster) getIntent().getSerializableExtra("poster");
        if (p!=null){
            String URL_SOVIET_ART = "http://sovietart.me";
            String imageURL = URL_SOVIET_ART + p.getFilepath();

            Glide.with(this)
                    .load(imageURL)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(posterView);
        }

    }
}
