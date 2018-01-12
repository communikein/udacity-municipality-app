package it.communikein.municipalia.ui.list.reports;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import it.communikein.municipalia.R;
import it.communikein.municipalia.data.model.Report;
import it.communikein.municipalia.databinding.ListItemReportBinding;
import it.communikein.municipalia.ui.list.reports.ReportsAdapter.ReportViewHolder;


public class ReportsAdapter extends RecyclerView.Adapter<ReportViewHolder> {

    public static final String LOG_TAG = ReportsAdapter.class.getSimpleName();

    private ArrayList<Report> mList;

    @Nullable
    private final ReportClickCallback mOnClickListener;
    public interface ReportClickCallback {
        void onListReportClick(Report report);
    }

    /**
     * Creates a ReportsAdapter.
     *
     * @param reportsClickCallback Used to talk to the UI and app resources
     */
    ReportsAdapter(@Nullable ReportClickCallback reportsClickCallback) {
        mOnClickListener = reportsClickCallback;
    }

    @Override
    public ReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ListItemReportBinding mBinding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_report,
                        parent, false);

        return new ReportViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(ReportViewHolder holder, int position) {
        Report report = mList.get(position);

        holder.mBinding.setReport(report);
        holder.mBinding.titleTextview.setText(report.getTitle());
        holder.mBinding.descriptionTextview.setText(report.getDescription());
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public void setList(final ArrayList<Report> newList) {
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
                    Report newItem = newList.get(newItemPosition);
                    Report oldItem = mList.get(oldItemPosition);

                    return oldItem.displayEquals(newItem);
                }
            });
            mList = newList;
            result.dispatchUpdatesTo(this);
        }
    }


    class ReportViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ListItemReportBinding mBinding;

        ReportViewHolder(ListItemReportBinding binding) {
            super(binding.getRoot());

            binding.getRoot().setOnClickListener(this);

            this.mBinding = binding;
        }

        @Override
        public void onClick(View v) {
            Report clicked = mBinding.getReport();

            if (mOnClickListener != null) {
                mOnClickListener.onListReportClick(clicked);
            }
        }
    }

}
