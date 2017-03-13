package com.example.michael.spendingtracker.addClasses;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michael.spendingtracker.R;
import com.example.michael.spendingtracker.SQLite.CategoryContract;
import com.example.michael.spendingtracker.SQLite.DBHelper;
import com.example.michael.spendingtracker.SQLite.ItemContract;
import com.example.michael.spendingtracker.SQLite.TransactionContract;

import java.util.ArrayList;
import java.util.List;

import static com.example.michael.spendingtracker.R.array.quantity;

public class AddTransaction extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinnerCurrency, spinnerCategory, spinnerItem, spinnerQuantity;
    TextView item;
    SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_transaction);

        DBHelper dbHelper = new DBHelper(this);

        SharedPreferences prefs = getSharedPreferences("PREFS_SETTINGS", MODE_PRIVATE);

        spinnerCurrency = (Spinner) findViewById(R.id.sCurrency);
        ArrayAdapter<CharSequence> aCurrency = ArrayAdapter.createFromResource(
                this, R.array.currencies, android.R.layout.simple_spinner_item);
        aCurrency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCurrency.setAdapter(aCurrency);
        spinnerCurrency.setSelection(prefs.getInt("currency_selection", 0));

        spinnerCategory = (Spinner) findViewById(R.id.sCategory);
        List<String> categories = dbHelper.getCategoryList();
        ArrayAdapter<String> aCategory = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, categories);
        aCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(aCategory);

        spinnerItem = (Spinner) findViewById(R.id.sName);

        spinnerQuantity = (Spinner) findViewById(R.id.sQuantity);
        ArrayAdapter<CharSequence> aQuantity = ArrayAdapter.createFromResource(
                this, quantity, android.R.layout.simple_spinner_item);
        aQuantity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQuantity.setAdapter(aQuantity);

        spinnerCategory.setOnItemSelectedListener(this);

    }

    public void onResume() {
        super.onResume();
        List<String> items = getAllItems();
        ArrayAdapter<String> aItems = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, items);
        aItems.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerItem.setAdapter(aItems);
        aItems.notifyDataSetChanged();

        DBHelper dbHelper = new DBHelper(this);
        List<String> categories = dbHelper.getCategoryList();
        ArrayAdapter<String> aCategory = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, categories);
        aCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(aCategory);
        aCategory.notifyDataSetChanged();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_transaction_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_addItem:
                Intent addItem = new Intent(this, AddItem.class);
                startActivity(addItem);
                return true;
            case R.id.action_addCategory:
                Intent addCategory = new Intent(this, AddCategory.class);
                startActivity(addCategory);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.sCurrency:

                break;
            case R.id.sQuantity:

                break;
            case R.id.sCategory:
                List<String> items = getAllItems();
                ArrayAdapter<String> aItems = new ArrayAdapter<>(
                        this, android.R.layout.simple_spinner_item, items);
                aItems.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerItem.setAdapter(aItems);
                aItems.notifyDataSetChanged();
                break;
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void save(View view) {
        String category = spinnerCategory.getSelectedItem().toString();
        String item = spinnerItem.getSelectedItem().toString();
        String currency = spinnerCurrency.getSelectedItem().toString();
        EditText etCost = (EditText) findViewById(R.id.etCost);

        if (spinnerQuantity.getSelectedItem().equals("Quantity")) {
            Toast.makeText(this, "Please select a quantity", Toast.LENGTH_SHORT).show();
        } else if (etCost.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please input the price", Toast.LENGTH_SHORT).show();
        } else {
            double cost = Double.parseDouble(etCost.getText().toString());
            int quantity = Integer.parseInt(spinnerQuantity.getSelectedItem().toString());
            double totalCost = cost * quantity;
            Toast.makeText(this, "Successfully saved your purchase of " + quantity + " " + item +
                            " for a total of " + totalCost + " " + currency + " in " + category,
                    Toast.LENGTH_LONG).show();
            addNewTransaction(totalCost, item, category, currency, quantity);
            Cursor cursor = categorySum(category);
            int catSum = cursor.getInt(cursor.getColumnIndex("catSum"));
            int costInt = Integer.parseInt(etCost.getText().toString());
            updateCategorySum(catSum + (costInt * quantity), category);
            finish();
        }
    }

    private Cursor categorySum(String categoryInput) {
        String category = categoryInput;
        String query = "SELECT sum(cost) AS catSum FROM transactions WHERE category = " +
                "'" + category + "'";
        Cursor cursor = mDb.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    private long updateCategorySum(int sum, String category) {
        ContentValues cv = new ContentValues();
        cv.put(CategoryContract.CategoryEntry.COLUMN_CATEGORY_SUM, sum);
        return mDb.update(CategoryContract.CategoryEntry.TABLE_NAME, cv, "'" + category + "'", null);
    }

    public void addCategory(View view) {
        Intent addCategory = new Intent(this, AddCategory.class);
        startActivity(addCategory);
    }

    public void addItem(View view) {
        Intent addItem = new Intent(this, AddItem.class);
        startActivity(addItem);
    }

    public void deleteCategory(View view) {
        DBHelper dbHelper = new DBHelper(this);

    }

    private List<String> getAllItems() {
        List<String> items = new ArrayList<>();
        if (spinnerCategory.getSelectedItem().equals(null)) {
            return null;
        } else {
            String categoryName = spinnerCategory.getSelectedItem().toString();
            String selectQuery = "SELECT * FROM " + ItemContract.ItemEntry.TABLE_NAME +
                    " WHERE " + ItemContract.ItemEntry.COLUMN_ITEM_CATEGORY + " IS '" + categoryName + "'";

            DBHelper dbHelper = new DBHelper(this);
            mDb = dbHelper.getReadableDatabase();

            Cursor cursor = mDb.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    items.add(cursor.getString(1));
                } while (cursor.moveToNext());
            }
        }
        return items;
    }

    private long addNewTransaction(Double cost,
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
}
