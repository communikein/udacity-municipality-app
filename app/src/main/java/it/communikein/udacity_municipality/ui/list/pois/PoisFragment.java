package it.communikein.udacity_municipality.ui.list.pois;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import it.communikein.udacity_municipality.R;
import it.communikein.udacity_municipality.data.model.Poi;
import it.communikein.udacity_municipality.databinding.FragmentPoisBinding;
import it.communikein.udacity_municipality.ui.list.news.NewsEventsFragment;
import it.communikein.udacity_municipality.viewmodel.PoisViewModel;
import it.communikein.udacity_municipality.viewmodel.factory.PoisViewModelFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class PoisFragment extends Fragment implements OnMapReadyCallback {

    private static final String LOG_TAG = NewsEventsFragment.class.getSimpleName();

    /*  */
    private FragmentPoisBinding mBinding;

    /* */
    @Inject
    PoisViewModelFactory viewModelFactory;

    /* */
    private PoisViewModel mViewModel;

    /* Might be null if Google Play services APK is not available. */
    private GoogleMap mMap = null;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private static final LatLng bergamo = new LatLng(45.6983, 9.6773);
    private static final LatLng rome = new LatLng(41.9028, 12.4964);


    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /* Inflate the layout for this fragment */
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_pois, container, false);

        initMap(savedInstanceState);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(PoisViewModel.class);

        initUI();
    }

    private void initUI() {
        mViewModel.getObservableAllPois().observe(this, list -> {
            if (list != null) {
                Log.d(LOG_TAG, "Updating the news list. " + list.size() + " elements.");
                updateUI(list);
            }
        });
    }

    private void initMap(Bundle savedInstanceState) {
        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mBinding.map.onCreate(mapViewBundle);
        mBinding.map.getMapAsync(this);
    }

    private void updateUI(List<Poi> pois) {
        updateMap(pois);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;

        updateMap(mViewModel.getObservableAllPois().getValue());
    }

    private void updateMap(List<Poi> pois) {
        if (mMap != null) {
            for (Poi poi : pois) {
                LatLng coords = new LatLng(poi.getLocationLat(), poi.getLocationLng());

                mMap.addMarker(new MarkerOptions().position(coords));
            }

            CameraPosition.Builder builder = new CameraPosition.Builder();
            builder.target(rome);
            builder.zoom(5f);

            CameraUpdate update = CameraUpdateFactory.newCameraPosition(builder.build());
            mMap.moveCamera(update);
        }
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
