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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.HashMap;
import java.util.List;

import norakomi.sovietposterart.Adapters.GridItem;
import norakomi.sovietposterart.Adapters.PosterAdapter;
import norakomi.sovietposterart.data.prefs.SovietArtMePrefs;
import norakomi.sovietposterart.networking.DataManager;

/**
 * Created by MEDION on 22-11-2015.
 */

public class NavigationViewActivity extends AppCompatActivity {

    // UI
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private RecyclerView mRecyclerView;
    private NavigationView mNavigationView;

    // DATA
    private PosterAdapter mPosterAdapter;
    private DataManager mDataManager;
    private SovietArtMePrefs mPrefs;
    private HashMap<String, Boolean> checkedCategories = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_view);
        mPrefs = SovietArtMePrefs.get(this);

        setupToolbarAndDrawer();

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        mPosterAdapter = new PosterAdapter(null, this, this);
        mRecyclerView = (RecyclerView) findViewById(R.id.poster_overview_recycler);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mPosterAdapter);

        mDataManager = new DataManager(this) {
            @Override
            public void onDataLoaded(List<? extends GridItem> data) {
                // add categories to navigation drawer
                Menu menu = mNavigationView.getMenu();

                for (String categoryName : mDataManager.getCategories()) {
                    menu.add(categoryName);
                    // check whether or not added menu item should be checked
                    // non stored items are set to checked (true) by default
                    boolean checked = mPrefs.isCategoryChecked(categoryName);
                    menu.getItem(menu.size() - 1).setChecked(checked);
                    checkedCategories.put(categoryName, checked);
                }

                // update adapter with loaded data
                mPosterAdapter.refreshData(data, checkedCategories);

//                checkEmptyState(); // see plaid HomeActivity line 289
            }
        };
    }

    private void setupToolbarAndDrawer() {
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

        /* Set menu item on navigation view */
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                // when category/menu item gets toggled change it's checked state and refresh recycler adapter
                menuItem.setChecked(!menuItem.isChecked());
                mPrefs.setCategoryChecked(menuItem.toString(), menuItem.isChecked());
                // update checked categories hashmap and pass it to the adapter
                checkedCategories.put(menuItem.toString(), menuItem.isChecked());
                mPosterAdapter.setCategories(checkedCategories);
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

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
