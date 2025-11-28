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

import java.util.List;

public class EventAdapter extends ArrayAdapter<Event> {
    private boolean showTimeDate;

    public EventAdapter(@NonNull Context context, List<Event> events, boolean showTimeDate) {
        super(context, 0, events);
        this.showTimeDate = showTimeDate;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Event event = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cell_event, parent, false);

        TextView TV_eventCell = convertView.findViewById(R.id.TV_eventCellName);

        String eventTitle;
        if (showTimeDate) {
            eventTitle = event.getName() + " " + CalendarUtilities.formattedTime(event.getTime()) + " " + CalendarUtilities.formattedDate(event.getDate());
        } else {
            eventTitle = event.getName() + " " + CalendarUtilities.formattedTime(event.getTime());
        }
        TV_eventCell.setText(eventTitle);

        String category = event.getCategory();
        switch (category) {
            case "Work":
                TV_eventCell.setBackgroundColor(Color.rgb(0, 0, 128));
                break;
            case "Family":
                TV_eventCell.setBackgroundColor(Color.rgb(150, 123, 182));
                break;
            case "Health":
                TV_eventCell.setBackgroundColor(Color.rgb(142, 250, 117));
                break;
            case "Sports":
                TV_eventCell.setBackgroundColor(Color.rgb(255, 211, 0));
                break;
            case "Fun":
                TV_eventCell.setBackgroundColor(Color.rgb(112, 2, 220));
                break;
            case "Deadline":
                TV_eventCell.setBackgroundColor(Color.rgb(100, 0, 0));
                break;
            default:  //handles the case of default
                TV_eventCell.setBackgroundColor(Color.rgb(0, 191, 196));
                break;
        }
        return convertView;
    }
}