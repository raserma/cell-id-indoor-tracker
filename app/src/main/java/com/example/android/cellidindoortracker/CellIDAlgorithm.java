package com.example.android.cellidindoortracker;

import android.content.Context;
import android.graphics.Point;
import android.net.wifi.ScanResult;

import java.util.List;

/**
 * Created by raul on 20.2.2015.
 */
public class CellIDAlgorithm {
    public Context mapViewActivityContext;

    public CellIDAlgorithm(Context context){
        this.mapViewActivityContext = context;
    }

    public Point getUserPosition(List<ScanResult> results){
        APDatabaseHandler apdbhandler = new APDatabaseHandler(mapViewActivityContext);

        /* Filters known APs from database */
        results = apdbhandler.filterKnownAPDB (results);
        /* Gets AP with strongest RSS */
        String bssidStrongest = getStrongestAP (results);
        /* Gets AP position from database */
        Point userPosition = apdbhandler.getAPPositionDB(bssidStrongest);

        return userPosition;
    }

    private String getStrongestAP (List<ScanResult> results){
        String bssid = null;
        int rss = 0, max = 0, index = 0;

        // Get strongest RSS AP from WiFi results list
        for(int i = 0; i < results.size(); i++){
            rss = results.get(i).level;

            // Comparison to find the maximum value
            if(rss > max) {
                max = rss;
                index = i;
            }
        }


        return results.get(index).BSSID;
    }
    private String removeLastDigitBssid (String bssid){
        if (bssid.length() > 0) {
            bssid = bssid.substring(0, bssid.length()-1);
        }
        return bssid;
    }
}
