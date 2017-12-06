package com.soumen.prithvi.callbackinterfaces;

import com.soumen.prithvi.dbops.CountryModel;

import java.util.ArrayList;

/**
 * Created by Soumen on 06-12-2017.
 */

public interface DataBackupInterface {
    public void onBackupCompleted(boolean done, ArrayList<CountryModel> countryModelArrayList);
}
