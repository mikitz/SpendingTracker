package com.example.michael.spendingtracker;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.michael.spendingtracker.adapters.TransactionAdapter;
import com.example.michael.spendingtracker.SQLite.DBHelper;
import com.example.michael.spendingtracker.SQLite.TransactionContract;

public class Recents extends AppCompatActivity {

    private TransactionAdapter mAdapter;
    SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recents);

        RecyclerView transactionsRecylerView;
        transactionsRecylerView = (RecyclerView) this.findViewById(R.id.transaction_recylerview);
        transactionsRecylerView.setLayoutManager(new LinearLayoutManager(this));

        DBHelper DBHelper = new DBHelper(this);
        mDb = DBHelper.getReadableDatabase();
        Cursor cursor = getAllTransactions();
        mAdapter = new TransactionAdapter(this, cursor);

        transactionsRecylerView.setAdapter(mAdapter);
    }

    private Cursor getAllTransactions() {
        return mDb.query(
                TransactionContract.TransactionEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                TransactionContract.TransactionEntry.COLUMN_TRANSACTION_TIMESTAMP
        );
    }

    public void onResume() {
        super.onResume();
        mAdapter.swapCursor(getAllTransactions());
    }
}
