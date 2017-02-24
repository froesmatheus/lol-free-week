package com.matheusfroes.lolfreeweek.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutCompat
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.view.View
import com.matheusfroes.lolfreeweek.R
import com.matheusfroes.lolfreeweek.adapters.ChampionSkinAdapter
import com.matheusfroes.lolfreeweek.db.ChampionDAO
import com.matheusfroes.lolfreeweek.models.Champion
import com.matheusfroes.lolfreeweek.models.Spell
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.champion_details_content.*

class ChampionDetailsActivity : AppCompatActivity(), View.OnClickListener {
    val dao by lazy { ChampionDAO(this) }
    var champion: Champion? = null
    val preferences: SharedPreferences by lazy {
        getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.champion_details_content)

        if (intent == null) return

        val currentApiVersion = preferences.getString("CURRENT_API_VERSION", "7.2.1")
        val championId = intent.getIntExtra("championId", -1)

        champion = dao.getChampion(championId.toLong())

        title = champion?.name


        tvChampionName.text = champion?.name
        tvChampionTitle.text = champion?.title
        tvChampionLore.text = Html.fromHtml(champion?.lore)

        rvChampionSkins.adapter = ChampionSkinAdapter(this, champion!!.skins)
        rvChampionSkins.layoutManager = LinearLayoutManager(this, LinearLayoutCompat.HORIZONTAL, false)
        rvChampionSkins.itemAnimator = DefaultItemAnimator()


        Picasso
                .with(this)
                .load("http://ddragon.leagueoflegends.com/cdn/$currentApiVersion/img/champion/${champion?.image}")
                .fit()
                .centerCrop()
                .into(ivChampionImage)

        Picasso
                .with(this)
                .load("http://ddragon.leagueoflegends.com/cdn/$currentApiVersion/img/spell/${champion!!.spells[0].image}")
                .fit()
                .centerCrop()
                .into(ivSpellQ)

        Picasso
                .with(this)
                .load("http://ddragon.leagueoflegends.com/cdn/$currentApiVersion/img/spell/${champion!!.spells[1].image}")
                .fit()
                .centerCrop()
                .into(ivSpellW)

        Picasso
                .with(this)
                .load("http://ddragon.leagueoflegends.com/cdn/$currentApiVersion/img/spell/${champion!!.spells[2].image}")
                .fit()
                .centerCrop()
                .into(ivSpellE)

        Picasso
                .with(this)
                .load("http://ddragon.leagueoflegends.com/cdn/$currentApiVersion/img/spell/${champion!!.spells[3].image}")
                .fit()
                .centerCrop()
                .into(ivSpellR)

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
