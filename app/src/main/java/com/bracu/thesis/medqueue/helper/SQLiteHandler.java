package com.bracu.thesis.medqueue.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Tahmid on 03-Aug-15.
 */
public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "android_api";

    private static final String TABLE_LOGIN = "login";

    private static final Sting KEY_ID = "id";
    private static final Sting KEY_NAME = "name";
    private static final Sting KEY_EMAIL = "email";
    private static final Sting KEY_USER_TYPE = "user_type"; //User type e.g doctor or patient
    private static final Sting KEY_UID = "uid";
    private static final Sting KEY_CREATED_AT = "created_at";

    public SQLiteHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE" + TABLE_LOGIN + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE," + KEY_USER_TYPE + " TEXT,"
                + KEY_UID + " TEXT," + KEY_CREATED_AT + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);

        Log.d(TAG, "Database tables created.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        
    }
}
