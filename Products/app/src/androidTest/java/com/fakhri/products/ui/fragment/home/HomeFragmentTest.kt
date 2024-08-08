package com.fakhri.products.ui.fragment.home

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.fakhri.products.MainActivity
import com.fakhri.products.R
import com.fakhri.products.data.network.paging.ProductPagingAdapter
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class HomeFragmentTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    private fun getRvCount(): Int {
        var itemCount = 0
        activityRule.scenario.onActivity {
           itemCount = it.findViewById<RecyclerView>(R.id.rvProducts).adapter?.itemCount!!
        }
        return itemCount
    }

//    @Test
//    fun testPaging() {
//        if (getRvCount() > 0){
//            onView(withId(R.id.rvProducts)).perform(RecyclerViewActions.actionOnItemAtPosition<ProductPagingAdapter.ProductViewHolder>(0, click()))
//            onView(withId(R.id.fragmentDetailRoot)).check(matches(ViewMatchers.isDisplayed()))
//            onView(withId(R.id.tvName)).check(matches(withText("Essence Mscara Lash Princess")))
//        }
//    }
}