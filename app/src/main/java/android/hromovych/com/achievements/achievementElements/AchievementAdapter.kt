package android.hromovych.com.achievements.achievementElements

import android.content.Context
import android.hromovych.com.achievements.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AchievementAdapter(
    private val context: Context,
    var achievements: List<Achievement>
) :
    RecyclerView.Adapter<AchievementAdapter.AchievementHolder>() {
    class AchievementHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val titleView: TextView = v.findViewById(R.id.achievement_title)
        private val descriptionView: TextView = v.findViewById(R.id.achievement_description)

        fun bind(achievement: Achievement) {
            titleView.text = achievement.title
            descriptionView.text = achievement.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AchievementHolder {
        return AchievementHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.item_achievement, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AchievementHolder, position: Int) {
        holder.bind(achievements[position])
    }

    override fun getItemCount(): Int {
        return achievements.size
    }


}