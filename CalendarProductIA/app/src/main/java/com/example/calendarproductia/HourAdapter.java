package com.example.calendarproductia;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.LocalTime;
import java.util.List;

public class HourAdapter extends ArrayAdapter<EventHour> {
    public HourAdapter(@NonNull Context context, List<EventHour> eventHours) {
        super(context, 0, eventHours);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        EventHour event = getItem(position);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cell_hour, parent, false);

        setHour(convertView, event.time);
        setEvents(convertView, event.events);

        return convertView;
    }

    private void setHour(View convertView, LocalTime time) {
        TextView timeTV = convertView.findViewById(R.id.timeTV);
        timeTV.setText(CalendarUtilities.formattedTime(time));
    }

    private void setEvents(View convertView, List<Event> events) {
        TextView event1 = convertView.findViewById(R.id.event1);
        TextView event2 = convertView.findViewById(R.id.event2);
        TextView event3 = convertView.findViewById(R.id.event3);

        switch (events.size()) {
            case 0:
                hideEvent(event1);
                hideEvent(event2);
                hideEvent(event3);
                break;
            case 1:
                setEvent(event1, events.get(0));
                hideEvent(event2);
                hideEvent(event3);
                break;
            case 2:
                setEvent(event1, events.get(0));
                setEvent(event2, events.get(1));
                hideEvent(event3);
                break;
            case 3:
                setEvent(event1, events.get(0));
                setEvent(event2, events.get(1));
                setEvent(event3, events.get(2));
                break;
            default:
                setEvent(event1, events.get(0));
                setEvent(event2, events.get(1));
                event3.setVisibility(View.VISIBLE);
                String eventsHidden = String.valueOf(events.size() - 2);
                eventsHidden += " More events";
                event3.setText(eventsHidden);
                break;
        }
    }

    private void setEvent(TextView textView, Event event) {
        textView.setText(event.getName());
        String category = event.getCategory();
        switch (category) {
            case "Work":
                textView.setBackgroundColor(Color.rgb(0, 0, 128));
                break;
            case "Family":
                textView.setBackgroundColor(Color.rgb(150, 123, 182));
                break;
            case "Health":
                textView.setBackgroundColor(Color.rgb(142, 250, 117));
                break;
            case "Sports":
                textView.setBackgroundColor(Color.rgb(255, 211, 0));
                break;
            case "Fun":
                textView.setBackgroundColor(Color.rgb(112, 2, 220));
                break;
            case "Deadline":
                textView.setBackgroundColor(Color.rgb(100, 0, 0));
                break;
            default:  //handles the case of default
                textView.setBackgroundColor(Color.rgb(0, 191, 196));
                break;
        }
        textView.setVisibility(View.VISIBLE);
    }

    private void hideEvent(TextView tv) {
        tv.setVisibility(View.INVISIBLE);
    }

}













