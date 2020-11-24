package android.hromovych.com.achievements.achievementElements

import android.content.Context
import android.hromovych.com.achievements.R
import android.hromovych.com.achievements.database.BaseLab
import android.os.Bundle
import android.util.SparseBooleanArray
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AchievementFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private var adapter: AchievementAdapter? = null
    private var groupID: Long = -1
    private var callbacks: AchievementCallbacks? = null
    private var actionMode: ActionMode? = null
    private lateinit var achievements: List<Achievement>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        groupID = arguments!!.getLong(ARG_GROUP_ID)
        (activity as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_back)
            title = BaseLab(context).getGroup(groupID).title

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_element_list, container, false)
        recyclerView = view as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        return view
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    override fun onPause() {
        super.onPause()
        adapter = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as AchievementCallbacks
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    companion object {
        private const val ARG_GROUP_ID = "group id"

        @JvmStatic
        fun newInstance(groupId: Long) =
            AchievementFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_GROUP_ID, groupId)
                }
            }
    }

    private fun updateUI() {
        achievements = getAchievements()
        if (adapter == null) {
            adapter = AchievementAdapter(context!!, achievements, {
                callbacks!!.onAchievementClick(it)
            },
                {
                    onListItemSelect(it)
                })
            recyclerView.adapter = adapter
        } else {
            adapter!!.achievements = achievements
            adapter!!.notifyDataSetChanged()
        }
    }

    private fun onListItemSelect(position: Int) {
        val adapter = adapter!!
        adapter.toggleSelection(position)

        val hasCheckedItems = adapter.getSelectedCount() > 0

        if (hasCheckedItems && actionMode == null) {
            actionMode = (activity as AppCompatActivity).startActionMode(
                AchievementActionModeCallback(
                    context, adapter, achievements, this
                )
            )
        } else if (!hasCheckedItems && actionMode != null)
            actionMode?.finish()

        actionMode?.title = "${adapter.getSelectedCount()} selected"
    }

    fun deleteRows() {
        val selected: SparseBooleanArray = adapter!!.selectedItemsIds
        val achievementsIdList = mutableListOf<Long>()

        for (i in 0..selected.size()) {
            if (selected.valueAt(i)) {
                achievementsIdList.add(achievements[selected.keyAt(i)].id)
            }
        }

        BaseLab(context).deleteAchievements(achievementsIdList)
        updateUI()
        actionMode?.finish()
    }

    private fun getAchievements(): List<Achievement> {
        return BaseLab(context).getAchievements(groupID)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.achievement_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> fragmentManager!!.popBackStackImmediate()
            R.id.add_menu -> {
                val lab = BaseLab(context)
                val achievement =
                    Achievement(groupID).apply {
                        imageId = lab.getGroup(groupID).imageId
                        id = lab.addAchievement(this)
                    }
                callbacks!!.onAchievementClick(achievement)
            }
        }
        return true
    }

    fun setNullToActionMode() {
        actionMode = null
    }
}