package com.soldiersofmobile.todoexpert.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.soldiersofmobile.todoexpert.api.TodoItem;


public class TodoDao {

    /**
     * Nazwy kolumn w DB.
     */
    public static final String C_ID = "_id";
    public static final String C_CONTENT = "content";
    public static final String C_DONE = "done";
    public static final String C_USER_ID = "user_id";
    public static final String C_CREATED_AT = "created_at";
    public static final String C_UPDATED_AT = "updated_at";

    /**
     * Nazwa tabeli, w której przechowywane będa obiekty
     */
    public static final String TABLE_NAME = "todos";

    private final DbHelper dbHelper;


    public TodoDao(DbHelper dbHelper) {

        this.dbHelper = dbHelper;
    }


    public void create(TodoItem todoFromApi, String userId) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(C_ID, todoFromApi.objectId);
        contentValues.put(C_CONTENT, todoFromApi.content);
        contentValues.put(C_DONE, todoFromApi.done);
        contentValues.put(C_USER_ID, userId);

        database.insertWithOnConflict(TodoDao.TABLE_NAME, null,
                contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        database.close();

    }

    public Cursor queryByUserId(String userId) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor query = database.query(TABLE_NAME, null, String.format("%s=?", C_USER_ID), new String[]{userId}, null, null, String.format("%s,%s",C_DONE,C_CONTENT));
        return query;

    }


}
