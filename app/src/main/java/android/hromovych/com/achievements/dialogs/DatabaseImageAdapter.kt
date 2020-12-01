package android.hromovych.com.achievements.dialogs

import android.graphics.BitmapFactory
import android.hromovych.com.achievements.R
import android.hromovych.com.achievements.database.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView

class DatabaseImageAdapter(val images: List<Image>, private val onClickAction: (Long) -> Unit) : RecyclerView.Adapter<DatabaseImageAdapter.DatabaseHolder>() {
    inner class DatabaseHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private val imageButton = view as ImageButton
        private lateinit var image: Image

        init {
            view.setOnClickListener(this)
        }

        fun bind(image: Image) {
            this.image = image
            if (image.second.isNotEmpty())
                imageButton.setImageBitmap(
                    BitmapFactory.decodeByteArray(image.second, 0, image.second.size))
        }

        override fun onClick(v: View?) {
            onClickAction(image.first)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DatabaseHolder =
        DatabaseHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        )

    override fun onBindViewHolder(holder: DatabaseHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int {
        return images.size
    }
}