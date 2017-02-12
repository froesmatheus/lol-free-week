package com.matheusfroes.lolfreeweek.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.matheusfroes.lolfreeweek.models.Champion


class ChampionDAO(context: Context) {
    val db: SQLiteDatabase by lazy {
        Db(context).writableDatabase
    }
    val spellDAO by lazy {
        SpellDAO(context)
    }

    val skinDAO by lazy {
        SkinDAO(context)
    }

    fun insert(champion: Champion): Boolean {
        val cvChampion = ContentValues()

        cvChampion.put(Db.COLUMN_ID_CHAMPIONS, champion.id)
        cvChampion.put(Db.COLUMN_NAME_CHAMPIONS, champion.name)
        cvChampion.put(Db.COLUMN_KEY_CHAMPIONS, champion.key)
        cvChampion.put(Db.COLUMN_IMAGE_CHAMPIONS, champion.image)
        cvChampion.put(Db.COLUMN_LORE_CHAMPIONS, champion.lore)
        cvChampion.put(Db.COLUMN_TITLE_CHAMPIONS, champion.title)
        cvChampion.put(Db.COLUMN_ALERT_ON_CHAMPIONS, if (champion.alertOn) 1 else 0)

        // Inserting champion spells
        champion.spells.forEach {
            spellDAO.insert(it, champion.id)
        }

        // Inserting champion skins
        champion.skins.forEach {
            skinDAO.insert(it, champion.id)
        }

        val insertedId = db.insert(Db.TABLE_CHAMPIONS, null, cvChampion)

        return insertedId != -1L
    }

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
                cv.put(Db.COLUMN_ALERT_ON_CHAMPIONS, if (champion.alertOn) 1 else 0)

                spellDAO.insertList(champion.spells, champion.id, db)

                skinDAO.insertList(champion.skins, champion.id, db)

                db.insert(Db.TABLE_CHAMPIONS, null, cv)
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    fun update(champion: Champion) {
        val cv = ContentValues()

        cv.put(Db.COLUMN_NAME_CHAMPIONS, champion.name)
        cv.put(Db.COLUMN_KEY_CHAMPIONS, champion.key)
        cv.put(Db.COLUMN_IMAGE_CHAMPIONS, champion.image)
        cv.put(Db.COLUMN_LORE_CHAMPIONS, champion.lore)
        cv.put(Db.COLUMN_TITLE_CHAMPIONS, champion.title)
        cv.put(Db.COLUMN_ALERT_ON_CHAMPIONS, if (champion.alertOn) 1 else 0)

        db.update(Db.TABLE_CHAMPIONS, cv, "_id = ?", arrayOf(champion.id.toString()))
    }

