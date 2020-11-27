package android.hromovych.com.achievements.group

import android.content.Context
import android.hromovych.com.achievements.R
import android.hromovych.com.achievements.database.BaseLab
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GroupListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private var adapter: GroupAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.apply {
            setTitle(R.string.app_name)
        }
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
            adapter = GroupAdapter(context!!, groups,
                { group: Group -> { callbacks!!.onGroupClick(group) } },
                { view: View?, group: Group -> showPopupMenu(view, group) })
            recyclerView.adapter = adapter
        } else {
            adapter!!.groups = groups
            adapter!!.notifyDataSetChanged()
        }
    }

    private fun showPopupMenu(view: View?, group: Group) {
        val popupMenu = PopupMenu(context, view, Gravity.CENTER)
        popupMenu.inflate(R.menu.group_popupmenu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.edit_menu -> {
                    UpdateGroupDialog(group) { updatedGroup: Group ->
                        BaseLab(context).updateGroup(updatedGroup)
                        updateList()
                    }.show(fragmentManager!!, null)
                    true
                }
                R.id.delete_menu -> {
                    BaseLab(context).deleteGroupFull(group.id)
                    updateList()
                    true
                }
                else -> false
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popupMenu.setForceShowIcon(true)
        }
        popupMenu.show()
    }

    private fun getGroups(): List<Group> {
        return BaseLab(context).getGroups()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.group_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_menu -> {
                UpdateGroupDialog(Group()) {
                    BaseLab(context).addGroup(it)
                    updateList()
                }.show(fragmentManager!!, null)
            }
            else -> Toast.makeText(activity, "${item.title} not realised", Toast.LENGTH_SHORT).show()

        }
        return true
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            GroupListFragment()

        private const val TAG = "GroupListFragment"
        private var callbacks: GroupCallbacks? = null
        private const val PICK_IMAGE_REQUEST_CODE = 255

    }
}