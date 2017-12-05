package com.soumen.prithvi.activities

import android.app.Dialog
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
import com.soumen.prithvi.R
import com.soumen.prithvi.adapters.CountryAdapter
import com.soumen.prithvi.extras.AppCommonValues
import com.soumen.prithvi.models.Country
import com.soumen.prithvi.rest.ApiInterface
import com.soumen.prithvi.rest.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardActivity : AppCompatActivity() {

    @BindView(R.id.prithviDrawer)
    lateinit var dupliDrawer: DrawerLayout
    @BindView(R.id.navMenu)
    lateinit var navMenu: NavigationView
    @BindView(R.id.prithviToolbar)
    lateinit var prithviToolbar: Toolbar
    @BindView(R.id.rclCountryList)
    lateinit var rclCountryList: RecyclerView

    lateinit var allCountryList: ArrayList<Country>
    lateinit var allCountryAdapter: CountryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_dashboard)
        ButterKnife.bind(this)

        rclCountryList.setLayoutManager(LinearLayoutManager(this@DashboardActivity));

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
            val apiService = ApiClient.getClient().create(ApiInterface::class.java)
            val call = apiService.getAllCountryList()
            call.enqueue(object : Callback<List<Country>> {
                override fun onResponse(call: Call<List<Country>>, response: Response<List<Country>>) {
                    for(i in 0 until response.body()!!.size) {
                        allCountryList = ArrayList(response.body()!!)
                        allCountryAdapter = CountryAdapter(this@DashboardActivity, allCountryList)
                        rclCountryList.adapter = allCountryAdapter
                    }
                }
                override fun onFailure(call: Call<List<Country>>, t: Throwable) {
                    Log.e("Failed", t.localizedMessage)
                }
            })
        } else {
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