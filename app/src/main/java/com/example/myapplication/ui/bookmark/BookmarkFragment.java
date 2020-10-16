package com.example.myapplication.ui.bookmark;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentBookmarkBinding;
import com.example.myapplication.object.Bookmark;
import com.example.myapplication.object.Food;
import com.example.myapplication.object.History;
import com.example.myapplication.object.MessageEvent;
import com.example.myapplication.object.User;
import com.example.myapplication.ui.search.SearchAdapter;
import com.example.myapplication.utils.DatabaseUtil;
import com.example.myapplication.utils.DateUtils;
import com.example.myapplication.utils.StringUtil;
import com.example.myapplication.utils.TinyDB;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class BookmarkFragment extends Fragment implements BookmarkAdapter.Callback {
    FragmentBookmarkBinding binding;
    String todayTotalKcal;
    TinyDB tinyDB;
    DatabaseUtil databaseUtil;
    List<Bookmark> bookmarkList;
    List<Food> foodList;
    BookmarkAdapter bookmarkAdapter;
    User currentUser;
    List<Food> bookmarkFoodList;
    List<History>  historyList;
    List<History>  historyListOfCurrentUser;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBookmarkBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        tinyDB = new TinyDB(getContext());
        todayTotalKcal = tinyDB.getString("total_today_consumption");

        databaseUtil = new DatabaseUtil(getActivity());
        currentUser = tinyDB.getObject("current_user", User.class); // retrieves the object from storage

        historyList = new ArrayList<>();
        historyListOfCurrentUser = new ArrayList<>();

        try {
            historyList = databaseUtil.getAllHistory();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (historyList.size() > 0){
            for (History history: historyList){
                if (history.getUsername().equals(currentUser.getId())
                        && DateUtils.formatDate(history.getTrainningDate(), DateUtils.DATE_FORMAT12, DateUtils.DATE_FORMAT_17)
                        .equals(DateUtils.formatDate(DateUtils.getCurrentDay(),DateUtils.DATE_FORMAT12, DateUtils.DATE_FORMAT_17))){
                    historyListOfCurrentUser.add(history);
                }
            }
        }

        if (!StringUtil.isNullOrEmpty(todayTotalKcal)){
            binding.totalKcalInDay.setText(todayTotalKcal);
        }

        try {
            bookmarkList = databaseUtil.getAllBookmark();
            foodList = databaseUtil.getAllFood();
        } catch (Exception e) {
            e.printStackTrace();
        }

        bookmarkFoodList = new ArrayList<>();
        for(Bookmark bookmark: bookmarkList){
            if (currentUser.getId().equals(String.valueOf(bookmark.getUsername()))){
                //Update foodlist
                for (Food food:foodList){
                    if (food.getId() == bookmark.getFoodId()){
                        food.setCheckBookmark(1);
                        bookmarkFoodList.add(food);
                    }
                }
            }
        }

        if (bookmarkFoodList.size() > 0){
            binding.rcFoodBookmark.setVisibility(View.VISIBLE);
            binding.textView6.setVisibility(View.GONE);
            binding.imageView.setVisibility(View.GONE);
        } else {
            binding.rcFoodBookmark.setVisibility(View.GONE);
            binding.textView6.setVisibility(View.VISIBLE);
            binding.imageView.setVisibility(View.VISIBLE);
        }

        bookmarkAdapter = new BookmarkAdapter(getActivity());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rcFoodBookmark.setLayoutManager(mLayoutManager);
        binding.rcFoodBookmark.setItemAnimator(new DefaultItemAnimator());
        binding.rcFoodBookmark.setAdapter(bookmarkAdapter);
        bookmarkAdapter.setCallback(this);
        bookmarkAdapter.setListData(getContext(), bookmarkFoodList, historyListOfCurrentUser);

        return view;
    }

    @Override
    public void onClickBookmarkItem(Food food) {
        databaseUtil.saveFoodToBookmark(food, currentUser);
    }

    @Override
    public void onClickUnBookmarkItem(Food food) {
        databaseUtil.removeFoodToBookmark(food, currentUser);
    }

    @Override
    public void onClickBtnPosItem(int kCal) {
        if (historyListOfCurrentUser.size() >= 1){
            Toast.makeText(getContext(), "You have chosen food for today. Please review the history.", Toast.LENGTH_SHORT).show();
            return;
        }
        int currentKcal = Integer.valueOf(binding.totalKcalInDay.getText().toString());
        binding.totalKcalInDay.setText(String.valueOf(currentKcal + kCal));
        tinyDB.putString("total_today_consumption", binding.totalKcalInDay.getText().toString());
    }

    @Override
    public void onClickBtnNegItem(int kCal) {
        if (historyListOfCurrentUser.size() >= 1){
            Toast.makeText(getContext(), "You have chosen food for today. Please review the history.", Toast.LENGTH_SHORT).show();
            return;
        }
        int currentKcal = Integer.valueOf(binding.totalKcalInDay.getText().toString());
        currentKcal -= kCal;
        if (currentKcal <= 0 ) {
            binding.totalKcalInDay.setText("0");
            return;
        }
        binding.totalKcalInDay.setText(String.valueOf(currentKcal));
        tinyDB.putString("total_today_consumption", binding.totalKcalInDay.getText().toString());
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        tinyDB.putString("total_today_consumption", binding.totalKcalInDay.getText().toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        try {
            bookmarkList = databaseUtil.getAllBookmark();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (event.getKey() == "SAVE_BOOKMARK_TO_DB_SUCCESS" && (Boolean) event.getObject() == true){
            Toast.makeText(getContext(), "Save bookmark success !", Toast.LENGTH_SHORT).show();

        }
        if (event.getKey() == "DELETE_BOOKMARK_TO_DB_SUCCESS" && (Boolean) event.getObject() == true){
            Toast.makeText(getContext(), "Delete bookmark success !", Toast.LENGTH_SHORT).show();
        }
    };
}