package com.madhavth.firebaselearning

import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.FirebaseStorage
import com.madhavth.firebaselearning.Widgets.DEFAULT_IMAGE_LINK
import com.madhavth.firebaselearning.Widgets.IMAGE_SEARCH
import com.madhavth.firebaselearning.Widgets.INTENT_EXTRA_IMAGE
import kotlinx.android.synthetic.main.activity_image_search_result.*
import timber.log.Timber
import java.io.File


class ImageSearchResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_search_result)

        val imageUrl = intent.extras?.getString(INTENT_EXTRA_IMAGE)
        val url =  imageUrl +IMAGE_SEARCH

        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        webView.loadUrl(imageUrl)

        Timber.d("the url loading right now is $url")
    }
}
