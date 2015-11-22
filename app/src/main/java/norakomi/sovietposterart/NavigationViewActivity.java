package norakomi.sovietposterart;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import norakomi.sovietposterart.Adapters.GridItem;
import norakomi.sovietposterart.Adapters.PosterAdapter;
import norakomi.sovietposterart.helpers.App;
import norakomi.sovietposterart.networking.APIManager;

/**
 * Created by MEDION on 22-11-2015.
 */

public class NavigationViewActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private RecyclerView mRecyclerView;
    private PosterAdapter mPosterAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_view);

        setupToolbar();

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        mPosterAdapter = new PosterAdapter(null, this);
        mRecyclerView = (RecyclerView) findViewById(R.id.poster_overview_recycler);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mPosterAdapter);

        new APIManager(this) {
            @Override
            public void onDataLoaded(List<? extends GridItem> data) {
                mPosterAdapter.refreshData(data);
            }
        };
    }

    private void setupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbarMain);
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        mActionBarDrawerToggle =
                new ActionBarDrawerToggle(this,
                        mDrawerLayout,
                        mToolbar,
                        R.string.drawer_open,
                        R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();

        NavigationView view = (NavigationView) findViewById(R.id.navigation_view);
        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                App.toast("touched menuitem: " + menuItem.getTitle());
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
