/*
 * Copyright 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package norakomi.sovietposterart.Adapters;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import norakomi.sovietposterart.R;
import norakomi.sovietposterart.data.DataManager;
import norakomi.sovietposterart.data.pojo.Poster;
import norakomi.sovietposterart.helpers.App;


/**
 * Adapter for the main screen grid of items
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_SOVIET_ART_ME_POSTERS = 0;
    private static final int TYPE_SOVIET_ART_COM_POSTERS = 1;
    private static final int TYPE_RIJKSMUSEUM_PAINTINGS = 2;
    private static final int TYPE_LOADING_MORE = -1;


    // we need to hold on to an activity ref for the shared element transitions :/
    private final Activity activity;
    private final LayoutInflater layoutInflater;
    private
    @Nullable
    DataManager dataManager;

    private List<GridItem> items;

    public RecyclerAdapter(Activity activity, DataManager dataManager) {
        this.activity = activity;
        this.dataManager = dataManager;
        layoutInflater = LayoutInflater.from(this.activity);
        items = new ArrayList<>();

        setHasStableIds(true);
    }

    public void refreshAdapter(List<? extends GridItem> data) {
        items.clear();
        items.addAll(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        App.log("onCreateViewHolder: viewType=" + viewType);
        switch (viewType) {
            case TYPE_SOVIET_ART_ME_POSTERS:
                return new SovietArtMeHolder(layoutInflater.inflate(R.layout.soviet_art_me_item, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        App.log("onBindViewHolder: position=" + position);

        GridItem item = getItem(position);
        if (item instanceof Poster) {
            App.log("binding a SovietArtMe item");
            bindSovietArtMePoster((Poster) getItem(position), (SovietArtMeHolder) holder);
        } else {
            App.log("not a Poster instance");
        }


        // todo: add loading animation over all the rows
//        else {
//            bindLoadingViewHolder((RijksMuseumArtHolder) holder, position);
//        }
    }

    private void bindSovietArtMePoster(final Poster poster, final SovietArtMeHolder holder) {
        final ImageButton posterImage = holder.poster;
        String imageURL = App.URL_SOVIET_ART + poster.getFilepath();

        Glide.with(activity)
                .load(imageURL)
                .into(posterImage);

        posterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo: handle button touching
                App.log("image clicked");
            }
        });
    }


    @Override
    public int getItemViewType(int position) {
        App.log("getting ItemViewType");
        GridItem item = getItem(position);
        if (item instanceof GridItem) {
            App.log("itemType instance Griditem");
            return TYPE_SOVIET_ART_ME_POSTERS;
        }

        App.log("itemType instance NOT!!! Griditem");

        return TYPE_LOADING_MORE;
    }

    private GridItem getItem(int position) {
        return items.get(position);
    }

    private void add(GridItem item) {
        items.add(item);
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }


    @Override
    public long getItemId(int position) {
        if (getItemViewType(position) == TYPE_LOADING_MORE) {
            return -1L;
        }
        return getItem(position).id;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }




    // ************************************* //
    /* ViewHolder for different source types */

    /* protected */ class SovietArtMeHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.poster_image)
        ImageButton poster;

        public SovietArtMeHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
