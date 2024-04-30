package com.example.task91p;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "advertsDatabase.db";

    private static final String TABLE_ADVERTS = "adverts";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_PLACE_NAME = "placeName";
    private static final String COLUMN_PLACE_ADDRESS = "placeAddress";
    private static final String COLUMN_PLACE_LATITUDE = "placeLatitude";
    private static final String COLUMN_PLACE_LONGITUDE = "placeLongitude";

    private static DBHelper instance;

    public static synchronized DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context.getApplicationContext());
        }
        return instance;
    }

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_ADVERTS + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_TYPE + " TEXT,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_DESCRIPTION + " TEXT,"
                    + COLUMN_PHONE + " TEXT,"
                    + COLUMN_DATE + " TEXT,"
                    + COLUMN_PLACE_NAME + " TEXT,"
                    + COLUMN_PLACE_ADDRESS + " TEXT,"
                    + COLUMN_PLACE_LATITUDE + " REAL,"
                    + COLUMN_PLACE_LONGITUDE + " REAL" + ")";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_ADVERTS;

    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public boolean insertAdvert(String type, String name, String description, String phone, String date, String placeName,String placeAddress,Double placeLatitude,Double placeLongitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TYPE, type);
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_PLACE_NAME, placeName);
        values.put(COLUMN_PLACE_ADDRESS, placeAddress);
        values.put(COLUMN_PLACE_LATITUDE, placeLatitude);
        values.put(COLUMN_PLACE_LONGITUDE, placeLongitude);

        long result = -1;
        try {
            result = db.insert(TABLE_ADVERTS, null, values);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return result != -1;
    }

    public List<Advert> getAllAdverts() {
        List<Advert> adverts = new ArrayList<>();

        try (SQLiteDatabase db = this.getReadableDatabase(); Cursor cursor = db.query(TABLE_ADVERTS, new String[]{COLUMN_ID, COLUMN_TYPE, COLUMN_NAME, COLUMN_DESCRIPTION, COLUMN_PHONE, COLUMN_DATE, COLUMN_PLACE_NAME,COLUMN_PLACE_ADDRESS,COLUMN_PLACE_LATITUDE,COLUMN_PLACE_LONGITUDE}, null, null, null, null, null)) {
            if (cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex(COLUMN_ID);
                int descriptionIndex = cursor.getColumnIndex(COLUMN_DESCRIPTION);
                int nameIndex = cursor.getColumnIndex(COLUMN_NAME);
                int phoneIndex = cursor.getColumnIndex(COLUMN_PHONE);
                int typeIndex = cursor.getColumnIndex(COLUMN_TYPE);
                int dateIndex = cursor.getColumnIndex(COLUMN_DATE);
                int placeNameIndex = cursor.getColumnIndex(COLUMN_PLACE_NAME);
                int placeAddressIndex = cursor.getColumnIndex(COLUMN_PLACE_ADDRESS);
                int placeLatitudeIndex = cursor.getColumnIndex(COLUMN_PLACE_LATITUDE);
                int placeLongitudeIndex = cursor.getColumnIndex(COLUMN_PLACE_LONGITUDE);

                if (idIndex == -1 || descriptionIndex == -1 || nameIndex == -1 || phoneIndex == -1 || typeIndex == -1 || dateIndex == -1 ||placeNameIndex==-1||placeAddressIndex==-1||placeLatitudeIndex==-1||placeLongitudeIndex==-1) {
                    throw new IllegalArgumentException("Column not found");
                }

                do {
                    int id = cursor.getInt(idIndex);
                    String description = cursor.getString(descriptionIndex);
                    String name = cursor.getString(nameIndex);
                    String phone = cursor.getString(phoneIndex);
                    String type = cursor.getString(typeIndex);
                    String placeName = cursor.getString(placeNameIndex);
                    String placeAddress = cursor.getString(placeAddressIndex);
                    Double placeLatitude = cursor.getDouble(placeLatitudeIndex);
                    Double placeLongitude = cursor.getDouble(placeLongitudeIndex);
                    String date = cursor.getString(dateIndex);

                    adverts.add(new Advert(id, type, name, description, phone, date, placeName,placeAddress,placeLatitude,placeLongitude));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return adverts;
    }
    public Advert getAdvertById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Advert advert = null;
        Cursor cursor = null;
        try {
            String[] columns = {COLUMN_ID, COLUMN_TYPE, COLUMN_NAME, COLUMN_DESCRIPTION, COLUMN_PHONE, COLUMN_DATE, COLUMN_PLACE_NAME, COLUMN_PLACE_ADDRESS, COLUMN_PLACE_LATITUDE, COLUMN_PLACE_LONGITUDE};
            String selection = COLUMN_ID + " = ?";
            String[] selectionArgs = {String.valueOf(id)};

            cursor = db.query(TABLE_ADVERTS, columns, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex(COLUMN_ID);
                int typeIndex = cursor.getColumnIndex(COLUMN_TYPE);
                int nameIndex = cursor.getColumnIndex(COLUMN_NAME);
                int descriptionIndex = cursor.getColumnIndex(COLUMN_DESCRIPTION);
                int phoneIndex = cursor.getColumnIndex(COLUMN_PHONE);
                int dateIndex = cursor.getColumnIndex(COLUMN_DATE);
                int placeNameIndex = cursor.getColumnIndex(COLUMN_PLACE_NAME);
                int placeAddressIndex = cursor.getColumnIndex(COLUMN_PLACE_ADDRESS);
                int placeLatitudeIndex = cursor.getColumnIndex(COLUMN_PLACE_LATITUDE);
                int placeLongitudeIndex = cursor.getColumnIndex(COLUMN_PLACE_LONGITUDE);

                if (idIndex != -1 && typeIndex != -1 && nameIndex != -1 && descriptionIndex != -1 && phoneIndex != -1 && dateIndex != -1 && placeNameIndex != -1 && placeAddressIndex != -1 && placeLatitudeIndex != -1 && placeLongitudeIndex != -1) {
                    int retrievedId = cursor.getInt(idIndex);
                    String type = cursor.getString(typeIndex);
                    String name = cursor.getString(nameIndex);
                    String description = cursor.getString(descriptionIndex);
                    String phone = cursor.getString(phoneIndex);
                    String date = cursor.getString(dateIndex);
                    String placeName = cursor.getString(placeNameIndex);
                    String placeAddress = cursor.getString(placeAddressIndex);
                    Double placeLatitude = cursor.getDouble(placeLatitudeIndex);
                    Double placeLongitude = cursor.getDouble(placeLongitudeIndex);

                    advert = new Advert(retrievedId, type, name, description, phone, date, placeName, placeAddress, placeLatitude, placeLongitude);
                } else {
                    throw new IllegalArgumentException("One or more columns are missing in the database.");
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
        return advert;
    }


    public boolean deleteAdvertById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        int deletedRows = 0;
        try {
            deletedRows = db.delete(TABLE_ADVERTS, selection, selectionArgs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return deletedRows > 0;
    }

}

