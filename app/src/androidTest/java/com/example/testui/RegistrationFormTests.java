package com.example.testui;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RegistrationFormTests {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<MainActivity>(MainActivity.class);

    @Before
    public void setup() {
        Intents.init();
    }

    @After
    public void release() {
        Intents.release();
    }

    @Test
    public void birthdate_underage_test() {
        //birthdate value - 18yo without 1 day
        int[] date = TestHelper.getCurrentDate();
        TestHelper.setDate(date[0] - 18, date[1], date[2] + 1);

        //nickname value
        onView(withId(R.id.login)).perform(typeText(TestHelper.VALID_NICK), closeSoftKeyboard());

        //password value (from test parameters)
        onView(withId(R.id.pwd)).perform(typeText(TestHelper.VALID_PWD), closeSoftKeyboard());

        //ToS checkbox value
        onView(withId(R.id.consent)).perform(click());

        //validation
        onView(withId(R.id.confirm)).perform(click());
        TestHelper.isErrorMsgDisplayed(R.string.err_msg_age);
    }

    @Test
    public void birthdate_adult_test() {
        //birthdate value
        int[] date = TestHelper.getCurrentDate();
        TestHelper.setDate(date[0] - 18, date[1], date[2]);

        //nickname value
        onView(withId(R.id.login)).perform(typeText(TestHelper.VALID_NICK), closeSoftKeyboard());

        //password value (from test parameters)
        onView(withId(R.id.pwd)).perform(typeText(TestHelper.VALID_PWD), closeSoftKeyboard());

        //ToS checkbox value
        onView(withId(R.id.consent)).perform(click());

        //validation
        onView(withId(R.id.confirm)).perform(click());
        intended(hasComponent(ContentActivity.class.getName()));
    }

    @Test
    public void tos_unchecked_test() {
        //nickname value
        onView(withId(R.id.login)).perform(typeText(TestHelper.VALID_NICK), closeSoftKeyboard());

        //password value (from test parameters)
        onView(withId(R.id.pwd)).perform(typeText(TestHelper.VALID_PWD), closeSoftKeyboard());

        //birthdate value
        int[] date = TestHelper.getCurrentDate();
        TestHelper.setDate(date[0] - 18, date[1], date[2]);

        //validation
        onView(withId(R.id.confirm)).perform(click());
        TestHelper.isErrorMsgDisplayed(R.string.err_msg_tos);
    }

    @Test
    public void all_valid_test() {
        //nickname value
        onView(withId(R.id.login)).perform(typeText(TestHelper.VALID_NICK), closeSoftKeyboard());

        //password value (from test parameters)
        onView(withId(R.id.pwd)).perform(typeText(TestHelper.VALID_PWD), closeSoftKeyboard());

        //birthdate value
        int[] date = TestHelper.getCurrentDate();
        TestHelper.setDate(date[0] - TestHelper.VALID_AGE, date[1], date[2]);

        //ToS checkbox value
        onView(withId(R.id.consent)).perform(click());

        //validation
        onView(withId(R.id.confirm)).perform(click());
        intended(hasComponent(ContentActivity.class.getName()));
    }

}
