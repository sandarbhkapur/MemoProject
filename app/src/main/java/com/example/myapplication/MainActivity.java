package com.example.myapplication;

import static android.text.format.DateFormat.format;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements com.example.myapplication.DatePickerDialog.SaveDateListener {

    private Memo currentMemo;
    float userRating;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentMemo = new Memo();

        initListButton();
        initSettingButton();
        initToggleButton();
        setForEditing(false);
        initChangeDateButton();
        initSavebutton();
        initTextChangedEvents();

        Bundle extras = getIntent().getExtras();
        if (extras != null )
        {
            initMemo(extras.getInt("memoId"));
        }
        else
        {
            currentMemo = new Memo();
        }
    }

    private void initListButton(){
        ImageButton ibList = findViewById(R.id.imageButtonList);
        ibList.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this, MemoList.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initSettingButton(){
        ImageButton ibList = findViewById(R.id.imageButtonSetting);
        ibList.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this, memo_setting.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initToggleButton(){
        final ToggleButton editToggle = (ToggleButton)findViewById(R.id.toggleButtonEdit);
        editToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                setForEditing(editToggle.isChecked());
            }
        });
    }

    public void initMemo(int id)
    {
        MemoDataSource ds = new MemoDataSource(MainActivity.this);
        try
        {
            ds.open();
            currentMemo = ds.getSpecificMemo(id);
            ds.close();
        }
        catch(Exception e)
        {
            Toast.makeText(this, "Loading Memo Failed", Toast.LENGTH_LONG).show();
        }

        EditText editSubject  = findViewById(R.id.editSubject);
        EditText editBodyMultiLine = findViewById(R.id.editBodyMultiLine);
        TextView date = findViewById(R.id.textdate);
        //TextView textImportanceNumber = findViewById(R.id.textImportanceNumber);


        editSubject.setText(currentMemo.getSubject());
        editBodyMultiLine.setText(currentMemo.getBody());
        int currentRating = currentMemo.getRating();
        System.out.println("rating: "+currentRating);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        ratingBar.setRating(currentRating);
        //textImportanceNumber.setText(String.valueOf(currentRating));
        date.setText(DateFormat.format("MM/dd/yyyy",
                currentMemo.getDate().getTimeInMillis()).toString());
    }


    private void initChangeDateButton()
    {
        Button changeDate = findViewById(R.id.btnDate);
        changeDate.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                FragmentManager fm = getSupportFragmentManager();
                com.example.myapplication.DatePickerDialog datePickerDialog = new com.example.myapplication.DatePickerDialog();
                datePickerDialog.show(fm, "DatePick");
            }
        });
    }

    private void setForEditing(boolean enabled){
        EditText editSubject = findViewById(R.id.editSubject);
        EditText editBodyMultiLine = findViewById(R.id.editBodyMultiLine);
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        TextView textdate = findViewById(R.id.textdate);
        Button btnDate = findViewById(R.id.btnDate);

        editSubject.setEnabled(enabled);
        editBodyMultiLine.setEnabled(enabled);
        ratingBar.setEnabled(enabled);
        textdate.setEnabled(enabled);
        btnDate.setEnabled(enabled);
    }


    private void initSavebutton()
    {
        Button saveButton = findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                boolean wasSuccessful;
                MemoDataSource ds = new MemoDataSource(MainActivity.this);
                try {
                    ds.open();
                    if(currentMemo.getMemoID() == -1)
                    {
                        System.out.println("IN INSERT");
                        wasSuccessful = ds.insertMemo(currentMemo);
                        if(wasSuccessful)
                        {
                            System.out.print("I am here");
                            int newId = ds.getLastMemoId();
                            currentMemo.setMemoID(newId);
                        }
                    }
                    else
                    {
                        wasSuccessful = ds.updateMemo(currentMemo);
                    }
                    ds.close();
                }
                catch (Exception e)
                {
                    wasSuccessful = false;
                }

                if(wasSuccessful)
                {
                    ToggleButton editToggle = findViewById(R.id.toggleButtonEdit);
                    editToggle.toggle();
                    setForEditing(false);
                }
            }
        });
    }


    private void initTextChangedEvents()
    {
        final EditText etSubject = findViewById(R.id.editSubject);
        etSubject.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Auto - generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //auto - generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                currentMemo.setSubject(etSubject.getText().toString());
            }
        });

        final EditText editBodyMultiLine = findViewById(R.id.editBodyMultiLine);
        editBodyMultiLine.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentMemo.setBody(editBodyMultiLine.getText().toString());
            }
        });

        final RatingBar rating = findViewById(R.id.ratingBar);
        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                userRating = ratingBar.getRating();
                System.out.println("user rating inside: "+ userRating);
                currentMemo.setRating((int) userRating);
            }
        });
    }

    private void hideKeyboard()
    {
        InputMethodManager imn = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        EditText editSubject = findViewById(R.id.editSubject);
        imn.hideSoftInputFromWindow(editSubject.getWindowToken(), 0);

        EditText editBodyMultiLine = findViewById(R.id.editBodyMultiLine);
        imn.hideSoftInputFromWindow(editBodyMultiLine.getWindowToken(), 0);

        RatingBar ratingBar = findViewById(R.id.ratingBar);
        imn.hideSoftInputFromWindow(ratingBar.getWindowToken(), 0);

        TextView textdate = findViewById(R.id.textdate);
        imn.hideSoftInputFromWindow(textdate.getWindowToken(), 0);

    }

    @Override
    public void didFinishDatePickerDialog(Calendar selectedTime) {
        System.out.println("selectedTime in main activity: "+selectedTime );
        TextView date = findViewById(R.id.textdate);
        date.setText(format("MM/dd/yyy", selectedTime));
        currentMemo.setDate(selectedTime);

    }

}