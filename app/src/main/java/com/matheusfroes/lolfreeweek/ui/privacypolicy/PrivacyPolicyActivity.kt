package com.matheusfroes.lolfreeweek.ui.privacypolicy

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.matheusfroes.lolfreeweek.R
import kotlinx.android.synthetic.main.activity_privacy_policy.*

class PrivacyPolicyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_policy)

        webView.loadUrl("file:///android_res/raw/privacy_policy.html")
    }
}
