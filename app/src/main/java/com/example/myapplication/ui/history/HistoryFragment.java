package com.example.myapplication.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentHistoryBinding;
import com.example.myapplication.databinding.FragmentRecommendBinding;
import com.example.myapplication.object.History;
import com.example.myapplication.object.User;
import com.example.myapplication.ui.recommend.RecommendAdapter;
import com.example.myapplication.ui.recommend.RecommendFragment;
import com.example.myapplication.utils.Constant;
import com.example.myapplication.utils.DatabaseUtil;
import com.example.myapplication.utils.DateUtils;
import com.example.myapplication.utils.TinyDB;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import devs.mulham.horizontalcalendar.utils.Utils;

public class HistoryFragment extends Fragment implements View.OnClickListener, HistoryAdapter.Callback {
    FragmentHistoryBinding binding;
    User currentUser;
    TinyDB tinyDB;
    List<History> historyList, historyListOfCurrentUser, historyListOfCurrentUserByDate;
    DatabaseUtil databaseUtil;
    HistoryAdapter adapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHistoryBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        tinyDB = new TinyDB(getContext());
        currentUser = tinyDB.getObject("current_user", User.class); // retrieves the object from storage

        /* starts before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH,-1);

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH,0);

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(binding.getRoot(), R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(5)
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                adapter.setListData(getContext(), null );
                if (historyListOfCurrentUser.size() > 0){
                    String dateStr = DateUtils.convertCalendarToString(date, DateUtils.DATE_FORMAT_1);

                    historyListOfCurrentUserByDate = new ArrayList<>();
                    for (History history : historyListOfCurrentUser){
                        if (DateUtils.formatDate(history.getTrainningDate(), DateUtils.DATE_FORMAT12, DateUtils.DATE_FORMAT_1)
                                .equals(dateStr)) {
                            historyListOfCurrentUserByDate.add(history);
                            adapter.setListData(getContext(), historyListOfCurrentUserByDate );
                        }
                    }
                }
            }
        });

        databaseUtil = new DatabaseUtil(getContext());
        historyList = new ArrayList<>();
        historyListOfCurrentUser = new ArrayList<>();

        try {
            historyList = databaseUtil.getAllHistory();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (historyList.size() > 0){
            for (History history: historyList){
                if (history.getUsername().equals(currentUser.getId())){
                    historyListOfCurrentUser.add(history);
                }
            }
        }

        if (historyListOfCurrentUser.size() > 0){
            String dateStr = DateUtils.convertCalendarToString(endDate, DateUtils.DATE_FORMAT_1);
            historyListOfCurrentUserByDate = new ArrayList<>();
            for (History history : historyListOfCurrentUser){
                if (DateUtils.formatDate(history.getTrainningDate(), DateUtils.DATE_FORMAT12, DateUtils.DATE_FORMAT_1)
                        .equals(dateStr)) {
                    historyListOfCurrentUserByDate.add(history);
                }
            }
        }

        adapter = new HistoryAdapter(getActivity());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rcHistory.setLayoutManager(mLayoutManager);
        binding.rcHistory.setItemAnimator(new DefaultItemAnimator());
        binding.rcHistory.setAdapter(adapter);
        adapter.setListData(getContext(), historyListOfCurrentUserByDate );

        adapter.setCallback(this);

        binding.btnQuit.setOnClickListener(this);

        if (historyListOfCurrentUserByDate != null && historyListOfCurrentUserByDate.size() > 0){
            historyListOfCurrentUserByDate = new ArrayList<>();
            for (History history : historyListOfCurrentUser){
                if (Utils.daysBetween(DateUtils.convertStringToCalender(history.getTrainningDate(), DateUtils.DATE_FORMAT12), endDate)
                        < Constant.DAY_NUMBER_TO_VIEW_DATA) {
                    historyListOfCurrentUserByDate.add(history);
                }
            }

            Collections.sort(historyListOfCurrentUserByDate, new Comparator<History>() {
                @Override
                public int compare(History o1, History o2) {
                    if (DateUtils.convertStringToCalender(o1.getTrainningDate(),DateUtils.DATE_FORMAT12)
                            .before(DateUtils.convertStringToCalender(o2.getTrainningDate(),DateUtils.DATE_FORMAT12))) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            });

            ArrayList<BarEntry> entries = new ArrayList<>();
            List<String> vals = new ArrayList<>();
            try {
                vals = DateUtils.getLast5day();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < historyListOfCurrentUserByDate.size(); i++){
                for(int j = 0; j < vals.size(); j ++){
                    if (DateUtils.formatDate(historyListOfCurrentUserByDate.get(i).getTrainningDate(),DateUtils.DATE_FORMAT12, DateUtils.DATE_FORMAT_17).equals(vals.get(j))){
                        entries.add(new BarEntry(Float.valueOf(historyListOfCurrentUserByDate.get(i).getCalories()),
                                j));
                    }
                }

            }
            BarDataSet bardataset = new BarDataSet(entries, "Kcal");

            BarData data = null;
            try {
                data = new BarData(DateUtils.getLast5day(), bardataset);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            binding.barchart.setData(data); // set the data and list of labels into chart
            binding.barchart.setDescription("Chart for last 5 day");  // set the description
            bardataset.setColors(ColorTemplate.LIBERTY_COLORS);
            binding.barchart.animateY(5000);
        }

        return view;
    }

    @Override
    public void onClick(View v) {
            tinyDB.putObject("current_user", new User());
            getActivity().finish();
    }


    @Override
    public void onItemClick(History history) {
        tinyDB.putString("history_total_today_consumption",history.getCalories() );
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_navigation_history_to_navigation_recommend, null);


    }
}