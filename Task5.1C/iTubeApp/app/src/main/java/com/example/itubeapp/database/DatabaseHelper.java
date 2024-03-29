package com.example.itubeapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.itubeapp.Video;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "iTubeApp.db";
    private static final int DATABASE_VERSION = 1;
    private static DatabaseHelper instance;
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "UID";
    private static final String COLUMN_USER_NAME = "UserName";
    private static final String COLUMN_FULL_NAME = "FullName";
    private static final String COLUMN_PASSWORD = "Password";

    private static final String TABLE_VIDEOS = "videos";
    private static final String COLUMN_VIDEO_ID = "VID";
    private static final String COLUMN_VIDEO_USER_ID = "UID";
    private static final String COLUMN_URL = "URL";


    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USER_NAME + " TEXT UNIQUE,"
            + COLUMN_FULL_NAME + " TEXT,"
            + COLUMN_PASSWORD + " TEXT" + ")";

    private static final String CREATE_TABLE_VIDEOS = "CREATE TABLE " + TABLE_VIDEOS + "("
            + COLUMN_VIDEO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_VIDEO_USER_ID + " INTEGER,"
            + COLUMN_URL + " TEXT,"
            + "FOREIGN KEY(" + COLUMN_VIDEO_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "))";

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_VIDEOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIDEOS);
        onCreate(db);
    }

    public boolean addVideo(int UID, String URL) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("UID", UID);
        values.put("URL", URL);

        long result = -1;
        try {
            result = db.insert(TABLE_VIDEOS, null, values);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return result != -1;
    }

    public boolean addUser(String userName, String fullName, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, userName);
        values.put(COLUMN_FULL_NAME, fullName);
        values.put(COLUMN_PASSWORD, password);

        long result = -1;
        try {
            result = db.insert(TABLE_USERS, null, values);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return result != -1;
    }

    public Integer checkUser(String userName, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        Integer userUid = null; //

        String[] columnsToReturn = {COLUMN_USER_ID};
        String selection = COLUMN_USER_NAME + " = ? AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {userName, password};

        try {
            cursor = db.query(TABLE_USERS, columnsToReturn, selection, selectionArgs, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int uidColumnIndex = cursor.getColumnIndex(COLUMN_USER_ID);
                if (uidColumnIndex != -1) {
                    userUid = cursor.getInt(uidColumnIndex);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return userUid;
    }


    public List<Video> getUserPlaylist(int userID) {
        List<Video> playlist = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {COLUMN_VIDEO_ID, COLUMN_VIDEO_USER_ID, COLUMN_URL};
        String selection = COLUMN_VIDEO_USER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(userID)};

        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_VIDEOS, columns, selection, selectionArgs, null, null, null);

            if (cursor.moveToFirst()) {
                do {
                    int sidIndex = cursor.getColumnIndex(COLUMN_VIDEO_ID);
                    int uidIndex = cursor.getColumnIndex(COLUMN_VIDEO_USER_ID);
                    int urlIndex = cursor.getColumnIndex(COLUMN_URL);

                    if (sidIndex != -1 && uidIndex != -1 && urlIndex != -1) {
                        int SID = cursor.getInt(sidIndex);
                        int UID = cursor.getInt(uidIndex);
                        String URL = cursor.getString(urlIndex);
                        Video video = new Video(SID, UID, URL);
                        playlist.add(video);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return playlist;
    }

    public boolean clearUserPlaylist(int userID) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = COLUMN_VIDEO_USER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(userID)};

        int deletedRows = 0;
        try {
            deletedRows = db.delete(TABLE_VIDEOS, selection, selectionArgs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return deletedRows > 0;
    }

    public boolean checkUserExists(String userName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_USER_ID};
        String selection = COLUMN_USER_NAME + " = ?";
        String[] selectionArgs = {userName};
        Cursor cursor = null;
        boolean userExists = false;

        try {
            cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                userExists = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return userExists;
    }
}

