package com.techdev.goalbuzz;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

/**
 * Created by Imran Khan on 1/25/2021.
 * Email : context.imran@gmail.com
 */
public class Main {

    public static void main(String[] args) {
        Main main = new Main();
        System.out.println(main.getTime("2021-02-17T18:00:00Z"));
    }

    public String getTime(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date value;
        String dt = "";
        try {
            value = formatter.parse(dateString);
            SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault());
            timeFormatter.setTimeZone(TimeZone.getDefault());
            dt = timeFormatter.format(Objects.requireNonNull(value));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dt;
    }
}
