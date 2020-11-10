package android.hromovych.com.achievements.group

import android.content.Context
import android.hromovych.com.achievements.R
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GroupListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private var adapter: GroupAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as GroupCallbacks
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_group_list, container, false)

        recyclerView = v.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL, false
        )
        return v
    }

    override fun onResume() {
        super.onResume()
        updateList()
    }

    override fun onPause() {
        super.onPause()
        adapter = null
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private fun updateList() {
        val groups: List<Group> = getGroups()

        if (adapter == null) {
            adapter = GroupAdapter(context!!, groups)
            { group: Group -> { callbacks!!.onGroupClick(group) } }
            recyclerView.adapter = adapter
        } else {
            adapter!!.groups = groups
            adapter!!.notifyDataSetChanged()
        }
    }

    private fun getGroups(): List<Group> {
        val groups = ArrayList<Group>()
        groups.add(Group("First", "image", 50))
        groups.add(Group("2", "image", 75))
        groups.add(Group("3", "image", 35))
        groups.add(Group("4", "image", 77))
        return groups
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.group_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            else -> Toast.makeText(activity, item.title, Toast.LENGTH_SHORT).show()

        }
        return true
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            GroupListFragment()

        private const val TAG = "GroupListFragment"
        private var callbacks: GroupCallbacks? = null
    }


}