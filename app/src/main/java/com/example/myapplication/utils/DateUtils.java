package com.example.myapplication.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.myapplication.utils.StringUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

public class DateUtils {

    public static final String DATE_FORMAT_1 = "yyyy-MM-dd";
    public static final String DATE_FORMAT_2 = "dd/MM/yyyy";
    public static final String DATE_FORMAT_4 = " HH:mm dd/MM/yyyy";
    public static final String DATE_FORMAT_5 = "dd/MM/yyyy";
    public static final String DATE_FORMAT_7= "yyyy/MM/dd";
    public static final String DATE_FORMAT_15 = "yyyy";
    public static final String DATE_FORMAT_6 = " HH:mm";
    public static final String DATE_FORMAT_10 = "yyyy";
    public static final String DATE_FORMAT_11 = "MM yyyy";
    public static final String DATE_FORMAT12 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String DATE_FORMAT13 = "HH:mm:ss dd/MM/yyyy";
    public static final String DATE_FORMAT_14 = "MM yyyy";
    public static final String DATE_FORMAT_16 = "dd-MM";
    public static final String DATE_FORMAT_17 = "dd/MM";
    public static final Long MINUTE_MILLIS = TimeUnit.MINUTES.toMillis(1);
    public static final Long HOUR_MILLIS = TimeUnit.HOURS.toMillis(1);
    public static final Long DAY_MILLIS = TimeUnit.DAYS.toMillis(1);
    private static List<String> stringList = new ArrayList<>();

    public static String formatDate(String date, String initDateFormat, String endDateFormat) {
        if (StringUtil.isNullOrEmpty(date)) {
            return "";
        }
        @SuppressLint("SimpleDateFormat") Date initDate = null;
        try {
            initDate = new SimpleDateFormat(initDateFormat).parse(date);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat(endDateFormat);
            return formatter.format(initDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getCurrentTimeByPattern(String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
        return sdf.format(new Date());
    }



    public static String formatDateGMT(String date, String initDateFormat, String endDateFormat) {
        if (StringUtil.isNullOrEmpty(date)) {
            return "";
        }
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat(initDateFormat);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        @SuppressLint("SimpleDateFormat") Date initDate = null;
        try {
            initDate = simpleDateFormat.parse(date);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat(endDateFormat);
            return formatter.format(initDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String convertMillisecondToTime(long millisecond, String outPattern) {
        Date date = new Date(millisecond);
        DateFormat formatter = new SimpleDateFormat(outPattern, Locale.getDefault());
        return formatter.format(date);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static Calendar convertStringToCalender(String date, String format)  {
        Calendar cal = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            cal.setTime(Objects.requireNonNull(sdf.parse(date)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal;

    }

    public static String convertCalendarToString(Calendar calendar, String outPattern){
        calendar.add(Calendar.DATE, 0);
        Date date = calendar.getTime();
        SimpleDateFormat format1 = new SimpleDateFormat(outPattern);
        String inActiveDate = null;
        inActiveDate = format1.format(date);

        if (!StringUtil.isNullOrEmpty(inActiveDate)) {
            return inActiveDate;
        }
        return "";
    }

    public static String getCurrentDay(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 0);
        Date date = cal.getTime();
        SimpleDateFormat format1 = new SimpleDateFormat(DATE_FORMAT12);
        String inActiveDate = null;
        inActiveDate = format1.format(date);

        if (!StringUtil.isNullOrEmpty(inActiveDate)) {
            return inActiveDate;
        }
        return "";
    }

    /**
     * check is @param timeInMilli smaller than current time 1 hour
     *
     * @param timeInMilli
     * @return
     */
    public static boolean isSmallerThanOneHour(long timeInMilli) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        return currentTime - timeInMilli < (60 * 60 * 1000);
    }

    public static String getTimGo(Calendar calendar, String init) {
        if (calendar == null) {
            return null;
        }
        Long now = Calendar.getInstance().getTimeInMillis();
        Long time = calendar.getTimeInMillis();
        Long diff = now - time;
        if (diff < TimeUnit.MINUTES.toMillis(1)) {
            return "Vừa xong";
        } else if (diff < 60 * MINUTE_MILLIS) {
            return (diff / MINUTE_MILLIS) + " phút trước";
        } else if (diff < 24 * HOUR_MILLIS) {
            return (diff / HOUR_MILLIS) + " giờ trước";
        } else if (diff < 7 * 24 * HOUR_MILLIS) {
            return (diff / DAY_MILLIS) + " ngày trước";
        } else {
            return formatDateGMT(init, DATE_FORMAT12, DATE_FORMAT_2);
        }
    }

    public static String previousDateString(int amount)
            throws ParseException {
        String dateString = getCurrentDay();
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_1);
        Date myDate = dateFormat.parse(dateString);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(myDate);
        calendar.add(Calendar.DAY_OF_YEAR,-amount);
        Date previousDate = calendar.getTime();
        return formatDate(dateFormat.format(previousDate), DATE_FORMAT_1, DATE_FORMAT_17);
    }

    public static String getDayAfterCurrent(String pattern, int amountDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -amountDay);
        Date newDate = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
        return sdf.format(newDate);
    }

    static int count = Constant.DAY_NUMBER_TO_VIEW_DATA;
    public static List<String> getLast5day() throws ParseException {
        count--;
        if (count >= 0) {
            stringList.add(previousDateString(count));
            getLast5day();
        }
        return stringList;
    }


}
