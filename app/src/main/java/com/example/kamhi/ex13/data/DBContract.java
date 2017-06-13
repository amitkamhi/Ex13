package com.example.kamhi.ex13.data;

import android.provider.BaseColumns;

/**
 * Created by Kamhi on 19/5/2017.
 */

public class DBContract {

    // Database Version
    static final int DATABASE_VERSION = 4;

    // Database Name
    static final String DATABASE_NAME = "GridData";

    public static abstract class ItemEntry implements BaseColumns {
        // table name
        static final String TABLE_ITEMS = "items";

        // able Columns names
        public static final String KEY_ID = _ID;
        public static final String KEY_NUMBER = "number";
        public static final String KEY_COLOR = "color";
        public static final String KEY_ORDER = "sort";

        static final String CREATE_ITEMS_TABLE = "CREATE TABLE " + TABLE_ITEMS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NUMBER + " INTEGER UNIQUE NOT NULL,"
                + KEY_COLOR + " INTEGER, " + KEY_ORDER + " INTEGER UNIQUE )";

    }




}
