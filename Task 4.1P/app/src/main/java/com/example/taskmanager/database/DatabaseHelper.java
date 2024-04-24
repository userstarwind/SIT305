package com.example.taskmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.taskmanager.Task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper instance;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "taskManagerDatabase";
    private static final String TABLE_TASKS = "tasks";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";
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
        String CREATE_TASKS_TABLE =  "CREATE TABLE tasks (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT," +
                "description TEXT," +
                "dueDate TEXT" +
                ")";
        db.execSQL(CREATE_TASKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);

        onCreate(db);
    }

    public void addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("title", task.getTitle());
        values.put("description", task.getDescription());
        values.put("dueDate", task.getDueDate().toString());

        db.insert("tasks", null, values);
        db.close();
    }

    public Task getTask(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("tasks", new String[] { "id", "title", "description", "dueDate" },
                "id = ?", new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("id");
            int titleIndex = cursor.getColumnIndex("title");
            int descriptionIndex = cursor.getColumnIndex("description");
            int dueDateIndex = cursor.getColumnIndex("dueDate");

            if (idIndex != -1 && titleIndex != -1 && descriptionIndex != -1 && dueDateIndex != -1) {
                Task task = new Task(
                        cursor.getInt(idIndex),
                        cursor.getString(titleIndex),
                        cursor.getString(descriptionIndex),
                        LocalDate.parse(cursor.getString(dueDateIndex))
                );
                cursor.close();
                db.close();
                return task;
            }
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return null;
    }

    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_TASKS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex("id");
                int titleIndex = cursor.getColumnIndex("title");
                int descriptionIndex = cursor.getColumnIndex("description");
                int dueDateIndex = cursor.getColumnIndex("dueDate");

                if (idIndex != -1 && titleIndex != -1 && descriptionIndex != -1 && dueDateIndex != -1) {
                    Task task = new Task(
                            cursor.getInt(idIndex),
                            cursor.getString(titleIndex),
                            cursor.getString(descriptionIndex),
                            LocalDate.parse(cursor.getString(dueDateIndex))
                    );
                    taskList.add(task);
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return taskList;
    }

    public int updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("title", task.getTitle());
        values.put("description", task.getDescription());
        values.put("dueDate", task.getDueDate().toString());

        return db.update(TABLE_TASKS, values, "id = ?", new String[]{String.valueOf(task.getId())});
    }

    public void deleteTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, "id = ?", new String[] { String.valueOf(task.getId()) });
        db.close();
    }


}



