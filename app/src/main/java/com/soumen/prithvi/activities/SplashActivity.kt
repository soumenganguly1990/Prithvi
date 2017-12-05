package com.soumen.prithvi.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import butterknife.BindView
import butterknife.ButterKnife
import com.airbnb.lottie.LottieAnimationView
import com.soumen.prithvi.R
import com.soumen.prithvi.extras.AppCommonValues
import com.soumen.prithvi.utils.TinyDB

class SplashActivity : AppCompatActivity() {

    @BindView(R.id.imgLottieView)
    lateinit var imgLottieView: LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)
        ButterKnife.bind(this)

        readWhetherFirstTimeOrNot()
    }

    /**
     * Check if the app is being opened for the first time or not
     */
    fun readWhetherFirstTimeOrNot() {
        val tinyDB = TinyDB(this@SplashActivity)
        if (!tinyDB.getBoolean(AppCommonValues._ISFIRSTRUNTAG)) {
            /* this is the first run, so redirect to intro page */
            tinyDB.putBoolean(AppCommonValues._ISFIRSTRUNTAG, true)
            reDirectToNextPageAferADelay(AppCommonValues._INTROPAGE)
        } else {
            reDirectToNextPageAferADelay(AppCommonValues._DASHBOARDPAGE)
        }
    }

    /**
     * Redirects user to the next page/ screen
     * @param whichPage
     */
    private fun reDirectToNextPageAferADelay(whichPage: Int) {
        val mHandler = Handler()
        mHandler.postDelayed(Runnable {
            mHandler.removeCallbacks { this }
            imgLottieView.pauseAnimation()
            when (whichPage) {
                AppCommonValues._INTROPAGE -> {
                    startActivity(Intent(this@SplashActivity, IntroActivity::class.java))
                }
                AppCommonValues._DASHBOARDPAGE -> {
                    startActivity(Intent(this@SplashActivity, DashboardActivity::class.java))
                }
            }
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
            this@SplashActivity.finish()
        }, AppCommonValues._REDIRECTDELAY.toLong())
    }
}