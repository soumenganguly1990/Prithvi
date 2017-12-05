package com.soumen.prithvi.extras;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Soumen on 10/16/2017.
 */

public class AppCommonValues {

    public static final String HELVETICAROUNDEDBOLD = "fonts/hrb.otf";

    /* --- TinyDB tags here --- */
    public static final String _ISFIRSTRUNTAG = "isFirstRun";

    /* page tag, used for redirection */
    public static final int _INTROPAGE = 1;
    public static final int _DASHBOARDPAGE = 2;
    public static final int _COUNTRYDETAILSPAGE = 3;
    public static final int _ABOUTPAGE = 4;

    /* delay values */
    public static final int _REDIRECTDELAY = 3 * 1000;
    public static final int _VIBRATIONTIME = 25;
    public static final int _DRAWERDELAY = 250;

    /* Url for country data */
    public static final String BASEURL = "https://restcountries.eu/rest/";
    public static final String COUNTRYURL = "v2/all";

    /**
     * This method checks whether a working internet connection is available or not
     * @param context
     * @return
     */
    public static boolean isInternetAvailable(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}