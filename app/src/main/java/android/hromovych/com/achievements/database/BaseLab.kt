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
        db.insert(DBSchema.GroupTable.TABLE_NAME, null, group.getContentValues())
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
            DBSchema.AchievementTable.COL_COMPLETED
        )

        with(cursor) {
            while (moveToNext()) {
                achievements.add(cursor.getAchievement())
            }
        }

        return achievements
    }

    fun getAchievements(groupId: Long, completed: Boolean): MutableList<Achievement> {
        val achievements = mutableListOf<Achievement>()

        val cursor = db.query(
            DBSchema.AchievementTable.TABLE_NAME,
            null,
            "${DBSchema.AchievementTable.COL_GROUP_ID} = ? and ${DBSchema.AchievementTable.COL_COMPLETED} = ?",
            arrayOf(groupId.toString(), completed.toDatabaseString()),
            null,
            null,
            DBSchema.AchievementTable.COL_COMPLETED
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

     fun deleteAchievements(groupId: Long) = db.delete(
            DBSchema.AchievementTable.TABLE_NAME,
            "${DBSchema.AchievementTable.COL_GROUP_ID} = ?",
            arrayOf(groupId.toString())
        )


    fun updateAchievement(achievement: Achievement) = db.update(
        DBSchema.AchievementTable.TABLE_NAME,
        achievement.getContentValues(),
        "${DBSchema.AchievementTable.COL_ID} = ?",
        arrayOf(achievement.id.toString())
    )

    fun updateGroup(group: Group) = db.update(
        DBSchema.GroupTable.TABLE_NAME,
        group.getContentValues(),
        "${DBSchema.GroupTable.COL_ID} = ?",
        arrayOf(group.id.toString())
    )

    fun deleteGroupFull(id: Long) {
        deleteAchievements(id)
        deleteGroup(id)
    }

    private fun deleteGroup(id: Long) = db.delete(
        DBSchema.GroupTable.TABLE_NAME,
        "${DBSchema.GroupTable.COL_ID} = ?",
        arrayOf(id.toString())
    )

    fun getImage(id: Long): ByteArray {
        val cursor = db.query(
            DBSchema.ImageTable.TABLE_NAME,
            null,
            "${DBSchema.ImageTable.COL_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        with(cursor) {
            return if (moveToFirst())
                cursor.getBlob(getColumnIndex(DBSchema.ImageTable.COL_IMAGE))
            else
                byteArrayOf()
        }
    }

    fun addImage(byteArray: ByteArray?) = db.insert(
        DBSchema.ImageTable.TABLE_NAME,
        null,
        ContentValues().apply {
            put(DBSchema.ImageTable.COL_IMAGE, byteArray)
        }
    )

}

private fun Group.getContentValues(): ContentValues? = ContentValues().apply {
    put(DBSchema.GroupTable.COL_TITLE, this@getContentValues.title)
    put(DBSchema.GroupTable.COL_IMAGE_ID, this@getContentValues.imageId)
}


private fun Cursor.getAchievement(): Achievement {
    val _id = getLong(getColumnIndex(DBSchema.AchievementTable.COL_ID))
    val _groupID = getLong(getColumnIndex(DBSchema.AchievementTable.COL_GROUP_ID))
    val _title = getString(getColumnIndex(DBSchema.AchievementTable.COL_TITLE))
    val _description = getString(getColumnIndex(DBSchema.AchievementTable.COL_DESCRIPTION))
    val _color = getInt(getColumnIndex(DBSchema.AchievementTable.COL_COLOR))
    val _completed = getInt(getColumnIndex(DBSchema.AchievementTable.COL_COMPLETED))
    val _image = getLong(getColumnIndex(DBSchema.AchievementTable.COL_IMAGE_ID))

    return Achievement(groupId = _groupID).apply {
        id = _id
        title = _title
        description = _description
        color = _color
        imageId = _image
        completed = _completed != 0
    }
}

private fun Cursor.getGroup(): Group {
    val _id = getLong(getColumnIndex(DBSchema.GroupTable.COL_ID))
    val titleString = getString(getColumnIndex(DBSchema.GroupTable.COL_TITLE))
    val imagePath = getLong(getColumnIndex(DBSchema.GroupTable.COL_IMAGE_ID))

    return Group().apply {
        title = titleString
        id = _id
        imageId = imagePath
    }
}

private fun Achievement.getContentValues(): ContentValues {
    val achievement = this
    return ContentValues().apply {
        put(DBSchema.AchievementTable.COL_TITLE, achievement.title)
        put(DBSchema.AchievementTable.COL_IMAGE_ID, achievement.imageId)
        put(DBSchema.AchievementTable.COL_DESCRIPTION, achievement.description)
        put(DBSchema.AchievementTable.COL_GROUP_ID, achievement.groupId)
        put(DBSchema.AchievementTable.COL_COLOR, achievement.color)
        put(DBSchema.AchievementTable.COL_COMPLETED, achievement.completed)

    }
}

fun Boolean.toDatabaseString(): String {
    return if (this) "1" else "0"
}