    fun updateList(champions: List<Champion>) {
        db.beginTransaction()
        try {
            val cv = ContentValues()

            champions.forEach { champion ->
                cv.put(Db.COLUMN_NAME_CHAMPIONS, champion.name)
                cv.put(Db.COLUMN_KEY_CHAMPIONS, champion.key)
                cv.put(Db.COLUMN_IMAGE_CHAMPIONS, champion.image)
                cv.put(Db.COLUMN_LORE_CHAMPIONS, champion.lore)
                cv.put(Db.COLUMN_TITLE_CHAMPIONS, champion.title)
                cv.put(Db.COLUMN_ALERT_ON_CHAMPIONS, if (champion.alertOn) 1 else 0)

                db.update(Db.TABLE_CHAMPIONS, cv, "_id = ?", arrayOf(champion.id.toString()))
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    fun delete(championId: Int): Boolean {
        val rows = db.delete(Db.TABLE_CHAMPIONS, "${Db.COLUMN_ID_CHAMPIONS} = ?", arrayOf(championId.toString()))

        return rows != 0
    }

    fun deleteAll() = db.delete(Db.TABLE_CHAMPIONS, "1", null)


    fun getChampions(): List<Champion> {
        val champions = mutableListOf<Champion>()

        val columns = arrayOf(
                Db.COLUMN_ID_CHAMPIONS,
                Db.COLUMN_IMAGE_CHAMPIONS,
                Db.COLUMN_KEY_CHAMPIONS,
                Db.COLUMN_LORE_CHAMPIONS,
                Db.COLUMN_NAME_CHAMPIONS,
                Db.COLUMN_TITLE_CHAMPIONS,
                Db.COLUMN_ALERT_ON_CHAMPIONS)

        val cursor = db.query(Db.TABLE_CHAMPIONS, columns, null, null, null, null, null)

        if (cursor.count > 0) {
            cursor.moveToFirst()

            do {
                val name = cursor.getString(cursor.getColumnIndex(Db.COLUMN_NAME_CHAMPIONS))
                val id = cursor.getInt(cursor.getColumnIndex(Db.COLUMN_ID_CHAMPIONS))
                val image = cursor.getString(cursor.getColumnIndex(Db.COLUMN_IMAGE_CHAMPIONS))
                val key = cursor.getString(cursor.getColumnIndex(Db.COLUMN_KEY_CHAMPIONS))
                val lore = cursor.getString(cursor.getColumnIndex(Db.COLUMN_LORE_CHAMPIONS))
                val title = cursor.getString(cursor.getColumnIndex(Db.COLUMN_TITLE_CHAMPIONS))

                val alertOnInt = cursor.getInt(cursor.getColumnIndex(Db.COLUMN_ALERT_ON_CHAMPIONS))
                val alertOn = alertOnInt == 1

                val champion = Champion(id, image, key, lore, name, title, alertOn = alertOn)

                champion.spells = spellDAO.getSpellsByChampionId(id)
                champion.skins = skinDAO.getSkinsByChampionId(id)

                champions.add(champion)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return champions
    }

    fun getChampionsByAlert(alert: Boolean): MutableList<Champion> {
        val champions = mutableListOf<Champion>()

        val columns = arrayOf(
                Db.COLUMN_ID_CHAMPIONS,
                Db.COLUMN_IMAGE_CHAMPIONS,
                Db.COLUMN_KEY_CHAMPIONS,
                Db.COLUMN_LORE_CHAMPIONS,
                Db.COLUMN_NAME_CHAMPIONS,
                Db.COLUMN_TITLE_CHAMPIONS,
                Db.COLUMN_ALERT_ON_CHAMPIONS)

        val alertInt = if (alert) 1 else 0
        val cursor = db.
                query(Db.TABLE_CHAMPIONS, columns, "${Db.COLUMN_ALERT_ON_CHAMPIONS} = ?", arrayOf(alertInt.toString()), null, null, null)

        if (cursor.count > 0) {
            cursor.moveToFirst()

            do {
                val name = cursor.getString(cursor.getColumnIndex(Db.COLUMN_NAME_CHAMPIONS))
                val id = cursor.getInt(cursor.getColumnIndex(Db.COLUMN_ID_CHAMPIONS))
                val image = cursor.getString(cursor.getColumnIndex(Db.COLUMN_IMAGE_CHAMPIONS))
                val key = cursor.getString(cursor.getColumnIndex(Db.COLUMN_KEY_CHAMPIONS))
                val lore = cursor.getString(cursor.getColumnIndex(Db.COLUMN_LORE_CHAMPIONS))
                val title = cursor.getString(cursor.getColumnIndex(Db.COLUMN_TITLE_CHAMPIONS))

                val alertOnInt = cursor.getInt(cursor.getColumnIndex(Db.COLUMN_ALERT_ON_CHAMPIONS))
                val alertOn = alertOnInt == 1

                val champion = Champion(id, image, key, lore, name, title, alertOn = alertOn)

                champion.spells = spellDAO.getSpellsByChampionId(id)
                champion.skins = skinDAO.getSkinsByChampionId(id)

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

            val alertOnInt = cursor.getInt(cursor.getColumnIndex(Db.COLUMN_ALERT_ON_CHAMPIONS))
            val alertOn = alertOnInt == 1


            val champion = Champion(id, image, key, lore, name, title, alertOn = alertOn)

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

        val cursor = db.
                query(Db.TABLE_FREE_CHAMPIONS, columns, null, null, null, null, null)


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

    fun insertFreeChampions(championIds: List<Long>) {
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
