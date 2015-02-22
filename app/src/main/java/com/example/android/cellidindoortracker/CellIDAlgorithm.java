package com.example.android.cellidindoortracker;

import android.content.Context;
import android.graphics.Point;
import android.net.wifi.ScanResult;

import java.util.List;

/**
 * CellIDAlgorithm whose purpose is finding user position by applying the necessary algorithm:
 *      -Cell ID algorithm: getting the AP with strongest RSS and showing its position on map
 */
public class CellIDAlgorithm {
    public Context mapViewActivityContext;

    public CellIDAlgorithm(Context context){
        this.mapViewActivityContext = context;
    }

    /**
     * Main method which finds user position based on WiFi scan results.
     * @param results WiFi scan results list with all the known AP data
     * @return Point object with AP position
     *              Point(-10, -10) if something went wrong
     */
    public Point getUserPosition(List<ScanResult> results){
        APDatabaseHandler apdbhandler = new APDatabaseHandler(mapViewActivityContext);

        /* Filters known APs from database */
        results = apdbhandler.filterKnownAPDB (results);

        // If all AP wifi scan results were filtered
        if(results.size() == 0)
            return new Point (-10, -10);

        /* Gets AP with strongest RSS */
        String bssidStrongest = getStrongestAP (results);
        /* Gets AP position from database */
        Point userPosition = apdbhandler.getAPPositionDB(bssidStrongest);

        return userPosition;
    }

    /**
     * Gets AP with strongest RSS from scan result lists
     * @param results WiFi scan results list with all the known AP data
     * @return strongest AP bssid
     */
    private String getStrongestAP (List<ScanResult> results){
        String bssid = null;
        int rss = 0, max = -120, index = 0;

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
}
