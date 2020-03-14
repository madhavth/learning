package com.madhavth.firebaselearning

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Matrix
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Size
import android.view.Surface
import android.view.TextureView
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import kotlinx.android.synthetic.main.activity_camera.*
import timber.log.Timber
import java.io.File
import java.util.concurrent.Executors

private const val REQUEST_CAMERA_PERMISSION = 10
private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

class CameraActivity : AppCompatActivity(), LifecycleOwner {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        viewFinder = myTextureView

        if(allPermissionGranted())
        {
            viewFinder.post{
                startCamera()
            }
        }
        else
        {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CAMERA_PERMISSION)
        }

        viewFinder.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            updateTransform()
        }
    }

    private val executor = Executors.newSingleThreadExecutor()
    private lateinit var viewFinder: TextureView

    private fun startCamera()
    {
        val previewConfig = PreviewConfig.Builder().apply{
            setTargetResolution(Size(640,480))
        }.build()

        val preview = Preview(previewConfig)

        preview.setOnPreviewOutputUpdateListener{
            val parent = viewFinder.parent as ViewGroup
            parent.removeView(viewFinder)
            parent.addView(viewFinder, 0)

            viewFinder.surfaceTexture = it.surfaceTexture
            updateTransform()
        }

        val imageCaptureConfig = ImageCaptureConfig.Builder().apply {
            setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
        }.build()

        val imageCapture = ImageCapture(imageCaptureConfig)
        imgbtnSnap.setOnClickListener{
            val file = File(externalMediaDirs.first(),
                "${System.currentTimeMillis()}.jpg")

            imageCapture.takePicture(file, executor,
            object: ImageCapture.OnImageSavedListener{
                override fun onImageSaved(file: File) {
                    val msg=  "Photo capture saved: ${file.absolutePath}"
                    Timber.d("CameraXapp : $msg")

                    viewFinder.post{
                        Toast.makeText(baseContext
                            , "Photo capture saved..",
                            Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onError(
                    imageCaptureError: ImageCapture.ImageCaptureError,
                    message: String,
                    cause: Throwable?
                ) {
                    val msg = "Photo capture failed: $message"
                    Timber.d("photo capture failed: $message")
                    viewFinder.post{
                        Toast.makeText(baseContext
                        , msg,
                        Toast.LENGTH_SHORT).show()
                    }
                }

            }
                )
        }


        CameraX.bindToLifecycle(this,preview, imageCapture)
    }

    private fun updateTransform()
    {
        val matrix = Matrix()

        val centerX = viewFinder.measuredWidth/2
        val centerY = viewFinder.measuredHeight/2

        val rotationDegrees = when(viewFinder.display.rotation)
        {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> return
        }

        matrix.postRotate(-rotationDegrees.toFloat(), centerX.toFloat(), centerY.toFloat())
        viewFinder.setTransform(matrix)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == REQUEST_CAMERA_PERMISSION)
        {
            if(allPermissionGranted())
            {
                viewFinder.post{
                    startCamera()
                }
            }
            else
            {
                Toast.makeText(this
                , "access not provided by the user!!!",
                Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun allPermissionGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }
}
