package com.matheusfroes.lolfreeweek.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.matheusfroes.lolfreeweek.data.model.Skin


class SkinDAO(context: Context) {
    val db: SQLiteDatabase by lazy {
        Db(context).writableDatabase
    }

    fun insert(skin: Skin, championId: Int): Boolean {
        val cv = ContentValues()

        cv.put(Db.COLUMN_NAME_SKINS, skin.name)
        cv.put(Db.COLUMN_NUM_SKINS, skin.num)
        cv.put(Db.COLUMN_CHAMPION_ID_SKINS, championId)
        cv.put(Db.COLUMN_CHAMPION_NAME_SKINS, skin.championName)

        val insertedId = db.insert(Db.TABLE_SKINS, null, cv)

        return insertedId != -1L
    }

    fun insertList(skins: List<Skin>, championId: Int, db: SQLiteDatabase) {
        val cv = ContentValues()

        skins.forEach { skin ->
            cv.put(Db.COLUMN_NAME_SKINS, skin.name)
            cv.put(Db.COLUMN_NUM_SKINS, skin.num)
            cv.put(Db.COLUMN_CHAMPION_ID_SKINS, championId)
            cv.put(Db.COLUMN_CHAMPION_NAME_SKINS, skin.championName)

            db.insert(Db.TABLE_SKINS, null, cv)
        }
    }

    fun delete(skinId: Int): Boolean {
        val rows = db.delete(Db.TABLE_SKINS, "${Db.COLUMN_ID_SKINS} = ?", arrayOf(skinId.toString()))

        return rows != 0
    }

    fun deleteAll() = db.delete(Db.TABLE_SKINS, "1", null)


    fun getSkinsByChampionId(championId: Int): MutableList<Skin> {
        val skins = mutableListOf<Skin>()

        val cursor = db.rawQuery("SELECT * FROM ${Db.TABLE_SKINS} WHERE ${Db.COLUMN_CHAMPION_ID_SKINS} = ?",
                arrayOf(championId.toString()))

        if (cursor.count > 0) {
            cursor.moveToFirst()

            do {
                val name = cursor.getString(cursor.getColumnIndex(Db.COLUMN_NAME_SKINS))
//                val championId = cursor.getInt(cursor.getColumnIndex(Db.COLUMN_CHAMPION_ID_SPELLS))
                val num = cursor.getInt(cursor.getColumnIndex(Db.COLUMN_NUM_SKINS))
                val championName = cursor.getString(cursor.getColumnIndex(Db.COLUMN_CHAMPION_NAME_SKINS))

                val skin = Skin(name, num, championName)

                skins.add(skin)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return skins
    }
}
