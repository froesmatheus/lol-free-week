package com.matheusfroes.lolfreeweek.ui.championdetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutCompat
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.view.View
import com.matheusfroes.lolfreeweek.R
import com.matheusfroes.lolfreeweek.data.dao.ChampionDAO
import com.matheusfroes.lolfreeweek.data.dto.SkinWithChampionName
import com.matheusfroes.lolfreeweek.data.model.Champion
import com.matheusfroes.lolfreeweek.data.model.Spell
import com.matheusfroes.lolfreeweek.data.source.UserPreferences
import com.matheusfroes.lolfreeweek.extra.appInjector
import com.matheusfroes.lolfreeweek.extra.loadImage
import kotlinx.android.synthetic.main.champion_details_content.*
import javax.inject.Inject

class ChampionDetailsActivity : AppCompatActivity(), View.OnClickListener {
    @Inject
    lateinit var preferences: UserPreferences

    val dao by lazy { ChampionDAO(this) }
    var champion: Champion? = null
    val adapter: ChampionSkinAdapter by lazy { ChampionSkinAdapter() }
    var championId: Long = -1

    companion object {
        private const val CHAMPION_ID = "champion_id"

        fun start(context: Context, championId: Int) {
            val intent = Intent(context, ChampionDetailsActivity::class.java)
            intent.putExtra(CHAMPION_ID, championId.toLong())
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.champion_details_content)
        appInjector.inject(this)

        if (intent == null) return

        val currentApiVersion = preferences.currentApiVersion
        championId = intent.getLongExtra(CHAMPION_ID, -1)

        scrollView.post {
            scrollView.scrollTo(0, 0)
        }

        champion = dao.getChampion(championId)

        title = champion?.name

        tvChampionName.text = champion?.name
        tvChampionTitle.text = champion?.title
        tvChampionLore.text = Html.fromHtml(champion?.lore)

        val skinsWithChampionName = champion?.skins.orEmpty().map { skin ->
            SkinWithChampionName(skin, champion?.key.orEmpty())
        }
        adapter.skins = skinsWithChampionName
        rvChampionSkins.adapter = adapter
        rvChampionSkins.layoutManager = LinearLayoutManager(this, LinearLayoutCompat.HORIZONTAL, false)
        rvChampionSkins.itemAnimator = DefaultItemAnimator()



        ivChampionImage.loadImage("http://ddragon.leagueoflegends.com/cdn/$currentApiVersion/img/champion/${champion?.image}")

        ivSpellQ.loadImage("http://ddragon.leagueoflegends.com/cdn/$currentApiVersion/img/spell/${champion!!.spells[0].image}")
        ivSpellW.loadImage("http://ddragon.leagueoflegends.com/cdn/$currentApiVersion/img/spell/${champion!!.spells[1].image}")
        ivSpellE.loadImage("http://ddragon.leagueoflegends.com/cdn/$currentApiVersion/img/spell/${champion!!.spells[2].image}")
        ivSpellR.loadImage("http://ddragon.leagueoflegends.com/cdn/$currentApiVersion/img/spell/${champion!!.spells[3].image}")

        ivSpellQ.setOnClickListener(this)
        ivSpellW.setOnClickListener(this)
        ivSpellE.setOnClickListener(this)
        ivSpellR.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        val spell = when (v.id) {
            R.id.ivSpellQ -> champion!!.spells[0]
            R.id.ivSpellW -> champion!!.spells[1]
            R.id.ivSpellE -> champion!!.spells[2]
            R.id.ivSpellR -> champion!!.spells[3]
            else -> null
        }

        showSpellDialog(spell!!)
    }

    private fun showSpellDialog(spell: Spell) {
        val dialog = AlertDialog.Builder(this)
                .setTitle(spell.name)
                .setMessage(Html.fromHtml(spell.description))
                .create()

        dialog.show()
    }
}
