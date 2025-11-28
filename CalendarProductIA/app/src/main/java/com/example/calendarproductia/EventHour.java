package com.example.calendarproductia;

import java.time.LocalTime;
import java.util.List;

class EventHour {
    LocalTime time;
    List<Event> events;

    public EventHour(LocalTime time, List<Event> events) {
        this.time = time;
        this.events = events;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}