package com.triadss.doctrack2;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.os.SystemClock;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.triadss.doctrack2.activity.patient.PatientHome;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MedicationTest {
    @Rule
    public ActivityScenarioRule<PatientHome> activityRule =
            new ActivityScenarioRule<PatientHome>(PatientHome.class);
    @Before
    public void setup() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }
    public void login(){
        onView(withId(R.id.email)).perform(typeText("testpatient198@gmail.com"));
        onView(withId(R.id.password)).perform((typeText("123456")), closeSoftKeyboard());
        onView(withId(R.id.btn_login)).perform(click());
        SystemClock.sleep(5000);
    }
    @Test
    public void viewCompletedMedication(){
        //login();
        SystemClock.sleep(5000);
        onView(withId(R.id.medication_menu)).perform(click());
        SystemClock.sleep(5000);
        onView(withId(R.id.fragment_patient_medication_completed)).check(matches(isDisplayed()));
    }
    @Test
    public void viewOngoingMedication(){
        login();
        SystemClock.sleep(5000);
        onView(withId(R.id.medication_menu)).perform(click());
        onView(withId(R.id.fragment_patient_medication_completed)).perform(swipeLeft());
        SystemClock.sleep(5000);
        onView(withId(R.id.fragment_patient_medication_ongoing)).check(matches(isDisplayed()));
    }
    @Test
    public void updateOngoingMedication(){
        viewOngoingMedication();
        SystemClock.sleep(5000);
        onView(withId(R.id.fragment_patient_medication_ongoing)).check(matches(isDisplayed()));
    }

}