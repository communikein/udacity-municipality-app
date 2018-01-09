package it.communikein.udacity_municipality.ui;

import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import it.communikein.udacity_municipality.R;
import it.communikein.udacity_municipality.databinding.ActivityMainBinding;
import it.communikein.udacity_municipality.ui.list.news.NewsEventsFragment;
import it.communikein.udacity_municipality.ui.list.pois.PoisFragment;

public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector,
        NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    private ActivityMainBinding mBinding;

    private final List<Fragment> fragments = new ArrayList<>();

    public static String FRAGMENT_SELECTED_TAG;

    private static final int INDEX_FRAGMENT_NEWS = 0;
    private static final int INDEX_FRAGMENT_POIS = 1;

    public static final String TAG_FRAGMENT_NEWS = "tab-news";
    public static final String TAG_FRAGMENT_POIS = "tab-pois";

    private static final long DRAWER_CLOSE_DELAY_MS = 250;
    private ActionBarDrawerToggle mDrawerToggle;
    private final Handler mDrawerActionHandler = new Handler();
    private final String DRAWER_ITEM_SELECTED = "drawer-item-selected";
    private int drawerItemSelectedId = R.id.navigation_home;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initUI(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(DRAWER_ITEM_SELECTED, drawerItemSelectedId);
        super.onSaveInstanceState(outState);
    }

    private void initUI(Bundle savedInstanceState) {
        buildFragmentsList();

        Window w = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            w.setNavigationBarColor(getColor(R.color.colorPrimary));
        }
        */

        setSupportActionBar(mBinding.toolbar);

        initDrawer(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void buildFragmentsList() {
        fragments.add(INDEX_FRAGMENT_NEWS, new NewsEventsFragment());
        fragments.add(INDEX_FRAGMENT_POIS, new PoisFragment());
    }

    private boolean navigate(int tab_id) {
        int index = INDEX_FRAGMENT_NEWS;

        switch (tab_id) {
            case R.id.navigation_home:
                break;
            case R.id.navigation_news_and_events:
                index = INDEX_FRAGMENT_NEWS;
                FRAGMENT_SELECTED_TAG = TAG_FRAGMENT_NEWS;
                break;
            case R.id.navigation_pois:
                index = INDEX_FRAGMENT_POIS;
                FRAGMENT_SELECTED_TAG = TAG_FRAGMENT_POIS;
                break;
            default:
                return false;
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.tab_container, fragments.get(index), FRAGMENT_SELECTED_TAG)
                .commit();
        return true;
    }

    private void initDrawer(Bundle savedInstanceState){
        initHeader();

        mDrawerToggle = new ActionBarDrawerToggle(this, mBinding.drawerLayout,
                mBinding.toolbar, R.string.open, R.string.close);
        mBinding.drawerLayout.addDrawerListener(mDrawerToggle);
        mBinding.navigation.setNavigationItemSelectedListener(this);

        if (savedInstanceState != null) {
            /* Get the last selected drawer menu item ID */
            drawerItemSelectedId = savedInstanceState.getInt(DRAWER_ITEM_SELECTED);

            /* Remove this entry to avoid problems further */
            savedInstanceState.remove(DRAWER_ITEM_SELECTED);
            if (savedInstanceState.size() == 0)
                savedInstanceState = null;
        }

        mBinding.navigation.getMenu()
                .findItem(drawerItemSelectedId)
                .setChecked(true);
        /*
         * If the savedInstanceState is not null, it means we don't need to display the fragment,
         * since it has already been saved and will be restored by the system
         */
        if (savedInstanceState == null) {
            navigate(R.id.navigation_news_and_events);
        }
    }

    private void initHeader() {
        View header = mBinding.navigation.getHeaderView(0);

        ImageView userImageView = header.findViewById(R.id.circleView);
        TextView userNameTextView = header.findViewById(R.id.user_name_textview);
        TextView userEmailTextView = header.findViewById(R.id.user_email_textview);
        ImageView userBackgroundView = header.findViewById(R.id.backgroundView);

        userImageView.setImageResource(R.mipmap.fumagalli);
        userNameTextView.setText(getString(R.string.holder_user_name));
        userEmailTextView.setText(getString(R.string.holder_user_email));
        userBackgroundView.setImageResource(R.mipmap.aldo_giovanni_giacomo);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem menuItem) {
        /* Update highlighted item in the navigation menu */
        drawerItemSelectedId = menuItem.getItemId();
        uncheckNavigationDrawersItems();
        menuItem.setChecked(true);

        /*
         * Allow some time after closing the drawer before performing real navigation
         * so the user can see what is happening.
         */
        mBinding.drawerLayout.closeDrawer(GravityCompat.START);
        mDrawerActionHandler.postDelayed(() ->
                navigate(menuItem.getItemId()), DRAWER_CLOSE_DELAY_MS);

        return true;
    }

    private void uncheckNavigationDrawersItems() {
        int size = mBinding.navigation.getMenu().size();

        for (int i=0; i<size; i++)
            mBinding.navigation.getMenu().getItem(i).setChecked(false);
    }

    @Override
    public void onBackPressed() {
        if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mBinding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

}
