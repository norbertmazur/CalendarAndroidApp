package com.example.calendarproductia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.example.calendarproductia.CalendarUtilities.weekDaysArray;
import static com.example.calendarproductia.CalendarUtilities.monthYearFromDate;
import static com.example.calendarproductia.CalendarUtilities.chosenDate;

public class WeekView extends AppCompatActivity implements CalendarAdapter.OnItemListener {
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekview);
        initWidgets();
        loadDB();
        setWeekView();
        setOnClickListener();
    }

    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.RV_calendar);
        monthYearText = findViewById(R.id.TV_monthYear);
        eventListView = findViewById(R.id.LV_event);
    }

    public void setWeekView() {
        monthYearText.setText(monthYearFromDate(chosenDate));//null reference - but how?
        ArrayList<LocalDate> days = weekDaysArray(chosenDate);
        RecyclerView.LayoutManager layoutMng = new GridLayoutManager(getApplicationContext(), 7);
        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        calendarRecyclerView.setLayoutManager(layoutMng);
        calendarRecyclerView.setAdapter(calendarAdapter);
        setEventAdapter();
    }

    private void loadDB() {
        SQLiteDBHandler sqLiteDBHandler = SQLiteDBHandler.instanceDB();
        sqLiteDBHandler.populateEventsListView();
    }

    public void previousWeek(View view) {
        chosenDate = chosenDate.minusWeeks(1);
        setWeekView();
    }

    public void nextWeek(View view) {
        chosenDate = chosenDate.plusWeeks(1);
        setWeekView();
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        chosenDate = date;
        setWeekView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setEventAdapter();
    }

    private void setEventAdapter() {
        List<Event> dailyEvents = Event.eventsForDate(CalendarUtilities.chosenDate);
        Collections.sort(dailyEvents, new Comparator<Event>() {
            @Override
            public int compare(Event event1, Event event2) {
                // Compare event times
                return event1.getTime().compareTo(event2.getTime());
            }
        });
        EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), dailyEvents, false);
        eventListView.setAdapter(eventAdapter);
    }

    private void setOnClickListener() {
        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event chosenEvent = (Event) eventListView.getItemAtPosition(position);
                Intent editEventIntent = new Intent(getApplicationContext(), EditEvent.class);
                editEventIntent.putExtra(Event.EDIT_EVENT, chosenEvent.getId());
                startActivity(editEventIntent);
            }
        });
    }

    public void newEventAction(View view) {
        startActivity(new Intent(this, EditEvent.class));
    }

    public void daily(View view) {
        startActivity(new Intent(this, DailyView.class));
    }

    public void goBack(View view) {
        onBackPressed();
    }

    public void upcoming(View view) {
        startActivity(new Intent(this, UpcomingEvents.class));
    }
}
