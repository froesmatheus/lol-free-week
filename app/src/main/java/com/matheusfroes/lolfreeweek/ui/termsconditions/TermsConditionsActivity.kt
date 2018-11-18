package com.matheusfroes.lolfreeweek.ui.termsconditions

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.matheusfroes.lolfreeweek.R
import kotlinx.android.synthetic.main.activity_terms_conditions.*

class TermsConditionsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_conditions)

        webView.loadUrl("file:///android_res/raw/terms_and_conditions.html")
    }
}
