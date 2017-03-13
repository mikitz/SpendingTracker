package com.example.michael.spendingtracker.addClasses;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.michael.spendingtracker.R;
import com.example.michael.spendingtracker.SQLite.CategoryContract;
import com.example.michael.spendingtracker.SQLite.DBHelper;

public class AddCategory extends AppCompatActivity {

    SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        DBHelper DBHelper = new DBHelper(this);
        mDb = DBHelper.getWritableDatabase();

    }

    public void save (View view) {
        EditText name = (EditText) findViewById(R.id.category_name_edittext);
        String categoryName = name.getText().toString();

        if (categoryName.isEmpty()) {
            Toast.makeText(this, "Please input a name", Toast.LENGTH_SHORT).show();
        } else {
            addNewCategory(categoryName);
            Toast.makeText(this, "Successfully saved " + categoryName, Toast.LENGTH_SHORT).show();
            name.clearFocus();
            name.setText("");
        }
    }

    private long addNewCategory(String name) {
        ContentValues cv = new ContentValues();
        cv.put(CategoryContract.CategoryEntry.COLUMN_CATEGORY_NAME, name);
        return mDb.insert(CategoryContract.CategoryEntry.TABLE_NAME, null, cv);
    }

}
