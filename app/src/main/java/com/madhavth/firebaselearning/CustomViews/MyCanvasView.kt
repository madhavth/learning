package com.madhavth.firebaselearning.CustomViews

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.madhavth.firebaselearning.R
import com.madhavth.firebaselearning.Widgets.BITMAP_IMAGE_NAME
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.Files


class MyCanvasView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {
    private val mBackgroundColor = ContextCompat.getColor(context, R.color.opaque_orange)
    private val mDrawColor = ContextCompat.getColor(context, R.color.opaque_yellow)
    private val mPath = Path()
    private lateinit var mExtraCanvas: Canvas
    private lateinit var mExtraBitmap: Bitmap
    private val mPaint = Paint()
    private var mx = 0f
    private var mY = 0f
    private val TouchTolerance = 4f
    private val mRect = Rect()
    var downloadUrl: Uri?= null

    init {
        mPaint.color = mDrawColor
        mPaint.isAntiAlias = true
        mPaint.isDither = true
        mPaint.strokeWidth = 12f
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.strokeJoin = Paint.Join.ROUND
        mPaint.style = Paint.Style.STROKE
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawBitmap(mExtraBitmap, 0f, 0f, null)
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {

        when (event?.action) {
            MotionEvent.ACTION_UP -> {
                touchUp()
            }

            MotionEvent.ACTION_DOWN -> {
                touchStart(event.x, event.y)
            }

            MotionEvent.ACTION_MOVE -> {
                touchMove(event.x, event.y)
                invalidate()
            }
        }

        return true
    }

    private fun touchUp() {
        mPath.reset()
    }

    private fun touchMove(x: Float, y: Float) {
        val dx = kotlin.math.abs(x - mx)
        val dy = kotlin.math.abs(y - mY)

        if (dx >= TouchTolerance || dy >= TouchTolerance) {
            mPath.quadTo(mx, mY, (x + mx) / 2, (y + mY) / 2)
            mx = x
            mY = y
            mExtraCanvas.drawPath(mPath, mPaint)
        }
    }


    private fun touchStart(x: Float, y: Float) {
        mPath.moveTo(x, y)
        mx = x
        mY = y
    }

    fun clearBitmap() {
        mExtraBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        mExtraCanvas = Canvas(mExtraBitmap)
        mExtraCanvas.drawColor(mBackgroundColor)
        invalidate()
    }


    fun saveBitmap() {
        try {
            val localPath = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            val file = File(localPath, BITMAP_IMAGE_NAME)
            FileOutputStream(file).use { out ->
                val check = mExtraBitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                Timber.d("FilePath $check saved bitmap as ${file.path}")
            }
        } catch (e: IOException) {
            Toast.makeText(
                context
                , "failed to save file",
                Toast.LENGTH_SHORT
            ).show()
            e.printStackTrace()
        }
    }


    @SuppressLint("BinaryOperationInTimber")
    fun  uploadImage()
    {
            val storage = FirebaseStorage.getInstance()
            val storageRef = storage.reference
            val path = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            var file = File(path, BITMAP_IMAGE_NAME)

            var myFile = Uri.fromFile(file)
            val imagesRef = storageRef.child("/images/${file.name}")

            var uploadTask = imagesRef.putFile(myFile)

            uploadTask.addOnFailureListener {
                Timber.d("Image failed due to ${it.message}")
            }.addOnSuccessListener {
                Toast.makeText(
                    context
                    , "Image upload successfully...",
                    Toast.LENGTH_SHORT
                ).show()
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Timber.d("fetched image")
                } else {
                    Toast.makeText(
                        context
                        , "Image failed to upload... ",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            imagesRef.downloadUrl.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    downloadUrl = task.result
                    Timber.d("the download link is $downloadUrl")
                } else {
                    Timber.d("failed to load link null uri ${task.result}")
                }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mExtraBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        mExtraCanvas = Canvas(mExtraBitmap)
        mExtraCanvas.drawColor(mBackgroundColor)
    }
}