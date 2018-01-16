package it.communikein.municipalia.ui.list.pois;


import android.arch.lifecycle.LiveData;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import it.communikein.municipalia.R;
import it.communikein.municipalia.data.model.Poi;
import it.communikein.municipalia.databinding.FragmentPoisBinding;
import it.communikein.municipalia.ui.MainActivity;
import it.communikein.municipalia.ui.list.news.NewsFragment;
import it.communikein.municipalia.viewmodel.PoisViewModel;
import it.communikein.municipalia.viewmodel.factory.PoisViewModelFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class PoisFragment extends Fragment {

    private static final String LOG_TAG = NewsFragment.class.getSimpleName();

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

    public PoisViewModel getViewModel() {
        return mViewModel;
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
        showTabs();

        initViewModel();
        initViewPager();
        initFab();
    }

    private void showTabs() {
        if (getActivity() != null) {
            ((MainActivity) getActivity()).showTabsLayout(TABS_TITLE);
        }
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(PoisViewModel.class);

        mViewModel.getObservableAllPois().observe(this, list -> {

        });
    }

    private void initViewPager() {
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            initViewPager(mBinding.viewpager);
            mainActivity.mBinding.tabs.setupWithViewPager(mBinding.viewpager);
        }
    }

    private void initFab() {
        mBinding.fab.setOnClickListener(v -> {
            // TODO: Call intent to start new activity
        });
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
            PoisListFragment poisListFragment = new PoisListFragment();

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
