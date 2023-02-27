package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MemoAdapter extends RecyclerView.Adapter {

    private ArrayList<Memo> memoData;
    private View.OnClickListener mOnItemClickListener;
    private boolean isDeleting;
    private Context parentContext;


    public class MemoViewHolder extends RecyclerView.ViewHolder{

        public TextView textSubject;

        public TextView textRating;
        public Button buttonDelete;

        public MemoViewHolder(@NonNull View itemView)
        {
            super(itemView);
            textSubject = itemView.findViewById(R.id.textSubject);
            textRating = itemView.findViewById(R.id.textRating);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);

            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);
        }

        public TextView getMemoTextSubject()
        {
            return textSubject;
        }

        public TextView getMemoTextRating()
        {
            return textRating;
        }

        public Button getDeleteButton()
        {
            return buttonDelete;
        }
    }

    public MemoAdapter(ArrayList<Memo> arrayList, Context context)
    {
        memoData = arrayList;
        parentContext = context;
    }

    public void setmOnItemClickListener(View.OnClickListener itemClickListener)
    {
        mOnItemClickListener = itemClickListener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new MemoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MemoViewHolder cvh = (MemoViewHolder) holder;
        cvh.getMemoTextSubject().setText(memoData.get(position).getSubject());
        //cvh.getMemoTextRating().setText(memoData.get(position).getRating());
        int currentRating = memoData.get(position).getRating();
        if(currentRating == 1)
        {
            String priority = "LOW";
            cvh.getMemoTextRating().setText(priority);
            ((MemoViewHolder) holder).textRating.setTextColor(Color.GREEN);
        }
        else if(currentRating == 2)
        {
            String priority = "NORMAl";
            cvh.getMemoTextRating().setText(priority);
            ((MemoViewHolder) holder).textRating.setTextColor(Color.BLACK);
        }
        else if(currentRating == 3)
        {
            String priority = "HIGH";
            cvh.getMemoTextRating().setText(priority);
            ((MemoViewHolder) holder).textRating.setTextColor(Color.RED);
        }
        else
        {
            String priority = "NONE";
            cvh.getMemoTextRating().setText(priority);
            ((MemoViewHolder) holder).textRating.setTextColor(Color.BLUE);
        }

        if(isDeleting)
        {
            cvh.getDeleteButton().setVisibility(View.VISIBLE);
            cvh.getDeleteButton().setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    deleteItem(position);
                }
            });
        }
        else
        {
            cvh.getDeleteButton().setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return memoData.size();
    }

    public void setDelete(boolean b)
    {
        isDeleting = b;
    }

    private void deleteItem(int position)
    {
        Memo contact = memoData.get(position);
        MemoDataSource ds = new MemoDataSource(parentContext);
        try
        {
            ds.open();
            boolean didDelete = ds.deleteMemo(contact.getMemoID());
            ds.close();
            if(didDelete)
            {
                memoData.remove(position);
                notifyDataSetChanged();
            }
            else
            {
                Toast.makeText(parentContext, "Delete Failed!", Toast.LENGTH_LONG).show();
            }
        }
        catch(Exception e)
        {
            Toast.makeText(parentContext, "Delete Failed!", Toast.LENGTH_LONG).show();
        }
    }
}
