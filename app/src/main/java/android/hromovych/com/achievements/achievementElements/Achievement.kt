package android.hromovych.com.achievements.achievementElements

class Achievement(var groupId: Long) {
    var id: Long = -1
    var color: String? = null
    var image: String? = null
    var title: String? = null
    var description: String? = null
    var completed: Boolean = false
}