package com.triadss.doctrack2;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


import static org.hamcrest.CoreMatchers.not;

import android.os.SystemClock;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.triadss.doctrack2.R;
import com.triadss.doctrack2.activity.LoginActivity;
import com.triadss.doctrack2.activity.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginActivityTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);
//    @Test
//    public void invalidEntries(){
//        onView(withId(R.id.logout_btn)).perform(click());
//        SystemClock.sleep(5000);
//        onView(withId(R.id.email)).perform((typeText("testpatient198@gmail.com12312")),closeSoftKeyboard());
//        onView(withId(R.id.password)).perform((typeText("123456")), closeSoftKeyboard());
//        onView(withId(R.id.btn_login)).perform(click());
//        SystemClock.sleep(5000);
//
//        onView(withId(R.id.logToyourAccount)).check(matches(withText("Email is incorrect.")));
//    }
    @Test
    public void validEntries(){
        onView(withId(R.id.logout_btn)).perform(click());
        onView(withId(R.id.email)).perform((typeText("testpatient198@gmail.com")),closeSoftKeyboard());;
        onView(withId(R.id.password)).perform((typeText("123456")), closeSoftKeyboard());
        onView(withId(R.id.btn_login)).perform(click());
        SystemClock.sleep(5000);
        onView(withId(R.id.activity_patient_home)).check(matches(isDisplayed()));
    }
    @Test
    public void invalidAccount(){
        onView(withId(R.id.logout_btn)).perform(click());
        SystemClock.sleep(5000);
        onView(withId(R.id.email)).perform((typeText("iwjshshsuej@gmail.com")), closeSoftKeyboard());
        onView(withId(R.id.password)).perform((typeText("28627383")), closeSoftKeyboard());
        onView(withId(R.id.btn_login)).perform(click());
        SystemClock.sleep(5000);

        onView(withId(R.id.logToyourAccount)).check(matches(withText("Please verify your email before logging in.")));
    }
    @Test
    public void validAccount(){
        onView(withId(R.id.email)).perform((typeText("testpatient198@gmail.com")), closeSoftKeyboard());;
        onView(withId(R.id.password)).perform((typeText("123456")), closeSoftKeyboard());
        onView(withId(R.id.btn_login)).perform(click());
        SystemClock.sleep(5000);
        onView(withId(R.id.activity_patient_home)).check(matches(isDisplayed()));
    }
}
