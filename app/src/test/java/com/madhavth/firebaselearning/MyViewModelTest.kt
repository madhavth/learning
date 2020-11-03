package com.madhavth.firebaselearning

import android.content.Context
import io.reactivex.rxkotlin.toFlowable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import okhttp3.*
import org.hamcrest.CoreMatchers.`is`
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.runners.MockitoJUnitRunner
import java.lang.Exception

@RunWith(MockitoJUnitRunner::class)
class MyViewModelTest {

    @Test
    fun testingSomethings1() {
        assertEquals(EmailValidator.isValidEmail("s"), true)
    }

    @Mock
    private lateinit var mockContext: Context


    @Test
    fun readSettingsFromFakeContext()
    {
        val fakeValue ="this is obviously a fake as fck string"
        `when`(mockContext.getString(R.string.demo_string)).thenReturn(fakeValue)

        val testing = mockContext.getString(R.string.demo_string)

        assertThat(testing, `is`(fakeValue))
    }


    @Mock
    private lateinit var fakeHttpClient: OkHttpClient

    @Test
    fun getDataFromApi(): Unit = runBlocking {


        val realHttpClient = OkHttpClient()
        val request = Request.Builder().url("https://www.google.com/").build()


        val response = fakeHttpClient.newCall(request).execute()

        println(response.body())

        assert(response.isSuccessful)
    }



}

class EmailValidator {
    companion object {
        fun isValidEmail(s: String): Boolean
        {
            return true
        }
    }
}