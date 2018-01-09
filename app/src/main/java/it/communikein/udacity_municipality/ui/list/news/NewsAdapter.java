package it.communikein.udacity_municipality.ui.list.news;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import it.communikein.udacity_municipality.R;
import it.communikein.udacity_municipality.data.model.Event;
import it.communikein.udacity_municipality.data.model.News;
import it.communikein.udacity_municipality.databinding.ListItemNewsBinding;
import it.communikein.udacity_municipality.ui.list.news.NewsAdapter.NewsViewHolder;

public class NewsAdapter extends RecyclerView.Adapter<NewsViewHolder> {

    private ArrayList<News> mList;

    @Nullable
    private final NewsClickCallback mOnClickListener;
    public interface NewsClickCallback {
        void onListNewsClick(News news);
        void onListEventClick(Event event);
    }

    /**
     * Creates a NewsAdapter.
     *
     * @param newsClickCallback Used to talk to the UI and app resources
     */
    NewsAdapter(@Nullable NewsClickCallback newsClickCallback) {
        mOnClickListener = newsClickCallback;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ListItemNewsBinding mBinding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_news,
                        parent, false);

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


    class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ListItemNewsBinding mBinding;

        NewsViewHolder(ListItemNewsBinding binding) {
            super(binding.getRoot());

            binding.getRoot().setOnClickListener(this);

            this.mBinding = binding;
        }

        @Override
        public void onClick(View v) {
            News clicked = mBinding.getNews();

            if (mOnClickListener != null) {
                if (clicked instanceof Event)
                    mOnClickListener.onListEventClick((Event) clicked);
                else
                    mOnClickListener.onListNewsClick(clicked);
            }
        }
    }
}
