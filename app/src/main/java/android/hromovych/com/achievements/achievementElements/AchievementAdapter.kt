package android.hromovych.com.achievements.achievementElements

import android.content.Context
import android.graphics.BitmapFactory
import android.hromovych.com.achievements.R
import android.hromovych.com.achievements.database.BaseLab
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AchievementAdapter(
    private val context: Context,
    var achievements: List<Achievement>,
    private val clickAchievement: (Achievement) -> Unit
) :
    RecyclerView.Adapter<AchievementAdapter.AchievementHolder>() {

    inner class AchievementHolder(v: View, private val clickAchievement: (Achievement) -> Unit) :
        RecyclerView.ViewHolder(v) {
        private val titleView: TextView = v.findViewById(R.id.achievement_title)
        private val imageView: ImageView = v.findViewById(R.id.achievement_image)
        private val descriptionView: TextView = v.findViewById(R.id.achievement_description)
        private lateinit var achievement: Achievement
        private val view = v

        init {
            v.setOnClickListener { clickAchievement(achievement) }
        }

        fun bind(achievement: Achievement) {
            this.achievement = achievement
            titleView.text = achievement.title
            descriptionView.text = achievement.description

            val imageBytes = BaseLab(context).getImage(achievement.imageId)
            if (imageBytes.isNotEmpty())
                imageView.setImageBitmap(
                    BitmapFactory.decodeByteArray(
                        imageBytes,
                        0,
                        imageBytes.size
                    )
                )
            imageView.setBackgroundColor(achievement.color)

            if (achievement.completed)
                view.setBackgroundResource(R.drawable.achievement_completed_item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AchievementHolder {
        return AchievementHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.item_achievement, parent, false), clickAchievement
        )
    }

    override fun onBindViewHolder(holder: AchievementHolder, position: Int) {
        holder.bind(achievements[position])
    }

    override fun getItemCount(): Int {
        return achievements.size
    }


}