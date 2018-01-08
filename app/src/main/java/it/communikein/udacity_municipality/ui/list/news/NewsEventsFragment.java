package it.communikein.udacity_municipality.ui.list.news;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import it.communikein.udacity_municipality.R;
import it.communikein.udacity_municipality.data.model.News;
import it.communikein.udacity_municipality.databinding.FragmentNewsEventsBinding;
import it.communikein.udacity_municipality.ui.MainActivity;
import it.communikein.udacity_municipality.viewmodel.NewsViewModel;
import it.communikein.udacity_municipality.viewmodel.factory.NewsViewModelFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsEventsFragment extends Fragment {

    private static final String LOG_TAG = NewsEventsFragment.class.getSimpleName();

    /*  */
    private FragmentNewsEventsBinding mBinding;

    /* */
    @Inject
    NewsViewModelFactory viewModelFactory;

    /* */
    private NewsViewModel mViewModel;


    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /* Inflate the layout for this fragment */
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_news_events, container, false);

        /*
         * A LinearLayoutManager is responsible for measuring and positioning item views within a
         * RecyclerView into a linear list. This means that it can produce either a horizontal or
         * vertical list.
         *
         * The third parameter (reverseLayout) should be true if you want to reverse your
         * layout. Generally, this is only true with horizontal lists that need to support a
         * right-to-left layout.
         */
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                getActivity(),
                LinearLayoutManager.VERTICAL,
                false);
        mBinding.listRecyclerview.setLayoutManager(layoutManager);

        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        mBinding.listRecyclerview.setHasFixedSize(true);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle();

        /* Create a new BookletAdapter. It will be responsible for displaying the list's items */
        final NewsEventsAdapter mAdapter = new NewsEventsAdapter(null);

        mViewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(NewsViewModel.class);

        mViewModel.getObservableNews().observe(this, list -> {
            if (list != null) {
                Log.d(LOG_TAG, "Updating the news list. " + list.size() + " elements.");
                mAdapter.setList((ArrayList<News>) list);
            }
        });

        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mBinding.listRecyclerview.setAdapter(mAdapter);
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
                actionBar.setTitle(R.string.title_news_and_events);
        }
    }

}
