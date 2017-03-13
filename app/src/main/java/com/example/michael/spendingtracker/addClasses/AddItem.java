package com.example.michael.spendingtracker.addClasses;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.michael.spendingtracker.R;
import com.example.michael.spendingtracker.SQLite.DBHelper;
import com.example.michael.spendingtracker.SQLite.ItemContract;

import java.util.List;

public class AddItem extends AppCompatActivity {

    Spinner spinnerCategory;
    EditText itemName;
    SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        DBHelper DBHelper = new DBHelper(this);
        mDb = DBHelper.getWritableDatabase();

        spinnerCategory = (Spinner) findViewById(R.id.sCategory);
        List<String> categories = DBHelper.getCategoryList();
        ArrayAdapter<String> aCategory = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, categories);
        aCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(aCategory);

    }

    public void saveItem(View view) {
        DBHelper DBHelper = new DBHelper(this);
        mDb = DBHelper.getWritableDatabase();

        itemName = (EditText) findViewById(R.id.item_name_edittext);
        String name = itemName.getText().toString();

        spinnerCategory = (Spinner) findViewById(R.id.sCategory);
        String category = spinnerCategory.getSelectedItem().toString();

        if (name.isEmpty()) {
            Toast.makeText(this, "Please input a name", Toast.LENGTH_SHORT).show();
        } else {
            addNewItem(name, category);
            Toast.makeText(this, "Successfully added the item, " + name + ", into " + category,
                    Toast.LENGTH_SHORT).show();
            itemName.clearFocus();
            itemName.setText("");
        }

    }

    private long addNewItem(String itemName, String categoryName) {
        ContentValues cv = new ContentValues();
        cv.put(ItemContract.ItemEntry.COLUMN_ITEM_NAME, itemName);
        cv.put(ItemContract.ItemEntry.COLUMN_ITEM_CATEGORY, categoryName);
        return mDb.insert(ItemContract.ItemEntry.TABLE_NAME, null, cv);
    }
}
