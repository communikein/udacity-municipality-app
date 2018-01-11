package it.communikein.udacity_municipality.ui.detail;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import it.communikein.udacity_municipality.R;

import it.communikein.udacity_municipality.databinding.ActivityPoiDetailBinding;

public class PoiDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = PoiDetailActivity.class.getSimpleName();

    private ActivityPoiDetailBinding mBinding;

    /* Might be null if Google Play services APK is not available. */
    private GoogleMap mMap = null;
    private static final LatLng bergamo = new LatLng(45.6983, 9.6773);
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_poi_detail);

        initUI(savedInstanceState);
    }

    private void initUI(Bundle savedInstanceState) {
        initMap(savedInstanceState);

        initToolbar();
    }

    private void initMap(Bundle savedInstanceState) {
        /*
         * *** IMPORTANT ***
         * MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
         * objects or sub-Bundles.
         */
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mBinding.map.onCreate(mapViewBundle);
        mBinding.map.getMapAsync(this);
    }

    private void initToolbar() {
        setSupportActionBar(mBinding.toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mBinding.collapsingToolbar.setTitle(" ");
        mBinding.collapsingToolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
        mBinding.appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    mBinding.collapsingToolbar.setTitle("Città dei mille");
                    mBinding.map.setAlpha(.7f);
                    isShow = true;
                } else if(isShow) {
                    // There should a space between double quote otherwise it won't work
                    mBinding.collapsingToolbar.setTitle(" ");
                    mBinding.map.setAlpha(1f);
                    isShow = false;
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;

        updateMap();
    }

    private void updateMap() {
        MarkerOptions markerOptions = new MarkerOptions()
                .position(bergamo)
                .title("Bergamo");

        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(markerOptions.getPosition(), 10f);

        mMap.addMarker(markerOptions);
        mMap.moveCamera(cu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            NavUtils.navigateUpFromSameTask(this);

        return super.onOptionsItemSelected(item);
    }




    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mBinding.map.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        mBinding.map.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mBinding.map.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBinding.map.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mBinding.map.onLowMemory();
    }
}
