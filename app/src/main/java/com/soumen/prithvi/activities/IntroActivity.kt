package com.soumen.prithvi.activities

import agency.tango.materialintroscreen.MaterialIntroActivity
import android.os.Bundle
import com.soumen.prithvi.R
import agency.tango.materialintroscreen.MessageButtonBehaviour
import agency.tango.materialintroscreen.SlideFragmentBuilder
import android.view.View
import butterknife.BindString
import butterknife.ButterKnife
import org.jetbrains.anko.toast

/**
 * Created by Soumen on 11/26/2017.
 */
class IntroActivity : MaterialIntroActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ButterKnife.bind(this)

        enableLastSlideAlphaExitTransition(true)
        backButtonTranslationWrapper
                .setEnterTranslation { view, percentage -> view.alpha = percentage }

        /* Add first slide to page */
        addSlide(SlideFragmentBuilder()
                .backgroundColor(R.color.colorPrimary)
                .buttonsColor(R.color.colorAccent)
                .image(R.drawable.earth)
                .title(firstslideheader)
                .description(firstslidecontent)
                .build())

        addSlide(SlideFragmentBuilder()
                .backgroundColor(R.color.colorPrimary)
                .buttonsColor(R.color.colorAccent)
                .image(R.mipmap.ic_launcher)
                .title(secondslideheader)
                .description(secondslidecontent)
                .build())

        addSlide(SlideFragmentBuilder()
                .backgroundColor(R.color.colorPrimary)
                .buttonsColor(R.color.colorAccent)
                .image(R.mipmap.ic_launcher)
                .title(thirdslidesheader)
                .description(thirdslidecontent)
                .build(),
                MessageButtonBehaviour(View.OnClickListener {
                    toast(viewgithub)
                }, github))
    }
}