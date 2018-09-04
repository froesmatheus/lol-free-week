package com.matheusfroes.lolfreeweek.data.dao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class Db(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        private val DB_NAME = "lol-free-week"
        private val DB_VERSION = 1

        val TABLE_CHAMPIONS = "champions"
        val COLUMN_ID_CHAMPIONS = "_id"
        val COLUMN_TITLE_CHAMPIONS = "title"
        val COLUMN_NAME_CHAMPIONS = "name"
        val COLUMN_KEY_CHAMPIONS = "key"
        val COLUMN_LORE_CHAMPIONS = "lore"
        val COLUMN_IMAGE_CHAMPIONS = "image"
        val COLUMN_ALERT_ON_CHAMPIONS = "alert_on"

        val TABLE_SPELLS = "spells"
        val COLUMN_ID_SPELLS = "_id"
        val COLUMN_NAME_SPELLS = "name"
        val COLUMN_DESCRIPTION_SPELLS = "description"
        val COLUMN_IMAGE_SPELLS = "image"
        val COLUMN_CHAMPION_ID_SPELLS = "champion_id"

        val TABLE_SKINS = "skins"
        val COLUMN_ID_SKINS = "_id"
        val COLUMN_NAME_SKINS = "name"
        val COLUMN_NUM_SKINS = "num"
        val COLUMN_CHAMPION_ID_SKINS = "champion_id"
        val COLUMN_CHAMPION_NAME_SKINS = "champion_name"

        val TABLE_FREE_CHAMPIONS = "free_champions"
        val COLUMN_CHAMPION_ID_FREE_CHAMPIONS = "_id"


        private val CREATE_TABLE_CHAMPIONS =
                """CREATE TABLE $TABLE_CHAMPIONS(
                $COLUMN_ID_CHAMPIONS INTEGER PRIMARY KEY,
                $COLUMN_TITLE_CHAMPIONS TEXT NOT NULL,
                $COLUMN_LORE_CHAMPIONS TEXT NOT NULL,
                $COLUMN_NAME_CHAMPIONS TEXT NOT NULL,
                $COLUMN_IMAGE_CHAMPIONS TEXT NOT NULL,
                $COLUMN_ALERT_ON_CHAMPIONS INTEGER NOT NULL,
                $COLUMN_KEY_CHAMPIONS TEXT NOT NULL);"""

        private val CREATE_TABLE_SPELLS =
                """CREATE TABLE $TABLE_SPELLS(
                $COLUMN_ID_SPELLS INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME_SPELLS TEXT NOT NULL,
                $COLUMN_DESCRIPTION_SPELLS TEXT NOT NULL,
                $COLUMN_IMAGE_SPELLS TEXT NOT NULL,
                $COLUMN_CHAMPION_ID_SPELLS INTEGER NOT NULL,
                 FOREIGN KEY ($COLUMN_CHAMPION_ID_SPELLS) REFERENCES $TABLE_CHAMPIONS($COLUMN_ID_CHAMPIONS));"""

        private val CREATE_TABLE_SKINS =
                """CREATE TABLE $TABLE_SKINS(
                $COLUMN_ID_SKINS INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME_SKINS TEXT NOT NULL,
                $COLUMN_NUM_SKINS INTEGER NOT NULL,
                $COLUMN_CHAMPION_NAME_SKINS TEXT NOT NULL,
                $COLUMN_CHAMPION_ID_SKINS INTEGER NOT NULL,
                 FOREIGN KEY ($COLUMN_CHAMPION_ID_SKINS) REFERENCES $TABLE_CHAMPIONS($COLUMN_ID_CHAMPIONS));"""

        private val CREATE_TABLE_FREE_CHAMPIONS =
                """CREATE TABLE $TABLE_FREE_CHAMPIONS(
                $COLUMN_CHAMPION_ID_FREE_CHAMPIONS INTEGER PRIMARY KEY,
                 FOREIGN KEY ($COLUMN_CHAMPION_ID_FREE_CHAMPIONS) REFERENCES $TABLE_CHAMPIONS($COLUMN_ID_CHAMPIONS));"""

    }


    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_CHAMPIONS)
        db.execSQL(CREATE_TABLE_SPELLS)
        db.execSQL(CREATE_TABLE_SKINS)
        db.execSQL(CREATE_TABLE_FREE_CHAMPIONS)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE $TABLE_CHAMPIONS;")
        db.execSQL("DROP TABLE $TABLE_SPELLS;")
        db.execSQL("DROP TABLE $TABLE_SKINS;")
        db.execSQL("DROP TABLE $TABLE_FREE_CHAMPIONS;")
        onCreate(db)
    }
}