package norakomi.sovietposterart;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ActionMenuView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toolbar;

import java.util.List;

import butterknife.Bind;
import butterknife.BindInt;
import butterknife.ButterKnife;
import norakomi.sovietposterart.Adapters.GridItem;
import norakomi.sovietposterart.Adapters.RecyclerAdapter;
import norakomi.sovietposterart.data.DataManager;
import norakomi.sovietposterart.utils.AnimUtils;

/**
 * Created by MEDION on 16-11-2015.
 */
public class PrimeActivity extends AppCompatActivity {

    @Bind(R.id.navigation_drawer)
    DrawerLayout mDrawer;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.stories_grid)
    RecyclerView recyclerView;

    @Bind(R.id.fab)
    ImageButton mFab;
    @BindInt(R.integer.num_columns)
    int columns; // depending on width being 540dp+ or not sets columns to 2 or 3

    private GridLayoutManager layoutManager;

    // data
    private DataManager dataManager;
    private RecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prime);
        ButterKnife.bind(this);

        setActionBar(toolbar);
        if (savedInstanceState == null) {
            animateToolbar();
        }

        dataManager = new DataManager(this) {
            @Override
            public void onDataLoaded(List<? extends GridItem> data) {
                adapter.refreshAdapter(data);
                checkEmptyState();
            }
        };
        adapter = new RecyclerAdapter(this, dataManager);
        recyclerView.setAdapter(adapter);
        layoutManager = new GridLayoutManager(this, columns);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 2;
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(gridScroll); // scrollListener takes care of elevating toolbar at right time
        recyclerView.addOnScrollListener(new InfiniteScrollListener(layoutManager, dataManager) {
            @Override
            public void onLoadMore() {
                // dataManager.loadAllDataSources();
            }
        });
        recyclerView.setHasFixedSize(true);
    }

    private int gridScrollY = 0;
    private RecyclerView.OnScrollListener gridScroll = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            gridScrollY += dy;
            if (gridScrollY > 0 && toolbar.getTranslationZ() != -1f) {
                toolbar.setTranslationZ(-1f);
            } else if (gridScrollY == 0 && toolbar.getTranslationZ() != 0) {
                toolbar.setTranslationZ(0f);
            }
        }
    };

    private void animateToolbar() {
        // this is gross but toolbar doesn't expose it's children to animate them :(
        View t = toolbar.getChildAt(0);
        if (t != null && t instanceof TextView) {
            TextView title = (TextView) t;

            // fade in and space out the title.  Animating the letterSpacing performs horribly so
            // fake it by setting the desired letterSpacing then animating the scaleX ¯\_(ツ)_/¯
            title.setAlpha(0f);
            title.setScaleX(0.8f);

            title.animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .setStartDelay(300)
                    .setDuration(900)
                    .setInterpolator(AnimUtils.getMaterialInterpolator(this));
        }
        View amv = toolbar.getChildAt(1);
        if (amv != null & amv instanceof ActionMenuView) {
            ActionMenuView actions = (ActionMenuView) amv;
            popAnim(actions.getChildAt(0), 500, 200); // filter
            popAnim(actions.getChildAt(1), 700, 200); // overflow
        }
    }

    private void popAnim(View v, int startDelay, int duration) {
        if (v != null) {
            v.setAlpha(0f);
            v.setScaleX(0f);
            v.setScaleY(0f);

            v.animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .setStartDelay(startDelay)
                    .setDuration(duration)
                    .setInterpolator(AnimationUtils.loadInterpolator(this, android.R.interpolator
                            .overshoot));
        }
    }

    private void checkEmptyState() {
        // todo: handle data not being loaded yet
    }


}
