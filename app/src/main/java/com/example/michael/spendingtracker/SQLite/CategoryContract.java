package com.example.michael.spendingtracker.SQLite;

import android.provider.BaseColumns;

public class CategoryContract {

    public static final class CategoryEntry implements BaseColumns {
        public static final String TABLE_NAME = "category";
        public static final String COLUMN_CATEGORY_NAME = "name";
        public static final String COLUMN_CATEGORY_SUM = "sum";
    }
}
