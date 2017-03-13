package com.example.michael.spendingtracker.SQLite;

import android.provider.BaseColumns;

public class TransactionContract {

    public static final class TransactionEntry implements BaseColumns {
        public static final String TABLE_NAME = "transactions";
        public static final String COLUMN_TRANSACTION_COST = "cost";
        public static final String COLUMN_TRANSACTION_ITEM = "item";
        public static final String COLUMN_TRANSACTION_CATEGORY = "category";
        public static final String COLUMN_TRANSACTION_CURRENCY = "currency";
        public static final String COLUMN_TRANSACTION_QUANTITY = "quantity";
        public static final String COLUMN_TRANSACTION_TIMESTAMP = "timestamp";

    }
}
