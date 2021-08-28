package com.techdev.goalbuzz.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class DateFormatter {
    private static final String TAG = "DateFormatter";
    private static DateFormatter instance;

    public static DateFormatter getInstance(){
        if (instance == null){
            instance = new DateFormatter();
        }
        return instance;
    }

    public String getTime(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date value = null;
        String dt = "";
        try {
            value = formatter.parse(dateString);
            SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            timeFormatter.setTimeZone(TimeZone.getDefault());
            dt = timeFormatter.format(Objects.requireNonNull(value));
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "getTime: " + e.getMessage());
        }
        return dt;
    }

    public String getDate(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date value = null;
        String dt = "";
        try {
            value = formatter.parse(dateString);
            SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            timeFormatter.setTimeZone(TimeZone.getDefault());
            dt = timeFormatter.format(Objects.requireNonNull(value));
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "getTime: " + e.getMessage());
        }
        return dt;
    }

    public Date getDateTime(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date value = null;
        try {
            value = formatter.parse(dateString);
            if (value != null){
                SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault());
                timeFormatter.setTimeZone(TimeZone.getDefault());
                value = timeFormatter.parse(timeFormatter.format(value));
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "getDateTime: " + e.getMessage());
        }
        return value;
    }

    public String getDateTimeToString(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date value = null;
        String dt = "";
        try {
            value = formatter.parse(dateString);
            SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault());
            timeFormatter.setTimeZone(TimeZone.getDefault());
            dt = timeFormatter.format(Objects.requireNonNull(value));
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "getTime: " + e.getMessage());
        }
        return dt;
    }

    public String getDay(String date){
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date d = format.parse(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(Objects.requireNonNull(d));
            SimpleDateFormat month_date = new SimpleDateFormat("MMM", Locale.getDefault());
            String month_name = month_date.format(cal.getTime());
//            String day = (String) DateFormat.format("EEEE", format.parse(date));
            String dt = String.format("%s", cal.get(Calendar.DAY_OF_MONTH));
//            String yr = String.format("%s", cal.get(Calendar.YEAR));
            return  String.format("%s %s", dt,month_name);
        } catch (ParseException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public long getDelay(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault());
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date value = null;
        String dt = "";
        try {
            value = formatter.parse(dateString);
            timeFormatter.setTimeZone(TimeZone.getDefault());
            dt = timeFormatter.format(Objects.requireNonNull(value));
            return Objects.requireNonNull(timeFormatter.parse(dt)).getTime() - Calendar.getInstance().getTime().getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "getDateTimeInDate: " + e.getMessage());
        }
        return 0;
    }

    public boolean hasTimeCrossedYet(String date){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 5);
        return  calendar.getTime().after(getDateTime(date));
    }
}
