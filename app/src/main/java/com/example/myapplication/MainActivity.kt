package com.example.myapplication

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.data.Preferences
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.ui.ListActivity
import com.example.myapplication.ui.SelectPhotoActivity
import com.example.myapplication.ui.WeatherActivity
import java.io.File


class MainActivity : AppCompatActivity() {

    private val REQUEST_CAMERA_PERMISSION = 1
    private var mCurrentPhotoPath = ""
    private var imageView: ImageView? = null
    private var pref: Preferences? = null

    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for ActivityMainBinding must not be null")

    private val selectPhotoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val selectedImageUri = result.data?.data
                imageView?.setImageURI(selectedImageUri)
            }
        }

    private val openCameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                savePhotoPath()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageView = binding.imageId
        pref = Preferences(this)
        loadLastPhoto()

        binding.btnButtonTakePhoto.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openCamera()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    REQUEST_CAMERA_PERMISSION
                )
            }
        }

        binding.btnButtonList.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
            startActivity(intent)
        }

        binding.btnButtonSelectPhoto.setOnClickListener {
            val intent = Intent(this, SelectPhotoActivity::class.java)
            selectPhotoLauncher.launch(intent)
        }

        binding.btnButtonWeather.setOnClickListener {
            val intent = Intent(this, WeatherActivity::class.java)
            startActivity(intent)
        }
    }

    private fun savePhotoPath() {
        val cursor = contentResolver.query(
            Uri.parse(mCurrentPhotoPath),
            Array(1) { MediaStore.Images.ImageColumns.DATA },
            null, null, null
        )
        cursor?.moveToFirst()
        val photoPath = cursor?.getString(0)
        cursor?.close()

        if (photoPath != null) {
            pref?.saveLastPhoto(photoPath)
            pref?.savePhotoToList(photoPath)
        }

        val file = photoPath?.let { File(it) }
        val uri = Uri.fromFile(file)
        imageView?.setImageURI(uri)
    }

    private fun loadLastPhoto() {
        val savedPhoto = pref?.getLastPhoto()
        if (savedPhoto != null) {
            val file = File(savedPhoto)
            val uri = Uri.fromFile(file)
            imageView?.setImageURI(uri)
        } else {
            Toast.makeText(this, "Фото не знайдено", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openCamera() {
        val values = ContentValues(1002)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        val fileUri = contentResolver
            .insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values
            )
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {
            mCurrentPhotoPath = fileUri.toString()
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
            intent.addFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION
                        or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
            openCameraLauncher.launch(intent)
        } else {
            Toast.makeText(this, "Камера недоступна", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                Toast.makeText(this, "Дозвіл на камеру не надано", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}



