package com.example.calendarproductia;

import static com.example.calendarproductia.CalendarUtilities.chosenDate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DailyView extends AppCompatActivity {

    private TextView monthDayText;
    private TextView weekDayTV;
    private ListView hourListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadDB();
        setContentView(R.layout.activity_dailyview);
        initWidgets();
    }

    private void initWidgets() {
        monthDayText = findViewById(R.id.monthDayText);
        weekDayTV = findViewById(R.id.dayOfWeekTV);
        hourListView = findViewById(R.id.hourListView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDayView();
    }

    private void setHour() {
        HourAdapter adapterHours = new HourAdapter(getApplicationContext(), hourEventListArray());
        hourListView.setAdapter(adapterHours);
    }
    private void setDayView() {
        monthDayText.setText(CalendarUtilities.monthDayFromDate(chosenDate));
        String weekDay = chosenDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
        weekDayTV.setText(weekDay);
        setHour();
    }
    private List<EventHour> hourEventListArray() {
        ArrayList<EventHour> list = new ArrayList<>();

        for (int hour = 0; hour < 24; hour++) {
            LocalTime time = LocalTime.of(hour, 0);
            List<Event> events = Event.eventsDateTime(chosenDate, time);
            EventHour eventHour = new EventHour(time, events);
            list.add(eventHour);
        }
        return list;
    }

    public void previousDayAction(View view) {
        chosenDate = chosenDate.minusDays(1);
        setDayView();
    }

    public void nextDayAction(View view) {
        chosenDate = chosenDate.plusDays(1);
        setDayView();
    }

    private void loadDB() {
        SQLiteDBHandler sqLiteDBHandler = SQLiteDBHandler.instanceDB();
        sqLiteDBHandler.populateEventsListView();
    }

    public void newEventAction(View view) {
        startActivity(new Intent(this, EditEvent.class));
    }

    public void goBack(View view) {
        onBackPressed();
    }
}