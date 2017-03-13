package com.example.michael.spendingtracker.SQLite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "spending.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_CATEGORY_TABLE = "CREATE TABLE " +
                CategoryContract.CategoryEntry.TABLE_NAME + " (" +
                CategoryContract.CategoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CategoryContract.CategoryEntry.COLUMN_CATEGORY_NAME + " TEXT NOT NULL, " +
                CategoryContract.CategoryEntry.COLUMN_CATEGORY_SUM + " INTEGER " +
                ");";
        final String SQL_CREATE_ITEM_TABLE = "CREATE TABLE " +
                ItemContract.ItemEntry.TABLE_NAME + " (" +
                ItemContract.ItemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ItemContract.ItemEntry.COLUMN_ITEM_NAME + " TEXT NOT NULL, " +
                ItemContract.ItemEntry.COLUMN_ITEM_CATEGORY + " TEXT NOT NULL " +
                ");";
        final String SQL_CREATE_TRANSACTION_TABLE = "CREATE TABLE " +
                TransactionContract.TransactionEntry.TABLE_NAME + " (" +
                TransactionContract.TransactionEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TransactionContract.TransactionEntry.COLUMN_TRANSACTION_COST + " DOUBLE NOT NULL, " +
                TransactionContract.TransactionEntry.COLUMN_TRANSACTION_ITEM + " TEXT NOT NULL, " +
                TransactionContract.TransactionEntry.COLUMN_TRANSACTION_CATEGORY + " TEXT NOT NULL, " +
                TransactionContract.TransactionEntry.COLUMN_TRANSACTION_CURRENCY + " TEXT NOT NULL, " +
                TransactionContract.TransactionEntry.COLUMN_TRANSACTION_QUANTITY + " INTEGER NOT NULL, " +
                TransactionContract.TransactionEntry.COLUMN_TRANSACTION_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP " +
                ");";
        sqLiteDatabase.execSQL(SQL_CREATE_CATEGORY_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_ITEM_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TRANSACTION_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CategoryContract.CategoryEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ItemContract.ItemEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TransactionContract.TransactionEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean removeCateogry(long id) {
        SQLiteDatabase mDb = this.getReadableDatabase();
        return mDb.delete(CategoryContract.CategoryEntry.TABLE_NAME,
                CategoryContract.CategoryEntry._ID + "=" + id, null) > 0;
    }

    public List<String> getCategoryList() {
        List<String> categories = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + CategoryContract.CategoryEntry.TABLE_NAME;

        SQLiteDatabase mDb = this.getReadableDatabase();
        Cursor cursor = mDb.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                categories.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        return categories;
    }
}
