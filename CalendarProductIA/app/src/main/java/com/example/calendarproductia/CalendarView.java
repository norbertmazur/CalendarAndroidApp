package com.example.calendarproductia;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarView extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final ArrayList<LocalDate> days;
    public final View generalView;
    public final ImageView eventIcon;
    public final TextView monthDay;
    private final CalendarAdapter.OnItemListener onItemListener;

    public CalendarView(@NonNull View itemView, CalendarAdapter.OnItemListener onItemListener, ArrayList<LocalDate> days) {
        super(itemView);
        generalView = itemView.findViewById(R.id.generalView);
        monthDay = itemView.findViewById(R.id.TV_cellDay);
        eventIcon = itemView.findViewById(R.id.eventIcon);
        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);
        this.days = days;
    }

    @Override
    public void onClick(View view) {
        onItemListener.onItemClick(getAdapterPosition(), days.get(getAdapterPosition()));
    }
}