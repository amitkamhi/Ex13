package com.example.kamhi.ex13.data;

import java.util.HashSet;

//import com.example.testsql.MyAdapter;
import com.example.kamhi.ex13.data.DBContract.ItemEntry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;

public class DatabaseHelper extends SQLiteOpenHelper {
    // All Static variables
    private static DatabaseHelper mInstance = null;
    private static SQLiteDatabase db = null;
    private Context context=null;

    public final static int SORT_BY_NUMS = 1;
    public final static int SORT_BY_COLORS = 2;
    public final static int SHUFFLE = 3;

    private DatabaseHelper(Context context) {//making it private to avoid creating multiple instances
        super(context, DBContract.DATABASE_NAME, null, DBContract.DATABASE_VERSION);
        this.context = context;
    }

    public static DatabaseHelper getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new DatabaseHelper(ctx);
            db = mInstance.getWritableDatabase();
        }
        return mInstance;
    }

    @Override
    public void onConfigure (SQLiteDatabase db){
        db.setForeignKeyConstraintsEnabled (true);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        this.db =db;


    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db =db;
        String str = ItemEntry.CREATE_ITEMS_TABLE;
        db.execSQL(ItemEntry.CREATE_ITEMS_TABLE);

        this.addItem(new Item(-1,Color.WHITE));//dummy item place holder
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed IMPORTANT: MAKE SURE TO DROP THE TABLES IN THE CORRECT ORDER TO MAINTAIN REFERENTIAL INTEGRITY
        db.execSQL("DROP TABLE IF EXISTS '" + ItemEntry.TABLE_ITEMS +"';");

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    public void addItem(Item item) {

        ContentValues values = new ContentValues();
        values.put(ItemEntry.KEY_NUMBER, item.getNumber());
        values.put(ItemEntry.KEY_COLOR, item.getColor());

        // Inserting Row
        item.setId(db.insert(ItemEntry.TABLE_ITEMS, null, values));
    }

    Item getItem(long id) {
        Item item = null;
        Cursor cursor = db.query(ItemEntry.TABLE_ITEMS, new String[] { ItemEntry.KEY_ID,
                        ItemEntry.KEY_NUMBER, ItemEntry.KEY_COLOR}, ItemEntry.KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
            item = packItemFromCursor(cursor);
        }
        return item;
    }

    private Item packItemFromCursor(Cursor cursor){
        return new Item(cursor.getInt(cursor.getColumnIndex(ItemEntry.KEY_ID)),
                cursor.getInt(cursor.getColumnIndex(ItemEntry.KEY_NUMBER)),
                cursor.getInt(cursor.getColumnIndex(ItemEntry.KEY_COLOR)));
    }

    public HashSet<Integer> getExistingNumbers() {
        // Select All Query
        String selectQuery = "SELECT  * FROM " + ItemEntry.TABLE_ITEMS;

        Cursor cursor = db.rawQuery(selectQuery, null);

        HashSet<Integer> itemsNums = new HashSet<Integer>();
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                int valNum = cursor.getInt(cursor.getColumnIndex(ItemEntry.KEY_NUMBER));
                if (valNum != -1) itemsNums.add(valNum);
            } while (cursor.moveToNext());
        }

        // return contact list
        return itemsNums;
    }


    public Cursor getAllItems(int sortBy) {
        // Select All Query
        String selectQuery = "SELECT  * FROM " + ItemEntry.TABLE_ITEMS;
        switch (sortBy){
            case SORT_BY_NUMS:
                selectQuery += " ORDER BY " + ItemEntry.KEY_NUMBER + " ASC";
                break;
            case SORT_BY_COLORS:
                selectQuery += " ORDER BY " + ItemEntry.KEY_COLOR + " DESC";
                break;
            case SHUFFLE:
                selectQuery += " ORDER BY " + ItemEntry._ID + " DESC";
                break;
        }
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public int updateItem(Item item) {
        ContentValues values = new ContentValues();
        values.put(ItemEntry.KEY_ID, item.getId());
        values.put(ItemEntry.KEY_COLOR, item.getColor());
        values.put(ItemEntry.KEY_NUMBER, item.getNumber());

        // updating row
        return db.update(ItemEntry.TABLE_ITEMS, values, ItemEntry.KEY_ID + " = ?",
                new String[] { String.valueOf(item.getId()) });
    }

    public void deleteItem(long itemID) {
        db.delete(ItemEntry.TABLE_ITEMS, ItemEntry.KEY_ID + " = ?",
                new String[] { String.valueOf(itemID) });
    }

    public void deleteAllItems() {
        db.delete(ItemEntry.TABLE_ITEMS, null,null);
        db.execSQL(ItemEntry.CREATE_ITEMS_TABLE);
    }

    public int getItemsCount() {
        String countQuery = "SELECT  * FROM " + ItemEntry.TABLE_ITEMS;

        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

}