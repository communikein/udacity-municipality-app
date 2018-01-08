package it.communikein.udacity_municipality.ui.list.pois;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import it.communikein.udacity_municipality.R;
import it.communikein.udacity_municipality.data.model.Poi;
import it.communikein.udacity_municipality.databinding.ListItemPoiBinding;
import it.communikein.udacity_municipality.ui.list.pois.PoisListAdapter.PoiViewHolder;

public class PoisListAdapter extends RecyclerView.Adapter<PoiViewHolder> {

    @Nullable
    private final PoiClickCallback mOnClickListener;

    private ArrayList<Poi> mList;

    public interface PoiClickCallback {
        void onListItemClick(String id);
    }

    /**
     * Creates a NewsEventsAdapter.
     *
     * @param newsClickCallback Used to talk to the UI and app resources
     */
    PoisListAdapter(@Nullable PoiClickCallback newsClickCallback) {
        mOnClickListener = newsClickCallback;
    }

    @Override
    public PoiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ListItemPoiBinding mBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.list_item_poi, parent, false);
        mBinding.setCallback(mOnClickListener);

        return new PoiViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(PoiViewHolder holder, int position) {
        Poi poi = mList.get(position);

        holder.mBinding.setPoi(poi);
        holder.mBinding.titleTextview.setText(poi.getTitle());
        holder.mBinding.descriptionTextview.setText(poi.getDescription());
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }


    public void setList(final ArrayList<Poi> newList) {
        if (mList == null) {
            mList = newList;
            notifyItemRangeInserted(0, mList.size());
        }
        else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mList.size();
                }

                @Override
                public int getNewListSize() {
                    return newList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mList.get(oldItemPosition).equals(newList.get(newItemPosition));
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Poi newItem = newList.get(newItemPosition);
                    Poi oldItem = mList.get(oldItemPosition);
                    return oldItem.displayEquals(newItem);
                }
            });
            mList = newList;
            result.dispatchUpdatesTo(this);
        }
    }


    class PoiViewHolder extends RecyclerView.ViewHolder {

        final ListItemPoiBinding mBinding;

        PoiViewHolder(ListItemPoiBinding binding) {
            super(binding.getRoot());

            this.mBinding = binding;
        }
    }
}
