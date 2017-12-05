package com.soumen.prithvi.utils

import android.support.v4.view.ViewPager
import android.view.View

/**
 * Created by Soumen on 04-12-2017.
 */
class FadePageTransformer : ViewPager.PageTransformer {
    override fun transformPage(view: View, position: Float) {
        view.alpha = 1 - Math.abs(position)
        if (position < 0) {
            view.scrollX = (view.width.toFloat() * position).toInt()
        } else if (position > 0) {
            view.scrollX = -(view.width.toFloat() * -position).toInt()
        } else {
            view.scrollX = 0
        }
    }
}