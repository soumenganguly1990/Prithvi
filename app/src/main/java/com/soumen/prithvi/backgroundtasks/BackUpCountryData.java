package com.soumen.prithvi.backgroundtasks;

import android.content.Context;
import android.os.AsyncTask;
import com.soumen.prithvi.callbackinterfaces.DataBackupInterface;
import com.soumen.prithvi.dbops.CountryModel;
import com.soumen.prithvi.models.Country;
import java.util.ArrayList;
import java.util.Arrays;
import io.realm.Realm;

/**
 * Created by Soumen on 06-12-2017.
 */

public class BackUpCountryData extends AsyncTask<String, Integer, Integer> {

    ArrayList<Country> countryList;
    ArrayList<CountryModel> countryModelArrayList;
    Context context;
    Realm realm;
    int count  = 0;
    public DataBackupInterface mDataBackupInterface = null;

    public BackUpCountryData(Context context, ArrayList<Country> countryList) {
        this.countryList = countryList;
        this.context = context;
        this.countryModelArrayList = new ArrayList();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(String... strings) {
        Realm.init(context);
        realm = Realm.getDefaultInstance();
        for(int i = 0;i < countryList.size();i ++) {
            Country country = countryList.get(i);
            CountryModel c1 = new CountryModel();
            c1.setName(country.getName());
            c1.setAlpha2Code(country.getAlpha2Code());
            c1.setAlpha3Code(country.getAlpha3Code());
            c1.setAltSpellings(getFormattedString(new ArrayList(Arrays.asList(country.getAltSpellings()))));
            c1.setArea(country.getArea());
            c1.setBorders(getFormattedString(new ArrayList(Arrays.asList(country.getBorders()))));
            c1.setCallingCodes(getFormattedString(new ArrayList(Arrays.asList(country.getCallingCodes()))));
            c1.setCapital(country.getCapital());
            c1.setCurrencies(getFormattedString(new ArrayList(Arrays.asList(country.getCurrencies()))));
            c1.setDemonym(country.getDemonym());
            c1.setFlag(country.getFlag());
            c1.setGini(country.getGini());
            c1.setLanguages(getFormattedString(new ArrayList(Arrays.asList(country.getLanguages()))));
            c1.setLatlng(getFormattedDouble(new ArrayList(Arrays.asList(country.getLatlng()))));
            c1.setNativeName(country.getNativeName());
            c1.setPopulation(country.getPopulation());
            c1.setRegion(country.getRegion());
            c1.setSubregion(country.getSubregion());
            c1.setRegionalBlocs(getFormattedString(new ArrayList(Arrays.asList(country.getRegionalBlocs()))));
            c1.setTimezones(getFormattedString(new ArrayList(Arrays.asList(country.getTimezones()))));
            c1.setTopLevelDomain(getFormattedString(new ArrayList(Arrays.asList(country.getTopLevelDomain()))));
            c1.setNumericCode(country.getNumericCode());

            realm.beginTransaction();
            realm.insertOrUpdate(c1);
            realm.commitTransaction();

            count ++;
            countryModelArrayList.add(c1);
        }
        return count;
    }

    /**
     * Drops the [, ] characters from the a list's tostring equiv return
     * @param someList
     * @return
     */
    private String getFormattedString(ArrayList someList) {
        try {
            String str = someList.toString();
            if (str == null) {
                str = "";
            } else {
                str = str.replace("[", "");
                str = str.replace("]", "");
            }
            return str;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * same, just takes arraylist of double as parameter
     * @param someList
     * @return
     */
    private String getFormattedDouble(ArrayList<Double> someList) {
        try {
            String str = someList.toString();
            if (str == null) {
                str = "";
            } else {
                str = str.replace("[", "");
                str = str.replace("]", "");
            }
            return str;
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    protected void onPostExecute(Integer i) {
        super.onPostExecute(i);
        if(i == countryList.size()) {
            mDataBackupInterface.onBackupCompleted(true, countryModelArrayList);
        } else {
            mDataBackupInterface.onBackupCompleted(false, null);
        }
    }
}