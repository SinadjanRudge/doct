package com.triadss.doctrack2;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.os.SystemClock;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.triadss.doctrack2.activity.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class HomeTest {
    @Rule
        public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<MainActivity>(MainActivity.class);
    @Test
    public void logout(){
        onView(withId(R.id.email)).perform(typeText("testpatient198@gmail.com"));
        onView(withId(R.id.password)).perform((typeText("123456")), closeSoftKeyboard());
        onView(withId(R.id.btn_login)).perform(click());
        SystemClock.sleep(5000);
        onView(withId(R.id.activity_patient_home)).check(matches(isDisplayed()));
        onView(withId(R.id.textView15)).check(matches(withText("Hello Patient")));
        SystemClock.sleep(5000);
        onView(withId(R.id.menu)).perform(click());
        SystemClock.sleep(5000);
    }
    public void clickLogout(){
        onView(withId(R.id.logout_btn)).perform(click());
    }
    @Test
    public void checkDisplay(){
        onView(withId(R.id.logout_btn)).perform(click());
        onView(withId(R.id.email)).perform(typeText("testpatient198@gmail.com"));
        onView(withId(R.id.password)).perform((typeText("123456")), closeSoftKeyboard());
        onView(withId(R.id.btn_login)).perform(click());
        SystemClock.sleep(5000);
        onView(withId(R.id.activity_patient_home)).check(matches(isDisplayed()));
        onView(withId(R.id.textView15)).check(matches(withText("Hello Patient")));

    }
}