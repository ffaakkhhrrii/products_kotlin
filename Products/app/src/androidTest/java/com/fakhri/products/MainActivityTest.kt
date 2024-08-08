package com.fakhri.products

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest{

    @Before
    fun setUp(){
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun testBottomMenu(){
        onView(withId(R.id.homeFragment)).perform(click())
        onView(withId(R.id.fragmentHomeRoot)).check(matches(isDisplayed()))

        onView(withId(R.id.fragmentIntent)).perform(click())
        onView(withId(R.id.fragmentIntentRoot)).check(matches(isDisplayed()))

        onView(withId(R.id.fragmentProfile)).perform(click())
        onView(withId(R.id.fragmentProfileRoot)).check(matches(isDisplayed()))

        onView(withId(R.id.homeFragment)).perform(click())
        onView(withId(R.id.fragmentHomeRoot)).check(matches(isDisplayed()))
    }
}