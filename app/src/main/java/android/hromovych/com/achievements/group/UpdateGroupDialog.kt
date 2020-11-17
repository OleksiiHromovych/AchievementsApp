package android.hromovych.com.achievements.group

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.hromovych.com.achievements.R
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
import androidx.fragment.app.DialogFragment
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView

class UpdateGroupDialog(val group: Group, val okListener: (Group) -> Unit) : DialogFragment() {
    private lateinit var title: EditText
    private lateinit var viewInstance: View
    private lateinit var imageButton: ImageButton

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
            okListener(group)
            dialog!!.dismiss()
        }

        imageButton = viewInstance.findViewById<ImageButton>(R.id.image_button).apply {
            setOnClickListener {
                CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .setCropShape(CropImageView.CropShape.RECTANGLE)
                    .start(context, this@UpdateGroupDialog)
            }
        }
//        imageButton = viewInstance.findViewById<ImageButton>(R.id.image_button).apply {
//            setOnClickListener {
//                val getIntent = Intent(Intent.ACTION_GET_CONTENT)
//                getIntent.type = "image/*"
//
//                val pickIntent =
//                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//                pickIntent.type = "image/*"
//
//                val chooserIntent = Intent.createChooser(getIntent, "Select Image")
//                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))
//
//                startActivityForResult(chooserIntent, PICK_IMAGE_REQUEST_CODE)
//            }
//        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE_REQUEST_CODE) {
            Toast.makeText(context, "result", Toast.LENGTH_SHORT).show()
            val imageUri = data?.data
            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.decodeBitmap(
                    ImageDecoder.createSource(
                        requireContext().contentResolver,
                        imageUri!!
                    )
                )
            } else {
                MediaStore.Images.Media.getBitmap(requireContext().contentResolver, imageUri)
            }
            imageButton.setImageBitmap(bitmap)
        }
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

    companion object {
        const val PICK_IMAGE_REQUEST_CODE = 255
    }
}