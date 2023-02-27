package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class memo_setting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_setting);
        initListButton();
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
}