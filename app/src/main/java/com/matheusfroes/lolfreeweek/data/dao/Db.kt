package com.matheusfroes.lolfreeweek.data.dao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class Db(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        private const val DB_NAME = "lol-free-week"
        private const val DB_VERSION = 1

        const val TABLE_CHAMPIONS = "champions"
        const val COLUMN_ID_CHAMPIONS = "_id"
        const val COLUMN_TITLE_CHAMPIONS = "title"
        const val COLUMN_NAME_CHAMPIONS = "name"
        const val COLUMN_KEY_CHAMPIONS = "key"
        const val COLUMN_LORE_CHAMPIONS = "lore"
        const val COLUMN_IMAGE_CHAMPIONS = "image"

        const val TABLE_CHAMPION_ALERTS = "alerts"
        const val COLUMN_ID_CHAMPION = "_id"

        const val TABLE_SPELLS = "spells"
        const val COLUMN_ID_SPELLS = "_id"
        const val COLUMN_NAME_SPELLS = "name"
        const val COLUMN_DESCRIPTION_SPELLS = "description"
        const val COLUMN_IMAGE_SPELLS = "image"
        const val COLUMN_CHAMPION_ID_SPELLS = "champion_id"

        const val TABLE_SKINS = "skins"
        const val COLUMN_ID_SKINS = "_id"
        const val COLUMN_NAME_SKINS = "name"
        const val COLUMN_NUM_SKINS = "num"
        const val COLUMN_CHAMPION_ID_SKINS = "champion_id"
        const val COLUMN_CHAMPION_NAME_SKINS = "champion_name"

        const val TABLE_FREE_CHAMPIONS = "free_champions"
        const val COLUMN_CHAMPION_ID_FREE_CHAMPIONS = "_id"


        private const val CREATE_TABLE_CHAMPIONS =
                """CREATE TABLE $TABLE_CHAMPIONS(
                $COLUMN_ID_CHAMPIONS INTEGER PRIMARY KEY,
                $COLUMN_TITLE_CHAMPIONS TEXT NOT NULL,
                $COLUMN_LORE_CHAMPIONS TEXT NOT NULL,
                $COLUMN_NAME_CHAMPIONS TEXT NOT NULL,
                $COLUMN_IMAGE_CHAMPIONS TEXT NOT NULL,
                $COLUMN_KEY_CHAMPIONS TEXT NOT NULL);"""

        private const val CREATE_TABLE_SPELLS =
                """CREATE TABLE $TABLE_SPELLS(
                $COLUMN_ID_SPELLS INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME_SPELLS TEXT NOT NULL,
                $COLUMN_DESCRIPTION_SPELLS TEXT NOT NULL,
                $COLUMN_IMAGE_SPELLS TEXT NOT NULL,
                $COLUMN_CHAMPION_ID_SPELLS INTEGER NOT NULL,
                 FOREIGN KEY ($COLUMN_CHAMPION_ID_SPELLS) REFERENCES $TABLE_CHAMPIONS($COLUMN_ID_CHAMPIONS));"""

        private const val CREATE_TABLE_SKINS =
                """CREATE TABLE $TABLE_SKINS(
                $COLUMN_ID_SKINS INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME_SKINS TEXT NOT NULL,
                $COLUMN_NUM_SKINS INTEGER NOT NULL,
                $COLUMN_CHAMPION_NAME_SKINS TEXT NOT NULL,
                $COLUMN_CHAMPION_ID_SKINS INTEGER NOT NULL,
                 FOREIGN KEY ($COLUMN_CHAMPION_ID_SKINS) REFERENCES $TABLE_CHAMPIONS($COLUMN_ID_CHAMPIONS));"""

        private const val CREATE_TABLE_FREE_CHAMPIONS =
                """CREATE TABLE $TABLE_FREE_CHAMPIONS(
                $COLUMN_CHAMPION_ID_FREE_CHAMPIONS INTEGER PRIMARY KEY,
                 FOREIGN KEY ($COLUMN_CHAMPION_ID_FREE_CHAMPIONS) REFERENCES $TABLE_CHAMPIONS($COLUMN_ID_CHAMPIONS));"""

        private const val CREATE_TABLE_CHAMPION_ALERTS =
                """CREATE TABLE $TABLE_CHAMPION_ALERTS(
                $COLUMN_ID_CHAMPION INTEGER PRIMARY KEY,
                 FOREIGN KEY ($COLUMN_ID_CHAMPION) REFERENCES $TABLE_CHAMPIONS($COLUMN_ID_CHAMPIONS));"""

    }


    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_CHAMPIONS)
        db.execSQL(CREATE_TABLE_SPELLS)
        db.execSQL(CREATE_TABLE_SKINS)
        db.execSQL(CREATE_TABLE_FREE_CHAMPIONS)
        db.execSQL(CREATE_TABLE_CHAMPION_ALERTS)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE $TABLE_CHAMPIONS;")
        db.execSQL("DROP TABLE $TABLE_SPELLS;")
        db.execSQL("DROP TABLE $TABLE_SKINS;")
        db.execSQL("DROP TABLE $TABLE_FREE_CHAMPIONS;")
        db.execSQL("DROP TABLE $TABLE_CHAMPION_ALERTS;")
        onCreate(db)
    }
}