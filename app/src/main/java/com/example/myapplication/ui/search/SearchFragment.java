package com.example.myapplication.ui.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.databinding.FragmentSearchBinding;
import com.example.myapplication.object.Bookmark;
import com.example.myapplication.object.Food;
import com.example.myapplication.object.History;
import com.example.myapplication.object.MessageEvent;
import com.example.myapplication.object.User;
import com.example.myapplication.utils.DatabaseUtil;
import com.example.myapplication.utils.DateUtils;
import com.example.myapplication.utils.StringUtil;
import com.example.myapplication.utils.TinyDB;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment
        implements TextWatcher, SearchAdapter.Callback {

    SearchAdapter searchAdapter;
    FragmentSearchBinding binding;
    List<Food> foodList;
    List<Bookmark> bookmarkList;
    DatabaseUtil databaseUtil;
    SearchAdapter.Callback callback;
    User currentUser;
    TinyDB tinyDB;
    String todayTotalKcal;
    List<History>  historyList;
    List<History>  historyListOfCurrentUser;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSearchBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();


        databaseUtil = new DatabaseUtil(getActivity());
        try {
            foodList = databaseUtil.getAllFood();
            bookmarkList = databaseUtil.getAllBookmark();
        } catch (Exception e) {
            e.printStackTrace();
        }

        binding.edSearch.addTextChangedListener(this);

        searchAdapter = new SearchAdapter(getActivity());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rcFood.setLayoutManager(mLayoutManager);
        binding.rcFood.setItemAnimator(new DefaultItemAnimator());
        binding.rcFood.setAdapter(searchAdapter);
        searchAdapter.setCallback(this);

        tinyDB = new TinyDB(getContext());
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

        for(Bookmark bookmark: bookmarkList){
            if (currentUser.getId().equals(String.valueOf(bookmark.getUsername()))){
                //Update foodlist
                for (Food food:foodList){
                    if (food.getId() == bookmark.getFoodId()){
                        food.setCheckBookmark(1);
                    }
                }
            }
        }
        searchAdapter.setListData(getContext(), foodList, historyListOfCurrentUser);

        todayTotalKcal = tinyDB.getString("total_today_consumption");
        if (!StringUtil.isNullOrEmpty(todayTotalKcal)){
            binding.totalKcalInDay.setText(todayTotalKcal);
        }
        return view;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (StringUtil.isNullOrEmpty(String.valueOf(s))){
            try {
//                foodList = databaseUtil.getAllFood();
                updateBookmarkList(foodList);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        try {
            List<Food> newFoodList = new ArrayList<>();
            newFoodList = databaseUtil.getFoodsBySearchKey(s.toString());
            updateBookmarkList(newFoodList);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            updateBookmarkList(foodList);
        }
        if (event.getKey() == "DELETE_BOOKMARK_TO_DB_SUCCESS" && (Boolean) event.getObject() == true){
            Toast.makeText(getContext(), "Delete bookmark success !", Toast.LENGTH_SHORT).show();
            updateBookmarkList(foodList);
        }
    };

    private void updateBookmarkList(List<Food> foodList){
        for(Bookmark bookmark: bookmarkList){
            if (currentUser.getId().equals(String.valueOf(bookmark.getUsername()))){

                for (Food food:foodList){
                    if (food.getId() == bookmark.getFoodId()){
                        food.setCheckBookmark(1);
                    }
                }
            }
        }
        searchAdapter.setListData(getContext(), foodList, historyListOfCurrentUser);
    }
}