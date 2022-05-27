package com.example.project

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        Log.d("TRYING", SQL_CREATE_ENTRIES)
        db.execSQL(SQL_CREATE_ENTRIES)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }
    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 8
        const val DATABASE_NAME = "FeedReader.db"

        const val TABLE_NAME = "card"
        const val COLUMN_ID = "_id"
        const val COLUMN_QUESTION = "question"
        const val COLUMN_ANSWER = "answer"

        const val SQL_CREATE_ENTRIES =
            "CREATE TABLE $TABLE_NAME (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "$COLUMN_QUESTION TEXT," +
                    "$COLUMN_ANSWER TEXT)"

        const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_NAME"
    }

    fun deleteCard(q: String, a: String) : Int {
        val db = this.writableDatabase
        Log.d("TRYING", "gebreurt dit?")

        val rows = db.delete(TABLE_NAME, "$COLUMN_QUESTION LIKE ? AND $COLUMN_ANSWER LIKE ?", arrayOf(q, a))



        db.close()

        return rows
    }

    fun getCards(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null);
    }

    fun addCard(q: String, a: String): Long {
        val db = this.writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_QUESTION, q)
            put(COLUMN_ANSWER, a)
        }

        return db.insert(TABLE_NAME, null, values)
    }
}
