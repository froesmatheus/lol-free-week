package com.matheusfroes.lolfreeweek.activities


import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceActivity
import android.preference.PreferenceFragment
import android.view.MenuItem
import com.matheusfroes.lolfreeweek.R
import com.matheusfroes.lolfreeweek.db.ChampionDAO
import com.matheusfroes.lolfreeweek.db.SkinDAO
import com.matheusfroes.lolfreeweek.db.SpellDAO
import com.matheusfroes.lolfreeweek.models.Champion
import net.rithms.riot.api.RiotApi
import net.rithms.riot.constant.staticdata.ChampData
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * A [PreferenceActivity] that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 *
 *
 * See [
   * Android Design: Settings](http://developer.android.com/design/patterns/settings.html) for design guidelines and the [Settings
   * API Guide](http://developer.android.com/guide/topics/ui/settings.html) for more information on developing a Settings UI.
 */
class SettingsActivity : AppCompatPreferenceActivity() {
    val championDAO by lazy {
        ChampionDAO(this)
    }
    val spellsDAO by lazy {
        SpellDAO(this)
    }
    val skinsDAO by lazy {
        SkinDAO(this)
    }
    val api = RiotApi("RGAPI-0fc93c3d-27bb-4eec-bc2b-f110489aa27d")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupActionBar()
        addPreferencesFromResource(R.xml.pref_general)

        val updateListPreference = findPreference("update_list")
        val openIntroPreference = findPreference("open_intro")

        updateListPreference.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            remakeChampionsList()
            true
        }

        openIntroPreference.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            startActivity(Intent(applicationContext, IntroActivity::class.java))
            finish()
            true
        }
    }

    private fun remakeChampionsList() {
        val progress = ProgressDialog(this)
        progress.setMessage(getString(R.string.info_download_champion_information))
        progress.isIndeterminate = false
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        progress.show()
        doAsync {
            spellsDAO.deleteAll()
            skinsDAO.deleteAll()
            championDAO.deleteAll()

            val current = resources.configuration.locale

            val response = api.getDataChampionList(current.toString(), null, true, ChampData.IMAGE, ChampData.SKINS, ChampData.SPELLS, ChampData.LORE)

            var i = 1
            val champList = response.data.map {
                val champ = Champion()
                champ.copyChampion(it.value)
                progress.progress = i++
                champ
            }

            championDAO.insertList(champList)
            uiThread {
                progress.cancel()
            }
        }
    }


    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private fun isXLargeTablet(context: Context): Boolean {
        return context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_XLARGE
    }


    /**
     * Set up the [android.app.ActionBar], if the API is available.
     */
    private fun setupActionBar() {
        val actionBar = supportActionBar
        actionBar.setDisplayHomeAsUpEnabled(true)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                this.finish()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    /**
     * {@inheritDoc}
     */
    override fun onIsMultiPane(): Boolean {
        return isXLargeTablet(this)
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    override fun isValidFragment(fragmentName: String): Boolean {
        return PreferenceFragment::class.java.name == fragmentName
    }
}
