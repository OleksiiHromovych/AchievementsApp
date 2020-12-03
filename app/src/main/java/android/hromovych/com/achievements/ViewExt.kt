package android.hromovych.com.achievements

import android.graphics.BitmapFactory
import android.hromovych.com.achievements.database.BaseLab
import android.widget.ImageButton


fun ImageButton.setImageFromBase(imageId: Long) {
    val imageBytes = BaseLab(context).getImage(imageId)
    if (imageBytes.isNotEmpty())
        setImageBitmap(
            BitmapFactory.decodeByteArray(
                imageBytes,
                0,
                imageBytes.size
            )
        )
}