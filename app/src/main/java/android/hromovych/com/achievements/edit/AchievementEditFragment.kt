package android.hromovych.com.achievements.edit

import android.hromovych.com.achievements.R
import android.hromovych.com.achievements.database.BaseLab
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

private const val ARG_ACHIEVEMENT_ID = "achievement id"


class AchievementEditFragment : Fragment() {
    private var id: Long = -1
    private lateinit var title: EditText
    private lateinit var description: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getLong(ARG_ACHIEVEMENT_ID)
        }
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_achievement_edit, container, false)

        title = v.findViewById(R.id.achievement_title)
        description = v.findViewById(R.id.achievement_description)

        updateData()

        return v
    }

    private fun updateData() {
        val achievement = BaseLab(context).getAchievement(id)
        title.setText(achievement.title)
        description.setText(achievement.description)

        (activity as AppCompatActivity).supportActionBar?.apply {
            title = achievement.groupId.toString()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(id: Long) =
            AchievementEditFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_ACHIEVEMENT_ID, id)
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.achievement_edit, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> fragmentManager!!.popBackStackImmediate()
            else -> Toast.makeText(context, item.title, Toast.LENGTH_SHORT).show()
        }
        return true
    }
}