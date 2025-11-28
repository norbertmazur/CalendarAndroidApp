package com.example.calendarproductia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.time.LocalDate;
import java.util.ArrayList;

import static com.example.calendarproductia.CalendarUtilities.monthDaysArray;
import static com.example.calendarproductia.CalendarUtilities.monthYearFromDate;

public class MainActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener {
    private RecyclerView calendarRecyclerView;
    private TextView monthYearText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidgets();
        loadDB();
        CalendarUtilities.chosenDate = LocalDate.now();
        setMonthView();
    }

    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.RV_calendar);
        monthYearText = findViewById(R.id.TV_monthYear);
    }
    private void loadDB() {
        SQLiteDBHandler sqLiteDBHandler = SQLiteDBHandler.instanceDB();
        sqLiteDBHandler.populateEventsListView();
    }

    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(CalendarUtilities.chosenDate));
        ArrayList<LocalDate> monthDays = monthDaysArray();

        CalendarAdapter calendarAdapter = new CalendarAdapter(monthDays, this);
        RecyclerView.LayoutManager layoutMng = new GridLayoutManager(getApplicationContext(), 7); //spanCount:7 means that there is going to be 7 columns in the RecyclerView
        calendarRecyclerView.setLayoutManager(layoutMng);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    public void previousMonth(View view) //works for back month button | it selects the variable selectedDate and decreases its month by one e.g. if its March by clicking the back month button it becomes February
    {
        CalendarUtilities.chosenDate = CalendarUtilities.chosenDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonth(View view) //works for next month button | it selects the variable selectedDate and increases its month by one e.g. if its March by clicking the next month button it becomes April
    {
        CalendarUtilities.chosenDate = CalendarUtilities.chosenDate.plusMonths(1);
        setMonthView();
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        if (date != null) {
            CalendarUtilities.chosenDate = date;
            setMonthView();
        }
    }
    public void weeklyAction(View view) {
        startActivity(new Intent(this, WeekView.class));
    }

    public void upcoming(View view) {
        startActivity(new Intent(this, UpcomingEvents.class));
    }
}


