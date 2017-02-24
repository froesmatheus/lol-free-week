package com.matheusfroes.lolfreeweek.activities

import agency.tango.materialintroscreen.MaterialIntroActivity
import agency.tango.materialintroscreen.SlideFragmentBuilder
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import com.matheusfroes.lolfreeweek.R


class IntroActivity : MaterialIntroActivity() {
    val preferences: SharedPreferences by lazy {
        getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        preferences.edit().putBoolean("FIRST_ACCESS", false).apply()
        startActivity(Intent(applicationContext, MainActivity::class.java))
        finish()
    }

}
