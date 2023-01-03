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
import androidx.test.filters.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

@RunWith(Parameterized.class)
@LargeTest
public class RegistrationFormTests_Password {

    enum TestResult { ACCEPTED, LENGTH_ERROR, VALUES_ERROR };

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<MainActivity>(MainActivity.class);

    @Parameterized.Parameters
    public static List<Object> data() {
        return Arrays.asList(new Object[][] {
                { "a234567", TestResult.LENGTH_ERROR, "too_short" },
                { "a2345678", TestResult.ACCEPTED, "min_length" },
                { "a2345678901234567890123456789012", TestResult.ACCEPTED, "max_length" },
                { "a23456789012345678901234567890123", TestResult.LENGTH_ERROR, "too_long" },
                { "abcdefghijk", TestResult.VALUES_ERROR, "missing_numbers" },
                { "123456789", TestResult.VALUES_ERROR, "missing_characters" },
        });
    }

    @Parameterized.Parameter(value = 0)
    public String fieldValue;

    @Parameterized.Parameter(value = 1)
    public TestResult expectedResult;

    @Parameterized.Parameter(value = 2)
    public String testLabel;

    @Before
    public void setup() {
        Intents.init();
    }

    @After
    public void release() {
        Intents.release();
    }

    @Test
    public void password_test() {
        //nickname value
        onView(withId(R.id.login)).perform(typeText(TestHelper.VALID_NICK), closeSoftKeyboard());

        //password value (from test parameters)
        onView(withId(R.id.pwd)).perform(typeText(fieldValue), closeSoftKeyboard());

        //birthdate value
        int[] date = TestHelper.getCurrentDate();
        TestHelper.setDate(date[0] - TestHelper.VALID_AGE, date[1], date[2]);

        //ToS checkbox value
        onView(withId(R.id.consent)).perform(click());

        if(expectedResult == TestResult.ACCEPTED) {
            onView(withId(R.id.confirm)).perform(click());
            intended(hasComponent(ContentActivity.class.getName()));
        }
        else {
            onView(withId(R.id.confirm)).perform(click());
            TestHelper.isErrorMsgDisplayed(expectedResult == TestResult.LENGTH_ERROR ? R.string.err_msg_pwd_length : R.string.err_msg_pwd_signs);
        }
    }
}
