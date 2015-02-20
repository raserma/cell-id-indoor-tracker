package com.example.android.cellidindoortracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by raul on 20.2.2015.
 */
public class APDatabaseHandler extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "measurements";
    private static final String TABLE_BSSIDS = "bssids";
    private static final String TABLE_MEASUREMENTS = "measurements";
    private static final String TABLE_COEFFICIENTS = "coefficients";
    // bssids table columns names
    private static final String KEY_BSSID_ID = "id";
    private static final String KEY_BSSID_NAME = "name";
    // measurements table column names
    private static final String KEY_MEASUREMENT_ID = "id";
    private static final String KEY_BSSID = "id_bssid";
    private static final String KEY_RSS = "value_rss";
    private static final String KEY_DISTANCE = "value_distance";
    // coefficients table column names
    private static final String KEY_COEFFICIENT_ID = "id";
    //private static final String KEY_BSSID = "id_bssid";
    private static final String KEY_COEFFICIENT_VALUE = "value_coefficient";
    // AP MAC ADDRESS LIST
    private static final String BSSID1 = "00:17:0f:d9:71:d";
    private static final String BSSID2 = "00:1b:0a:d9:71:d"; // Wrong MAC
    private static final String BSSID3 = "00:17:0f:d9:6f:d";
    private static final String BSSID4 = "f4:7f:35:f6:ab:a";
    private static final String BSSID5 = "18:33:9d:fe:9c:6";
    private static final String BSSID6 = "18:33:9d:fe:91:c";
    private static final String BSSID7 = "18:33:9d:f9:31:8";
    private static final String BSSID8 = "18:33:9d:f9:84:7";
    private static final String BSSID9 = "04:da:d2:a7:2f:c";
    private static final String BSSID10 = "04:da:d2:29:bf:8";
    private static final String BSSID11 = "04:da:d2:57:0a:3";
    private static final String BSSID12 = "04:da:d2:29:c4:c";
    private static final String BSSID13 = "04:da:d2:57:0a:3";
    private static final String BSSID14 = "04:da:d2:56:ee:e";
    private static final String BSSID15 = "04:da:d2:29:c2:3";
    private static final String BSSID16 = "04:da:d2:57:0d:a";
    private static final String BSSID17 = "04:da:d2:29:b4:0";
    private static final String BSSID18 = "04:da:d2:57:0e:5";
    /**
     * CONSTRUCTORS
     */
    public APDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * INSTANCE METHODS
     */

    /** Only runs once, when there is no database in the system */

    public void onCreate(SQLiteDatabase db) {
        // create bssids table
        String CREATE_BSSID_TABLE = "CREATE TABLE " + TABLE_BSSIDS + "("
                + KEY_BSSID_ID + " INTEGER PRIMARY KEY,"
                + KEY_BSSID_NAME + " TEXT" + ")";
        db.execSQL(CREATE_BSSID_TABLE);
        // fills bssids table
        String [] bssids = {
                BSSID1, BSSID2, BSSID3, BSSID4, BSSID5, BSSID6, BSSID7, BSSID8, BSSID9, BSSID10,
                BSSID11, BSSID12, BSSID13, BSSID14, BSSID15, BSSID16, BSSID17, BSSID18
        };
        ContentValues bssidValues = new ContentValues();
        for (int i = 1; i < bssids.length + 1; i++) {
            bssidValues.put(KEY_BSSID_NAME, bssids[i-1]);
            // insert bssid names into bssids table
            db.insert(TABLE_BSSIDS, null, bssidValues);
        }
        // create measurements table
        String CREATE_MEASUREMENTS_TABLE = "CREATE TABLE " + TABLE_MEASUREMENTS + "("
                + KEY_MEASUREMENT_ID + " INTEGER PRIMARY KEY,"
                + KEY_BSSID + " INTEGER,"
                + KEY_RSS + " INTEGER,"
                + KEY_DISTANCE + " INTEGER" + ")";
        db.execSQL(CREATE_MEASUREMENTS_TABLE);
        // create coefficients table
        String CREATE_COEFFICIENTS_TABLE = "CREATE TABLE " + TABLE_COEFFICIENTS + "("
                + KEY_COEFFICIENT_ID + " INTEGER PRIMARY KEY,"
                + KEY_BSSID + " INTEGER,"
                + KEY_COEFFICIENT_VALUE + " DOUBLE" + ")";
        db.execSQL(CREATE_COEFFICIENTS_TABLE);
    }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // Drop older table if existed
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_BSSIDS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEASUREMENTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_COEFFICIENTS);
    // Create tables again
            onCreate(db);
    }
}
