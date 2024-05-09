package com.triadss.doctrack2;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.os.SystemClock;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.triadss.doctrack2.activity.healthprof.HealthProfHome;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UserAccounts {
    @Rule
    public ActivityScenarioRule<HealthProfHome> activityRule =
            new ActivityScenarioRule<HealthProfHome>(HealthProfHome.class);
    public void login(){
        onView(withId(R.id.email)).perform(typeText("testhealthprof@gmail.com"));
        onView(withId(R.id.password)).perform((typeText("test@123456")), closeSoftKeyboard());
        onView(withId(R.id.btn_login)).perform(click());
        SystemClock.sleep(5000);
    }
    @Test
    public void addUserAccount(){
        //login();
        onView(withId(R.id.patient_menu)).perform(click());
        onView(withId(R.id.patient_add_btn)).perform(click());

    }
    @Test
    public void viewUserAccount(){
        login();
        onView(withId(R.id.patient_menu)).perform(click());
        onView(withId(R.id.fragment_patient)).check(matches(isDisplayed()));
    }
    @Test
    public void updateUserAccount(){

        onView(withId(R.id.patient_menu)).perform(click());
        onView(withId(R.id.fragment_patient)).check(matches(isDisplayed()));
    }

}