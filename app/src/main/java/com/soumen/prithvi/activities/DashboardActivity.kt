package com.soumen.prithvi.activities

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import butterknife.BindView
import butterknife.ButterKnife
import com.soumen.prithvi.backgroundtasks.BackUpCountryData
import com.soumen.prithvi.R
import com.soumen.prithvi.adapters.CountryAdapter
import com.soumen.prithvi.callbackinterfaces.DataBackupInterface
import com.soumen.prithvi.dbops.CountryModel
import com.soumen.prithvi.extras.AppCommonValues
import com.soumen.prithvi.models.Country
import com.soumen.prithvi.rest.ApiInterface
import com.soumen.prithvi.rest.ApiClient
import io.realm.Realm
import io.realm.RealmQuery
import io.realm.RealmResults
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardActivity : AppCompatActivity(), DataBackupInterface {

    @BindView(R.id.prithviDrawer)
    lateinit var dupliDrawer: DrawerLayout
    @BindView(R.id.navMenu)
    lateinit var navMenu: NavigationView
    @BindView(R.id.prithviToolbar)
    lateinit var prithviToolbar: Toolbar
    @BindView(R.id.rclCountryList)
    lateinit var rclCountryList: RecyclerView

    lateinit var call: Call<List<Country>>
    lateinit var apiService: ApiInterface
    lateinit var allCountryList: ArrayList<Country>
    var countryModelArrayList: ArrayList<CountryModel>? = null
    lateinit var allCountryAdapter: CountryAdapter

    lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_dashboard)
        ButterKnife.bind(this)

        rclCountryList.setLayoutManager(LinearLayoutManager(this@DashboardActivity))

        setSupportActionBar(prithviToolbar)
        setUpPrithviDrawer()
        setUpNavigationController()
        proceedToShowCountryData()
    }

    override fun onBackPressed() {
        /* overriding backpress, app just needs to be minimized */
        moveTaskToBack(true)
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

    private fun proceedToShowCountryData() {
        if(AppCommonValues.isInternetAvailable(this@DashboardActivity)) {
            apiService = ApiClient.getClient().create(ApiInterface::class.java)
            call = apiService.getAllCountryList()
            call.enqueue(object : Callback<List<Country>> {
                override fun onResponse(call: Call<List<Country>>, response: Response<List<Country>>) {
                        AppCommonValues.DIDRESULTCAMEAFTERRESTCALL = true
                        allCountryList = ArrayList(response.body())
                        startBackingUpData(allCountryList)
                }
                override fun onFailure(call: Call<List<Country>>, t: Throwable) {
                    Log.e("Failed", t.localizedMessage)
                }
            })
        } else {
            AppCommonValues.DIDRESULTCAMEAFTERRESTCALL = false
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
        toast("data backup completed")
        this.countryModelArrayList = countryModelArrayList
        populateCountryList(countryModelArrayList)
    }

    /**
     * Populates the recyclerview with data
     */
    private fun populateCountryList(data: ArrayList<CountryModel>) {
        allCountryAdapter = CountryAdapter(this@DashboardActivity, data)
        rclCountryList.adapter = allCountryAdapter
    }

    private fun proceedWithOfflineRoutine() {
        var builder: AlertDialog.Builder = AlertDialog.Builder(this@DashboardActivity)
        builder.setTitle(R.string.app_name)
        builder.setMessage("You are offline. Would you like to work offline with previously downloaded data?")
        builder.setPositiveButton("Yes", DialogInterface.OnClickListener { dialogInterface, i ->
            checkIfOfflineDataIsAvailable()
        })
        builder.setNegativeButton("No", DialogInterface.OnClickListener { dialogInterface, i ->
            toast("Please turn on your wifi or mobile data to download details")
        })
        builder.show()
    }

    private fun checkIfOfflineDataIsAvailable() {
        Realm.init(this@DashboardActivity)
        realm = Realm.getDefaultInstance()
        var query: RealmQuery<CountryModel> = realm.where(CountryModel::class.java)
        var results: RealmResults<CountryModel> = query.findAll()
        if(results.size > 0) {
            Log.e("found", "data found")
            populateCountryList(ArrayList(results))
        } else {
            toast("Oopsie, no data")
        }
    }

    /**
     * Shows a custom dialog with some info on it
     */
    private fun showAboutDialog() {
        /*val aboutDialog = Dialog(this@DashboardActivity)
        aboutDialog.setContentView(R.layout.aboutdialog)
        val btnAboutOk = aboutDialog.findViewById(R.id.btnAboutOk) as AppCompatButton
        aboutDialog.setCancelable(true)
        aboutDialog.setCanceledOnTouchOutside(true)
        btnAboutOk.setOnClickListener { aboutDialog.dismiss() }
        aboutDialog.show()*/
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_dashboard, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.mnuSearch -> {
            }
            R.id.mnuRefresh -> {
            }
        }
        return true
    }
}