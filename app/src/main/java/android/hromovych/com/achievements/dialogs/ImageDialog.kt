package android.hromovych.com.achievements.dialogs

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView

//AlertDialog for choice how to pick image. From database or do new one
fun createImageAlertDialog(
    context: Context,
    fragment: Fragment,
    onDatabaseImageClickListener: (Long) -> Unit
) =
    AlertDialog.Builder(context)
        .setTitle("Action for image")
        .setPositiveButton("New") { _, _ ->

            CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .start(context, fragment)

        }

        .setNegativeButton("From base") { _, _ ->
            DatabaseImageDialog(context, onDatabaseImageClickListener).show()
        }
        .create()

