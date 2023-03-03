package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class memo_setting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_setting);
        initListButton();
        initSettingsButton();
        initSettings();
        initSortByClick();
        initSortOrderClick();
    }

    private void initListButton(){
        ImageButton ibList = findViewById(R.id.imageButtonList);
//        ibList.setEnabled(false);
        ibList.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(memo_setting.this, MemoList.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initSettingsButton() {
        ImageButton ibSettings = findViewById(R.id.imageButtonSetting);
        ibSettings.setEnabled(false);
        ibSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(memo_setting.this, memo_setting.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    private void initSettings() {
        String sortBy = getSharedPreferences("MyMemoListPreferences", Context.MODE_PRIVATE)
                .getString("sortField", "subject");
        String sortOrder = getSharedPreferences("MyMemoListPreferences", Context.MODE_PRIVATE)
                .getString("sortOrder", "ASC");
        RadioButton rbSubject = findViewById(R.id.radioSubject);
        RadioButton rbImportance = findViewById(R.id.radioImportance);
        RadioButton rbDate = findViewById(R.id.radioDate);

        if (sortBy.equalsIgnoreCase("subject")) {
            rbSubject.setChecked(true);
        } else if (sortBy.equalsIgnoreCase("importance")) {
            rbImportance.setChecked(true);
        } else {
            rbDate.setChecked(true);
        }

        RadioButton rbAscending = findViewById(R.id.radioAscending);
        RadioButton rbDescending = findViewById(R.id.radioDescending);

        if (sortOrder.equalsIgnoreCase("ASC")) {
            rbAscending.setChecked(true);
        } else {
            rbDescending.setChecked(true);
        }
    }
    private void initSortByClick() {
        RadioGroup rgSortBy = findViewById(R.id.radioGroupSortBy);
        rgSortBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rbSubject = findViewById(R.id.radioSubject);
                RadioButton rbImportance = findViewById(R.id.radioImportance);
                if (rbSubject.isChecked()) {
                    getSharedPreferences("MyMemoListPreferences", Context.MODE_PRIVATE).edit()
                            .putString("sortField", "subject").apply();
                } else if (rbImportance.isChecked()){
                    getSharedPreferences("MyMemoListPreferences", Context.MODE_PRIVATE).edit()
                            .putString("sortField", "importance").apply();
                }
                else{
                    getSharedPreferences("MyMemoListPreferences", Context.MODE_PRIVATE).edit()
                            .putString("sortField", "date").apply();
                }

            }
        });
    }
    private void initSortOrderClick() {
        RadioGroup rgSortOrder = findViewById(R.id.radioGroupSortOrder);
        rgSortOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rbAscending = findViewById(R.id.radioAscending);
                if (rbAscending.isChecked()) {
                    getSharedPreferences("MyMemoListPreferences", Context.MODE_PRIVATE).edit()
                            .putString("sortOrder", "ASC").apply();
                }
                else{
                    getSharedPreferences("MyMemoListPreferences", Context.MODE_PRIVATE).edit()
                            .putString("sortOrder", "DESC").apply();
                }
            }
        });
    }

}
