package com.example.calendarproductia;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Event {

    public static ArrayList<Event> eventsList = new ArrayList<>();
    private int id;
    public static String EDIT_EVENT = "edit_event";
    private String category;

    public static List<Event> eventsForDate(LocalDate date) {
        SQLiteDBHandler sqLiteDBHandler = SQLiteDBHandler.instanceDB();
        return sqLiteDBHandler.getAllEventsForDate(date);
    }

    public static List<Event> allEvents() {
        SQLiteDBHandler sqLiteDBHandler = SQLiteDBHandler.instanceDB();
        return sqLiteDBHandler.getAllEvents();
    }

    public static List<Event> eventsDateTime(LocalDate date, LocalTime time) {

        SQLiteDBHandler sqLiteDBHandler = SQLiteDBHandler.instanceDB();
        List<Event> events = new ArrayList<>();

        for (Event event : sqLiteDBHandler.getAllEventsForDate(date)) //event from eventsList
        {
            int eventHour = event.time.getHour();
            int cellHour = time.getHour();
            if (event.getDate().equals(date) && eventHour == cellHour)
                events.add(event);
        }
        return events;
    }

    private String name;
    private LocalDate date;
    private LocalTime time;

    public Event(int id, String name, LocalDate date, LocalTime time, String category) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.category = category;
    }

    public static Event getEventID(int passedEventID, LocalDate date) {

        for (Event event : eventsForDate(date)) {
            if (event.getId() == passedEventID)
                return event;
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}