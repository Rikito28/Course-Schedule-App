package com.dicoding.courseschedule.ui.home

import android.app.Activity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.ui.add.AddCourseActivity
import junit.framework.TestCase
import org.junit.Rule
import org.junit.Test

class HomeActivityTest {
    @get:Rule
    var activityRule = ActivityScenarioRule(HomeActivity::class.java)
    private var course: Activity? = null

    @Test
    fun testHomeActivity() {
        onView(withId(R.id.action_add))
            .check(matches(isDisplayed()))
            .perform(ViewActions.click())
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            run {
                course = ActivityLifecycleMonitorRegistry.getInstance()
                    .getActivitiesInStage(Stage.RESUMED).elementAtOrNull(0)
                TestCase.assertTrue(course?.javaClass == AddCourseActivity::class.java)
            }
        }
    }
}