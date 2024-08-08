package com.fakhri.products.ui.fragment.profile.editprofile

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.fakhri.products.MainActivity
import com.fakhri.products.ProgressBarHandler
import com.fakhri.products.R
import kotlinx.coroutines.delay
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class FragmentEditProfileTest{

    @Before
    fun setUp(){
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun testChangeNameUser(){
        onView(withId(R.id.fragmentProfile)).perform(click())
        ProgressBarHandler().waitUntilGoneProgressBar()
        onView(withId(R.id.fragmentProfileRoot))
            .check(matches(ViewMatchers.isDisplayed()))

        onView(withId(R.id.btnEditProfile)).perform(click())
        onView(withId(R.id.fragmentEditProfileRoot))
            .check(matches(ViewMatchers.isDisplayed()))

        onView(withId(R.id.edtFirstName)).perform(
            typeText("Fakhri"),
            closeSoftKeyboard()
        )

        onView(withId(R.id.btnUpdate)).check(matches(isDisplayed()))
        onView(withId(R.id.btnUpdate)).perform(click())
    }

    //onView(withId(R.id.tvName)).check(ViewAssertions.matches(ViewMatchers.withText("Fakhri Zain")))


}