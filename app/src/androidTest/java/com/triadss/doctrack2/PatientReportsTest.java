package com.triadss.doctrack2;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.triadss.doctrack2.activity.patient.PatientHome;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class PatientReportsTest {
    @Rule
    public ActivityScenarioRule<PatientHome> activityRule =
            new ActivityScenarioRule<PatientHome>(PatientHome.class);
    @Test
    public void checkDisplay(){
        onView(withId(R.id.report_menu)).perform(click());
        onView(withId(R.layout.fragment_patient_reports_list)).check(matches(isDisplayed()));
        onView(withId(R.layout.list_reports)).check(matches(isDisplayed()));
    }

}