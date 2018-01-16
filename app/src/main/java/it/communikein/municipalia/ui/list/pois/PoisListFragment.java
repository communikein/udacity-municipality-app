package it.communikein.municipalia.ui.list.pois;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import it.communikein.municipalia.R;
import it.communikein.municipalia.data.model.Poi;
import it.communikein.municipalia.databinding.SimpleListBinding;
import it.communikein.municipalia.ui.detail.PoiDetailActivity;
import it.communikein.municipalia.viewmodel.PoisViewModel;


public class PoisListFragment extends Fragment implements PoisListAdapter.OnListItemClickListener {

    public static final String LOG_TAG = PoisListFragment.class.getSimpleName();

    private SimpleListBinding mBinding;

    /* Create a new BookletAdapter. It will be responsible for displaying the list's items */
    private PoisListAdapter mAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /* Inflate the layout for this fragment */
        mBinding = DataBindingUtil.inflate(inflater, R.layout.simple_list, container, false);

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

        mAdapter = new PoisListAdapter(this);

        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mBinding.listRecyclerview.setAdapter(mAdapter);

        if (getParentViewModel() != null) {
            getParentViewModel().getObservableAllPois().observe(this, list -> {
                if (list != null) {
                    Log.d(LOG_TAG, "New data received. Size: " + list.size());
                    mAdapter.setList((ArrayList<Poi>) list);
                }
            });

        }

        mBinding.fab.setVisibility(View.GONE);
    }

    private PoisViewModel getParentViewModel() {
        if (getParentFragment() != null)
            return ((PoisFragment) getParentFragment()).getViewModel();
        else
            return null;
    }

    @Override
    public void onListPoiClick(Poi poi) {
        Intent intent = new Intent(getActivity(), PoiDetailActivity.class);
        startActivity(intent);
    }
}
