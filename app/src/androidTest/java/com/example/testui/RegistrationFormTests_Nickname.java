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
public class RegistrationFormTests_Nickname {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<MainActivity>(MainActivity.class);

    @Parameterized.Parameters
    public static List<Object> data() {
        return Arrays.asList(new Object[][] {
                { "12345", false, "too_short" },
                { "123456", true, "min_length" },
                { "12345678901234567890123456789012", true, "max_length" },
                { "123456789012345678901234567890123", false, "too_long" },
        });
    }

    @Parameterized.Parameter(value = 0)
    public String fieldValue;

    @Parameterized.Parameter(value = 1)
    public boolean acceptTest;

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
    public void nickname_test() {
        //nickname value (from test parameters)
        onView(withId(R.id.login)).perform(typeText(fieldValue), closeSoftKeyboard());

        //password value
        onView(withId(R.id.pwd)).perform(typeText(TestHelper.VALID_PWD), closeSoftKeyboard());

        //birthdate value
        int[] date = TestHelper.getCurrentDate();
        TestHelper.setDate(date[0] - TestHelper.VALID_AGE, date[1], date[2]);

        //ToS checkbox value
        onView(withId(R.id.consent)).perform(click());

        if(acceptTest) {
            onView(withId(R.id.confirm)).perform(click());
            intended(hasComponent(ContentActivity.class.getName()));
        }
        else {
            onView(withId(R.id.confirm)).perform(click());
            TestHelper.isErrorMsgDisplayed(R.string.err_msg_nickname);
        }
    }
}
