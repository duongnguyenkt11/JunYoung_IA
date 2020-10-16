package com.example.myapplication.ui.search;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.base.BaseViewHolder;
import com.example.myapplication.databinding.ItemFoodRowBinding;
import com.example.myapplication.object.Food;
import com.example.myapplication.object.History;
import com.example.myapplication.object.User;
import com.example.myapplication.utils.StringUtil;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private Callback mCallback;
    private Context mContext;

    List<Food> listData;
    List<History> listHistory;

    public SearchAdapter(Context context) {
        mContext = context;
    }

    public SearchAdapter() {

    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public void setListData(Context context, List<Food> listData , List<History> listHistory) {
        this.mContext = context;
        this.listData = listData;
        this.listHistory = listHistory;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    @NonNull
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFoodRowBinding binding =
                ItemFoodRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ContentViewHolder(binding, new MyCustomEditTextListener());
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
        void onClickBookmarkItem(Food data);
        void onClickUnBookmarkItem(Food data);
        void onClickBtnPosItem(int kCal);
        void onClickBtnNegItem(int kCal);
    }


    public class ContentViewHolder extends BaseViewHolder {

        ItemFoodRowBinding binding;
        MyCustomEditTextListener myCustomEditTextListener;

        ContentViewHolder(ItemFoodRowBinding binding, MyCustomEditTextListener myCustomEditTextListener) {
            super(binding.getRoot());
            this.binding = binding;
            this.myCustomEditTextListener = myCustomEditTextListener;
            binding.tvTotalKcal.addTextChangedListener(myCustomEditTextListener);
        }

        @Override
        protected void clear() {
            binding.tvName.setText("");
            binding.tvKcal.setText("");
            binding.tvPortion.setText("");
            binding.tvTotalKcal.setText("");
        }
        public void onBind(final int position) {
            if (listData == null) return;

            binding.tvName.setText(listData.get(position).getFoodName());
            binding.tvKcal.setText(String.valueOf(listData.get(position).getCalories()) + " Kcal");
            binding.tvPortion.setText(String.valueOf(listData.get(position).getPortionSize()) + " gam");

            myCustomEditTextListener.updatePosition(position);
            binding.tvTotalKcal.setText(listData.get(position).getTotalKcal());

            if (listData.get(position).getCheckBookmark() == 1){
                binding.ivBookmark.setImageResource(R.drawable.ic_baseline_selected_bookmark_24);
            } else {
                binding.ivBookmark.setImageResource(R.drawable.ic_baseline_bookmark_border_24);
            }
            if (!StringUtil.isNullOrEmpty(binding.tvTotalKcal.getText().toString()) &&
                    Integer.valueOf(binding.tvTotalKcal.getText().toString()) > 0){
                binding.clMain.setBackgroundColor(mContext.getResources().getColor(R.color.primary));
            } else {
                binding.clMain.setBackgroundColor(mContext.getResources().getColor(R.color.colorBackgroundItem));
            }
            binding.ivBookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listData.get(position).getCheckBookmark() == 1){
                        listData.get(position).setCheckBookmark(0);
                        binding.ivBookmark.setImageResource(R.drawable.ic_baseline_bookmark_border_24);
                        mCallback.onClickUnBookmarkItem(listData.get(position));
                    } else {
                        listData.get(position).setCheckBookmark(1);
                        binding.ivBookmark.setImageResource(R.drawable.ic_baseline_selected_bookmark_24);
                        mCallback.onClickBookmarkItem(listData.get(position));
                    }
                }
            });

            binding.btnpos.setOnClickListener(new View.OnClickListener() {
                int kCal = 0;
                @Override
                public void onClick(View v) {
                    mCallback.onClickBtnPosItem(listData.get(position).getCalories());
                    if (listHistory.size() >= 1){
                        return;
                    }
                    kCal = listData.get(position).getCalories() +
                            Integer.valueOf(binding.tvTotalKcal.getText().toString());
                    binding.tvTotalKcal.setText(String.valueOf(kCal));
                    if (kCal > 0){
                        binding.clMain.setBackgroundColor(mContext.getResources().getColor(R.color.primary));
                    } else {
                        binding.clMain.setBackgroundColor(mContext.getResources().getColor(R.color.colorBackgroundItem));
                    }
                }
            });

            binding.btnneg.setOnClickListener(new View.OnClickListener() {
                int kCal = 0;
                @Override
                public void onClick(View v) {
                    mCallback.onClickBtnNegItem(listData.get(position).getCalories());
                    if (listHistory.size() >= 1){
                        return;
                    }
                    kCal = Integer.valueOf(binding.tvTotalKcal.getText().toString()) -
                            listData.get(position).getCalories();
                    if (kCal < 0) {
                        binding.tvTotalKcal.setText("0");
                        return;
                    }
                    binding.tvTotalKcal.setText(String.valueOf(kCal));

                    if (kCal > 0){
                        binding.clMain.setBackgroundColor(mContext.getResources().getColor(R.color.primary));
                    } else {
                        binding.clMain.setBackgroundColor(mContext.getResources().getColor(R.color.colorBackgroundItem));
                    }
                }
            });
        }
    }

    private class MyCustomEditTextListener implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            listData.get(position).setTotalKcal(charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }


}
