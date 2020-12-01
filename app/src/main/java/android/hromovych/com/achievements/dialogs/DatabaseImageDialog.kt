package android.hromovych.com.achievements.dialogs

import android.app.Dialog
import android.content.Context
import android.hromovych.com.achievements.R
import android.hromovych.com.achievements.database.BaseLab
import android.os.Bundle
import android.view.Window
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DatabaseImageDialog(context: Context, private val onImageClickAction: (Long) -> Unit) :
    Dialog(context) {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_recycler_view)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(context, 4)

        recyclerView.adapter = DatabaseImageAdapter(getImages()) {
            onImageClickAction(it)
            dismiss()
        }
    }

    private fun getImages() = BaseLab(context).getImages()

}