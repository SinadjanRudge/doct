package com.triadss.doctrack2;

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

import com.triadss.doctrack2.activity.healthprof.HealthProfHome;
import com.triadss.doctrack2.activity.patient.PatientHome;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ConnectDevice {
    @Rule
    public ActivityScenarioRule<PatientHome> activityRule =
            new ActivityScenarioRule<PatientHome>(PatientHome.class);
    public void login(){
        onView(withId(R.id.email)).perform(typeText("testpatient198@gmail.com"));
        onView(withId(R.id.password)).perform((typeText("123456")), closeSoftKeyboard());
        onView(withId(R.id.btn_login)).perform(click());
        SystemClock.sleep(5000);
    }

    @Test
    public void checkpaired(){
        onView(withId(R.id.device_menu)).perform(click());
        SystemClock.sleep(5000);
        onView(withId(R.id.fragment_device)).check(matches(isDisplayed()));
        onView(withId(R.id.DeviceNameValue)).check(matches(withText("Galaxy Watch4 (7Y4V)")));
        onView(withId(R.id.DeviceIDValue)).check(matches(withText("5d18ad22")));
        onView(withId(R.id.IsNearbyValue)).check(matches(withText("true")));
    }

    @Test
    public void sync(){
        onView(withId(R.id.device_menu)).perform(click());
        SystemClock.sleep(5000);
        onView(withId(R.id.fragment_device)).check(matches(isDisplayed()));
        onView(withId(R.id.SyncDeviceBtn)).perform(click());
        onView(withText("Sync successful")).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }

}