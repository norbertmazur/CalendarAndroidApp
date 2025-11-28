package com.example.calendarproductia;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CalendarUtilities {
    public static LocalDate chosenDate;

    public static String formattedDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        return date.format(formatter);
    }

    public static String formattedTime(LocalTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return time.format(formatter);
    }

    public static String monthYearFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy");
        return date.format(formatter);
    }

    public static String monthDayFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d");
        return date.format(formatter);
    }

    public static ArrayList<LocalDate> monthDaysArray() {
        ArrayList<LocalDate> monthDaysArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(chosenDate);
        int monthDays = yearMonth.lengthOfMonth(); //how many days in a month
        LocalDate previousMonth = chosenDate.minusMonths(1);
        LocalDate nextMonth = chosenDate.plusMonths(1);
        YearMonth previousYearMonth = YearMonth.from(previousMonth);
        int previousMonthDays = previousYearMonth.lengthOfMonth();
        LocalDate firstOfMonth = chosenDate.withDayOfMonth(1); //first day of month
        int weekDay = firstOfMonth.getDayOfWeek().getValue();
        for (int i = 1; i <= 42; i++) {
            if (i <= weekDay)
                monthDaysArray.add(LocalDate.of(previousMonth.getYear(), previousMonth.getMonth(), previousMonthDays + i - weekDay));
            else if (i > monthDays + weekDay)
                monthDaysArray.add(LocalDate.of(nextMonth.getYear(), nextMonth.getMonth(), i - weekDay - monthDays));
            else
                monthDaysArray.add(LocalDate.of(chosenDate.getYear(), chosenDate.getMonth(), i - weekDay));
        }
        return monthDaysArray;
    }

    public static ArrayList<LocalDate> weekDaysArray(LocalDate chosenDate) {
        ArrayList<LocalDate> days = new ArrayList<>();
        LocalDate currentDate = findSunday(chosenDate);
        LocalDate endDate = currentDate.plusWeeks(1);
        while (currentDate.isBefore(endDate)) {
            days.add(currentDate);
            currentDate = currentDate.plusDays(1);
        }
        return days;
    }

    private static LocalDate findSunday(LocalDate currentDate) {
        LocalDate weekBack = currentDate.minusWeeks(1);
        while (currentDate.isAfter(weekBack)) {
            if (currentDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                return currentDate;
            }
            currentDate = currentDate.minusDays(1);
        }
        return null;
    }
}