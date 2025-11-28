package com.example.calendarproductia;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class CalendarAdapter extends RecyclerView.Adapter<CalendarView>
{
    private final int greenHighlightColor = Color.rgb(196, 255, 212);
    private final ArrayList<LocalDate> days;
    private final OnItemListener onItemListener;

    public CalendarAdapter(ArrayList<LocalDate> days, OnItemListener onItemListener)
    {
        this.days = days;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public CalendarView onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cell_calendar, parent, false);
        ViewGroup.LayoutParams layoutParameters = view.getLayoutParams();
        if(days.size() > 15) //month view
            layoutParameters.height = (int) (parent.getHeight() * 0.166666); //cell_event as 0.1(6) of the whole height
        else // week view
            layoutParameters.height = parent.getHeight();

        return new CalendarView(view, onItemListener, days);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarView view, int position)
    {
        final LocalDate date = days.get(position);

        view.monthDay.setText(String.valueOf(date.getDayOfMonth()));

        if (date.equals(CalendarUtilities.chosenDate)) {
            view.generalView.setBackgroundColor(Color.LTGRAY);
        }

        if (date.getMonth().equals(CalendarUtilities.chosenDate.getMonth())) {
            view.monthDay.setTextColor(Color.BLACK);
        } else {
            view.monthDay.setTextColor(Color.LTGRAY);
        }

        if (date.isEqual(LocalDate.now())) {
            view.generalView.setBackgroundColor(greenHighlightColor);
        }

        List<Event> eventsDateArr = Event.eventsForDate(date);
        if (!eventsDateArr.isEmpty()) {
            view.eventIcon.setVisibility(View.VISIBLE);
            int color;
            switch (eventsDateArr.size()) { //checks each case separately to avoid too many if statements
                case 1:
                    color = Color.rgb(237, 14, 100);
                    break;
                case 2:
                    color = Color.rgb(90, 3, 184);
                    break;
                case 3:
                    color = Color.rgb(72, 191, 145);
                    break;
                default:
                    color = Color.rgb(255, 219, 88);
                    break;
            }
            view.eventIcon.setColorFilter(color);
        } else {
            view.eventIcon.setVisibility(View.GONE); // Hide eventIcon if no events
        }
    }

    @Override
    public int getItemCount()
    {
        return days.size();
    }

    public interface OnItemListener
    {
        void onItemClick(int position, LocalDate date);
    }
}