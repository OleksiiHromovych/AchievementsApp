package android.hromovych.com.achievements.group

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.hromovych.com.achievements.R
import android.hromovych.com.achievements.database.BaseLab
import android.hromovych.com.achievements.dialogs.createImageAlertDialog
import android.hromovych.com.achievements.setImageFromBase
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
import androidx.fragment.app.DialogFragment
import com.theartofdev.edmodo.cropper.CropImage
import java.io.ByteArrayOutputStream


class UpdateGroupDialog(val group: Group, val okListener: (Group) -> Unit) : DialogFragment() {
    private lateinit var title: EditText
    private lateinit var viewInstance: View
    private lateinit var imageButton: ImageButton

    private var imageBaseId: Long? = null
    private var isImageFromBase: Boolean? = null

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

        viewInstance.findViewById<Button>(R.id.ok_button).setOnClickListener(
            okClickListener
        )

        imageButton = viewInstance.findViewById<ImageButton>(R.id.image_button).apply {

            setImageFromBase(group.imageId)

            setOnClickListener {
                createImageAlertDialog(context, this@UpdateGroupDialog) {
                    imageBaseId = it
                    setImageFromBase(it)
                    isImageFromBase = true
                }.show()
            }
        }
    }

    private val okClickListener = View.OnClickListener {
        group.title = title.text.toString()

        group.imageId = if (isImageFromBase != null) {
            if (isImageFromBase!!)  //image choices from database
                imageBaseId!!
            else //was used image picker, so save to base this image
                BaseLab(context).addImage(imageViewToByte(imageButton))
        } else //Image button wasn't pressed, so use default image and don't save to database
            -1
        okListener(group)
        dialog!!.dismiss()
    }

    private fun imageViewToByte(image: ImageButton): ByteArray? {
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
            isImageFromBase = false
        }
    }

}