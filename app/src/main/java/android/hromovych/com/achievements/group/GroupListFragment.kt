package android.hromovych.com.achievements.group

import android.app.Dialog
import android.content.Context
import android.hromovych.com.achievements.R
import android.hromovych.com.achievements.database.BaseLab
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
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
            adapter = GroupAdapter(context!!, groups,
                { group: Group -> { callbacks!!.onGroupClick(group) } },
                {view: View?, group: Group -> showPopupMenu(view, group)})
            recyclerView.adapter = adapter
        } else {
            adapter!!.groups = groups
            adapter!!.notifyDataSetChanged()
        }
    }

    private fun showPopupMenu(view: View?, group: Group){
        val popupMenu = PopupMenu(context, view, Gravity.CENTER)
        popupMenu.inflate(R.menu.group_popupmenu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId){
                R.id.edit_menu -> {
                    updateGroupDialog(group){updatedGroup: Group ->
                        BaseLab(context).updateGroup(updatedGroup)
                        updateList()
                    }
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
        val groups = BaseLab(context).getGroups()
        return groups
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.group_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_menu -> {
                updateGroupDialog(Group()) {
                    BaseLab(context).addGroup(it)
                    updateList()
                }

            }
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

    private fun updateGroupDialog(group: Group, okListener: (Group) -> Unit) {
        val dialog = Dialog(context!!, R.style.Theme_Dialog)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_new_group)

        val title = dialog.findViewById<EditText>(R.id.title_view).apply { setText(group.title) }
        dialog.findViewById<Button>(R.id.cancel_button).setOnClickListener {
            dialog.dismiss()
        }

        dialog.findViewById<Button>(R.id.ok_button).setOnClickListener {
            group.title = title.text.toString()
            okListener(group)
            dialog.dismiss()
        }
        dialog.show()
    }

}