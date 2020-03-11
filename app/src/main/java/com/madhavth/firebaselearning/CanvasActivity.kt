package com.madhavth.firebaselearning

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import com.madhavth.firebaselearning.Widgets.BITMAP_IMAGE_NAME
import com.madhavth.firebaselearning.Widgets.INTENT_EXTRA_IMAGE
import kotlinx.android.synthetic.main.activity_canvas.*
import timber.log.Timber
import java.io.File

class CanvasActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Draw"
        setContentView(R.layout.activity_canvas)

        btnClear.setOnClickListener {
            myCanvas.clearBitmap()
        }

        btnMinimize.setOnClickListener {
            myCanvas.saveBitmap()
            val path = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            val file = File(path, BITMAP_IMAGE_NAME)
            if (file.exists()) {
                Log.d("FilePath", file.path)
                val drawable = Drawable.createFromPath(file.path)
                imgView.background = drawable
                Toast.makeText(
                    applicationContext
                    , "drawable set",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    applicationContext, "invalid file bitmap",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        btnClose.setOnClickListener {
            //show loading or something here
            uploadImage()
        }
    }


    @SuppressLint("BinaryOperationInTimber")
    fun  uploadImage()
    {
        myCanvas.saveBitmap()
        var downloadUrl: Uri?
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val path = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        var file = File(path, BITMAP_IMAGE_NAME)

        var myFile = Uri.fromFile(file)
        val imagesRef = storageRef.child("/images/${file.name}")

        var uploadTask = imagesRef.putFile(myFile)

        uploadTask.addOnFailureListener {
            Timber.d("Image failed due to ${it.message}")
        }.addOnSuccessListener {
            Toast.makeText(
                this
                , "Image upload successfully...",
                Toast.LENGTH_SHORT
            ).show()
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Timber.d("fetched image")
            } else {
                Toast.makeText(
                    this
                    , "Image failed to upload... ",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        imagesRef.downloadUrl.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                downloadUrl = task.result
                startImageSearch(downloadUrl)
                Timber.d("the download link is $downloadUrl")
            } else {
                Timber.d("failed to load link null uri ${task.result}")
            }
        }
    }

    private fun startImageSearch(uri: Uri?)
    {
        if (uri != null) {
            val intent = Intent(this@CanvasActivity, ImageSearchResultActivity::class.java)
            intent.putExtra(INTENT_EXTRA_IMAGE, uri.toString())
            startActivity(intent)
        } else {
            Timber.d("null urll")
            Toast.makeText(
                this@CanvasActivity
                , "null url",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}
