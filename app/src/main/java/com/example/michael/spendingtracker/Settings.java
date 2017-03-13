package com.example.michael.spendingtracker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class Settings extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinnerTheme, spinnerCurrency, spinnerLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        SharedPreferences prefs = getSharedPreferences("PREFS_SETTINGS", MODE_PRIVATE);

        spinnerTheme = (Spinner) findViewById(R.id.sTheme);
        ArrayAdapter<CharSequence> aTheme = ArrayAdapter.createFromResource(
                this, R.array.themes, android.R.layout.simple_spinner_item);
        aTheme.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTheme.setAdapter(aTheme);
        spinnerTheme.setOnItemSelectedListener(this);
        spinnerTheme.setSelection(prefs.getInt("theme_selection", 0));

        spinnerCurrency = (Spinner) findViewById(R.id.sCurrency);
        ArrayAdapter<CharSequence> aCurrency = ArrayAdapter.createFromResource(
                this, R.array.currencies, android.R.layout.simple_spinner_item);
        aCurrency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCurrency.setAdapter(aCurrency);
        spinnerCurrency.setOnItemSelectedListener(this);
        spinnerCurrency.setSelection(prefs.getInt("currency_selection", 0));

        spinnerLanguage = (Spinner) findViewById(R.id.sLanguage);
        ArrayAdapter<CharSequence> aLanguage = ArrayAdapter.createFromResource(
                this, R.array.languages, android.R.layout.simple_spinner_item);
        aLanguage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLanguage.setAdapter(aLanguage);
        spinnerLanguage.setOnItemSelectedListener(this);
        spinnerLanguage.setSelection(prefs.getInt("language_selection", 0));



        EditText budget = (EditText) findViewById(R.id.etBudget);
        budget.setText(prefs.getString("budget", "0"));
        budget.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                SharedPreferences prefs = getSharedPreferences("PREFS_SETTINGS", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                EditText budget = (EditText) findViewById(R.id.etBudget);
                String vBudget = budget.getText().toString();

                editor.putString("budget", vBudget);
                editor.apply();
            }
        });

    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        SharedPreferences prefs = getSharedPreferences("PREFS_SETTINGS", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        int cPos = spinnerCurrency.getSelectedItemPosition();
        int tPos = spinnerTheme.getSelectedItemPosition();
        int lPos = spinnerLanguage.getSelectedItemPosition();

        switch (parent.getId()) {
            case R.id.sTheme:
                editor.putInt("theme_selection", tPos);
//                if (spinnerTheme.getSelectedItem().equals("Dark")) {
//                    this.setTheme(R.style.Dark);
//                    this.recreate();
//                } else if (spinnerTheme.getSelectedItem().equals("Light")) {
//                    this.setTheme(R.style.Light);
//                    this.recreate();
//                } else if (spinnerTheme.getSelectedItem().equals("Green")) {
//                    this.setTheme(R.style.Green);
//                    this.recreate();
//                }
                break;
            case R.id.sCurrency:
                editor.putInt("currency_selection", cPos);
                break;
            case R.id.sLanguage:
                editor.putInt("language_selection", lPos);
                break;
        }
        editor.apply();
    }

    public void onNothingSelected(AdapterView<?> parent) {

    }
}
