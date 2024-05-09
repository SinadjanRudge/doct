package com.triadss.doctrack2;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.core.AllOf.allOf;

import android.app.DatePickerDialog;
import android.os.SystemClock;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.triadss.doctrack2.activity.healthprof.HealthProfHome;
import com.triadss.doctrack2.activity.patient.PatientHome;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AppointmentsTest {
    @Rule
    public ActivityScenarioRule<HealthProfHome> activityRule =
            new ActivityScenarioRule<HealthProfHome>(HealthProfHome.class);
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
    //@Test
    public void requestAppointment(){
        login();
        onView(withId(R.id.appointment_menu)).perform(click());
        SystemClock.sleep(5000);
        onView(withId(R.id.textInputPurpose)).perform(typeText("For Checkup"));
        onView(withId(R.id.datebutton)).perform(click());
        SystemClock.sleep(5000);
        onView(withId(R.id.idBtnPickTime)).perform(click());
        SystemClock.sleep(5000);
        onView(withId(R.id.confirmbutton)).perform(click());

    }
    //@Test
//    public void rescheduleAppointment(){
//        onView(withId(R.id.email)).perform(typeText("testhealthprof@gmail.com"));
//        onView(withId(R.id.password)).perform((typeText("test@123456")), closeSoftKeyboard());
//        onView(withId(R.id.btn_login)).perform(click());
//        SystemClock.sleep(5000);
//        onView(withId(R.id.appointment_menu)).perform(click());
//        SystemClock.sleep(5000);
//        onView(withId(R.id.fragment_health_professional_upcoming)).check(matches(isDisplayed()));
//        onView(withId(R.id.accept_button)).perform(click());
//    }
    //@Test
    public void rejectAppointment(){
        SystemClock.sleep(5000);
        onView(withId(R.id.appointment_menu)).perform(click());
        SystemClock.sleep(5000);
        onView(withId(R.id.fragment_health_professional_upcoming)).check(matches(isDisplayed()));
        onView(withId(R.id.reject_button)).perform(click());
    }
    //@Test
    public void viewUpcomingAppointments(){
        SystemClock.sleep(5000);
        onView(withId(R.id.appointment_menu)).perform(click());
        SystemClock.sleep(5000);
        onView(withId(R.id.fragment_health_professional_upcoming)).check(matches(isDisplayed()));
    }
//    @Test
    public void viewPendingAppointments(){
        SystemClock.sleep(5000);
        onView(withId(R.id.appointment_menu)).perform(click());
        SystemClock.sleep(5000);
        onView(withId(R.id.fragment_health_professional_upcoming)).perform(swipeLeft());
        SystemClock.sleep(5000);
        onView(withId(R.id.fragment_health_professional_pending)).check(matches(isDisplayed()));
    }

    //@Test
    public void cancelPendingAppointment(){
        onView(withId(R.id.email)).perform(typeText("testhealthprof@gmail.com"));
        onView(withId(R.id.password)).perform((typeText("test@123456")), closeSoftKeyboard());
        onView(withId(R.id.btn_login)).perform(click());
        SystemClock.sleep(5000);
        onView(withId(R.id.appointment_menu)).perform(click());
        SystemClock.sleep(5000);
        onView(withId(R.id.fragment_health_professional_upcoming)).perform(swipeLeft());
        SystemClock.sleep(5000);
        //onView(withId(R.id.btn_cancel)).perform(click());
    }
    //@Test
    public void rescheduleAppointment(){
        onView(withId(R.id.email)).perform(typeText("testhealthprof@gmail.com"));
        onView(withId(R.id.password)).perform((typeText("test@123456")), closeSoftKeyboard());
        onView(withId(R.id.btn_login)).perform(click());
        SystemClock.sleep(5000);
        onView(withId(R.id.appointment_menu)).perform(click());
        SystemClock.sleep(10000);
        onView(withId(R.id.updateDate)).perform(click());
        SystemClock.sleep(5000);
        onView(withId(R.id.updateTime)).perform(click());
        SystemClock.sleep(5000);
        onView(withId(R.id.confirmbutton));
    }
    @Test
    public void viewAppointmentStatus(){
        login();
        SystemClock.sleep(5000);
        onView(withId(R.id.appointment_menu)).perform(click());
        SystemClock.sleep(5000);
        onView(withId(R.id.activity_patient_request)).perform(swipeLeft());
        SystemClock.sleep(5000);
        onView(withId(R.id.fragment_patient_appointment_pending)).perform(swipeLeft());
        SystemClock.sleep(5000);
        onView(withId(R.id.fragment_patient_appointment_status)).check(matches(isDisplayed()));
    }

}