package com.example.testui;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.widget.DatePicker;

import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.matcher.ViewMatchers;

import org.hamcrest.Matchers;

import java.util.Calendar;

public class TestHelper {

    public static final String VALID_NICK = "nickname";
    public static final String VALID_PWD = "heslo123";
    public static final int VALID_AGE = 20;

    public static int[] getCurrentDate() {
        int[] d = new int[3];
        final Calendar c = Calendar.getInstance();
        d[0] = c.get(Calendar.YEAR);
        d[1] = c.get(Calendar.MONTH)+1;
        d[2] = c.get(Calendar.DAY_OF_MONTH);
        return d;
    }

    public static void setDate(int year, int month, int day) {
        onView(withId(R.id.datePick)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(year, month, day));
        onView(withId(android.R.id.button1)).perform(click());
    }

    public static void isErrorMsgDisplayed(int msgID) {
        //toast msg check - not working
        //onView(withText(msgID)).inRoot(withDecorView(Matchers.not(decorView))).check(matches(isDisplayed()));

        //msg in textView
        onView(withId(R.id.err_msg)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.err_msg)).check(matches(withText(msgID)));
    }
}
