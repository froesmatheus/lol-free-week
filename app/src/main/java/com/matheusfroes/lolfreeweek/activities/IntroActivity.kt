package com.matheusfroes.lolfreeweek.activities

import agency.tango.materialintroscreen.MaterialIntroActivity
import agency.tango.materialintroscreen.SlideFragmentBuilder
import android.os.Bundle
import com.matheusfroes.lolfreeweek.R


class IntroActivity : MaterialIntroActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addSlide(SlideFragmentBuilder()
                .backgroundColor(R.color.colorPrimary)
                .buttonsColor(R.color.colorPrimaryDark)
                .image(agency.tango.materialintroscreen.R.drawable.ic_next)
                .title(resources.getString(R.string.app_name))
                .description("Veja os campeões que estão na Free Week no LOL")
                .build())

        addSlide(SlideFragmentBuilder()
                .backgroundColor(R.color.colorPrimary)
                .buttonsColor(R.color.colorPrimaryDark)
                .image(R.drawable.intro_image)
                .title("Informações sobre os campeões")
                .description("Veja as informações sobre os campeões \n(Habilidades, Skins, Descrição)")
                .build())

        addSlide(SlideFragmentBuilder()
                .backgroundColor(R.color.colorPrimary)
                .buttonsColor(R.color.colorPrimaryDark)
                .image(R.drawable.intro_image2)
                .title("Alertas")
                .description(resources.getString(R.string.alert_message))
                .build())
    }
}
