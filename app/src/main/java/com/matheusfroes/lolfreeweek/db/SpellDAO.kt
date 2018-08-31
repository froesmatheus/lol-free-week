package com.matheusfroes.lolfreeweek.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.matheusfroes.lolfreeweek.data.model.Spell


class SpellDAO(context: Context) {
    val db: SQLiteDatabase by lazy {
        Db(context).writableDatabase
    }

    fun insert(spell: Spell, championId: Int): Boolean {
        val cv = ContentValues()

        cv.put(Db.COLUMN_NAME_SPELLS, spell.name)
        cv.put(Db.COLUMN_IMAGE_SPELLS, spell.image)
        cv.put(Db.COLUMN_DESCRIPTION_SPELLS, spell.description)
        cv.put(Db.COLUMN_CHAMPION_ID_SPELLS, championId)

        val insertedId = db.insert(Db.TABLE_SPELLS, null, cv)

        return insertedId != -1L
    }

    fun insertList(spells: List<Spell>, championId: Int, db: SQLiteDatabase) {
        val cv = ContentValues()

        spells.forEach { spell ->
            cv.put(Db.COLUMN_NAME_SPELLS, spell.name)
            cv.put(Db.COLUMN_IMAGE_SPELLS, spell.image)
            cv.put(Db.COLUMN_DESCRIPTION_SPELLS, spell.description)
            cv.put(Db.COLUMN_CHAMPION_ID_SPELLS, championId)

            db.insert(Db.TABLE_SPELLS, null, cv)
        }
    }

    fun delete(spellId: Int): Boolean {
        val rows = db.delete(Db.TABLE_SPELLS, "${Db.COLUMN_ID_SPELLS} = ?", arrayOf(spellId.toString()))

        return rows != 0
    }

    fun deleteAll() = db.delete(Db.TABLE_SPELLS, "1", null)


    fun getSpellsByChampionId(championId: Int): MutableList<Spell> {
        val spells = mutableListOf<Spell>()

        val cursor = db.rawQuery("SELECT * FROM ${Db.TABLE_SPELLS} WHERE ${Db.COLUMN_CHAMPION_ID_SPELLS} = ?",
                arrayOf(championId.toString()))

        if (cursor.count > 0) {
            cursor.moveToFirst()

            do {
                val name = cursor.getString(cursor.getColumnIndex(Db.COLUMN_NAME_SPELLS))
//                val championId = cursor.getInt(cursor.getColumnIndex(Db.COLUMN_CHAMPION_ID_SPELLS))
                val description = cursor.getString(cursor.getColumnIndex(Db.COLUMN_DESCRIPTION_SPELLS))
                val image = cursor.getString(cursor.getColumnIndex(Db.COLUMN_IMAGE_SPELLS))
                val id = cursor.getInt(cursor.getColumnIndex(Db.COLUMN_ID_SPELLS))

                val spell = Spell(id, image, description, name)

                spells.add(spell)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return spells
    }
}
