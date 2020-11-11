package android.hromovych.com.achievements.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.hromovych.com.achievements.database.DBSchema.AchievementTable
import android.hromovych.com.achievements.database.DBSchema.GroupTable


class DataBaseHelper(var context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "create table ${GroupTable.TABLE_NAME} (" +
                    "${GroupTable.COL_ID} integer primary key autoincrement, " +
                    "${GroupTable.COL_TITLE} text, " +
                    "${GroupTable.COL_IMAGE} text)"
        )

        db?.execSQL(
            "create table ${AchievementTable.TABLE_NAME} (" +
                    "${AchievementTable.COL_ID} integer primary key autoincrement, " +
                    "${AchievementTable.COL_GROUP_ID} integer, " +
                    "${AchievementTable.COL_TITLE} text, " +
                    "${AchievementTable.COL_DESCRIPTION} text, " +
                    "${AchievementTable.COL_COLOR} text, " +
                    "${AchievementTable.COL_IMAGE} text)"
        )


    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop table if exists ${GroupTable.TABLE_NAME}")
        db?.execSQL("drop table if exists ${AchievementTable.TABLE_NAME}")
        onCreate(db)
    }

    companion object{
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Achievement.db"
    }

}