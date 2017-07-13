package com.cse110.apk404.myCalendar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.*;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Debug;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//import android.app.Fragment;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.cse110.apk404.myCalendar.R;
import com.cse110.apk404.myCalendar.eventListHandler.CalendarEvent;
import com.cse110.apk404.myCalendar.eventListHandler.DynamicEvent;
import com.cse110.apk404.myCalendar.eventListHandler.EventListHandler;
import com.cse110.apk404.myCalendar.eventListHandler.StaticEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * This is a calender view fragment that extends the CalendarViewBaseFragment and
 * override onMonthChange() to pull events from the database to be displayed in the
 * MainActivity.
 */
public class CalendarViewFragment extends CalendarViewBaseFragment {

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {

        String finisehdEventColor = "#DCDCDC";

        // Populate the week view with some events.
        List<WeekViewEvent> event_list_UI = new ArrayList<WeekViewEvent>();

        ArrayList<? extends CalendarEvent> static_event_list = new ArrayList<>();
        ArrayList<? extends CalendarEvent> dynamic_event_list = new ArrayList<>();

        try {
            static_event_list = EventListHandler.getStaticList().getList();
            dynamic_event_list = EventListHandler.getDynamicList().getList();
            Log.d("EventLength", static_event_list.size() + "");

        } catch (Exception e) {
            Log.e("Error03", e.getMessage());
        }

        // Loop through the static events and populate event_list_UI when the loaded month
        // is the specified newMonth, if not, three of the same events would be loaded
        for (int i = 0; i < static_event_list.size(); i++) {
            StaticEvent event_temp = (StaticEvent) static_event_list.get(i);

            DateFormat time = new SimpleDateFormat("MM");
            int event_month = Integer.parseInt(time.format(event_temp.getStartTime().getTime()));

            // Set past event as finished
            if(event_temp.getEndTime().before(Calendar.getInstance()) && !event_temp.isFinished()) {
                event_temp.setFinished(true);
            }
            if (event_month == newMonth) {
                String description = "";
                description += event_temp.getName();
                if (!event_temp.getLocation().equals("")) {
                    description += " - " + event_temp.getLocation();
                }
                WeekViewEvent event = new WeekViewEvent(event_temp.getId(), description, event_temp.getStartTime(), event_temp.getEndTime());
                if (event_temp.isFinished()) {
                    event.setColor(Color.parseColor(finisehdEventColor));
                } else {
                    event.setColor(Color.parseColor(event_temp.getColor()));
                }
                event_list_UI.add(event);
            }
        }

        // Loop through dynamic event list and load UI.
        for (int i = 0; i < dynamic_event_list.size(); i++) {
            DynamicEvent event_temp = (DynamicEvent) dynamic_event_list.get(i);

            DateFormat time = new SimpleDateFormat("MM");
            int event_month = Integer.parseInt(time.format(event_temp.getStartTime().getTime()));

            // Set past event as finished
            if(event_temp.getDeadline().before(Calendar.getInstance()) && !event_temp.isFinished()) {
                event_temp.setFinished(true);
            }
            if (event_month == newMonth) {
                String description = "";
                description += event_temp.getName();
                if (!event_temp.getLocation().equals("")) {
                    description += " - " + event_temp.getLocation();
                }
                WeekViewEvent event = new WeekViewEvent(event_temp.getId(), description, event_temp.getStartTime(), event_temp.getEndTime());

                if (event_temp.isFinished()) {
                    event.setColor(Color.parseColor(finisehdEventColor));
                } else {
                    event.setColor(Color.parseColor(event_temp.getColor()));
                }
                event_list_UI.add(event);
            }
        }
//
//        Calendar startTime = Calendar.getInstance();
//        startTime.set(Calendar.HOUR_OF_DAY, 3);
//        startTime.set(Calendar.MINUTE, 30);
//        startTime.set(Calendar.MONTH, newMonth - 1);
//        startTime.set(Calendar.YEAR, newYear);
//        Calendar endTime = (Calendar) startTime.clone();
//        endTime.set(Calendar.HOUR_OF_DAY, 4);
//        endTime.set(Calendar.MINUTE, 30);
//        endTime.set(Calendar.MONTH, newMonth - 1);
//        WeekViewEvent event = new WeekViewEvent(2, getEventTitle(startTime), startTime, endTime);
//        Log.d("Event", 2 + "      " + getEventTitle(startTime));
//        event.setColor(getResources().getColor(R.color.event_color_02));
//        event_list_UI.add(event);
//
//        Log.d("newMonth", newMonth + "");

//
//        startTime = Calendar.getInstance();
//        startTime.set(Calendar.HOUR_OF_DAY, 4);
//        startTime.set(Calendar.MINUTE, 20);
//        startTime.set(Calendar.MONTH, newMonth - 1);
//        startTime.set(Calendar.YEAR, newYear);
//        endTime = (Calendar) startTime.clone();
//        endTime.set(Calendar.HOUR_OF_DAY, 5);
//        endTime.set(Calendar.MINUTE, 0);
//        event = new WeekViewEvent(3, getEventTitle(startTime), startTime, endTime);
//        event.setColor(getResources().getColor(R.color.event_color_03));
//        events.add(event);
//
//        startTime = Calendar.getInstance();
//        startTime.set(Calendar.HOUR_OF_DAY, 5);
//        startTime.set(Calendar.MINUTE, 30);
//        startTime.set(Calendar.MONTH, newMonth - 1);
//        startTime.set(Calendar.YEAR, newYear);
//        endTime = (Calendar) startTime.clone();
//        endTime.add(Calendar.HOUR_OF_DAY, 2);
//        endTime.set(Calendar.MONTH, newMonth - 1);
//        event = new WeekViewEvent(4, getEventTitle(startTime), startTime, endTime);
//        event.setColor(getResources().getColor(R.color.event_color_02));
//        events.add(event);
//
//        startTime = Calendar.getInstance();
//        startTime.set(Calendar.HOUR_OF_DAY, 5);
//        startTime.set(Calendar.MINUTE, 0);
//        startTime.set(Calendar.MONTH, newMonth - 1);
//        startTime.set(Calendar.YEAR, newYear);
//        startTime.add(Calendar.DATE, 1);
//        endTime = (Calendar) startTime.clone();
//        endTime.add(Calendar.HOUR_OF_DAY, 3);
//        endTime.set(Calendar.MONTH, newMonth - 1);
//        event = new WeekViewEvent(5, getEventTitle(startTime), startTime, endTime);
//        event.setColor(getResources().getColor(R.color.event_color_03));
//        events.add(event);
//
//        startTime = Calendar.getInstance();
//        startTime.set(Calendar.DAY_OF_MONTH, 15);
//        startTime.set(Calendar.HOUR_OF_DAY, 3);
//        startTime.set(Calendar.MINUTE, 0);
//        startTime.set(Calendar.MONTH, newMonth - 1);
//        startTime.set(Calendar.YEAR, newYear);
//        endTime = (Calendar) startTime.clone();
//        endTime.add(Calendar.HOUR_OF_DAY, 3);
//        event = new WeekViewEvent(6, getEventTitle(startTime), startTime, endTime);
//        event.setColor(getResources().getColor(R.color.event_color_04));
//        events.add(event);
//
//        startTime = Calendar.getInstance();
//        startTime.set(Calendar.DAY_OF_MONTH, 1);
//        startTime.set(Calendar.HOUR_OF_DAY, 3);
//        startTime.set(Calendar.MINUTE, 0);
//        startTime.set(Calendar.MONTH, newMonth - 1);
//        startTime.set(Calendar.YEAR, newYear);
//        endTime = (Calendar) startTime.clone();
//        endTime.add(Calendar.HOUR_OF_DAY, 3);
//        event = new WeekViewEvent(7, getEventTitle(startTime), startTime, endTime);
//        event.setColor(getResources().getColor(R.color.event_color_01));
//        events.add(event);
//
//        startTime = Calendar.getInstance();
//        startTime.set(Calendar.DAY_OF_MONTH, startTime.getActualMaximum(Calendar.DAY_OF_MONTH));
//        startTime.set(Calendar.HOUR_OF_DAY, 15);
//        startTime.set(Calendar.MINUTE, 0);
//        startTime.set(Calendar.MONTH, newMonth - 1);
//        startTime.set(Calendar.YEAR, newYear);
//        endTime = (Calendar) startTime.clone();
//        endTime.add(Calendar.HOUR_OF_DAY, 3);
//        event = new WeekViewEvent(8, getEventTitle(startTime), startTime, endTime);
//        event.setColor(getResources().getColor(R.color.event_color_02));
//        events.add(event);
//
//        //AllDay event
//        startTime = Calendar.getInstance();
//        startTime.set(Calendar.HOUR_OF_DAY, 0);
//        startTime.set(Calendar.MINUTE, 0);
//        startTime.set(Calendar.MONTH, newMonth - 1);
//        startTime.set(Calendar.YEAR, newYear);
//        endTime = (Calendar) startTime.clone();
//        endTime.add(Calendar.HOUR_OF_DAY, 23);
//        event = new WeekViewEvent(9, getEventTitle(startTime), null, startTime, endTime, true);
//        event.setColor(getResources().getColor(R.color.event_color_04));
//        events.add(event);
//        events.add(event);
//
//        startTime = Calendar.getInstance();
//        startTime.set(Calendar.DAY_OF_MONTH, 8);
//        startTime.set(Calendar.HOUR_OF_DAY, 2);
//        startTime.set(Calendar.MINUTE, 0);
//        startTime.set(Calendar.MONTH, newMonth - 1);
//        startTime.set(Calendar.YEAR, newYear);
//        endTime = (Calendar) startTime.clone();
//        endTime.set(Calendar.DAY_OF_MONTH, 10);
//        endTime.set(Calendar.HOUR_OF_DAY, 23);
//        event = new WeekViewEvent(10, getEventTitle(startTime), null, startTime, endTime, true);
//        event.setColor(getResources().getColor(R.color.event_color_03));
//        events.add(event);
//
//        // All day event until 00:00 next day
//        startTime = Calendar.getInstance();
//        startTime.set(Calendar.DAY_OF_MONTH, 10);
//        startTime.set(Calendar.HOUR_OF_DAY, 0);
//        startTime.set(Calendar.MINUTE, 0);
//        startTime.set(Calendar.SECOND, 0);
//        startTime.set(Calendar.MILLISECOND, 0);
//        startTime.set(Calendar.MONTH, newMonth - 1);
//        startTime.set(Calendar.YEAR, newYear);
//        endTime = (Calendar) startTime.clone();
//        endTime.set(Calendar.DAY_OF_MONTH, 11);
//        event = new WeekViewEvent(11, getEventTitle(startTime), null, startTime, endTime, true);
//        event.setColor(getResources().getColor(R.color.event_color_01));
//        events.add(event);


        return event_list_UI;
    }
}
