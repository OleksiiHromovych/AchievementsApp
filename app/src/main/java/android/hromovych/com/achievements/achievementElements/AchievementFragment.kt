package android.hromovych.com.achievements.achievementElements

import android.hromovych.com.achievements.R
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
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
        setHasOptionsMenu(true)
        groupID = arguments!!.getSerializable(ARG_GROUP_ID) as UUID
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar!!.setHomeAsUpIndicator(R.drawable.ic_back)
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

    override fun onDestroy() {
        super.onDestroy()
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.achievement_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home -> fragmentManager!!.popBackStackImmediate()
        }
        return true
    }



}