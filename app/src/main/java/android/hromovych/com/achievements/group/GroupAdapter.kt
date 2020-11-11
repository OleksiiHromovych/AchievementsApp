package android.hromovych.com.achievements.group

import android.content.Context
import android.hromovych.com.achievements.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GroupAdapter(private val context: Context, var groups: List<Group>,
                   private val itemClickListener: (Group) -> () -> Unit
) :

    RecyclerView.Adapter<GroupAdapter.GroupHolder>() {
    class GroupHolder(v: View, private var itemClickListener: (Group) -> () -> Unit) :
        RecyclerView.ViewHolder(v),  View.OnClickListener{

        private val titleView: TextView = v.findViewById(R.id.group_item_title_text_view)
        private val imageView: ImageView = v.findViewById(R.id.group_item_image_view)
        private val percentView: TextView = v.findViewById(R.id.group_item_percentTextView)
        private val progressBar: ProgressBar = v.findViewById(R.id.group_item_cupProgressBar)
        private val countView: TextView = v.findViewById(R.id.group_item_countTextView)
        private lateinit var group: Group

        init {
            v.setOnClickListener(this)
        }

        fun bind(group: Group) {
            this.group = group
            titleView.text = group.title
            percentView.text = group.percent.toString()
            progressBar.progress = group.percent
            countView.text = "${group.percent} / 100"
        }

        override fun onClick(v: View?) {itemClickListener(group).invoke()}

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupHolder {
        val v = LayoutInflater.from(context)
            .inflate(R.layout.item_group_list, parent, false)
        return GroupHolder(v, itemClickListener)
    }

    override fun onBindViewHolder(holder: GroupHolder, position: Int) {
        holder.bind(groups[position])
    }

    override fun getItemCount() = groups.size
}