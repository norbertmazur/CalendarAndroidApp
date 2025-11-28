package com.example.calendarproductia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UpcomingEvents extends AppCompatActivity {
    private ListView upcomingEventsLV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcomingevents);
        initWidgets();
        loadDB();
        setEventAdapter();
        setOnClickListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setEventAdapter();
    }

    private void initWidgets() {
        upcomingEventsLV = findViewById(R.id.LV_upcomingEvents);
    }

    private void setEventAdapter() {
        List<Event> upcomingEvents = Event.allEvents();
        Collections.sort(upcomingEvents, new Comparator<Event>() {
            @Override
            public int compare(Event event1, Event event2) {
                // Compare event dates
                int dateComparison = event1.getDate().compareTo(event2.getDate());

                if (dateComparison == 0) {
                    // If dates are the same, compare event times
                    return event1.getTime().compareTo(event2.getTime());
                } else {
                    return dateComparison;
                }
            }
        });
        EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), upcomingEvents, true);
        upcomingEventsLV.setAdapter(eventAdapter);
    }

    private void setOnClickListener() {
        upcomingEventsLV.setOnItemClickListener((parent, view, position, id) -> {
            Event chosenEvent = (Event) upcomingEventsLV.getItemAtPosition(position);
            Intent editEventIntent = new Intent(getApplicationContext(), EditEvent.class);
            editEventIntent.putExtra(Event.EDIT_EVENT, chosenEvent.getId());
            startActivity(editEventIntent);
        });
    }

    private void loadDB() {
        SQLiteDBHandler sqLiteDBHandler = SQLiteDBHandler.instanceDB();
        sqLiteDBHandler.populateEventsListView();
    }

    public void goBack(View view) {
        onBackPressed();
    }
}
