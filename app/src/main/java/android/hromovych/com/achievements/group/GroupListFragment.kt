package android.hromovych.com.achievements.group

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.hromovych.com.achievements.R
import android.hromovych.com.achievements.database.BaseLab
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.*
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

        dialog.findViewById<ImageButton>(R.id.image_button).apply {
            setOnClickListener {
                val getIntent = Intent(Intent.ACTION_GET_CONTENT)
                getIntent.type = "image/*"

                val pickIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                pickIntent.type = "image/*"

                val chooserIntent = Intent.createChooser(getIntent, "Select Image")
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

                startActivityForResult(chooserIntent, PICK_IMAGE_REQUEST_CODE)
            }
        }

        dialog.show()
    }

}