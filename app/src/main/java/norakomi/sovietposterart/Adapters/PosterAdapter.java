package norakomi.sovietposterart.Adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.ColorMatrixColorFilter;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.cache.plus.ImageLoader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import norakomi.sovietposterart.PosterViewActivity;
import norakomi.sovietposterart.R;
import norakomi.sovietposterart.data.pojo.Poster;
import norakomi.sovietposterart.helpers.App;
import norakomi.sovietposterart.networking.VolleySingleton;
import norakomi.sovietposterart.ui.ObservableColorMatrix;

/**
 * Created by MEDION on 15-10-2015.
 */
public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.PosterViewHolder> {


    private final String TAG = PosterAdapter.class.getSimpleName();
    public ArrayList<Poster> mPosters = new ArrayList<>();
    public ArrayList<Poster> mSelectedPosters = new ArrayList<>();
    private final Context mContext;
    private final Activity mHostActivity;
    private HashMap<String, Boolean> mCheckedCategories;

    public PosterAdapter(ArrayList<Poster> listPosters, Context context, Activity hostActivity) {
        mContext = context;
        this.mHostActivity = hostActivity;

        if (listPosters != null)
            mPosters.addAll(listPosters);

        // JSON download request: see Lecture 36 Slidenerd
        VolleySingleton volley = VolleySingleton.getInstance();
        RequestQueue requestQue = volley.getRequestQueue();
        ImageLoader imageLoader = volley.getImageLoader();
        refreshData(listPosters);

    }

    public void setCategories(HashMap<String, Boolean> checkedCategories) {
        mCheckedCategories = new HashMap<>(checkedCategories);
        if (checkedCategories != null && checkedCategories.size() > 0) {
            App.log("filling selected posters array");
            mSelectedPosters.clear();

            for (int i = 0; i < mPosters.size(); i++) {
                for (Map.Entry<String, Boolean> category : checkedCategories.entrySet()) {
                    String key = category.getKey();
                    boolean value = category.getValue();
                    if (mPosters.get(i).getCategory().equals(key) && value) {
                        mSelectedPosters.add(mPosters.get(i));
                        break;
                    }
                }
            }

            App.log(TAG, "filling selected posters array with size " + mSelectedPosters.size());


        }

        App.log("in refresh with size posters " + mPosters.size());
        notifyDataSetChanged();
    }

    public void refreshData(List<? extends GridItem> posters) {
        refreshData(posters, null);
    }

    public void refreshData(List<? extends GridItem> posters, HashMap<String, Boolean> checkedCategories) {
        if (posters == null) {
            mPosters = new ArrayList<>();
        } else {
            mPosters.clear();
            mPosters.addAll((Collection<? extends Poster>) posters);
        }

        if (checkedCategories != null && checkedCategories.size() > 0) {
            App.log("filling selected posters array");
            mSelectedPosters.clear();

            for (int i = 0; i < mPosters.size(); i++) {
                for (Map.Entry<String, Boolean> category : checkedCategories.entrySet()) {
                    String key = category.getKey();
                    boolean value = category.getValue();
                    if (mPosters.get(i).getCategory().equals(key) && value) {
                        mSelectedPosters.add(mPosters.get(i));
                        break;
                    }
                }
            }

            App.log(TAG, "filling selected posters array with size " + mSelectedPosters.size());


        }

        App.log("in refresh with size posters " + mPosters.size());
        notifyDataSetChanged();
    }

    @Override
    public PosterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyler_item, null);

        return new PosterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PosterViewHolder holder, int position) {
        String URL_SOVIET_ART = "http://sovietart.me";
        String imageURL = URL_SOVIET_ART + mSelectedPosters.get(position).getFilepath();
        final Poster p = mSelectedPosters.get(position);
//        App.log(TAG, position + ". in onBindViewHolder for poster: " + p.getTitle() + " /w url: " + imageURL);

        final ImageView iv = holder.poster;
        Glide.with(mContext)
                .load(imageURL)
                /* Adding a listener here to see when resource becomes available and perform */
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                        App.log(TAG, "onResourceReady: from cache:"+isFromMemoryCache + " isfirstresource:"+isFirstResource);
                        if (!p.hasFadedIn) {
                            iv.setHasTransientState(true);
                            final ObservableColorMatrix cm = new ObservableColorMatrix();
                            ObjectAnimator saturation = ObjectAnimator.ofFloat(cm,
                                    ObservableColorMatrix.SATURATION, 0f, 1f);
                            saturation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener
                                    () {
                                @Override
                                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                    // just animating the color matrix does not invalidate the
                                    // drawable so need this update listener.  Also have to create a
                                    // new color filter as the matrix is immutable :(
                                    if (iv.getDrawable() != null) {
                                        iv.getDrawable().setColorFilter(new
                                                ColorMatrixColorFilter(cm));
                                    }
                                }
                            });
                            saturation.setDuration(2000);
                            saturation.setInterpolator(AnimationUtils.loadInterpolator(mHostActivity,
                                    android.R.interpolator.fast_out_slow_in));
                            saturation.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {

                                    iv.setHasTransientState(false);
                                }
                            });
                            saturation.start();
                            p.hasFadedIn = true;
                        }

                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(holder.poster);
    }


    @Override
    public int getItemCount() {
        return mSelectedPosters.size();
    }

    class PosterViewHolder extends RecyclerView.ViewHolder {

        final ImageView poster;

        public PosterViewHolder(View itemView) {
            super(itemView);
            poster = (ImageView) itemView.findViewById(R.id.poster_imageview);
            poster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    App.toast("tapping on view");
                    Poster p = mPosters.get(getLayoutPosition());

                    poster.setTransitionName("poster");
                    Intent intent = new Intent(mContext, PosterViewActivity.class);
                    intent.putExtra("poster", p);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ActivityOptions options =
                            ActivityOptions.makeSceneTransitionAnimation(mHostActivity,
                                    Pair.create(v, "poster"),
                                    Pair.create(v, "poster_view"));
                    mContext.startActivity(intent, options.toBundle());
                }
            });
        }
    }
}
