package com.example.myapplication.ui.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.base.BaseViewHolder;
import com.example.myapplication.databinding.ItemHistoryRowBinding;
import com.example.myapplication.object.History;
import com.example.myapplication.utils.DateUtils;


import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private Callback mCallback;
    private Context mContext;

    List<History> listData;

    public HistoryAdapter(Context context) {
        mContext = context;
    }

    public HistoryAdapter() {

    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public void setListData(Context context, List<History> historyList) {
        this.mContext = context;
        this.listData = historyList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    @NonNull
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHistoryRowBinding binding =
                ItemHistoryRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ContentViewHolder(binding);
    }

    @Override
    public int getItemCount() {
        if (listData == null || listData.size() <= 0) {
            return 0;
        } else {
            return listData.size();
        }
    }
    public interface Callback {
        void onItemClick(History history);
    }


    public class ContentViewHolder extends BaseViewHolder {
        ItemHistoryRowBinding binding;
        ContentViewHolder(ItemHistoryRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        protected void clear() {
            binding.tvTotalCalories.setText("");
            binding.tvTimeTraining.setText("");
        }
        public void onBind(final int position) {
            if (listData == null) return;
            binding.tvTotalCalories.setText(listData.get(position).getCalories()+ " KCal");
            binding.tvTimeTraining.setText(listData.get(position).getMinute()+ " Minute");
            String date = DateUtils.formatDate(listData.get(position).getTrainningDate(), DateUtils.DATE_FORMAT12, DateUtils.DATE_FORMAT_6);
            binding.tvHour.setText(date);

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.onItemClick(listData.get(position));
                }
            });
        }
    }

}
