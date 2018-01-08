package it.communikein.udacity_municipality.ui.list.news;

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
import it.communikein.udacity_municipality.ui.list.news.NewsEventsAdapter.NewsViewHolder;

public class NewsEventsAdapter extends RecyclerView.Adapter<NewsViewHolder> {

    @Nullable
    private final NewsClickCallback mOnClickListener;

    private ArrayList<News> mList;

    public interface NewsClickCallback {
        void onListItemClick(String id);
    }

    /**
     * Creates a NewsEventsAdapter.
     *
     * @param newsClickCallback Used to talk to the UI and app resources
     */
    NewsEventsAdapter(@Nullable NewsClickCallback newsClickCallback) {
        mOnClickListener = newsClickCallback;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        NewsEventListItemBinding mBinding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.news_event_list_item,
                        parent, false);
        mBinding.setCallback(mOnClickListener);

        return new NewsViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        News news = mList.get(position);

        holder.mBinding.setNews(news);
        holder.mBinding.descriptionTextview.setText(news.getDescription());
        holder.mBinding.titleTextview.setText(news.getTitle());
    }

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


    class NewsViewHolder extends RecyclerView.ViewHolder {

        final NewsEventListItemBinding mBinding;

        NewsViewHolder(NewsEventListItemBinding binding) {
            super(binding.getRoot());

            this.mBinding = binding;
        }
    }
}
