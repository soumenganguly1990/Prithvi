package com.soumen.prithvi.activities

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.soumen.prithvi.R
import butterknife.BindString
import butterknife.BindView
import butterknife.ButterKnife
import com.soumen.prithvi.adapters.IntroPagerAdapter
import com.soumen.prithvi.utils.FadePageTransformer

/**
 * Created by Soumen on 11/26/2017.
 */
class IntroActivity : AppCompatActivity() {

    @BindString(R.string.firstslideheader)
    lateinit var firstslideheader: String
    @BindString(R.string.firstslidecontent)
    lateinit var firstslidecontent: String
    @BindString(R.string.secondslideheader)
    lateinit var secondslideheader: String
    @BindString(R.string.secondslidecontent)
    lateinit var secondslidecontent: String
    @BindString(R.string.thirdslidesheader)
    lateinit var thirdslidesheader: String
    @BindString(R.string.thirdslidecontent)
    lateinit var thirdslidecontent: String
    @BindString(R.string.github)
    lateinit var github: String
    @BindString(R.string.viewgithub)
    lateinit var viewgithub: String

    @BindView(R.id.view_pager)
    lateinit var viewPager: ViewPager
    @BindView(R.id.layoutDots)
    lateinit var dotsLayout: LinearLayout
    @BindView(R.id.btn_skip)
    lateinit var btnSkip: Button
    @BindView(R.id.btn_next)
    lateinit var btnNext: Button

    private lateinit var myViewPagerAdapter: IntroPagerAdapter
    private lateinit var layouts: IntArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= 21) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        setContentView(R.layout.activity_intro)
        ButterKnife.bind(this@IntroActivity)

        layouts = intArrayOf(R.layout.fragment_intro1, R.layout.fragment_intro2, R.layout.fragment_intro3)

        addBottomDots(0)
        changeStatusBarColor()

        myViewPagerAdapter = IntroPagerAdapter(this@IntroActivity, layouts)
        viewPager.adapter = myViewPagerAdapter
        viewPager.setPageTransformer(true, FadePageTransformer())
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener)

        btnSkip.setOnClickListener { launchHomeScreen() }

        btnNext.setOnClickListener {
            val current = getItem(+1)
            if (current < layouts.size) {
                viewPager.currentItem = current
            } else {
                launchHomeScreen()
            }
        }
    }

    /**
     * This method is used to add dot like textview with html based textcontent that is a dot
     */
    fun addBottomDots(currentPage: Int) {
        var size: Int = layouts.size
        val colorsActive: IntArray = resources.getIntArray(R.array.array_dot_active)
        val colorsInactive: IntArray = resources.getIntArray(R.array.array_dot_inactive)
        dotsLayout.removeAllViews()
        for (i in 0 until size) {
            var txtView: TextView = TextView(this@IntroActivity)
            txtView.text = Html.fromHtml("&#8226;")
            txtView.textSize = 35f
            if (i == currentPage)
                txtView.setTextColor(colorsActive[currentPage])
            else
                txtView.setTextColor(colorsInactive[currentPage])
            dotsLayout.addView(txtView)
        }
    }

    private fun getItem(i: Int): Int {
        return viewPager.currentItem + i
    }

    private fun launchHomeScreen() {
        startActivity(Intent(this@IntroActivity, DashboardActivity::class.java))
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
        finish()
    }

    internal var viewPagerPageChangeListener: ViewPager.OnPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            addBottomDots(position)
            if (position == layouts.size - 1) {
                btnNext.text = getString(R.string.start)
                btnSkip.visibility = View.GONE
            } else {
                btnNext.text = getString(R.string.next)
                btnSkip.visibility = View.VISIBLE
            }
        }
        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
        override fun onPageScrollStateChanged(arg0: Int) {}
    }

    /**
     * This code makes the status bar panel transparent
     */
    private fun changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
    }
}