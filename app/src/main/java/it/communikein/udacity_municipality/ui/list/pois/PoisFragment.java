package it.communikein.udacity_municipality.ui.list.pois;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import it.communikein.udacity_municipality.R;
import it.communikein.udacity_municipality.databinding.FragmentPoisBinding;
import it.communikein.udacity_municipality.ui.MainActivity;
import it.communikein.udacity_municipality.ui.list.news.NewsEventsFragment;
import it.communikein.udacity_municipality.viewmodel.PoisViewModel;
import it.communikein.udacity_municipality.viewmodel.factory.PoisViewModelFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class PoisFragment extends Fragment {

    private static final String LOG_TAG = NewsEventsFragment.class.getSimpleName();

    public static final ArrayList<String> TABS_TITLE = new ArrayList<>();
    private static final String FRAGMENT_MAP_TITLE = "Map";
    private static final String FRAGMENT_LIST_TITLE = "List";

    /*  */
    private FragmentPoisBinding mBinding;

    /* */
    @Inject
    PoisViewModelFactory viewModelFactory;

    /* */
    private PoisViewModel mViewModel;


    public PoisFragment() {
        TABS_TITLE.add(FRAGMENT_MAP_TITLE);
        TABS_TITLE.add(FRAGMENT_LIST_TITLE);
    }

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

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle();

        Log.d(LOG_TAG, "Get the View Model.");

        mViewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(PoisViewModel.class);

        Log.d(LOG_TAG, "Init the UI.");

        initUI();
    }

    private void initUI() {
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.showTabsLayout(TABS_TITLE);

            initViewPager(mBinding.viewpager);
            mainActivity.mBinding.tabs.setupWithViewPager(mBinding.viewpager);
        }
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
                actionBar.setTitle(R.string.title_pois);
        }
    }


    private void initViewPager(ViewPager viewPager) {
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            Adapter adapter = new Adapter(getChildFragmentManager());

            PoisMapFragment poisMapFragment = new PoisMapFragment();
            poisMapFragment.setViewModel(mViewModel);

            PoisListFragment poisListFragment = new PoisListFragment();
            poisListFragment.setViewModel(mViewModel);

            adapter.addFragment(poisMapFragment, FRAGMENT_MAP_TITLE);
            adapter.addFragment(poisListFragment, FRAGMENT_LIST_TITLE);

            viewPager.setAdapter(adapter);
        }
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
