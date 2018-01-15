package it.communikein.municipalia.ui.list.news;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import it.communikein.municipalia.R;
import it.communikein.municipalia.data.model.Event;
import it.communikein.municipalia.data.model.News;
import it.communikein.municipalia.databinding.ListItemNewsBinding;
import it.communikein.municipalia.databinding.ListItemNewsBigBinding;
import it.communikein.municipalia.ui.list.news.NewsAdapter.NewsViewHolder;
import it.communikein.municipalia.utilities.ComunicappDateUtils;

public class NewsAdapter extends RecyclerView.Adapter<NewsViewHolder> {

    private static final int VIEW_TYPE_BIG = 0;
    private static final int VIEW_TYPE_NORMAL = 1;

    private ArrayList<News> mList;
    private Context mContext;

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
    NewsAdapter(@Nullable NewsClickCallback newsClickCallback, Context context) {
        mOnClickListener = newsClickCallback;
        mContext = context;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_BIG:
                ListItemNewsBigBinding mBindingBig = DataBindingUtil
                        .inflate(LayoutInflater.from(parent.getContext()),
                                R.layout.list_item_news_big,
                                parent,
                                false);

                return new NewsViewHolder(mBindingBig);

            case VIEW_TYPE_NORMAL:
                ListItemNewsBinding mBinding = DataBindingUtil
                        .inflate(LayoutInflater.from(parent.getContext()),
                                R.layout.list_item_news,
                                parent,
                                false);

                return new NewsViewHolder(mBinding);

            default:
                throw new IllegalArgumentException("Invalid view type, value of " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        News news = mList.get(position);

        holder.bindData(news);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return VIEW_TYPE_BIG;
        else
            return VIEW_TYPE_NORMAL;
    }


    public void setList(final ArrayList<News> newList) {
        ArrayList<News> tempList = new ArrayList<>();
        for(News news : newList){
            if (news.areFieldsSet())
                tempList.add(news);
        }

        if (mList == null) {
            mList = tempList;
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
                    return tempList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mList.get(oldItemPosition).equals(tempList.get(newItemPosition));
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    News newItem = tempList.get(newItemPosition);
                    News oldItem = mList.get(oldItemPosition);
                    return oldItem.displayEquals(newItem);
                }
            });
            mList = tempList;
            result.dispatchUpdatesTo(this);
        }
    }


    class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ListItemNewsBinding mBinding = null;
        private ListItemNewsBigBinding mBindingBig = null;

        NewsViewHolder(ListItemNewsBinding binding) {
            super(binding.getRoot());

            binding.getRoot().setOnClickListener(this);
            binding.getRoot().setFocusable(true);

            this.mBinding = binding;
        }

        NewsViewHolder(ListItemNewsBigBinding binding) {
            super(binding.getRoot());

            binding.getRoot().setOnClickListener(this);
            binding.getRoot().setFocusable(true);

            this.mBindingBig = binding;
        }

        @Override
        public void onClick(View v) {
            News clicked;
            if (mBinding != null)
                clicked = mBinding.getNews();
            else
                clicked = mBindingBig.getNews();

            if (mOnClickListener != null) {
                if (clicked instanceof Event)
                    mOnClickListener.onListEventClick((Event) clicked);
                else
                    mOnClickListener.onListNewsClick(clicked);
            }
        }

        public void bindData(News news) {
            String friendly_date = ComunicappDateUtils
                    .getFriendlyDateString(mContext, news.getTimestamp(), true, true);

            if (mBinding != null) {
                mBinding.setNews(news);
                mBinding.timestampTextview.setText(friendly_date);
                mBinding.titleTextview.setText(news.getTitle());

                if (news.getImage() != null) {
                    Glide.with(mBinding.getRoot())
                            .load(news.getImage())
                            .into(mBinding.iconImageview);
                }
                else {
                    mBinding.iconImageview.setImageResource(news.getImageDefault());
                }
            }
            else {
                mBindingBig.setNews(news);
                mBindingBig.timestampTextview.setText(friendly_date);
                mBindingBig.titleTextview.setText(news.getTitle());

                if (news.getImage() != null) {
                    Glide.with(mBindingBig.getRoot())
                            .load(news.getImage())
                            .into(mBindingBig.iconImageview);
                }
                else {
                    mBindingBig.iconImageview.setImageResource(news.getImageDefault());
                }
            }
        }
    }
}
