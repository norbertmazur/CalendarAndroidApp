package com.example.calendarproductia;

import static com.example.calendarproductia.CalendarUtilities.chosenDate;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class EditEvent extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private ArrayList<CategoryItem> CategoryList;
    private com.example.calendarproductia.CategoryAdapter CategoryAdapter;
    private Event chosenEvent;
    private Button deleteBTN;

    private EditText eventNameET;
    private TextView eventDateTV, eventTimeTV;
    private DatePickerDialog.OnDateSetListener setListener;
    private DatePickerDialog datePickerDialog;
    private LocalTime time;
    public String textCyclical;
    public String clickedCategoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editevent);
        initWidgets();
        checkEdit();
        time = LocalTime.now();
        eventDateTV.setText("Date: " + CalendarUtilities.formattedDate(chosenDate));
        eventTimeTV.setText("Time: " + CalendarUtilities.formattedTime(time));
        initCategoryList();
        initSpinnerCategories();
        initSpinnerCyclical();
    }
    private void initSpinnerCategories() {
        Spinner spinnerCategories = findViewById(R.id.categories);
        CategoryAdapter = new CategoryAdapter(this, CategoryList);
        spinnerCategories.setAdapter(CategoryAdapter);
        spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CategoryItem clickedItem = (CategoryItem) parent.getItemAtPosition(position);
                clickedCategoryName = clickedItem.getCategoryName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void initSpinnerCyclical() {
        Spinner spinnerCyclical = findViewById(R.id.cyclical);
        ArrayAdapter<CharSequence> adapterCyclical = ArrayAdapter.createFromResource(this,
                R.array.cyclical, android.R.layout.simple_spinner_item);
        adapterCyclical.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCyclical.setAdapter(adapterCyclical);
        spinnerCyclical.setOnItemSelectedListener(this);
    }

        private void initWidgets() {
        eventNameET = findViewById(R.id.ET_eventName);
        eventDateTV = findViewById(R.id.TV_eventDate);
        eventTimeTV = findViewById(R.id.TV_eventTime);
        deleteBTN = findViewById(R.id.deleteEventBTN);
    }

    private void checkEdit() {
        Intent prevIntent = getIntent();
        int passedEventID = prevIntent.getIntExtra(Event.EDIT_EVENT, -1);
        chosenEvent = Event.getEventID(passedEventID, chosenDate);
        if (chosenEvent != null) {
            eventNameET.setText(chosenEvent.getName());
            eventDateTV.setText(chosenEvent.getDate().toString());
            eventTimeTV.setText(chosenEvent.getTime().toString());
        } else {
            deleteBTN.setVisibility(View.INVISIBLE);
        }
    }

    private void initCategoryList() {
        CategoryList = new ArrayList<>();
        CategoryList.add(new CategoryItem("Default", R.drawable.baseline_circle_24));
        CategoryList.add(new CategoryItem("Work", R.drawable.baseline_work_24));
        CategoryList.add(new CategoryItem("Family", R.drawable.baseline_family_restroom_24));
        CategoryList.add(new CategoryItem("Health", R.drawable.baseline_health_and_safety_24));
        CategoryList.add(new CategoryItem("Sports", R.drawable.baseline_sports_volleyball_24));
        CategoryList.add(new CategoryItem("Fun", R.drawable.baseline_music_note_24));
        CategoryList.add(new CategoryItem("Deadline", R.drawable.baseline_timer_24));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        textCyclical = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void showDatePickerDialog(View view) {
        int year = chosenDate.getYear();
        int month = chosenDate.getMonthValue() - 1; // Month is 0-based
        int day = chosenDate.getDayOfMonth();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view1, year1, month1, dayOfMonth) -> {
                    chosenDate = LocalDate.of(year1, month1 + 1, dayOfMonth);
                    eventDateTV.setText("Date: " + CalendarUtilities.formattedDate(chosenDate));
                }, year, month, day);
        datePickerDialog.show();
    }

    public void showTimePickerDialog(View view) {
        // Create a TimePickerDialog and show it
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view1, hourOfDay, minute) -> {
                    // Handle the selected time here
                    time = LocalTime.of(hourOfDay, minute);
                    eventTimeTV.setText("Time: " + CalendarUtilities.formattedTime(time));
                }, time.getHour(), time.getMinute(), true);
        timePickerDialog.show();
    }

    public void saveEvent(View view) {
        SQLiteDBHandler sqLiteDBHandler = SQLiteDBHandler.instanceDB();
        LocalTime time_event = LocalTime.parse(CalendarUtilities.formattedTime(time));
        String eventName = eventNameET.getText().toString();

        //adding event if no event clicked
        if (chosenEvent == null) {
            int id = Event.eventsForDate(chosenDate).size();
            if (chosenDate.isBefore(LocalDate.now())) {
                // The event is set for the past - based on the day
                Toast.makeText(this, "Cannot set events in the past. Try again!",
                        Toast.LENGTH_SHORT).show();
            } else if (chosenDate.isEqual(LocalDate.now()) && time_event.isBefore(LocalTime.now())) {
                //The event is set for the past based on time
                Toast.makeText(this, "Cannot set events in the past. Try again!",
                        Toast.LENGTH_SHORT).show();
            } else {
                if (eventName.isEmpty()) {
                    Toast.makeText(this, "Set the event name!", Toast.LENGTH_SHORT).show();
                } else if (eventName.length() > 20) {
                    Toast.makeText(this, "The event name must not be over 12 characters!", Toast.LENGTH_SHORT).show();
                } else {
                    Event newEvent = new Event(id, eventName, chosenDate, time_event, clickedCategoryName);
                    sqLiteDBHandler.addEvent(newEvent);
                    if (textCyclical.equals("Daily")) {
                        LocalDate nextDay = chosenDate.plusDays(1);
                        while (nextDay.isBefore(LocalDate.now().plusYears(1))) {
                            Event dailyEvent = new Event(id, eventName, nextDay, time_event, clickedCategoryName);
                            sqLiteDBHandler.addEvent(dailyEvent);
                            nextDay = nextDay.plusDays(1);
                        }
                    } else if (textCyclical.equals("Weekly")) {
                        LocalDate nextWeek = chosenDate.plusWeeks(1);
                        while (nextWeek.isBefore(LocalDate.now().plusYears(1))) {
                            Event weeklyEvent = new Event(id, eventName, nextWeek, time_event, clickedCategoryName);
                            sqLiteDBHandler.addEvent(weeklyEvent);
                            nextWeek = nextWeek.plusWeeks(1);
                        }
                    } else if (textCyclical.equals("Monthly")) {
                        LocalDate nextMonth = chosenDate.plusMonths(1);
                        while (nextMonth.isBefore(LocalDate.now().plusYears(1))) {
                            Event monthlyEvent = new Event(id, eventName, nextMonth, time_event, clickedCategoryName);
                            sqLiteDBHandler.addEvent(monthlyEvent);
                            nextMonth = nextMonth.plusMonths(1);
                        }
                    } else if (textCyclical.equals("Annual")) {
                        LocalDate nextYear = chosenDate.plusYears(1);
                        while (nextYear.isBefore(LocalDate.now().plusYears(10))) {
                            Event annualEvent = new Event(id, eventName, nextYear, time_event, clickedCategoryName);
                            sqLiteDBHandler.addEvent(annualEvent);
                            nextYear = nextYear.plusYears(1);
                        }
                    }
                    finish();
                }
            }
        }
        //updating event
        else {
            chosenEvent.setName(eventName);
            chosenEvent.setTime(time_event);
            chosenEvent.setDate(chosenDate);
            chosenEvent.setCategory(clickedCategoryName);
            sqLiteDBHandler.updateEvent(chosenEvent);
            finish();
        }
    }

    public void goBack(View view) {
        onBackPressed();
    }

    public void deleteEvent(View view) {
        SQLiteDBHandler sqLiteDBHandler = SQLiteDBHandler.instanceDB();
        sqLiteDBHandler.deleteEvent(chosenEvent);
        finish();
    }


}
