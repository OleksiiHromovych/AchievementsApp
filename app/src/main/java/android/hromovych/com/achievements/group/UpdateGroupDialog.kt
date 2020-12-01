package android.hromovych.com.achievements.group

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.hromovych.com.achievements.R
import android.hromovych.com.achievements.database.BaseLab
import android.hromovych.com.achievements.dialogs.DatabaseImageDialog
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.ByteArrayOutputStream


class UpdateGroupDialog(val group: Group, val okListener: (Group) -> Unit) : DialogFragment() {
    private lateinit var title: EditText
    private lateinit var viewInstance: View
    private lateinit var imageButton: ImageButton

    private var imageBaseId: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.dialog_new_group, container)
        viewInstance = v
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)

        setData()

        return v
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.Theme_Dialog)
    }

    private fun setData() {

        viewInstance.findViewById<Button>(R.id.cancel_button).setOnClickListener {
            dialog!!.dismiss()
        }
        title = viewInstance.findViewById(R.id.title_view)
        title.setText(group.title)

        viewInstance.findViewById<Button>(R.id.ok_button).setOnClickListener {
            group.title = title.text.toString()

            val imageId = if (imageBaseId != null) imageBaseId!! else BaseLab(context).addImage(
                imageViewToByte(imageButton)
            )
            group.imageId = imageId

            okListener(group)
            dialog!!.dismiss()
        }

        imageButton = viewInstance.findViewById<ImageButton>(R.id.image_button).apply {

            setImageFromBase(group.imageId)

            setOnClickListener {
                AlertDialog.Builder(context)
                    .setTitle("Action for image")
                    .setPositiveButton("New") { _, _ ->

                        CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1, 1)
                            .setCropShape(CropImageView.CropShape.RECTANGLE)
                            .start(context, this@UpdateGroupDialog)

                    }

                    .setNegativeButton("From base") { _, _ ->
                        DatabaseImageDialog(context) {
                            imageBaseId = it
                            setImageFromBase(it)
                            Toast.makeText(context, "NegativeButton", Toast.LENGTH_SHORT).show()
                        }.show()
                    }
                    .create()
                    .show()
            }
        }
    }

    private fun ImageButton.setImageFromBase(imageId: Long) {
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

    fun imageViewToByte(image: ImageButton): ByteArray? {
        val bitmap = (image.drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.decodeBitmap(
                    ImageDecoder.createSource(
                        requireContext().contentResolver,
                        result.uri
                    )
                )
            } else {
                MediaStore.Images.Media.getBitmap(
                    requireContext().contentResolver, result.uri
                )
            }
            imageButton.apply {
                setImageBitmap(Bitmap.createScaledBitmap(bitmap, width, height, true))
            }
        }
    }

}