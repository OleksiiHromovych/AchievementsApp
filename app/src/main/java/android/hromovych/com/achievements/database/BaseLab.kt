package android.hromovych.com.achievements.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.hromovych.com.achievements.achievementElements.Achievement
import android.hromovych.com.achievements.group.Group

class BaseLab(private val context: Context?) {
    private var db: SQLiteDatabase = DataBaseHelper(context).writableDatabase

    fun addGroup(group: Group) {
        val values = ContentValues().apply {
            put(DBSchema.GroupTable.COL_TITLE, group.title)
            put(DBSchema.GroupTable.COL_IMAGE, group.image)
        }
        db.insert(DBSchema.GroupTable.TABLE_NAME, null, values)
    }

    fun getGroups(): MutableList<Group> {
        val groups = mutableListOf<Group>()

        val cursor = db.query(
            DBSchema.GroupTable.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )

        with(cursor) {
            while (moveToNext()) {
                groups.add(cursor.getGroup())
            }
        }

        return groups
    }

    fun addAchievement(achievement: Achievement): Long {

        return db.insert(DBSchema.AchievementTable.TABLE_NAME, null, achievement.getContentValues())
    }

    fun getAchievements(groupId: Long): MutableList<Achievement> {
        val achievements = mutableListOf<Achievement>()

        val cursor = db.query(
            DBSchema.AchievementTable.TABLE_NAME,
            null,
            "${DBSchema.AchievementTable.COL_GROUP_ID} = ?",
            arrayOf(groupId.toString()),
            null,
            null,
            "${DBSchema.AchievementTable.COL_COMPLETED} desc"
        )

        with(cursor) {
            while (moveToNext()) {
                achievements.add(cursor.getAchievement())
            }
        }

        return achievements
    }

    fun getAchievement(achievementsId: Long): Achievement {
        val cursor = db.query(
            DBSchema.AchievementTable.TABLE_NAME,
            null,
            "${DBSchema.AchievementTable.COL_ID} = ?",
            arrayOf(achievementsId.toString()),
            null,
            null,
            null
        )

        with(cursor) {
            moveToFirst()
            return cursor.getAchievement()
        }
    }

    fun clearDb() {
        db.execSQL("delete from ${DBSchema.GroupTable.TABLE_NAME}")
        db.execSQL("delete from ${DBSchema.AchievementTable.TABLE_NAME}")
    }

    fun getGroup(groupId: Long): Group {
        val cursor = db.query(
            DBSchema.GroupTable.TABLE_NAME,
            null,
            "${DBSchema.GroupTable.COL_ID} = ?",
            arrayOf(groupId.toString()),
            null,
            null,
            null
        )

        with(cursor) {
            moveToFirst()
            return cursor.getGroup()
        }
    }

    fun deleteAchievement(id: Long): Int = db.delete(
        DBSchema.AchievementTable.TABLE_NAME,
        "${DBSchema.AchievementTable.COL_ID} = ?",
        arrayOf(id.toString())
    )

    fun updateAchievement(achievement: Achievement) = db.update(
            DBSchema.AchievementTable.TABLE_NAME,
            achievement.getContentValues(),
            "${DBSchema.AchievementTable.COL_ID} = ?",
            arrayOf(achievement.id.toString())
        )

}

private fun Cursor.getAchievement(): Achievement {
    val _id = getLong(getColumnIndex(DBSchema.AchievementTable.COL_ID))
    val _groupID = getLong(getColumnIndex(DBSchema.AchievementTable.COL_GROUP_ID))
    val _title = getString(getColumnIndex(DBSchema.AchievementTable.COL_TITLE))
    val _description = getString(getColumnIndex(DBSchema.AchievementTable.COL_DESCRIPTION))
    val _color = getString(getColumnIndex(DBSchema.AchievementTable.COL_COLOR))
    val _completed = getInt(getColumnIndex(DBSchema.AchievementTable.COL_COMPLETED))
    val _image = getString(getColumnIndex(DBSchema.AchievementTable.COL_IMAGE))

    return Achievement(groupId = _groupID).apply {
        id = _id
        title = _title
        description = _description
        color = _color
        image = _image
        completed = _completed != 0
    }
}

private fun Cursor.getGroup(): Group {
    val _id = getLong(getColumnIndex(DBSchema.GroupTable.COL_ID))
    val titleString = getString(getColumnIndex(DBSchema.GroupTable.COL_TITLE))
    val imagePath = getString(getColumnIndex(DBSchema.GroupTable.COL_IMAGE))

    return Group().apply {
        title = titleString
        id = _id
        image = imagePath
    }
}

private fun Achievement.getContentValues(): ContentValues {
    val achievement = this
    return ContentValues().apply {
        put(DBSchema.AchievementTable.COL_TITLE, achievement.title)
        put(DBSchema.AchievementTable.COL_IMAGE, achievement.image)
        put(DBSchema.AchievementTable.COL_DESCRIPTION, achievement.description)
        put(DBSchema.AchievementTable.COL_GROUP_ID, achievement.groupId)
        put(DBSchema.AchievementTable.COL_COLOR, achievement.color)
        put(DBSchema.AchievementTable.COL_COMPLETED, achievement.completed)

    }
}