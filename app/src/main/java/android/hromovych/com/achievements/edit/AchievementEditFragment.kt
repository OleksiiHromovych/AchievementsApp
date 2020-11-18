package android.hromovych.com.achievements.edit

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.hromovych.com.achievements.R
import android.hromovych.com.achievements.achievementElements.Achievement
import android.hromovych.com.achievements.database.BaseLab
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.ByteArrayOutputStream

private const val ARG_ACHIEVEMENT_ID = "achievement id"


class AchievementEditFragment : Fragment() {
    private var id: Long = -1
    private lateinit var achievement: Achievement

    private lateinit var title: EditText
    private lateinit var description: EditText
    private lateinit var completedCheckBox: CheckBox
    private lateinit var imageButton: ImageButton
    private lateinit var colorButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments!!.let {
            id = it.getLong(ARG_ACHIEVEMENT_ID)
        }
        achievement = BaseLab(context).getAchievement(id)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_achievement_edit, container, false)

        setup(v)

        setData()

        return v
    }

    private fun setup(v: View) {
        title = v.findViewById(R.id.achievement_title)
        description = v.findViewById(R.id.achievement_description)

        title.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                achievement.title = s.toString()
                updateAchievement()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        description.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                achievement.description = s.toString()
                updateAchievement()
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        completedCheckBox = v.findViewById(R.id.completed_cb)
        completedCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            achievement.completed = isChecked
            updateAchievement()
        }

        imageButton = v.findViewById(R.id.achievement_image)
        imageButton.setOnClickListener {
            CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .start(context!!, this)
        }

        colorButton = v.findViewById(R.id.achievement_color)
        colorButton.setOnClickListener {
            ColorDialog { color: Int ->
                achievement.color = color
                updateAchievement()
                colorButton.setBackgroundColor(color)
            }.show(fragmentManager!!, null)
        }
    }

    private fun setData() {
        val baseLab = BaseLab(context)
        val achievement = baseLab.getAchievement(id)

        title.setText(achievement.title)
        description.setText(achievement.description)
        completedCheckBox.isChecked = achievement.completed
        colorButton.setBackgroundColor(achievement.color)

        val imageBytes = baseLab.getImage(achievement.imageId)

        if (imageBytes.isNotEmpty())

            imageButton.apply {
                setImageBitmap(
                    BitmapFactory.decodeByteArray(
                        imageBytes,
                        0,
                        imageBytes.size
                    )
                )
            }

        (activity as AppCompatActivity).supportActionBar?.apply {
            title = BaseLab(context).getGroup(achievement.groupId).title
        }
    }

    private fun updateAchievement() {
        BaseLab(context).updateAchievement(achievement)
    }

    companion object {
        @JvmStatic
        fun newInstance(id: Long) =
            AchievementEditFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_ACHIEVEMENT_ID, id)
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.achievement_edit, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> fragmentManager!!.popBackStackImmediate()
            R.id.delete_menu -> {
                BaseLab(context).deleteAchievement(id)
                fragmentManager!!.popBackStackImmediate()
            }
            else -> Toast.makeText(context, item.title, Toast.LENGTH_SHORT).show()
        }
        return true
    }

    fun imageViewToByte(image: ImageButton): ByteArray? {
        val bitmap = (image.drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
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
            val imageId = BaseLab(context).addImage(imageViewToByte(imageButton))
            achievement.imageId = imageId
            updateAchievement()
        }
    }

}