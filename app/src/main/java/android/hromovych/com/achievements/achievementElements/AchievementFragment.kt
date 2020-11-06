package android.hromovych.com.achievements.achievementElements

import android.content.Context
import android.hromovych.com.achievements.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class AchievementFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private var adapter: AchievementAdapter? = null
    private lateinit var groupID: UUID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        groupID = arguments!!.getSerializable(ARG_GROUP_ID) as UUID
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_element_list, container, false)
        recyclerView = view as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)

        updateUI()

        return view
    }

    companion object {
        private const val ARG_GROUP_ID = "group id"

        @JvmStatic
        fun newInstance(groupId: UUID) =
            AchievementFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_GROUP_ID, groupId)
                }
            }
    }

    private fun updateUI() {
        val achievements: List<Achievement> = getAchievements()
        if (adapter == null) {
            adapter = AchievementAdapter(context!!, achievements)
            recyclerView.adapter = adapter
        } else {
            adapter!!.achievements = achievements
            adapter!!.notifyDataSetChanged()
        }
    }

    private fun getAchievements(): List<Achievement> {
        val list = mutableListOf<Achievement>()
        for (index in 1..5)
            list.add(Achievement("Title $index", "Description $index is $groupID group"))
        return list
    }



    private class AchievementAdapter(
        private val context: Context,
        var achievements: List<Achievement>
    ) :
        RecyclerView.Adapter<AchievementAdapter.AchievementHolder>() {
        class AchievementHolder(v: View) : RecyclerView.ViewHolder(v) {
            val titleView: TextView = v.findViewById(R.id.achievement_title)
            val descriptionView: TextView = v.findViewById(R.id.achievement_description)

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
}