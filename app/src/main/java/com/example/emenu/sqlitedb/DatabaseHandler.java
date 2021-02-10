package com.example.emenu.sqlitedb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.emenu.pojos.User;
import com.example.emenu.utils.Common;

import java.util.Date;
import java.util.Properties;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "contactsManager";
    private static final String TABLE_USERS = "users";

    //users table column names
    private static final String USER_EMAIL = "email";
    private static final String USER_PASSWORD = "password";
    private static final String USER_CREATED_AT = "created_at";

    public DatabaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("app-log", "db create started");
        String CREATE_USERS_TABLE = " CREATE TABLE " + TABLE_USERS + "(" +
                USER_EMAIL + " TEXT PRIMARY KEY,"
                + USER_PASSWORD + " TEXT NOT NULL,"
                + USER_CREATED_AT + " TEXT NOT NULL)";
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("app-log", "db upgrade started");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public long createNewUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_EMAIL, user.getEmail());
        values.put(USER_PASSWORD, user.getPassword());
        values.put(USER_CREATED_AT, Common.convertDateToString(new Date()));

        long saveResult = db.insert(TABLE_USERS, null, values);
        db.close();

        return saveResult;
    }

    public User getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        User user = null;

        Cursor cursor = db.rawQuery("SELECT * from " + TABLE_USERS + " WHERE email=?", new String[]{email});
        if (cursor != null || cursor.getCount() > 0) {
            cursor.moveToFirst();
            user = new User(cursor.getString(0),
                    cursor.getString(1), Common.convertStringToDate(cursor.getString(2)));
            cursor.close();
        }

        return user;
    }

    public Properties registerUser(User user) {
        SQLiteDatabase db = this.getReadableDatabase();

        Properties props = new Properties();
        props.setProperty("rowNumber", "-1");
        props.setProperty("message", "Unexpected error occurred");

        Cursor cursor = db.rawQuery("SELECT * from " + TABLE_USERS + " WHERE email=? LIMIT 1", new String[]{user.getEmail()});

        if (cursor == null || !(cursor.moveToFirst()) || cursor.getCount() == 0) {

            ContentValues values = new ContentValues();
            values.put(USER_EMAIL, user.getEmail());
            values.put(USER_PASSWORD, user.getPassword());
            values.put(USER_CREATED_AT, Common.convertDateToString(new Date()));

            long saveResult = db.insert(TABLE_USERS, null, values);

            if (saveResult != -1) {
                Log.i("app-log", "User registered successfully ");
                props.setProperty("rowNumber", "0");
                props.setProperty("message", "Registered successfully !");
            } else {
                Log.i("app-log", "reg failed 102 ");

                props.setProperty("rowNumber", "-1");
                props.setProperty("message", "Registration failed ! Try again later.");
            }
        } else {
            cursor.close();
            props.setProperty("rowNumber", "-1");
            props.setProperty("message", "Email has already registered ! Please login or use different email !");
        }

        db.close();
        return props;
    }

    public Properties authenticateUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        Properties props = new Properties();
        props.setProperty("success", "0");
        props.setProperty("message", "Unexpected error occurred");

        Cursor cursor = db.rawQuery("SELECT * from " + TABLE_USERS + " WHERE " + USER_EMAIL + "=?", new String[]{email});

        if (cursor != null && cursor.moveToFirst()) {
            User user = new User(cursor.getString(0),
                    cursor.getString(1), Common.convertStringToDate(cursor.getString(2)));
            cursor.close();

            if (user.getPassword().equals(password)) {
                props.setProperty("success", "1");
                props.setProperty("message", "Login successful");
            } else {
                props.setProperty("success", "0");
                props.setProperty("message", "Invalid password");
            }
        } else {
            props.setProperty("success", "0");
            props.setProperty("message", "User not found");
        }

        db.close();
        return props;
    }

    // code to update the single contact
    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_PASSWORD, user.getPassword());

        return db.update(TABLE_USERS, values, USER_EMAIL + " = ?",
                new String[]{String.valueOf(user.getEmail())});
    }

    public void printAllUsers() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS, null);

        if (cursor != null && cursor.moveToFirst()) {
            Log.i("app-log", "user : " + cursor.getString(0).toString() + " " + cursor.getString(1).toString() + " " + cursor.getString(2).toString());
            cursor.close();
        } else {
            Log.i("app-log", "no users found");
        }

        db.close();
    }

    public void deleteUser(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, USER_EMAIL + " = ?",
                new String[]{email});
        db.close();
    }
}
