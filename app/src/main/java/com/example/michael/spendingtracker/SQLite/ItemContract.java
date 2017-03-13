package com.example.michael.spendingtracker.SQLite;

import android.provider.BaseColumns;

public class ItemContract {

    public static final class ItemEntry implements BaseColumns {
        public static final String TABLE_NAME = "item";
        public static final String COLUMN_ITEM_NAME = "name";
        public static final String COLUMN_ITEM_CATEGORY = "category";

    }
}
