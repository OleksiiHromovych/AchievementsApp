package android.hromovych.com.achievements.achievementElements

import android.graphics.Color

class Achievement(var groupId: Long) {
    var id: Long = -1
    var color: Int = Color.WHITE
    var imageId: Long = -1
    var title: String? = null
    var description: String? = null
    var completed: Boolean = false
}