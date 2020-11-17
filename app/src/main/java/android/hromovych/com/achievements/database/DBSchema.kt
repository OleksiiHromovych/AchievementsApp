package android.hromovych.com.achievements.database

import android.provider.BaseColumns

object DBSchema {
    object GroupTable : BaseColumns {
        const val TABLE_NAME = "Groups"

        const val COL_ID = "id"
        const val COL_TITLE = "title"
        const val COL_IMAGE_ID = "image"
    }

    object AchievementTable : BaseColumns {
        const val TABLE_NAME = "Achievement"

        const val COL_ID = "id"
        const val COL_GROUP_ID = "group_id"
        const val COL_TITLE = "title"
        const val COL_DESCRIPTION = "description"
        const val COL_IMAGE_ID = "image"
        const val COL_COLOR = "color"
        const val COL_COMPLETED = "completed"
    }

    object ImageTable: BaseColumns{
        const val TABLE_NAME = "Images"

        const val COL_ID = "id"
        const val COL_IMAGE = "image"
    }
}