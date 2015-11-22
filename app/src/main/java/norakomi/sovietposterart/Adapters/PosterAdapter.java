package norakomi.sovietposterart.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.cache.plus.ImageLoader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import norakomi.sovietposterart.R;
import norakomi.sovietposterart.data.pojo.Poster;
import norakomi.sovietposterart.helpers.App;
import norakomi.sovietposterart.networking.VolleySingleton;

/**
 * Created by MEDION on 15-10-2015.
 */
public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.PosterViewHolder> {


    private ArrayList<Poster> mPosters = new ArrayList<>();
    private final Context mContext;

    public PosterAdapter(ArrayList<Poster> listPosters, Context context) {
        mContext = context;
        if (listPosters != null)
            mPosters.addAll(listPosters);

        // JSON download request: see Lecture 36 Slidenerd
        VolleySingleton volley = VolleySingleton.getInstance();
        RequestQueue requestQue = volley.getRequestQueue();
        ImageLoader imageLoader = volley.getImageLoader();
        refreshData(listPosters);

    }

    public void refreshData(List<? extends GridItem> posters) {
        if (posters != null)
            App.log("posters size = " + posters.size());
        if (posters == null) {
            mPosters = new ArrayList<>();
        } else {
            mPosters.clear();
            mPosters.addAll((Collection<? extends Poster>) posters);
        }

        App.log("in refresh with size posters " + mPosters.size());
        notifyDataSetChanged();
    }

    @Override
    public PosterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
//        ImageView image = (ImageView) viewGroup.findViewById(R.id.poster_imageview);

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyler_item, null);


        return new PosterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PosterViewHolder holder, int position) {
        String URL_SOVIET_ART = "http://sovietart.me";
        String imageURL = URL_SOVIET_ART + mPosters.get(position).getFilepath();
        Poster p = mPosters.get(position);
        App.log("in onBindViewHolder for poster: " + p.getTitle() + " /w url: " + imageURL);

        Glide.with(mContext)
                .load(imageURL)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(holder.poster);
    }


    @Override
    public int getItemCount() {
        App.log("poster array getCount" + mPosters.size());
        return mPosters.size();
    }

    static class PosterViewHolder extends RecyclerView.ViewHolder {
        final ImageView poster;


        public PosterViewHolder(View itemView) {
            super(itemView);
            poster = (ImageView) itemView.findViewById(R.id.poster_imageview);
        }
    }
}
