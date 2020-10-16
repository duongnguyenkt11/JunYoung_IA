package com.example.myapplication.utils;

import android.content.Context;
import android.database.Cursor;

import com.example.myapplication.R;
import com.example.myapplication.dao.DatabaseOperations;
import com.example.myapplication.object.Activities;
import com.example.myapplication.object.Bookmark;
import com.example.myapplication.object.Food;
import com.example.myapplication.object.History;
import com.example.myapplication.object.User;

import java.util.ArrayList;
import java.util.List;

import static com.example.myapplication.utils.Constant.ACTIVITIES_TABLE;
import static com.example.myapplication.utils.Constant.BOOKMARK_TABLE;
import static com.example.myapplication.utils.Constant.DB_NAME;
import static com.example.myapplication.utils.Constant.FOOD_TABLE;
import static com.example.myapplication.utils.Constant.HISTORY_TABLE;
import static com.example.myapplication.utils.Constant.USER_TABLE;


public class DatabaseUtil {
    private Context mContext;

    public DatabaseUtil(Context mContext) {
        this.mContext = mContext;
    }


    public void saveUserInDB(User user) {
        String[] columnNames = mContext.getResources().getStringArray(R.array.user_table_columns);

        DatabaseOperations databaseOperations = new DatabaseOperations(mContext, DB_NAME);

        String[] values = {
                user.getUsername()
                , user.getPassword()
                , user.getGender()
                , user.getWeight()};

        try {
            databaseOperations.saveDataInDB(USER_TABLE, columnNames, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveFoodToBookmark(Food food, User user) {
        String[] columnNames = mContext.getResources().getStringArray(R.array.book_mark_columns);

        DatabaseOperations databaseOperations = new DatabaseOperations(mContext, DB_NAME);

        String[] values = {String.valueOf(food.getId()), user.getId(), food.getFoodName(), String.valueOf(food.getPortionSize()), String.valueOf(food.getCalories()), ""};

        try {
            databaseOperations.saveDataInDB(BOOKMARK_TABLE, columnNames, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeFoodToBookmark(Food food, User user) {
        DatabaseOperations databaseOperations = new DatabaseOperations(mContext, DB_NAME);
        try {
            databaseOperations.deleteFromDB(BOOKMARK_TABLE,
                    "food_id", String.valueOf(food.getId()),
                    "username", String.valueOf(user.getId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Bookmark> getAllBookmark() throws Exception {
        List<Bookmark> bookmarkList = new ArrayList<>();

        String[] columnNames = mContext.getResources().getStringArray(R.array.book_mark_columns_with_index);

        DatabaseOperations databaseOperations = new DatabaseOperations(mContext, DB_NAME);
        Cursor cursor = databaseOperations.retrieveAllFromDB(BOOKMARK_TABLE);
        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    Bookmark bookmark = new Bookmark();
                    bookmark.setId(cursor.getInt(cursor.getColumnIndex(columnNames[0])));
                    bookmark.setFoodId(cursor.getInt(cursor.getColumnIndex(columnNames[1])));
                    bookmark.setUsername(cursor.getString(cursor.getColumnIndex(columnNames[2])));
                    bookmark.setFoodName(cursor.getString(cursor.getColumnIndex(columnNames[3])));
                    bookmark.setPortionSize(cursor.getInt(cursor.getColumnIndex(columnNames[4])));
                    bookmark.setCalories(cursor.getInt(cursor.getColumnIndex(columnNames[5])));
                    bookmark.setQuantity(0);
                    bookmarkList.add(bookmark);
                } while (cursor.moveToNext());
            }
        }

        return bookmarkList;
    }

    public List<User> getAllUser() throws Exception {
        List<User> userList = new ArrayList<>();

        String[] columnNames = mContext.getResources().getStringArray(R.array.user_table_columns_with_index);

        DatabaseOperations databaseOperations = new DatabaseOperations(mContext, DB_NAME);
        Cursor cursor = databaseOperations.retrieveAllFromDB(USER_TABLE);
        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    User user = new User();
                    user.setId(cursor.getString(cursor.getColumnIndex(columnNames[0])));
                    user.setUsername(cursor.getString(cursor.getColumnIndex(columnNames[1])));
                    user.setPassword(cursor.getString(cursor.getColumnIndex(columnNames[2])));
                    user.setGender((cursor.getString(cursor.getColumnIndex(columnNames[3]))));
                    user.setWeight((cursor.getString(cursor.getColumnIndex(columnNames[4]))));
                    userList.add(user);
                } while (cursor.moveToNext());
            }
        }

        return userList;
    }

    public List<Activities> getAllActivities() throws Exception {
        List<Activities> activitiesList = new ArrayList<>();

        String[] columnNames = mContext.getResources().getStringArray(R.array.activities_table_columns);

        DatabaseOperations databaseOperations = new DatabaseOperations(mContext, DB_NAME);
        Cursor cursor = databaseOperations.retrieveAllFromDB(ACTIVITIES_TABLE);
        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    Activities activities = new Activities();
                    activities.setId(cursor.getInt(cursor.getColumnIndex(columnNames[0])));
                    activities.setActivitiesName(cursor.getString(cursor.getColumnIndex(columnNames[1])));
                    activities.setBaseCalorie(cursor.getInt(cursor.getColumnIndex(columnNames[2])));
                    activities.setImage((cursor.getString(cursor.getColumnIndex(columnNames[3]))));
                    activitiesList.add(activities);
                } while (cursor.moveToNext());
            }
        }

        return activitiesList;
    }

    public List<Food> getAllFood() throws Exception {
        List<Food> foodList = new ArrayList<>();

        String[] columnNames = mContext.getResources().getStringArray(R.array.food_table_columns);

        DatabaseOperations databaseOperations = new DatabaseOperations(mContext, DB_NAME);
        Cursor cursor = databaseOperations.retrieveAllFromDB(Constant.FOOD_TABLE);
        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    Food food = new Food();
                    food.setId(cursor.getInt(cursor.getColumnIndex(columnNames[0])));
                    food.setFoodName(cursor.getString(cursor.getColumnIndex(columnNames[1])));
                    food.setPortionSize(cursor.getInt(cursor.getColumnIndex(columnNames[2])));
                    food.setCalories((cursor.getInt(cursor.getColumnIndex(columnNames[3]))));
                    food.setImage((cursor.getString(cursor.getColumnIndex(columnNames[4]))));
                    foodList.add(food);
                } while (cursor.moveToNext());
            }
        }

        return foodList;
    }

    public List<Food> getFoodsBySearchKey(String searchKey) throws Exception {
        List<Food> foodList = new ArrayList<>();
        String[] columnNames = mContext.getResources().getStringArray(R.array.food_table_columns);

        String condition = "WHERE" + " food_name LIKE " + "'%" + searchKey + "%'";

        DatabaseOperations databaseOperations = new DatabaseOperations(mContext, DB_NAME);
        Cursor cursor = databaseOperations.retrieveAllFromDBCondition(FOOD_TABLE, condition);
        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    Food food = new Food();
                    food.setId(cursor.getInt(cursor.getColumnIndex(columnNames[0])));
                    food.setFoodName(cursor.getString(cursor.getColumnIndex(columnNames[1])));
                    food.setPortionSize(cursor.getInt(cursor.getColumnIndex(columnNames[2])));
                    food.setCalories((cursor.getInt(cursor.getColumnIndex(columnNames[3]))));
                    food.setImage((cursor.getString(cursor.getColumnIndex(columnNames[4]))));
                    foodList.add(food);
                } while (cursor.moveToNext());
            }
        }

        return foodList;
    }

    public void saveActivitiesToHistory(String activitieStr, int totalCalories, User user, int totalTime, String date) {
        String[] columnNames = mContext.getResources().getStringArray(R.array.history_column);

        DatabaseOperations databaseOperations = new DatabaseOperations(mContext, DB_NAME);

        String[] values = {activitieStr, user.getId(), String.valueOf(totalCalories), String.valueOf(totalTime), date, ""};

        try {
            databaseOperations.saveDataInDB(HISTORY_TABLE, columnNames, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<History> getAllHistory() throws Exception {
        List<History> historyList = new ArrayList<>();

        String[] columnNames = mContext.getResources().getStringArray(R.array.history_column_with_index);

        DatabaseOperations databaseOperations = new DatabaseOperations(mContext, DB_NAME);
        Cursor cursor = databaseOperations.retrieveAllFromDB(HISTORY_TABLE);
        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    History history = new History();
                    history.setId(cursor.getInt(cursor.getColumnIndex(columnNames[0])));
                    history.setActivitiesName(cursor.getString(cursor.getColumnIndex(columnNames[1])));
                    history.setUsername(cursor.getString(cursor.getColumnIndex(columnNames[2])));
                    history.setCalories((cursor.getString(cursor.getColumnIndex(columnNames[3]))));
                    history.setMinute((cursor.getString(cursor.getColumnIndex(columnNames[4]))));
                    history.setTrainningDate((cursor.getString(cursor.getColumnIndex(columnNames[5]))));
                    historyList.add(history);
                } while (cursor.moveToNext());
            }
        }

        return historyList;
    }
}
