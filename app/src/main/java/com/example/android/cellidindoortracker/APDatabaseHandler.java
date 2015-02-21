package com.example.android.cellidindoortracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Point;
import android.net.wifi.ScanResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by raul on 20.2.2015.
 */
public class APDatabaseHandler extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "accesspoints";
    private static final String TABLE_BSSIDS = "bssids";

    // bssids table columns names
    private static final String KEY_BSSID_ID = "id";
    private static final String KEY_BSSID_NAME = "name";
    private static final String KEY_BSSID_POS_X = "pos_x";
    private static final String KEY_BSSID_POS_Y = "pos_y";

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
                + KEY_BSSID_NAME + " TEXT, "
                + KEY_BSSID_POS_X + "INTEGER, "
                + KEY_BSSID_POS_Y + "INTEGER" + ")";
        db.execSQL(CREATE_BSSID_TABLE);
        // fills bssids table
        String [] bssids = {
                BSSID1, BSSID2, BSSID3, BSSID4, BSSID5, BSSID6, BSSID7, BSSID8, BSSID9, BSSID10,
                BSSID11, BSSID12, BSSID13, BSSID14, BSSID15, BSSID16, BSSID17, BSSID18
        };
        int [] bssidXPositions = {

        };
        int [] bssidYPositions = {

        };
        ContentValues bssidValues = new ContentValues();
        for (int i = 1; i < bssids.length + 1; i++) {
            bssidValues.put(KEY_BSSID_NAME, bssids[i-1]);
            // insert bssid names into bssids table
            db.insert(TABLE_BSSIDS, null, bssidValues);
        }

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BSSIDS);

        // Create tables again
        onCreate(db);
    }

    /**
     * Compares WiFi AP scan results with APs stored in database and remove those that are not in
     * it.
     * @param results WiFi scan results list with all the AP data scanned in MapViewActivity
     * @return WiFi scan results list with AP data already filtered.
     */
    public List<ScanResult> filterKnownAPDB (List<ScanResult> results){
        SQLiteDatabase db = this.getReadableDatabase();

        // Select all BSSIDs from bssids table
        String selectQuery = "SELECT " + KEY_BSSID_NAME + " FROM " + TABLE_BSSIDS;
        Cursor cursor = db.rawQuery(selectQuery, null);

        boolean apIsKnown;
        List<ScanResult> toRemove = new ArrayList<ScanResult>();

        /* Compares both lists element by element. If an item is not in known AP table,
        is inserted into toRemove list in order to remove it at the end */

        for (int j = 0; j < results.size(); j++) { // Scan results list
            apIsKnown = false;
            if (cursor.moveToFirst()) {
                for (int i = 0; i < cursor.getCount(); i++) { // Known APs in database
                    if (removeLastDigitBssid(results.get(j).BSSID).equals(cursor.getString(0)))
                        apIsKnown = true;
                    cursor.moveToNext();
                }

                // Remove AP from list
                if (!apIsKnown)
                    toRemove.add(results.get(j));

            }
        }
        // Removes all items stored into toRemove list from results list
        results.removeAll(toRemove);
        db.close();

        return results;
    }

    /**
     * Gets AP position from bssids table
     * @param bssid AP BSSID of interest
     * @return Point object with AP position
     */
    public Point getAPPositionDB (String bssid) {
        /* Removes last digit bssid in order to query database */
        bssid = removeLastDigitBssid(bssid);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BSSIDS, new String[]{KEY_BSSID_POS_X, KEY_BSSID_POS_Y},
                KEY_BSSID_NAME + "=?",
                new String[]{bssid}, null, null, null, null);
        int x = 0, y = 0;
        if (cursor.moveToFirst()) {
            x = Integer.parseInt(cursor.getString(0));
            y = Integer.parseInt(cursor.getString(1));
        }
        db.close();

        return new Point (x, y);
    }

    private String removeLastDigitBssid (String bssid){
        if (bssid.length() > 0) {
            bssid = bssid.substring(0, bssid.length()-1);
        }
        return bssid;
    }
}
