package com.example.ut4android.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.MediumTest
import com.example.ut4android.R
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@MediumTest
@ExperimentalCoroutinesApi
class LoginActivityTest {

    @get:Rule
    val rule = ActivityScenarioRule(LoginActivity::class.java)

//    @Before
//    fun setup() {
//        Intents.init()
//    }
//
//    @After
//    fun teardown() {
//        Intents.release()
//    }

    @Test
    fun testLogin() {
        // password is empty
        onView(withId(R.id.et_user_name)).perform(clearText())
            .perform(typeText("leo+1@spond.com"))
        onView(withId(R.id.et_password)).perform(clearText())
        onView(withId(R.id.btn_login)).check(matches(isNotEnabled()))

        // username is empty
        onView(withId(R.id.et_user_name)).perform(clearText())
        onView(withId(R.id.et_password)).perform(clearText())
            .perform(typeText("11111111"))
        onView(withId(R.id.btn_login)).check(matches(isNotEnabled()))

        onView(withId(R.id.et_user_name)).perform(clearText())
            .perform(typeText("leo+1@spond.com"))
        onView(withId(R.id.et_password)).perform(clearText())
            .perform(typeText("11111111"))

        onView(withId(R.id.btn_login)).check(matches(isEnabled()))
    }
}