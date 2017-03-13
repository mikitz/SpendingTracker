package com.example.michael.spendingtracker;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.michael.spendingtracker.adapters.CategoryAdapter;
import com.example.michael.spendingtracker.addClasses.AddCategory;
import com.example.michael.spendingtracker.addClasses.AddItem;
import com.example.michael.spendingtracker.addClasses.AddTransaction;
import com.example.michael.spendingtracker.SQLite.CategoryContract;
import com.example.michael.spendingtracker.SQLite.DBHelper;
import com.example.michael.spendingtracker.SQLite.ItemContract;
import com.example.michael.spendingtracker.SQLite.TransactionContract;
import com.example.michael.spendingtracker.fragments.RecentsFragment;
import com.example.michael.spendingtracker.fragments.SummaryFragment;
import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements SummaryFragment.OnFragmentInteractionListener,
                    RecentsFragment.OnFragmentInteractionListener {

    Random random = new Random();
    private CategoryAdapter mAdapter;
    SQLiteDatabase mDb;
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView categoryRecyclerView;
        categoryRecyclerView = (RecyclerView) this.findViewById(R.id.category_recylerview);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        DBHelper DBHelper = new DBHelper(this);
        mDb = DBHelper.getReadableDatabase();
        Cursor cursor = getAllCategories();
        mAdapter = new CategoryAdapter(this, cursor);

        categoryRecyclerView.setAdapter(mAdapter);

        cursor = getCategorySum();
        int catSum = cursor.getInt(cursor.getColumnIndex("catSum"));
        TextView catSUM = (TextView) findViewById(R.id.tvCatSum);
        catSUM.setText(String.valueOf(catSum));
    }

    private Cursor getCategorySum() {
        String category = "Grocery";
        String query = "SELECT sum(cost) AS catSum FROM transactions WHERE category = " +
                "'" + category + "'";
        Cursor cursor = mDb.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    private Cursor getAllCategories() {
        return mDb.query(
                CategoryContract.CategoryEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    public void recents(View view) {
        Intent intent = new Intent(this, Recents.class);
        startActivity(intent);
    }

    public void onResume() {
        super.onResume();
        mAdapter.swapCursor(getAllCategories());

        Cursor cursor = getCategorySum();
        int catSum = cursor.getInt(cursor.getColumnIndex("catSum"));
        TextView catSUM = (TextView) findViewById(R.id.tvCatSum);
        catSUM.setText(String.valueOf(catSum));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent settings = new Intent(this, Settings.class);
                startActivity(settings);
                return true;
            case R.id.action_addTransaction:
                Intent addTransaction = new Intent(this, AddTransaction.class);
                startActivity(addTransaction);
                return true;
            case R.id.action_addCategory:
                Intent addCategory = new Intent(this, AddCategory.class);
                startActivity(addCategory);
                return true;
            case R.id.action_addItem:
                Intent addItem = new Intent(this, AddItem.class);
                startActivity(addItem);
                return true;
            case R.id.action_addDefaults:
                insertDefaultCategory("Alcohol");
                insertDefaultCategory("Transportation");
                insertDefaultCategory("Grocery");
                insertDefaultCategory("Restaurant");
                insertDefaultItem("Chicken", "Grocery");
                insertDefaultItem("Rice", "Grocery");
                insertDefaultItem("Subway", "Transportation");
                insertDefaultItem("Bus", "Transportation");
                insertDefaultItem("Beer", "Alcohol");
                insertDefaultItem("Wine", "Alcohol");
                insertDefaultItem("Lim's", "Restaurant");
                insertDefaultItem("Martin's", "Restaurant");
                double d = 18000;
                insertDefaultTransaction(d, "Chicken", "Grocery", "KRW", 1);
                d = 12000;
                insertDefaultTransaction(d, "Rice", "Grocery", "KRW", 1);
                d = 2400;
                insertDefaultTransaction(d, "Subway", "Transportation", "KRW", 1);
                d = 1200;
                insertDefaultTransaction(d, "Bus", "Transportation", "KRW", 1);
                d = 5000;
                insertDefaultTransaction(d, "Beer", "Alcohol", "KRW", 1);
                d = 6000;
                insertDefaultTransaction(d, "Wine", "Alcohol", "KRW", 1);
                d = 12000;
                insertDefaultTransaction(d, "Lim's", "Restaurant", "KRW", 1);
                d = 7000;
                insertDefaultTransaction(d, "Martin's", "Restaurant", "KRW", 1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private long insertDefaultCategory(String name) {
        DBHelper DBHelper = new DBHelper(this);
        mDb = DBHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CategoryContract.CategoryEntry.COLUMN_CATEGORY_NAME, name);
        return mDb.insert(CategoryContract.CategoryEntry.TABLE_NAME, null, cv);
    }

    private long insertDefaultItem(String itemName, String categoryName) {
        ContentValues cv = new ContentValues();
        cv.put(ItemContract.ItemEntry.COLUMN_ITEM_NAME, itemName);
        cv.put(ItemContract.ItemEntry.COLUMN_ITEM_CATEGORY, categoryName);
        return mDb.insert(ItemContract.ItemEntry.TABLE_NAME, null, cv);
    }

    private long insertDefaultTransaction(Double cost,
                                   String item,
                                   String category,
                                   String currency,
                                   int quantity) {
        ContentValues cv = new ContentValues();
        cv.put(TransactionContract.TransactionEntry.COLUMN_TRANSACTION_COST, cost);
        cv.put(TransactionContract.TransactionEntry.COLUMN_TRANSACTION_ITEM, item);
        cv.put(TransactionContract.TransactionEntry.COLUMN_TRANSACTION_CATEGORY, category);
        cv.put(TransactionContract.TransactionEntry.COLUMN_TRANSACTION_CURRENCY, currency);
        cv.put(TransactionContract.TransactionEntry.COLUMN_TRANSACTION_QUANTITY, quantity);
        return mDb.insert(TransactionContract.TransactionEntry.TABLE_NAME, null, cv);
    }

//    public void rollD20(View view) {
//        int rand20 = random.nextInt(20)+1;
//        TextView d20roll = (TextView)findViewById(R.id.d20);
//        d20roll.setText(String.valueOf(rand20));
//    }

}
