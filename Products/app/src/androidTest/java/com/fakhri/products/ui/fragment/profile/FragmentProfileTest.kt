package com.fakhri.products.ui.fragment.profile

import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.fakhri.products.MainActivity
import com.fakhri.products.ProgressBarHandler
import com.fakhri.products.R
import okhttp3.internal.wait
import org.hamcrest.CoreMatchers.any
import org.hamcrest.Matcher
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class FragmentProfileTest{

    private fun isLoading(): Boolean{
        return try {
            onView(withId(R.id.pbUser)).wait()
            true
        }catch (e: NoMatchingViewException){
            false
        }
    }
    @Before
    fun setUp(){
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun testMoveToEditProfile(){
        onView(withId(R.id.fragmentProfile)).perform(click())
        ProgressBarHandler().waitUntilGoneProgressBar()
        onView(withId(R.id.fragmentProfileRoot))
            .check(matches(isDisplayed()))

            onView(withId(R.id.btnEditProfile)).perform(click())
            onView(withId(R.id.fragmentEditProfileRoot))
                .check(matches(isDisplayed()))
    }
}