package com.madhavth.firebaselearning.Widgets

import okhttp3.*


val BUTTON_ACTION = "something_new_or_whatever"
val BUTTON_OLD_ACTION = "old_Action_also_works"
val ACTION_UPDATE_WEATHER = "action_WEATHER_FETCH"

val BUTTON1_INTENT = "BUTTON_CLICK_1"
val BUTTON2_INTENT = "BUTTON_CLICK_2"
val BUTTON3_INTENT = "BUTTON_CLICK_3"

val ACTION_STOP_VIEW = "action.STOP_VIEW"

val BITMAP_IMAGE_NAME = "myBitMap.png"
val IMAGE_SEARCH = "https://images.google.com/searchbyimage?image_url="
val DEFAULT_IMAGE_LINK = "https://image.shutterstock.com/image-photo/white-transparent-leaf-on-mirror-260nw-1029171697.jpg"

val INTENT_EXTRA_IMAGE = "MyBitmapExtra"
val JSOUP_ADDRESS = "https://www.journaldev.com/23448/android-web-scraping-with-retrofit"

val DRAW_OVER_OTHER_APPS = 1111

fun uploadImage()
{
    val client = OkHttpClient().newBuilder()
        .build()
    val mediaType: MediaType? = MediaType.parse("text/plain")
    val body: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
        .addFormDataPart("image", "R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7")
        .build()
    val request: Request = Request.Builder()
        .url("https://api.imgur.com/3/image")
        .method("POST", body)
        .addHeader("Authorization", "Client-ID {{clientId}}")
        .build()
    val response: Response = client.newCall(request).execute()
}