package android.hromovych.com.achievements

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GroupListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private var adapter: GroupAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_group_list, container, false)

        recyclerView = v.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL, false)
        updateList()
        return v
    }

    private fun updateList() {
        val groups: List<Group> = getGroups()

        if (adapter == null){
            adapter = GroupAdapter(context!!, groups)
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

    companion object {
        @JvmStatic
        fun newInstance() =
            GroupListFragment()
        private const val TAG = "GroupListFragment"
    }

    private class GroupAdapter(private val context: Context, var groups: List<Group>): RecyclerView.Adapter<GroupAdapter.GroupHolder>() {
        class GroupHolder(v: View) : RecyclerView.ViewHolder(v){

            val titleView: TextView = v.findViewById(R.id.group_item_title_text_view)
            val imageView: ImageView = v.findViewById(R.id.group_item_image_view)
            val percentView: TextView = v.findViewById(R.id.group_item_percentTextView)
            val progressBar: ProgressBar = v.findViewById(R.id.group_item_cupProgressBar)
            init {
                imageView.clipToOutline = true
            }
            fun bind(group: Group) {
                titleView.text = group.title
                percentView.text = group.procent.toString()
                progressBar.progress = group.procent
//                imageView.setImageDrawable()
            }

        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupHolder {
            val v = LayoutInflater.from(context)
                .inflate(R.layout.item_group_list, parent, false)
            return GroupHolder(v);
        }

        override fun onBindViewHolder(holder: GroupHolder, position: Int) {
            holder.bind(groups[position])
        }

        override fun getItemCount() = groups.size
    }
}