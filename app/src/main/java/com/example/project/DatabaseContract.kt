package com.example.project

import android.provider.BaseColumns

object DatabaseContract {
    // Table contents are grouped together in an anonymous object.
    object DatabaseEntry : BaseColumns {
        const val TABLE_NAME = "card"
        const val _ID = "cardid"
        const val COLUMN_QUESTION = "question"
        const val COLUMN_ANSWER = "answer"
    }
}
