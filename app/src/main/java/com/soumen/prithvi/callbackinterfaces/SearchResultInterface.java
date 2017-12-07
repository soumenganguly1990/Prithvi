package com.soumen.prithvi.callbackinterfaces;

import com.soumen.prithvi.dbops.CountryModel;
import java.util.ArrayList;

/**
 * Created by Soumen on 07-12-2017.
 */

public interface SearchResultInterface {
    public void onSearchResultGenerated(ArrayList<CountryModel> searchResults);
}
