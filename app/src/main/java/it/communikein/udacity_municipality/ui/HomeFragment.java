package it.communikein.udacity_municipality.ui;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import it.communikein.udacity_municipality.R;
import it.communikein.udacity_municipality.databinding.FragmentHomeBinding;
import it.communikein.udacity_municipality.viewmodel.HomeViewModel;
import it.communikein.udacity_municipality.viewmodel.factory.HomeViewModelFactory;

public class HomeFragment extends Fragment {


    private static final String LOG_TAG = HomeFragment.class.getSimpleName();

    /*  */
    private FragmentHomeBinding mBinding;

    /* */
    @Inject
    HomeViewModelFactory viewModelFactory;

    /* */
    private HomeViewModel mViewModel;


    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /* Inflate the layout for this fragment */
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setTitle();
        hideTabs();

        initViewModel();
        initFab();
    }

    private void hideTabs() {
        if (getActivity() != null) {
            ((MainActivity) getActivity()).hideTabsLayout();
        }
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(HomeViewModel.class);

        mViewModel.getObservableNews().observe(this, list -> {
            if (list != null) {
                Log.d(LOG_TAG, "Updating the news list. " + list.size() + " elements.");

                // TODO: do something with the news
            }
        });

        mViewModel.getObservablePois().observe(this, list -> {
            if (list != null) {
                Log.d(LOG_TAG, "Updating the pois list. " + list.size() + " elements.");

                // TODO: do something with the pois
            }
        });

        mViewModel.getObservableReports().observe(this, list -> {
            if (list != null) {
                Log.d(LOG_TAG, "Updating the reports list. " + list.size() + " elements.");

                // TODO: do something with the reports
            }
        });
    }

    private void initFab() {
        mBinding.fab.setVisibility(View.GONE);
    }

    /**
     * Change the Activity's ActionBar title.
     */
    private void setTitle() {
        if (getActivity() != null) {
            /* Get a reference to the MainActivity ActionBar */
            ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
            /* If there is an ActionBar, set it's title */
            if (actionBar != null)
                actionBar.setTitle(R.string.title_home);
        }
    }

}
