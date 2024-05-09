package com.triadss.doctrack2;

import android.view.WindowManager;
import androidx.test.espresso.Root;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class ToastMatcher extends TypeSafeMatcher<Root> {

    @Override
    public void describeTo(Description description) {
        description.appendText("is toast");
    }

    @Override
    protected boolean matchesSafely(Root root) {
        int type = root.getWindowLayoutParams().get().type;
        if (type == WindowManager.LayoutParams.TYPE_TOAST) {
            // Check if Toast message matches the expected message
            // Replace "expectedMessage" with your expected message
            return true; // Return true if it matches
        }
        return false;
    }

    // Custom matcher for checking Toast messages
    public static ToastMatcher isToast() {
        return new ToastMatcher();
    }
}