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

import com.triadss.doctrack2.activity.MainActivity;
import com.triadss.doctrack2.activity.healthprof.HealthProfHome;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UserAccounts {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<MainActivity>(MainActivity.class);
    public void login(){
        onView(withId(R.id.email)).perform((typeText("mingmiing1@gmail.com")), closeSoftKeyboard());
        onView(withId(R.id.password)).perform((typeText("Muning111")), closeSoftKeyboard());
        onView(withId(R.id.btn_login)).perform(click());
        SystemClock.sleep(5000);
    }

    @Test
    public void addUserAccount(){
        onView(withId(R.id.healthprof_menu)).perform(click());
        onView(withId(R.id.addHealthProfBtn)).perform(click());
        onView(withId(R.id.editTextHWN)).perform((typeText("Michelle Placencia")), closeSoftKeyboard());
        onView(withId(R.id.editTextPosition)).perform((typeText("Doctor")), closeSoftKeyboard());
        onView(withId(R.id.editTextUserName)).perform((typeText("docmich")), closeSoftKeyboard());
        onView(withId(R.id.editTextEmail)).perform((typeText("placenciamichelle14@gmail.com")), closeSoftKeyboard());
        onView(withId(R.id.editTextPassword)).perform((typeText("password123")), closeSoftKeyboard());
        onView(withId(R.id.editTextGender)).perform((typeText("Female")), closeSoftKeyboard());
        onView(withId(R.id.buttonSubmit)).perform(click());
        onView(withId(R.id.listHealthProf)).check(matches(withText("Michelle Placencia")));
    }
    @Test
    public void viewUserAccount(){
        onView(withId(R.id.healthprof_menu)).perform(click());
        onView(withId(R.id.manageUserAccounts)).check(matches(isDisplayed()));
        SystemClock.sleep(5000);
        onView(withId(R.id.viewBtn)).perform(click());
        SystemClock.sleep(5000);
        onView(withId(R.id.healthWorkerName)).check(matches(withText("Mary Ann Inoc")));
        onView(withId(R.id.UserName)).check(matches(withText("maryann")));
        onView(withId(R.id.Gender)).check(matches(withText("Female")));
        onView(withId(R.id.Position)).check(matches(withText("Doctor")));
    }
    @Test
    public void updateUserAccount(){
        onView(withId(R.id.healthprof_menu)).perform(click());
        SystemClock.sleep(5000);
        onView(withId(R.id.updateBtn)).perform(click());
        onView(withId(R.id.editTextPosition)).check(matches(withText("Doctor")));
        onView(withId(R.id.buttonUpdateHealthProf)).perform(click());
    }

}