package com.matheusfroes.lolfreeweek.activities


import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.preference.ListPreference
import android.preference.PreferenceActivity
import android.preference.PreferenceFragment
import android.view.MenuItem
import com.matheusfroes.lolfreeweek.R
import com.matheusfroes.lolfreeweek.db.ChampionDAO
import com.matheusfroes.lolfreeweek.db.SkinDAO
import com.matheusfroes.lolfreeweek.db.SpellDAO
import com.matheusfroes.lolfreeweek.models.Champion
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.LibsBuilder
import net.rithms.riot.api.RiotApi
import net.rithms.riot.constant.Region
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
    val preferences: SharedPreferences by lazy {
        getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupActionBar()
        addPreferencesFromResource(R.xml.pref_general)

        val updateListPreference = findPreference("update_list")
        val aboutApp = findPreference("about_app")
        val chooseRegion = findPreference("choose_region") as ListPreference
        val regions = resources.getStringArray(R.array.lol_regions_values)

        var region = preferences.getString("REGION", "NA")
        var index = regions.indexOf(region)
        chooseRegion.setValueIndex(index)

        chooseRegion.setOnPreferenceClickListener {
            region = preferences.getString("REGION", "NA")
            index = regions.indexOf(region)
            chooseRegion.setValueIndex(index)
            true
        }


        chooseRegion.setOnPreferenceChangeListener { preference, newValue ->
            preferences.edit().putString("REGION", newValue.toString()).apply()
            true
        }


        updateListPreference.setOnPreferenceClickListener {
            remakeChampionsList()
            true
        }

        aboutApp.setOnPreferenceClickListener {
            LibsBuilder()
                    //provide a style (optional) (LIGHT, DARK, LIGHT_DARK_TOOLBAR)
                    .withActivityStyle(Libs.ActivityStyle.DARK)
                    .withAboutIconShown(true)
                    .withAutoDetect(true)
                    .withAboutSpecial1("RIOT LICENSE")
                    .withAboutSpecial1Description(getString(R.string.license_riot))
                    .withAboutSpecial2("ICON LICENSE")
                    .withAboutSpecial2Description("<div>Icons made by <a href='http://www.freepik.com' title='Freepik'>Freepik</a> from <a href='http://www.flaticon.com' title='Flaticon'>www.flaticon.com</a> is licensed by <a href='http://creativecommons.org/licenses/by/3.0/' title='Creative Commons BY 3.0' target='_blank'>CC 3.0 BY</a></div>")
                    .withAboutDescription("<a href='https://github.com/froesmatheus'>Matheus Fróes Marques</a>")
                    .withActivityTitle(getString(R.string.about_app))
                    .start(this)
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

            val region = Region.valueOf(preferences.getString("REGION", "NA"))
            val locale = preferences.getString("LOCALE", "en_US")

            val response = api.getDataChampionList(region, locale, null, true, ChampData.IMAGE, ChampData.SKINS, ChampData.SPELLS, ChampData.LORE)

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
