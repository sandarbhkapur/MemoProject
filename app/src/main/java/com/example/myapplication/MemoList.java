package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

public class MemoList extends AppCompatActivity {

    ArrayList<Memo> memos;
    MemoAdapter memoAdapter;

    private View.OnClickListener onItemClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder)view.getTag();
            int position = viewHolder.getAdapterPosition();
            int memoId = memos.get(position).getMemoID();
            Intent intent = new Intent(MemoList.this, MainActivity.class);
            intent.putExtra("memoId", memoId);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_list);

        initListButton();
        initSettingButton();
        initAddMemoButton();
        initDeleteSwitch();
        MemoDataSource ds = new MemoDataSource(this);


        try{
            ds.open();
            String sortBy = getSharedPreferences("MyMemoListPreferences",
                    Context.MODE_PRIVATE).getString("sortfield", "subject");
            String sortOrder = getSharedPreferences("MyMemoListPreferences",
                    Context.MODE_PRIVATE).getString("sortorder", "ASC");
            memos = ds.getMemos(sortBy,sortOrder);
            ds.close();
            RecyclerView memoList = findViewById(R.id.rvContacts);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            memoList.setLayoutManager(layoutManager);
            memoAdapter = new MemoAdapter(memos, this);
            memoList.setAdapter(memoAdapter);
            memoAdapter.setmOnItemClickListener(onItemClickListener);
        }
        catch (Exception e)
        {
            Toast.makeText(this, "Error retrieving memos", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        String sortBy = getSharedPreferences("MyMemoListPreferences",
                Context.MODE_PRIVATE).getString("sortfield", "subject");
        String sortOrder = getSharedPreferences("MyMemoListPreferences",
                Context.MODE_PRIVATE).getString("sortorder", "ASC");
        MemoDataSource ds = new MemoDataSource(this);
        try {
            ds.open();
            memos = ds.getMemos(sortBy, sortOrder);
            ds.close();
            if(memos.size() > 0 )
            {
                RecyclerView memoList = findViewById(R.id.rvContacts);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
                memoList.setLayoutManager(layoutManager);
                memoAdapter = new MemoAdapter(memos, this);
                memoList.setAdapter(memoAdapter);
                memoAdapter.setmOnItemClickListener(onItemClickListener);
            }
            else
            {
                Intent intent = new Intent(MemoList.this, MainActivity.class);
                startActivity(intent);
            }
        }
        catch(Exception e )
        {
            Toast.makeText(this, "Error retrieving memos", Toast.LENGTH_LONG).show();
        }
    }


    private void initListButton(){
        ImageButton ibList = findViewById(R.id.imageButtonList);
        ibList.setEnabled(false);
        ibList.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(MemoList.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initSettingButton(){
        ImageButton ibList = findViewById(R.id.imageButtonSetting);
        ibList.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(MemoList.this, memo_setting.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }



    private void initAddMemoButton()
    {
        Button newContact = findViewById(R.id.buttonAddContact);
        newContact.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                Intent intent = new Intent(MemoList.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initDeleteSwitch()
    {
        Switch s = findViewById(R.id.switchDelete);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Boolean status = compoundButton.isChecked();
                memoAdapter.setDelete(status);
                memoAdapter.notifyDataSetChanged();
            }
        });
    }


}