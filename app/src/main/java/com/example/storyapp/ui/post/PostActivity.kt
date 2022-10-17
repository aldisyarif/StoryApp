package com.example.storyapp.ui.post

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.example.storyapp.baseclass.BaseActivity
import com.example.storyapp.databinding.ActivityPostBinding
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
//import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

@AndroidEntryPoint
class PostActivity : BaseActivity() {

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    private val viewModel: PostViewModel by viewModels()

    private lateinit var currentPhotoPath: String
    private var getFile: File? = null


    private val binding : ActivityPostBinding by lazy {
        ActivityPostBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        goToCamera()
        goToGallery()
        uploadNewStory()
        observeResultPostStory()
    }
    private fun observeResultPostStory() {
        lifecycleScope.launchWhenStarted {
            viewModel.postResult.collect {
                if (it?.error == false){
                    Toast.makeText(this@PostActivity, it.message ?: "Success Post Story", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.errorResult.collect {
                Toast.makeText(this@PostActivity, it, Toast.LENGTH_SHORT).show()
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.loadingResult.collect {
                binding.loadingState.visibility =  if (it){
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        }

    }

    private fun uploadNewStory() {
        with(binding){
            btnUploadFeed.setOnClickListener {
                if (getFile != null && edtDescriptionStory.text?.isNotEmpty() == true){
                    val file = reduceFileImage(getFile as File)

                    val description = edtDescriptionStory.text.toString().toRequestBody("text/plain".toMediaType())
                    val lat: RequestBody? = null
                    val lon: RequestBody? = null

                    val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                        "photo",
                        file.name,
                        requestImageFile
                    )

                    viewModel.addNewStory(imageMultipart, description, lat, lon)
                } else {
                    Toast.makeText(this@PostActivity, "masukan comment", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun goToCamera() {
        with(binding){
            layoutToCamera.setOnClickListener {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                intent.resolveActivity(packageManager)

                com.example.storyapp.utils.createTempFile(this@PostActivity).also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this@PostActivity,
                        "com.example.storyapp",
                        it
                    )

                    currentPhotoPath = it.absolutePath
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    launcherIntentCamera.launch(intent)
                }
            }
        }
    }


    private fun goToGallery() {
        binding.layoutToGallery.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            val chooser = Intent.createChooser(intent, "Choose a Picture")
            launcherIntentGallery.launch(chooser)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if (it.resultCode == AppCompatActivity.RESULT_OK){

            val myFile = File(currentPhotoPath)

            getFile = myFile

            val result = BitmapFactory.decodeFile(myFile.path)

            if (result != null){
                binding.layoutResultImage.visibility = View.VISIBLE
                binding.layoutContainerAction.visibility = View.GONE
                binding.imgResultImage.setImageBitmap(result)
            } else {
                binding.layoutResultImage.visibility = View.GONE
                binding.layoutContainerAction.visibility = View.VISIBLE
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)

            getFile = myFile

            if (result != null){
                binding.layoutResultImage.visibility = View.VISIBLE
                binding.layoutContainerAction.visibility = View.GONE
                binding.imgResultImage.setImageURI(selectedImg)
            } else {
                binding.layoutResultImage.visibility = View.GONE
                binding.layoutContainerAction.visibility = View.VISIBLE
            }
        }
    }

    fun reduceFileImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > 1000000)
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()){
                Toast.makeText(
                    this,
                    "Tidak mendapatkan Permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    fun uriToFile(selectedImg: Uri, context: Context): File {
        val contentResolver: ContentResolver = context.contentResolver
        val myFile = com.example.storyapp.utils.createTempFile(context)

        val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
        val outputStream = FileOutputStream(myFile)

        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()

        return myFile
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }
}