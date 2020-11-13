package android.hromovych.com.achievements.group

import android.content.Context
import android.hromovych.com.achievements.R
import android.hromovych.com.achievements.database.BaseLab
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

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

        fun bind(group: Group, completeAmount: Int, fullAmount: Int) {
            this.group = group
            titleView.text = group.title

            val percent = (completeAmount / fullAmount.toFloat() * 100).roundToInt()
            percentView.text = percent.toString()
            progressBar.progress = percent
            countView.text = "$completeAmount / $fullAmount"
        }

        override fun onClick(v: View?) {itemClickListener(group).invoke()}

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupHolder {
        val v = LayoutInflater.from(context)
            .inflate(R.layout.item_group_list, parent, false)
        return GroupHolder(v, itemClickListener)
    }

    override fun onBindViewHolder(holder: GroupHolder, position: Int) {
        val lab = BaseLab(context)
        val group = groups[position]
        holder.bind(group,
            lab.getAchievements(groupId = group.id, completed = true).size,
            lab.getAchievements(groupId = group.id).size
        )
    }

    override fun getItemCount() = groups.size
}