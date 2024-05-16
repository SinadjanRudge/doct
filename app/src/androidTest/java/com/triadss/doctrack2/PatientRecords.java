package com.triadss.doctrack2;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

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
public class PatientRecords {
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
    public void addPatient(){
        login();
        onView(withId(R.id.patient_menu)).perform(click());
        onView(withId(R.id.patient_add_btn)).perform(click());
        onView(withId(R.id.input_patientID)).perform(typeText("20178343"));
        onView(withId(R.id.input_Email)).perform(typeText("mingpusa16@gmail.com"));
        onView(withId(R.id.input_fullName)).perform(typeText("Michelle Placencia"));
        onView(withId(R.id.input_Gender)).perform(click());
        onView(withText("Female")).perform(click());
        onView(withId(R.id.input_address)).perform(typeText("Maribago, Lapu-lapu City"));
        onView(withId(R.id.selectBirthDate)).perform(click());
        onView(withId(R.id.input_Status)).perform(click());
        onView(withText("Single")).perform(click());
        onView(withId(R.id.input_contactNo)).perform(typeText("09123478628"));
        onView(withId(R.id.input_course)).perform(click());
        onView(withText("BS in Information Technology")).perform(click());
        onView(withId(R.id.input_Year)).perform(click());
        onView(withText("4")).perform(click());
        onView(withId(R.id.nextBtn)).perform(click());
        onView(withId(R.id.hypertension)).perform(click()).check(matches(isChecked()));
        onView(withId(R.id.input_previous_hospitalization)).perform(typeText("Appendectomy"));
        onView(withId(R.id.cb_famHist_diabetes)).perform(click()).check(matches(isChecked()));
        onView(withId(R.id.nxtButton)).perform(click());
        SystemClock.sleep(5000);
        onView(withId(R.id.nxtButton)).perform(click());
        onView(withId(R.id.input_bloodPressure)).perform(typeText("120/80"));
        onView(withId(R.id.input_temperature)).perform(typeText("37"));
        onView(withId(R.id.input_spo2)).perform(typeText("98"));
        onView(withId(R.id.input_pulseRate)).perform(typeText("86"));
        onView(withId(R.id.input_weight)).perform(typeText("50"));
        onView(withId(R.id.input_height)).perform(typeText("150"));
        onView(withId(R.id.submitBtn)).perform(click());
    }
    @Test
    public void viewRecord(){
        onView(withId(R.id.patient_menu)).perform(click());
        SystemClock.sleep(5000);
        onView(withId(R.id.patientName)).perform(click());
        onView(withId(R.id.viewPatientRecord)).check(matches(isDisplayed()));
        onView(withId(R.id.textView_personal_info)).check(matches(isDisplayed()));
        onView(withId(R.id.textView_medical_history)).check(matches(isDisplayed()));
        onView(withId(R.id.textView_vital_sign)).check(matches(isDisplayed()));
        onView(withId(R.id.textView_medication)).check(matches(isDisplayed()));
    }
    @Test
    public void updateRecord(){
        viewRecord();
        onView(withId(R.id.nxtButton)).perform(click());
        SystemClock.sleep(5000);
        onView(withId(R.id.tuborculosis)).perform(click()).check(matches(isChecked()));
        onView(withId(R.id.input_previous_hospitalization)).perform(typeText("Tumor Resection"));
        onView(withId(R.id.cb_famHist_diabetes)).perform(click()).check(matches(isChecked()));
        onView(withId(R.id.nxtButton)).perform(click());
        SystemClock.sleep(5000);
        onView(withId(R.id.input_bloodPressure)).perform(typeText("120/80"));
        onView(withId(R.id.input_temperature)).perform(typeText("37"));
        onView(withId(R.id.input_spo2)).perform(typeText("98"));
        onView(withId(R.id.input_pulseRate)).perform(typeText("86"));
        onView(withId(R.id.input_weight)).perform(typeText("50"));
        onView(withId(R.id.input_height)).perform(typeText("150"));
        onView(withId(R.id.updateBtn)).perform(click());
    }

}