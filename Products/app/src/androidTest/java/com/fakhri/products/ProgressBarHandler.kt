package com.fakhri.products

import androidx.annotation.IdRes
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiSelector

class ProgressBarHandler {
    fun waitUntilGoneProgressBar(timeout: Long = 10000) {
        progressBar().waitUntilGone(timeout)
    }

    // Mengambil UiObject untuk progress bar
    private fun progressBar(): UiObject {
        return uiObjectWithId(R.id.pbUser)
    }

    // Mengambil UiObject berdasarkan resource ID
    private fun uiObjectWithId(@IdRes id: Int): UiObject {
        val resourceId = getTargetContext().resources.getResourceName(id)
        val selector = UiSelector().resourceId(resourceId)
        return UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).findObject(selector)
    }

    // Mendapatkan context target dari InstrumentationRegistry
    private fun getTargetContext() = InstrumentationRegistry.getInstrumentation().targetContext

}