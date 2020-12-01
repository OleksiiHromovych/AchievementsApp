package android.hromovych.com.achievements.edit

import android.hromovych.com.achievements.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ColorDialog(val onItemClick: (Int) -> Unit) : DialogFragment() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.dialog_recycler_view, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(context, 3)


        val colors = resources.getIntArray(R.array.dialog_colors)
        val adapter = ColorAdapter(colors)
        recyclerView.adapter = adapter
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.Theme_Dialog)
    }

    inner class ColorAdapter(private val colors: IntArray) :
        RecyclerView.Adapter<ColorAdapter.ColorHolder>() {

        inner class ColorHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
            private val button = v.findViewById<Button>(R.id.color_button)
            private var color:Int = -1

            init {
                v.setOnClickListener(this)
            }

            fun bind(color: Int) {
                this.color = color
                val drawable = button.background
                DrawableCompat.setTint(drawable, color)
            }

            override fun onClick(v: View?) {
                onItemClick(color)
                dismiss()
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorHolder {
            return ColorHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_color, parent, false)
            )
        }

        override fun onBindViewHolder(holder: ColorHolder, position: Int) {
            holder.bind(colors[position])
        }

        override fun getItemCount(): Int {
            return colors.size
        }
    }

}