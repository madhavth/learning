package com.madhavth.firebaselearning

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit3.AndroidJUnit3Builder
import androidx.test.rule.ActivityTestRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class ExpressoTest {

    @get:Rule
    var activityRule: ActivityTestRule<ScrapingActivity> = ActivityTestRule(
        ScrapingActivity::class.java
    )

    @Test
    fun pressButton()
    {
        onView(withId(R.id.btnService)).perform(click())
    }
}