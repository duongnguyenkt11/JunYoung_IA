package com.example.myapplication.ui.recommend;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.base.BaseViewHolder;
import com.example.myapplication.databinding.ItemActivitiesRowBinding;
import com.example.myapplication.object.Activities;
import com.example.myapplication.object.Food;

import java.util.List;

public class RecommendAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private Callback mCallback;
    private Context mContext;

    List<Activities> listData;

    public RecommendAdapter(Context context) {
        mContext = context;
    }

    public RecommendAdapter() {

    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public void setListData(Context context, List<Activities> activitiesList) {
        this.mContext = context;
        this.listData = activitiesList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    @NonNull
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemActivitiesRowBinding binding =
                ItemActivitiesRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ContentViewHolder(binding);
    }

    @Override
    public int getItemCount() {
        if (listData == null || listData.size() <= 0) {
            return -1;
        } else {
            return listData.size();
        }
    }
    public interface Callback {
    }


    public class ContentViewHolder extends BaseViewHolder {
        ItemActivitiesRowBinding binding;
        ContentViewHolder(ItemActivitiesRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        protected void clear() {
            binding.tvActivities.setText("");
        }
        public void onBind(final int position) {
            if (listData == null) return;
            binding.tvActivities.setText(listData.get(position).getActivitiesName());
        }
    }

}
