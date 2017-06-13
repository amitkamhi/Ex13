package com.example.kamhi.ex13;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import com.example.kamhi.ex13.data.DBContract;
import com.example.kamhi.ex13.data.DatabaseHelper;
import com.example.kamhi.ex13.data.Item;

import java.util.HashSet;

/**
 * Created by Kamhi on 19/5/2017.
 */

public class MyAdapter extends ResourceCursorAdapter {

    private DatabaseHelper dbh;
    private Context context;
    private int currOrder = DatabaseHelper.SORT_BY_NUMS;
    private static final int MAX_ITEMS = 16;
    public static final int MAX_NUMBERS = 99;

    public MyAdapter(Context context, int layout){
        super(context, layout, null, 0);
        this.dbh = DatabaseHelper.getInstance(context);
        this.context = context;
        Cursor cursor = this.dbh.getAllItems(currOrder);
        changeCursor(cursor);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        TextView tv = (TextView) view.findViewById(R.id.tvNumber);
        int number = cursor.getInt(cursor.getColumnIndex(DBContract.ItemEntry.KEY_NUMBER));
        view.setTag(cursor.getInt(cursor.getColumnIndex(DBContract.ItemEntry.KEY_ID)));
        int color = cursor.getInt(cursor.getColumnIndex(DBContract.ItemEntry.KEY_COLOR));
        ((GradientDrawable) view.getBackground()).setColor(color);

        if (number != -1){
            tv.setText(Integer.toString(number));
            view.setOnClickListener(null);
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    dbh.deleteItem((Integer)v.getTag());
                    changeCursor(dbh.getAllItems(currOrder));
                    notifyDataSetChanged();
                    return true;
                }
            });
        }
        else {
            tv.setText("...");
            if (cursor.getCount() < MAX_ITEMS)
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MainActivity)context).openNewItemDialog();
                    }
                });
            else
                view.setOnClickListener(null);
            view.setOnLongClickListener(null);
        }
    }

    //conection to the getExisting in the databasehelper
    public HashSet getExistingNumbers(){
        return this.dbh.getExistingNumbers();
    }

    //conection to the add in the databasehelper
    public void addNewItem(Item item){
        dbh.addItem(item);
        changeCursor(this.dbh.getAllItems(currOrder));

        notifyDataSetChanged();
    }

    //conection to the sort in the databasehelper
    public void sortItems(int sortBy){
        this.currOrder = sortBy;
        changeCursor(this.dbh.getAllItems(sortBy));
        notifyDataSetChanged();
    }
}