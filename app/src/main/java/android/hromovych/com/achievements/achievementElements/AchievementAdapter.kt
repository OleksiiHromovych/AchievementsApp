package android.hromovych.com.achievements.achievementElements

import android.content.Context
import android.graphics.BitmapFactory
import android.hromovych.com.achievements.R
import android.hromovych.com.achievements.database.BaseLab
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.util.isEmpty
import androidx.recyclerview.widget.RecyclerView

class AchievementAdapter(
    private val context: Context,
    var achievements: List<Achievement>,
    private val clickAchievement: (Achievement) -> Unit,
    private val longClickListener: (Int) -> Unit
) :
    RecyclerView.Adapter<AchievementAdapter.AchievementHolder>() {

    val selectedItemsIds: SparseBooleanArray = SparseBooleanArray()

    inner class AchievementHolder(v: View, private val clickAchievement: (Achievement) -> Unit) :
        RecyclerView.ViewHolder(v) {
        private val titleView: TextView = v.findViewById(R.id.achievement_title)
        private val imageView: ImageView = v.findViewById(R.id.achievement_image)
        private val descriptionView: TextView = v.findViewById(R.id.achievement_description)
        private lateinit var achievement: Achievement
        private val view = v

        init {
                v.setOnClickListener {
                    if (selectedItemsIds.isEmpty())
                        clickAchievement(achievement)
                    else
                        longClickListener(adapterPosition)
                }
            v.setOnLongClickListener {
                longClickListener(adapterPosition)
                true
            }
        }

        fun bind(pos: Int) {
            achievement = achievements[pos]

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

            val isSelected = selectedItemsIds[pos]
            if (isSelected) {
                if (achievement.completed)
                    view.setBackgroundResource(R.drawable.achievement_completed_selected_item)
                else
                    view.setBackgroundResource(R.drawable.achievement_selected_item)
            } else {
                if (achievement.completed)
                    view.setBackgroundResource(R.drawable.achievement_completed_item)
                else
                    view.setBackgroundResource(R.drawable.achievement_item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AchievementHolder {
        return AchievementHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.item_achievement, parent, false), clickAchievement
        )
    }

    override fun onBindViewHolder(holder: AchievementHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return achievements.size
    }


    fun toggleSelection(position: Int) = selectView(position, !selectedItemsIds.get(position))

    fun removeSelection() {
        selectedItemsIds.clear()
        notifyDataSetChanged()
    }

    fun selectView(position: Int, value: Boolean) {
        if (value) selectedItemsIds.put(position, value) else selectedItemsIds.delete(position)
        notifyDataSetChanged()
    }

    fun getSelectedCount() = selectedItemsIds.size()


}