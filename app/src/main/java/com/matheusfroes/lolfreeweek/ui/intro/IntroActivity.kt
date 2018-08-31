package com.matheusfroes.lolfreeweek.ui.intro

import agency.tango.materialintroscreen.MaterialIntroActivity
import agency.tango.materialintroscreen.SlideFragmentBuilder
import android.content.Intent
import android.os.Bundle
import com.matheusfroes.lolfreeweek.R
import com.matheusfroes.lolfreeweek.data.UserPreferences
import com.matheusfroes.lolfreeweek.appInjector
import com.matheusfroes.lolfreeweek.ui.freeweeklist.FreeWeekList
import javax.inject.Inject


class IntroActivity : MaterialIntroActivity() {

    @Inject
    lateinit var preferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appInjector.inject(this)

        addSlide(SlideFragmentBuilder()
                .backgroundColor(R.color.colorPrimary)
                .buttonsColor(R.color.colorPrimaryDark)
                .image(R.drawable.intro_image)
                .title(resources.getString(R.string.app_name))
                .description(getString(R.string.intro_1))
                .build())

        addSlide(SlideFragmentBuilder()
                .backgroundColor(R.color.colorPrimary)
                .buttonsColor(R.color.colorPrimaryDark)
                .image(R.drawable.intro_image2)
                .title(getString(R.string.info_intro_1_title))
                .description(getString(R.string.info_intro_1_description))
                .build())

        addSlide(SlideFragmentBuilder()
                .backgroundColor(R.color.colorPrimary)
                .buttonsColor(R.color.colorPrimaryDark)
                .image(R.drawable.intro_image3)
                .title(getString(R.string.info_intro_2_title))
                .description(resources.getString(R.string.alert_message))
                .build())
    }

    override fun onFinish() {
        preferences.firstAccess = false
        startActivity(Intent(applicationContext, FreeWeekList::class.java))
        finish()
    }

}
