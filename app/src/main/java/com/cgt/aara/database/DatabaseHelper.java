package com.cgt.aara.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cgt.aara.data.UserDataClass;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "UserDatabase";
    private static final String TABLE_NAME = "UserTable";
    private static final String COLUMN_PROFILE_PIC = "ProfilePic";
    private static final String COLUMN_NAME = "Name";
    private static final String COLUMN_MOBILE_NUMBER = "MobileNumber";
    private static final String COLUMN_DATE_OF_BIRTH = "DateOfBirth";
    private static final String COLUMN_ADDRESS = "Address";
    private static final String COLUMN_INCOME_SALARY = "IncomeSalary";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_PROFILE_PIC + " TEXT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_MOBILE_NUMBER + " TEXT,"
                + COLUMN_DATE_OF_BIRTH + " TEXT,"
                + COLUMN_ADDRESS + " TEXT,"
                + COLUMN_INCOME_SALARY + " TEXT"
                + ")";
        db.execSQL(CREATE_USER_TABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertUser(UserDataClass userDataClass) {
        try {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PROFILE_PIC, userDataClass.getProfilePic());
        values.put(COLUMN_NAME, userDataClass.getName());
        values.put(COLUMN_MOBILE_NUMBER, userDataClass.getMobileNumber());
        values.put(COLUMN_DATE_OF_BIRTH, userDataClass.getDateOfBirth());
        values.put(COLUMN_ADDRESS, userDataClass.getAddress());
        values.put(COLUMN_INCOME_SALARY, userDataClass.getIncomeSalary());
        db.insert(TABLE_NAME, null, values);
        db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("Range")
    public List<UserDataClass> getUserList() {
        List<UserDataClass> userDataClassList = new ArrayList<>();
        try {
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                UserDataClass userDataClass = new UserDataClass();
                userDataClass.setProfilePic(cursor.getString(cursor.getColumnIndex(COLUMN_PROFILE_PIC)));
                userDataClass.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                userDataClass.setMobileNumber(cursor.getString(cursor.getColumnIndex(COLUMN_MOBILE_NUMBER)));
                userDataClass.setDateOfBirth(cursor.getString(cursor.getColumnIndex(COLUMN_DATE_OF_BIRTH)));
                userDataClass.setAddress(cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS)));
                userDataClass.setIncomeSalary(cursor.getString(cursor.getColumnIndex(COLUMN_INCOME_SALARY)));
                userDataClassList.add(userDataClass);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userDataClassList;

    }

    // Add other database operations as needed
}


