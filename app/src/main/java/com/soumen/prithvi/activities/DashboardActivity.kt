package com.soumen.prithvi.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.app.SearchManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.*
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import butterknife.BindView
import butterknife.ButterKnife
import com.soumen.prithvi.backgroundtasks.BackUpCountryData
import com.soumen.prithvi.R
import com.soumen.prithvi.adapters.CountryAdapter
import com.soumen.prithvi.callbackinterfaces.DataBackupInterface
import com.soumen.prithvi.callbackinterfaces.SearchResultInterface
import com.soumen.prithvi.dbops.CountryModel
import com.soumen.prithvi.extras.AppCommonValues
import com.soumen.prithvi.models.Country
import com.soumen.prithvi.rest.ApiInterface
import com.soumen.prithvi.rest.ApiClient
import io.realm.Realm
import io.realm.RealmQuery
import io.realm.RealmResults
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardActivity : AppCompatActivity(), DataBackupInterface, SearchView.OnQueryTextListener, SearchResultInterface {

    @BindView(R.id.prithviDrawer)
    lateinit var dupliDrawer: DrawerLayout
    @BindView(R.id.navMenu)
    lateinit var navMenu: NavigationView
    @BindView(R.id.prithviToolbar)
    lateinit var prithviToolbar: Toolbar
    @BindView(R.id.rclCountryList)
    lateinit var rclCountryList: RecyclerView
    @BindView(R.id.rclSearchedList)
    lateinit var rclSearchedList: RecyclerView

    /* country related arraylists and adapters */
    lateinit var call: Call<List<Country>>
    lateinit var apiService: ApiInterface
    lateinit var pDialog: ProgressDialog
    lateinit var allCountryList: ArrayList<Country>
    var countryModelArrayList: ArrayList<CountryModel>? = null
    lateinit var allCountryAdapter: CountryAdapter
    var searchedCountryAdapter: CountryAdapter? = null

    /* realm db object */
    lateinit var realm: Realm

    /* adding search manager for searching countries by name */
    lateinit var searchManager: SearchManager
    lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_dashboard)
        ButterKnife.bind(this)

        rclCountryList.layoutManager = LinearLayoutManager(this@DashboardActivity)
        rclSearchedList.layoutManager = LinearLayoutManager(this@DashboardActivity)

        setSupportActionBar(prithviToolbar)
        setUpPrithviDrawer()
        setUpNavigationController()
        proceedToShowCountryData()
    }

    override fun onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.onActionViewCollapsed()
            searchView.setIconified(true)
            if(rclSearchedList.visibility == View.VISIBLE && isKeyboardShown()) {
                searchManager.stopSearch()
                closeKeyboard()
                makeActualListVisibleSearchListInvisible()
            }
            super.onBackPressed()
        } else {
            super.onBackPressed()
        }
    }

    /**
     * @task sets up the drawerlayout for the activity, crucial to display the hamburger
     */
    private fun setUpPrithviDrawer() {
        val actionBarDrawerToggle = object : ActionBarDrawerToggle(this, dupliDrawer,
                prithviToolbar, R.string.opendrawer, R.string.closedrawer) {
            override fun onDrawerClosed(drawerView: View?) {
                super.onDrawerClosed(drawerView)
            }

            override fun onDrawerOpened(drawerView: View?) {
                super.onDrawerOpened(drawerView)
            }
        }
        dupliDrawer.setDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
    }

    /**
     * @task handles the clicks on different nav menu items by id
     */
    private fun setUpNavigationController() {
        navMenu.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.mnuPrithvi -> closeDrawerAndProceed(R.id.mnuPrithvi)
                R.id.mnuMe -> closeDrawerAndProceed(R.id.mnuMe)
                R.id.navAbtTechgig -> closeDrawerAndProceed(R.id.navAbtTechgig)
            }
            false
        }
    }

    /**
     * Closes the drawer and do whatever is needed thereafter
     * @param menuId
     */
    private fun closeDrawerAndProceed(menuId: Int) {
        val mHandler = Handler()
        mHandler.postDelayed(object : Runnable {
            override fun run() {
                dupliDrawer.closeDrawers()
                mHandler.removeCallbacks(this)
            }
        }, AppCommonValues._DRAWERDELAY.toLong())
        when (menuId) {
            R.id.mnuPrithvi -> showAboutDialog()
            R.id.mnuMe -> startWebLinks("https://www.linkedin.com/in/soumenganguly1990")
            R.id.navAbtTechgig -> startWebLinks("https://www.techgig.com/")
        }
    }

    /**
     * Starts a link with apps taking action_view as their intent filter
     * @param link
     */
    private fun startWebLinks(link: String) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(link)
        startActivity(i)
    }

    private fun spawnDialog() {
        pDialog = ProgressDialog.show(this@DashboardActivity,
                getString(R.string.app_name), "Please wait...")
    }

    private fun dismissDialog() {
        try {
            pDialog.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun proceedToShowCountryData() {
        if (AppCommonValues.isInternetAvailable(this@DashboardActivity)) {
            apiService = ApiClient.getClient().create(ApiInterface::class.java)
            spawnDialog()
            call = apiService.getAllCountryList()
            call.enqueue(object : Callback<List<Country>> {
                override fun onResponse(call: Call<List<Country>>, response: Response<List<Country>>) {
                    allCountryList = ArrayList(response.body())
                    startBackingUpData(allCountryList)
                    dismissDialog()
                }

                override fun onFailure(call: Call<List<Country>>, t: Throwable) {
                    toast(R.string.nocountrydata)
                    proceedWithOfflineRoutine()
                }
            })
        } else {
            proceedWithOfflineRoutine()
        }
    }

    /**
     * Start backing up data in a background thread(here asynctask)
     */
    private fun startBackingUpData(data: ArrayList<Country>) {
        var b1: BackUpCountryData = BackUpCountryData(this@DashboardActivity, data)
        b1.execute()
        b1.mDataBackupInterface = this@DashboardActivity
    }

    override fun onBackupCompleted(done: Boolean, countryModelArrayList: ArrayList<CountryModel>) {
        this.countryModelArrayList = countryModelArrayList
        populateCountryList(countryModelArrayList)
    }

    /**
     * Populates the recyclerview with data
     */
    private fun populateCountryList(data: ArrayList<CountryModel>) {
        allCountryAdapter = CountryAdapter(this@DashboardActivity, data)
        allCountryAdapter.mSearchResultInterface = this@DashboardActivity
        rclCountryList.adapter = allCountryAdapter
    }

    private fun populateSearchedCountryList(data: ArrayList<CountryModel>) {
        searchedCountryAdapter = CountryAdapter(this@DashboardActivity, data)
        rclSearchedList.adapter = searchedCountryAdapter
    }

    private fun makeActualListVisibleSearchListInvisible() {
        rclCountryList.visibility = View.VISIBLE
        rclSearchedList.visibility = View.GONE
        searchedCountryAdapter = null
    }

    private fun makeSearchListVisibleActualListInvisible() {
        rclSearchedList.visibility = View.VISIBLE
        rclCountryList.visibility = View.GONE
    }

    private fun proceedWithOfflineRoutine() {
        var builder: AlertDialog.Builder = AlertDialog.Builder(this@DashboardActivity)
        builder.setTitle(R.string.app_name)
        builder.setMessage(R.string.youareoffline)
        builder.setPositiveButton("Yes", DialogInterface.OnClickListener { dialogInterface, i ->
            dialogInterface.dismiss()
            checkIfOfflineDataIsAvailable()
        })
        builder.setNegativeButton("No", DialogInterface.OnClickListener { dialogInterface, i ->
            dialogInterface.dismiss()
            longToast(R.string.wifiadvice)
        })
        builder.show()
    }

    private fun checkIfOfflineDataIsAvailable() {
        Realm.init(this@DashboardActivity)
        realm = Realm.getDefaultInstance()
        var query: RealmQuery<CountryModel> = realm.where(CountryModel::class.java)
        var results: RealmResults<CountryModel> = query.findAll()
        results.load()
        if (results.size > 0) {
            this.countryModelArrayList = ArrayList()
            countryModelArrayList!!.addAll(ArrayList(realm.copyFromRealm(results)))
        } else {
            showNoOfflineDataFoundDialog()
            return
        }
        realm.close()
        populateCountryList(countryModelArrayList!!)
    }

    private fun showNoOfflineDataFoundDialog() {
        var builder: AlertDialog.Builder = AlertDialog.Builder(this@DashboardActivity)
        builder.setTitle(R.string.app_name)
        builder.setMessage(R.string.noofflinedata)
        builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
            dialogInterface.dismiss()
        })
        builder.show()
    }

    /**
     * Shows a custom dialog with some info on it
     */
    private fun showAboutDialog() {
        val aboutDialog = Dialog(this@DashboardActivity)
        aboutDialog.setContentView(R.layout.aboutdialog)
        val btnAboutOk = aboutDialog.findViewById<AppCompatButton>(R.id.btnAboutClose)
        aboutDialog.setCancelable(true)
        aboutDialog.setCanceledOnTouchOutside(true)
        btnAboutOk.setOnClickListener { aboutDialog.dismiss() }
        aboutDialog.show()
    }

    private fun showRecyncDialog() {
        var builder: AlertDialog.Builder = AlertDialog.Builder(this@DashboardActivity)
        builder.setTitle(R.string.app_name)
        builder.setMessage(R.string.sync)
        builder.setPositiveButton("Yes", DialogInterface.OnClickListener { dialogInterface, i ->
            dialogInterface.dismiss()
            proceedToShowCountryData()
        })
        builder.setNegativeButton("No", DialogInterface.OnClickListener { dialogInterface, i ->
            dialogInterface.dismiss()
        })
        builder.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_dashboard, menu)

        searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        var searchMenuItem = menu.findItem(R.id.mnuSearch)
        searchView = searchMenuItem.getActionView() as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()))
        searchView.setSubmitButtonEnabled(true)
        searchView.setOnQueryTextListener(this)
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query.toString().equals("")) {
            toast(R.string.nothingentered)
        } else {
            if(countryModelArrayList == null || countryModelArrayList!!.size == 0) {
                /* do nothing, no data is present */
            } else {
                allCountryAdapter.filter.filter(query)
            }
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText.toString().equals("")) {
            makeActualListVisibleSearchListInvisible()
        } else {
            if(countryModelArrayList == null || countryModelArrayList!!.size == 0) {
                /* do nothing, no data */
            } else {
                allCountryAdapter.filter.filter(newText)
            }
        }
        return true
    }

    override fun onSearchResultGenerated(searchResults: java.util.ArrayList<CountryModel>?) {
        if (searchResults == null || searchResults.size == 0) {
            makeActualListVisibleSearchListInvisible()
        } else {
            makeSearchListVisibleActualListInvisible()
            populateSearchedCountryList(searchResults!!)
        }
    }

    @SuppressLint("ServiceCast")
    private fun isKeyboardShown(): Boolean {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return imm.isAcceptingText()
    }

    private fun closeKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mnuSync -> {
                if(!searchView.isIconified) {
                    toast(R.string.pleaseclosesearch)
                } else {
                    showRecyncDialog()
                }
            }
        }
        return true
    }
}