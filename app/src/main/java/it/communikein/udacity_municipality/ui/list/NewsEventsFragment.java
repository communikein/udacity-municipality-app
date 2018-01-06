package it.communikein.udacity_municipality.ui.list;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import it.communikein.udacity_municipality.R;
import it.communikein.udacity_municipality.databinding.FragmentNewsEventsBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsEventsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news_events, container, false);
    }

}
