package com.matheusfroes.lolfreeweek.data.dao

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.matheusfroes.lolfreeweek.data.model.Champion
import timber.log.Timber
import javax.inject.Inject


class ChampionDAO @Inject constructor(context: Context) {
    private val db: SQLiteDatabase by lazy { Db(context).writableDatabase }
    private val spellDAO by lazy { SpellDAO(context) }
    private val skinDAO by lazy { SkinDAO(context) }

    fun insertList(champions: List<Champion>) {
        db.beginTransaction()
        try {
            val cv = ContentValues()

            champions.forEach { champion ->
                cv.put(Db.COLUMN_ID_CHAMPIONS, champion.id)
                cv.put(Db.COLUMN_NAME_CHAMPIONS, champion.name)
                cv.put(Db.COLUMN_KEY_CHAMPIONS, champion.key)
                cv.put(Db.COLUMN_IMAGE_CHAMPIONS, champion.image)
                cv.put(Db.COLUMN_LORE_CHAMPIONS, champion.lore)
                cv.put(Db.COLUMN_TITLE_CHAMPIONS, champion.title)

                spellDAO.insertList(champion.spells, champion.id, db)

                skinDAO.insertList(champion.skins, champion.id, db)

                db.insert(Db.TABLE_CHAMPIONS, null, cv)
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    fun addAlerts(champions: List<Champion>) {
        db.beginTransaction()
        try {
            val cv = ContentValues()

            champions.forEach { champion ->
                cv.put(Db.COLUMN_ID_CHAMPION, champion.id)

                db.insert(Db.TABLE_CHAMPION_ALERTS, null, cv)
            }
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Timber.e(e)
        } finally {
            db.endTransaction()
        }
    }

    fun deleteAlert(championId: Int) {
        db.delete(Db.TABLE_CHAMPION_ALERTS, "_id = ?", arrayOf(championId.toString()))
    }

    private fun deleteAll() = db.delete(Db.TABLE_CHAMPIONS, "1", null)

    fun deleteDB() {
        deleteAll()
        skinDAO.deleteAll()
        spellDAO.deleteAll()
    }

    fun getChampionsByAlert(alert: Boolean): MutableList<Champion> {
        val champions = mutableListOf<Champion>()

        val query = """
            SELECT c.*
            FROM ${Db.TABLE_CHAMPIONS} c
            WHERE ${if (alert) "EXISTS" else "NOT EXISTS"} (
                SELECT *
                FROM ${Db.TABLE_CHAMPION_ALERTS} a
                WHERE c._id = a._id)""".trimIndent()

        val cursor = db.rawQuery(query, null)

        if (cursor.count > 0) {
            cursor.moveToFirst()

            do {
                val name = cursor.getString(cursor.getColumnIndex(Db.COLUMN_NAME_CHAMPIONS))
                val id = cursor.getInt(cursor.getColumnIndex(Db.COLUMN_ID_CHAMPIONS))
                val image = cursor.getString(cursor.getColumnIndex(Db.COLUMN_IMAGE_CHAMPIONS))
                val title = cursor.getString(cursor.getColumnIndex(Db.COLUMN_TITLE_CHAMPIONS))
                val champion = Champion(name = name, title = title, id = id, image = image)

                champions.add(champion)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return champions
    }

    fun getChampion(championId: Long): Champion? {
        val cursor =
                db.rawQuery("SELECT * FROM ${Db.TABLE_CHAMPIONS} WHERE ${Db.COLUMN_ID_CHAMPIONS} = ?",
                        arrayOf(championId.toString()))

        if (cursor.count > 0) {
            cursor.moveToFirst()

            val name = cursor.getString(cursor.getColumnIndex(Db.COLUMN_NAME_CHAMPIONS))
            val id = cursor.getInt(cursor.getColumnIndex(Db.COLUMN_ID_CHAMPIONS))
            val image = cursor.getString(cursor.getColumnIndex(Db.COLUMN_IMAGE_CHAMPIONS))
            val key = cursor.getString(cursor.getColumnIndex(Db.COLUMN_KEY_CHAMPIONS))
            val lore = cursor.getString(cursor.getColumnIndex(Db.COLUMN_LORE_CHAMPIONS))
            val title = cursor.getString(cursor.getColumnIndex(Db.COLUMN_TITLE_CHAMPIONS))

            val champion = Champion(id, image, key, lore, name, title)

            champion.spells = spellDAO.getSpellsByChampionId(id)
            champion.skins = skinDAO.getSkinsByChampionId(id)

            cursor.close()
            return champion
        }

        cursor.close()
        return null
    }

    fun getFreeToPlayChampions(): MutableList<Champion> {
        val champions = mutableListOf<Champion>()

        val columns = arrayOf(
                Db.COLUMN_CHAMPION_ID_FREE_CHAMPIONS)

        val cursor = db.query(Db.TABLE_FREE_CHAMPIONS, columns, null, null, null, null, null)


        if (cursor.count > 0) {
            cursor.moveToFirst()

            do {
                val id = cursor.getInt(cursor.getColumnIndex(Db.COLUMN_ID_CHAMPIONS))

                champions.add(this.getChampion(id.toLong())!!)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return champions
    }

    fun insertFreeChampions(championIds: List<Int>) {
        db.beginTransaction()
        try {
            val cv = ContentValues()

            championIds.forEach { id ->
                cv.put(Db.COLUMN_CHAMPION_ID_FREE_CHAMPIONS, id)

                db.insert(Db.TABLE_FREE_CHAMPIONS, null, cv)
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    fun deleteFreeChampions() {
        db.delete(Db.TABLE_FREE_CHAMPIONS, null, null)
    }
}
