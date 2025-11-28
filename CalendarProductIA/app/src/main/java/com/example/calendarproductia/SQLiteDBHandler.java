package com.example.calendarproductia;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SQLiteDBHandler extends SQLiteOpenHelper {

    //info about the database
    private static SQLiteDBHandler dbHandler;
    private static final String DATABASE_NAME = "CalendarDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_EVENTS = "Events";
    private static final String ID_EVENTS_FIELD = "id";
    private static final String NAME_FIELD = "name";
    private static final String DATE_FIELD = "date";
    private static final String TIME_FIELD = "time";
    private static final String CATEGORY_FIELD = "category";

    private static final String TABLE_USERS = "Users";
    private static final String ID_USERS = "id";
    private static final String USERNAME_FIELD = "username";
    private static final String PASSWORD_FIELD = "password";
    @SuppressLint("SimpleDateFormat")
    private static final DateFormat formatDate = new SimpleDateFormat("MM-dd-yyyy");
    @SuppressLint("SimpleDateFormat")
    private static final DateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
    @SuppressLint("SimpleDateFormat")
    private static final DateTimeFormatter formatLocalDate = DateTimeFormatter.ofPattern("MM-dd-yyyy");//helpful when managing deleted
    @SuppressLint("SimpleDateFormat")
    private static final DateTimeFormatter formatLocalTime = DateTimeFormatter.ofPattern("HH:mm:ss");

    public SQLiteDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        dbHandler = this;
    }

    public static SQLiteDBHandler instanceDB() {
        return dbHandler;
    }

    @Override
    public void onCreate(SQLiteDatabase dbHandler) {
        StringBuilder events;
        StringBuilder user;
        events = new StringBuilder()
                .append("CREATE TABLE ")
                .append(TABLE_EVENTS)
                .append("(")
                .append(ID_EVENTS_FIELD)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(NAME_FIELD)
                .append(" TEXT, ")
                .append(DATE_FIELD)
                .append(" TEXT, ")
                .append(TIME_FIELD)
                .append(" TEXT, ")
                .append(CATEGORY_FIELD)
                .append(" TEXT)");

        dbHandler.execSQL(events.toString());

        user = new StringBuilder()
                .append("CREATE TABLE ")
                .append(TABLE_USERS)
                .append("(")
                .append(ID_USERS)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(USERNAME_FIELD)
                .append(" TEXT, ")
                .append(PASSWORD_FIELD)
                .append(" TEXT)");
        dbHandler.execSQL(user.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public void addEvent(Event event) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME_FIELD, event.getName());
        contentValues.put(DATE_FIELD, getStringFromDate(event.getDate()));
        contentValues.put(TIME_FIELD, getStringFromTime(event.getTime()));
        contentValues.put(CATEGORY_FIELD, event.getCategory());

        sqLiteDatabase.insert(TABLE_EVENTS, null, contentValues);
    }

    public void addUser(String username, String password) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(USERNAME_FIELD, username);
        contentValues.put(PASSWORD_FIELD, password);

        sqLiteDatabase.insert(TABLE_USERS, null, contentValues);
    }

    public Boolean checkUserCredentials(String username, String password) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT*FROM " + TABLE_USERS +
                " WHERE username = ? AND password = ?", new String[]{username, password});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }


    public void populateEventsListView() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        try (Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_EVENTS, null)) {
            if (result.getCount() != 0) {
                while (result.moveToNext()) {
                    String name = result.getString(1);
                    String stringDate = result.getString(2);
                    String stringTime = result.getString(3);
                    Date date = getDateFromString(stringDate);
                    Date time = getTimeFromString(stringTime);
                    LocalTime localTime = convertDateToLocalTime(time);
                    LocalDate localDate = convertDateToLocalDate(date);
                    String category = result.getString(4);
                }
            }
        }
    }

    public List<Event> getAllEventsForDate(LocalDate specifiedDate) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        List<Event> events = new ArrayList<>();
        try (Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_EVENTS, null)) {
            if (result.getCount() != 0) {
                while (result.moveToNext()) {
                    int id = result.getInt(0);
                    String name = result.getString(1);
                    String stringDate = result.getString(2);
                    String stringTime = result.getString(3);
                    String category = result.getString(4);
                    Date date = getDateFromString(stringDate);
                    Date time = getTimeFromString(stringTime);
                    LocalTime localTime = convertDateToLocalTime(time);
                    LocalDate localDate = convertDateToLocalDate(date);
                    Event event = new Event(id, name, localDate, localTime, category);
                    if (event.getDate().equals(specifiedDate)) {
                        events.add(event);
                    }
                }
            }
        }
        return events;
    }

    public List<Event> getAllEvents() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        List<Event> events = new ArrayList<>();
        try (Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_EVENTS, null)) {
            if (result.getCount() != 0) {
                while (result.moveToNext()) {
                    int id = result.getInt(0);
                    String name = result.getString(1);
                    String stringDate = result.getString(2);
                    String stringTime = result.getString(3);
                    String category = result.getString(4);
                    Date date = getDateFromString(stringDate);
                    Date time = getTimeFromString(stringTime);
                    LocalTime localTime = convertDateToLocalTime(time);
                    LocalDate localDate = convertDateToLocalDate(date);
                    Event event = new Event(id, name, localDate, localTime, category);
                    events.add(event);
                }
            }
        }
        return events;
    }

    public void updateEvent(Event event) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        int id = event.getId();
        String name = event.getName();
        LocalDate localDate = event.getDate();
        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        String date = localDate.format(formatterDate);
        LocalTime localTime = event.getTime();
        DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm:ss");
        String time = localTime.format(formatterTime);
        String category = event.getCategory();

        String query = "UPDATE " + TABLE_EVENTS + " SET " + NAME_FIELD + " = '" + name + "'" +
                " , " + DATE_FIELD + " = '" + date + "'" + " , " + TIME_FIELD + " = '" + time + "'" + " , "
                + CATEGORY_FIELD + " = '" + category + "' WHERE " + ID_EVENTS_FIELD + " = '" + id + "'";
        sqLiteDatabase.execSQL(query);
    }

    public void deleteEvent(Event event) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        int id = event.getId();
        String query = "DELETE FROM " + TABLE_EVENTS + " WHERE " + ID_EVENTS_FIELD + " = '" + id + "'";
        sqLiteDatabase.execSQL(query);
    }

    private String getStringFromTime(LocalTime time) {
        if (time == null)
            return null;
        return formatLocalTime.format(time);
    }

    private Date getTimeFromString(String string) {
        try {
            return formatTime.parse(string);
        } catch (ParseException | NullPointerException e) {
            return null;
        }
    }

    private String getStringFromDate(LocalDate date) {
        if (date == null)
            return null;
        return formatLocalDate.format(date);
    }

    private Date getDateFromString(String string) {
        try {
            return formatDate.parse(string);
        } catch (ParseException | NullPointerException e) {
            throw new IllegalArgumentException("Cannot parse date from string: " + string);
        }
    }

    public static LocalDate convertDateToLocalDate(Date date) {
        Instant instant = date.toInstant();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
        LocalDate localDate = zonedDateTime.toLocalDate();
        return localDate;
    }

    public static LocalTime convertDateToLocalTime(Date date) {
        Instant instant = date.toInstant();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
        LocalTime localTime = zonedDateTime.toLocalTime();
        return localTime;
    }
}
