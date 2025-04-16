package com.example.myapplication.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.data.Preferences

class SelectPhotoActivity : AppCompatActivity() {

    private var imageView: ImageView? = null

    private val selectImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                val resultIntent = Intent()
                resultIntent.data = uri
                setResult(Activity.RESULT_OK, resultIntent)

                val pref = Preferences(this)
                pref.saveLastPhoto(uri.toString())

                showSelectedPhoto(uri)
            } else {
                Toast.makeText(this, "Фото не вибрано", Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_CANCELED)
                finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        imageView = ImageView(this).apply {
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            scaleType = ImageView.ScaleType.FIT_CENTER
        }

        setContentView(imageView)
        selectImageLauncher.launch("image/*")
    }

    private fun showSelectedPhoto(uri: Uri) {
        imageView?.setImageURI(uri)
    }
}
