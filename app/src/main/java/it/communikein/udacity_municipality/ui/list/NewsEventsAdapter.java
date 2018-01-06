package it.communikein.udacity_municipality.ui.list;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import it.communikein.udacity_municipality.R;
import it.communikein.udacity_municipality.data.model.News;
import it.communikein.udacity_municipality.databinding.NewsEventListItemBinding;
import it.communikein.udacity_municipality.ui.list.NewsEventsAdapter.NewsViewHolder;

public class NewsEventsAdapter extends RecyclerView.Adapter<NewsViewHolder> {

    @Nullable
    private final NewsClickCallback mOnClickListener;

    private ArrayList<News> mList;

    public interface NewsClickCallback {
        void onListItemClick(int id);
    }

    /**
     * Creates a NewsEventsAdapter.
     *
     * @param newsClickCallback Used to talk to the UI and app resources
     */
    NewsEventsAdapter(@Nullable NewsClickCallback newsClickCallback) {
        mOnClickListener = newsClickCallback;
    }


    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param parent    The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (like ours does) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new ForecastAdapterViewHolder that holds the View for each list item
     */
    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        NewsEventListItemBinding mBinding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.news_event_list_item,
                        parent, false);
        mBinding.setCallback(mOnClickListener);

        return new NewsViewHolder(mBinding);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the weather
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param holder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        holder.mBinding.setNews(mList.get(position));
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }


    public void setList(final ArrayList<News> newList) {
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
                    News newItem = newList.get(newItemPosition);
                    News oldItem = mList.get(oldItemPosition);
                    return oldItem.displayEquals(newItem);
                }
            });
            mList = newList;
            result.dispatchUpdatesTo(this);
        }
    }


    /**
     * A ViewHolder is a required part of the pattern for RecyclerViews. It mostly behaves as
     * a cache of the child views for a forecast item. It's also a convenient place to set an
     * OnClickListener, since it has access to the adapter and the views.
     */
    class NewsViewHolder extends RecyclerView.ViewHolder {

        final NewsEventListItemBinding mBinding;

        NewsViewHolder(NewsEventListItemBinding binding) {
            super(binding.getRoot());

            this.mBinding = binding;
        }
    }
}
