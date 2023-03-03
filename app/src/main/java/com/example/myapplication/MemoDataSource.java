package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.Calendar;

public class MemoDataSource {

    private SQLiteDatabase database;
    private MemoDBHelper dbHelper;

    public MemoDataSource(Context context)
    {
        dbHelper = new MemoDBHelper(context);
    }

    public void open() throws SQLException
    {
        database = dbHelper.getWritableDatabase();
    }

    public void close()
    {
        dbHelper.close();
    }

    public boolean insertMemo(Memo m)
    {
        boolean didSucceed = false;
        try{
            ContentValues initialValues = new ContentValues();

            initialValues.put("subject", m.getSubject());
            initialValues.put("body", m.getBody());
            initialValues.put("importance", m.getRating());
            initialValues.put("date", String.valueOf(m.getDate().getTimeInMillis()));

            didSucceed = database.insert("memo", null, initialValues) > 0;
        }
        catch (Exception e)
        {
            System.out.println("could not insert initial values");
            //do nothing - will return false if there is an exception
        }
        return didSucceed;
    }

    public boolean updateMemo(Memo m)
    {
        boolean didSucceed = false;
        try
        {
            Long rowId = (long) m.getMemoID();
            ContentValues updateValues = new ContentValues();

            updateValues.put("subject", m.getSubject());
            updateValues.put("body", m.getBody());
            updateValues.put("importance", m.getRating());
            updateValues.put("date", String.valueOf(m.getDate().getTimeInMillis()));

            didSucceed = database.update("memo", updateValues, "_id =" + rowId, null)>0;
        }
        catch(Exception e)
        {
            System.out.println("could not update values");
            //do nothing -will return false if there is an exception
        }
        return didSucceed;
    }

    public int getLastMemoId()
    {
        int lastId;
        try
        {
            String query = "Select MAX (_id) from memo";
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            lastId = cursor.getInt(0);
            cursor.close();
        }
        catch(Exception e)
        {
            lastId = -1;
        }
        return lastId;
    }


    public ArrayList<Memo> getMemos(String sortField, String sortOrder)
    {
        ArrayList<Memo> memos = new ArrayList<>();
        try
        {
            String query = "SELECT * FROM memo ORDER BY "+ sortField +  " " +sortOrder;
            Cursor cursor = database.rawQuery(query, null);

            Memo newMemo;
            cursor.moveToFirst();
            while(!cursor.isAfterLast())
            {
                newMemo = new Memo();
                newMemo.setMemoID(cursor.getInt(0));
                newMemo.setSubject(cursor.getString(1));
                newMemo.setBody(cursor.getString(2));
                newMemo.setRating(cursor.getInt(3));
                Calendar calender = Calendar.getInstance();
                calender.setTimeInMillis(Long.valueOf(cursor.getString(4)));
                newMemo.setDate(calender);

                memos.add(newMemo);
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch(Exception e)
        {
            memos = new ArrayList<Memo>();
        }
        return memos;
    }

    public Memo getSpecificMemo(int memoId)
    {
        Memo memo = new Memo();
        String query = "SELECT * FROM memo where _id= " + memoId;
        Cursor cursor = database.rawQuery(query, null);

        if(cursor.moveToFirst())
        {
            memo.setMemoID(cursor.getInt(0));
            memo.setSubject(cursor.getString(1));
            memo.setBody(cursor.getString(2));
            memo.setRating(cursor.getInt(3));
            Calendar calender = Calendar.getInstance();
            calender.setTimeInMillis(Long.valueOf(cursor.getString(4)));
            memo.setDate(calender);

            cursor.close();
        }
        return memo;
    }

    public boolean deleteMemo (int memoId)
    {
        boolean didDelete = false;
        try
        {
            didDelete = database.delete("memo", "_id="+memoId, null)>0;
        }
        catch (Exception e)
        {
            System.out.print("Could not delete memo");
            //do nothing -return value already set to false
        }
        return didDelete;
    }


}
