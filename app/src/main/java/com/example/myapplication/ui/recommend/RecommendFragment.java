package com.example.myapplication.ui.recommend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.databinding.FragmentRecommendBinding;
import com.example.myapplication.object.Activities;
import com.example.myapplication.object.User;
import com.example.myapplication.utils.DatabaseUtil;
import com.example.myapplication.utils.DateUtils;
import com.example.myapplication.utils.StringUtil;
import com.example.myapplication.utils.TinyDB;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class RecommendFragment extends Fragment implements View.OnClickListener{
    FragmentRecommendBinding binding;
    List<Activities> activitiesList;
    List<Activities> activitiesRecomList;
    DatabaseUtil databaseUtil;
    User currentUser;
    TinyDB tinyDB;
    int todayTotalKcal;
    RecommendAdapter recommendAdapter;
    float min = 0;
    boolean isFromHistory;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRecommendBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        tinyDB = new TinyDB(getContext());
        currentUser = tinyDB.getObject("current_user", User.class); // retrieves the object from storage

        databaseUtil = new DatabaseUtil(getContext());
        try {
            activitiesList = new ArrayList<>();
            activitiesList = databaseUtil.getAllActivities();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!StringUtil.isNullOrEmpty(tinyDB.getString("total_today_consumption"))){
            todayTotalKcal = Integer.valueOf(tinyDB.getString("total_today_consumption"));
        }
        if (todayTotalKcal == 0){
            if (!StringUtil.isNullOrEmpty(tinyDB.getString("history_total_today_consumption"))){
                isFromHistory = true;
                todayTotalKcal = Integer.valueOf(tinyDB.getString("history_total_today_consumption"));
                tinyDB.putString("history_total_today_consumption", "0");

            } else {
                todayTotalKcal = 0;
            }
        }

        if (todayTotalKcal > 0){
            binding.rcActivities.setVisibility(View.VISIBLE);
            binding.textView6.setVisibility(View.GONE);
            binding.imageView.setVisibility(View.GONE);
            activitiesRecomList = new ArrayList<>();

            for (Activities activity : activitiesList){
                int realCalories = calculateCalories(currentUser,activity.getBaseCalorie())
                        .intValue();
                if (todayTotalKcal >= realCalories ){
                    todayTotalKcal = todayTotalKcal - realCalories;
                    activity.setActivitiesName(activity.getActivitiesName() + " (30 minute)");
                    activitiesRecomList.add(activity);
                    min = 30;
                } else if(todayTotalKcal > 0 && todayTotalKcal < realCalories){
                    min = (float) Math.ceil(((double)todayTotalKcal * 30)/realCalories);
                    activity.setActivitiesName(activity.getActivitiesName() + " ("+ min +" minute)");
                    activitiesRecomList.add(activity);
                    break;
                }
            }

            recommendAdapter = new RecommendAdapter(getActivity());
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            binding.rcActivities.setLayoutManager(mLayoutManager);
            binding.rcActivities.setItemAnimator(new DefaultItemAnimator());
            binding.rcActivities.setAdapter(recommendAdapter);
            recommendAdapter.setListData(getContext(), activitiesRecomList );
            String totalAct = "";
            for (Activities activities : activitiesRecomList) {
                totalAct += activities.getActivitiesName().concat("; ");
            }
            binding.tvTotalSum.setText(totalAct);

            binding.btnConfirm.setOnClickListener(this);

        } else {
            binding.rcActivities.setVisibility(View.GONE);
            binding.textView6.setVisibility(View.VISIBLE);
            binding.imageView.setVisibility(View.VISIBLE);
        }

         binding.btnConfirm.setVisibility(isFromHistory ? View.GONE : View.VISIBLE);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == binding.btnConfirm.getId()){

            if(activitiesRecomList.size() == 0) {return;}
            String activitiesStr = "";
            for (Activities activities : activitiesRecomList){
                activitiesStr += activities.getActivitiesName() + " ;";
            }
            int totalTime = (int) ((activitiesRecomList.size() - 1) * 30 + min);
            databaseUtil.saveActivitiesToHistory(activitiesStr,
                    Integer.valueOf(tinyDB.getString("total_today_consumption")),
                    currentUser,
                    totalTime,
                    DateUtils.getCurrentDay());

            activitiesRecomList.clear();
            binding.tvTotalSum.setText("No data");
            activitiesList.clear();
            recommendAdapter.setListData(getContext(), activitiesRecomList );
            tinyDB.putString("total_today_consumption", "0");

            Toast.makeText(getContext(), "Save to history success", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * In case gender is female, base calories = 90% base_calorie_per_hour_of_activity value
     * @param baseCalo Base calories
     * @return (weight/65) * base_calorie_per_hour_of_activity
     */
    private BigDecimal calculateCalories(User user, int baseCalo) {
        if (user.getGender().equals("1")) {
            baseCalo = (baseCalo * 90) / 100;
        }
        BigDecimal weight = new BigDecimal(user.getWeight());
        BigDecimal baseWeight = new BigDecimal(65);

        return weight.divide(baseWeight, 2, RoundingMode.UP).multiply(new BigDecimal(baseCalo));
    }
}