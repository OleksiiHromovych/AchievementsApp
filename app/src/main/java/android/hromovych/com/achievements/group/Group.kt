package android.hromovych.com.achievements.group

import java.util.*

data class Group(var title: String, var image: String, var procent: Int) {
    val id: UUID = UUID.randomUUID()
}