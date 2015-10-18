package norakomi.sovietposterart.Adapters;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.cache.plus.ImageLoader;
import com.android.volley.error.VolleyError;

import java.util.ArrayList;

import norakomi.sovietposterart.R;
import norakomi.sovietposterart.helpers.App;
import norakomi.sovietposterart.networking.VolleySingleton;
import norakomi.sovietposterart.pojo.Poster;

/**
 * Created by MEDION on 15-10-2015.
 */
public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.PosterViewHolder> {


    private ArrayList<Poster> mPosters = new ArrayList<>();
    private VolleySingleton mVolley;
    private ImageLoader mImageLoader;
    private RequestQueue mRequestQue;
    private final String URL_SOVIET_ART = "http://sovietart.me";

    public PosterAdapter(ArrayList<Poster> listPosters) {
        if (listPosters != null)
            mPosters.addAll(listPosters);

        // JSON download request: see Lecture 36 Slidenerd
        mVolley = VolleySingleton.getInstance();
        mRequestQue = mVolley.getRequestQueue();
        mImageLoader = mVolley.getImageLoader();
        refreshPosters(listPosters);

    }

    public void refreshPosters(ArrayList<Poster> posters) {
        App.log("passed posters = " + posters.size());
        if (mPosters == null) {
            mPosters = new ArrayList<>();
        } else {
            mPosters.clear();
        }

        mPosters.addAll(posters);
        App.log("in refresh with size posters " + mPosters.size() + "  passed postersize = " + posters.size());

//        notifyItemRangeChanged(0, mPosters.size());
        notifyDataSetChanged();
    }

    @Override
    public PosterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyler_item, null);

        PosterViewHolder viewHolder = new PosterViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final PosterViewHolder holder, int position) {
        String imageURL = URL_SOVIET_ART + mPosters.get(position).getFilepath();
        Poster p = mPosters.get(position);
        App.log("in onBindViewHolder for poster: " + p.getTitle() + " /w url: " + imageURL);
        mImageLoader.get(imageURL, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                Drawable image = response.getBitmap();
                if (image == null) {
                    App.log("IMAGE IS NULL!!!!!");
                } else {
                    App.log("IMAGE IS NOT NULLLLLLL!!!!!");

                    App.log("trying to set image on poster" + "   holder width=" + holder.poster.getWidth()
                            + "  padding left on holder  = " + holder.poster.getPaddingLeft());

                    BitmapDrawable b = response.getBitmap();
                    App.log("intrinsic width" + b.getIntrinsicWidth());

                    // calculate new height for holder
                    int imageWidth = response.getBitmap().getIntrinsicWidth();
                    int imageHeight = response.getBitmap().getIntrinsicHeight();
                    int holderWidth = holder.poster.getWidth();
                    float factor = (float) holderWidth / (float) imageWidth;
                    float newHolderHeight = factor * (float) imageHeight;

                    holder.poster.getLayoutParams().height = (int) newHolderHeight;
                    holder.poster.requestLayout();
                }

//                App.log("get width ="+image.getBounds().width());
//                holder.poster.getLayoutParams().height = 300;
//                holder.poster.requestLayout();
                holder.poster.setImageDrawable(image);
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                App.toast("ERROR DOWNLOADING IMAGE");
            }
        });

    }


    @Override
    public int getItemCount() {
        App.log("poster array getCount" + mPosters.size());
        return mPosters.size();
    }

    static class PosterViewHolder extends RecyclerView.ViewHolder {
        ImageView poster;


        public PosterViewHolder(View itemView) {
            super(itemView);
            poster = (ImageView) itemView.findViewById(R.id.poster_imageview);
        }
    }
}
