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
import android.support.test.espresso.UiController;
//import android.support.test.espresso.ViewAction;
import androidx.test.espresso.ViewAction;
//import android.support.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import android.view.View;
import android.widget.Button;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.triadss.doctrack2.activity.MainActivity;
import com.triadss.doctrack2.activity.healthprof.HealthProfHome;
import com.triadss.doctrack2.activity.patient.PatientHome;
import com.triadss.doctrack2.dto.DateTimeDto;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AppointmentsTest {
    DateTimeDto PickerActions = null;
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<MainActivity>(MainActivity.class);
    @Before
    public void setup() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

//    @Test
//    public void requestAppointment(){
//        onView(withId(R.id.appointment_menu)).perform(click());
//        onView(withId(R.id.textInputPurpose)).perform(typeText("For Checkup"));
//        onView(withId(R.id.datebutton)).perform(click());
//        SystemClock.sleep(5000);
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DAY_OF_MONTH, 1);
//        onView(withId(android.R.id.button1)).perform(PickerActions.setDate(
//                calendar.get(Calendar.YEAR),
//                calendar.get(Calendar.MONTH) + 1,
//                calendar.get(Calendar.DAY_OF_MONTH)));
//        onView(withText("OK")).perform(click());
//        onView(withId(R.id.idBtnPickTime)).perform(click());
//        onView(withText("9:00 am - 10:00 am")).perform(click());
//        onView(withId(R.id.confirmbutton)).perform(click());
//        onView(withText("Requested")).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
//    }
    @Test
    public void acceptAppointment(){
        onView(withId(R.id.email)).perform(typeText("testhealthprof@gmail.com"));
        onView(withId(R.id.password)).perform((typeText("test@123456")), closeSoftKeyboard());
        onView(withId(R.id.btn_login)).perform(click());
        SystemClock.sleep(5000);
        onView(withId(R.id.appointment_menu)).perform(click());
        SystemClock.sleep(5000);
        onView(withId(R.id.fragment_health_professional_upcoming)).check(matches(isDisplayed()));
//        onView(withId(R.id.accept_button)).perform(click());

        onView(withId(R.id.recyclerView))
                .perform(
                        (androidx.test.espresso.ViewAction) RecyclerViewActions.actionOnItemAtPosition(
                                0,
                                new ViewAction() {
                                    @Override
                                    public Matcher<View> getConstraints() {
                                        return null;
                                    }

                                    @Override
                                    public void perform(androidx.test.espresso.UiController uiController, View view) {
                                        View button = view.findViewById(R.id.accept_button);
                                        // Maybe check for null
                                        button.performClick();
                                    }

                                    @Override
                                    public String getDescription() {
                                        return "Click on specific button";
                                    }
                                })
                );
        //onView(withText("Accepted")).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }
//    @Test
//    public void rejectAppointment(){
//        onView(withId(R.id.email)).perform(typeText("testhealthprof@gmail.com"));
//        onView(withId(R.id.password)).perform((typeText("test@123456")), closeSoftKeyboard());
//        onView(withId(R.id.btn_login)).perform(click());
//        SystemClock.sleep(5000);
//        onView(withId(R.id.appointment_menu)).perform(click());
//        SystemClock.sleep(5000);
//        onView(withId(R.id.fragment_health_professional_upcoming)).check(matches(isDisplayed()));
//        onView(withId(R.id.reject_button)).perform(click());
//       // onView(withText("Rejected")).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
//    }

//    @Test
//    public void cancelPendingAppointment(){
//        onView(withId(R.id.appointment_menu)).perform(click());
//        SystemClock.sleep(5000);
//        onView(withId(R.id.fragment_health_professional_upcoming)).perform(swipeLeft());
//        SystemClock.sleep(5000);
//        onView(withId(R.id.btn_cancel)).perform(click());
//        onView(withText("Cancelled")).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
//    }
//    @Test
//    public void rescheduleAppointment(){
//        onView(withId(R.id.appointment_menu)).perform(click());
//        SystemClock.sleep(10000);
//        onView(withId(R.id.updateDate)).perform(click());
//        SystemClock.sleep(5000);
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DAY_OF_MONTH, 1);
//        onView(withId(android.R.id.button1)).perform(PickerActions.setDate(
//                calendar.get(Calendar.YEAR),
//                calendar.get(Calendar.MONTH) + 1,
//                calendar.get(Calendar.DAY_OF_MONTH)));
//        onView(withText("OK")).perform(click());
//        onView(withId(R.id.updateDate)).perform(click());
//        onView(withText("10:00 am - 11:00 am")).perform(click());
//        SystemClock.sleep(5000);
//        onView(withId(R.id.confirmbutton));
//        onView(withText("Rescheduled")).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
//    }
//    @Test
//    public void viewAppointmentStatus(){
//        onView(withId(R.id.email)).perform(typeText("testhealthprof@gmail.com"));
//        onView(withId(R.id.password)).perform((typeText("test@123456")), closeSoftKeyboard());
//        onView(withId(R.id.btn_login)).perform(click());
//        SystemClock.sleep(5000);
//        onView(withId(R.id.appointment_menu)).perform(click());
//        SystemClock.sleep(5000);
//        onView(withId(R.id.fragment_health_professional_upcoming)).perform(swipeLeft());
//        SystemClock.sleep(5000);
//        onView(withId(R.id.fragment_health_professional_pending)).perform(swipeLeft());
//        SystemClock.sleep(5000);
//        onView(withId(R.id.fragment_health_professional_status)).check(matches(isDisplayed()));
//    }

}