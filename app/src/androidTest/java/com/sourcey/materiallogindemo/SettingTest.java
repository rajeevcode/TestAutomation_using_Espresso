package com.sourcey.materiallogindemo;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith ( AndroidJUnit4.class )
@SmallTest
@FixMethodOrder ( MethodSorters.NAME_ASCENDING )
public class SettingTest {
    @Rule
    public ActivityTestRule <MainActivity> activityRule = new ActivityTestRule <> (
            MainActivity.class );

    /**
     * Perform action of waiting for a specific time.
     */
    public static ViewAction waitFor(final long millis) {
        return new ViewAction () {
            @Override
            public Matcher <View> getConstraints() {
                return isRoot ();
            }

            @Override
            public String getDescription() {
                return "Wait for " + millis + " milliseconds.";
            }

            @Override
            public void perform(UiController uiController, final View view) {
                uiController.loopMainThreadForAtLeast ( millis );
            }
        };
    }

    @Before
    public void signUp() {
        onView ( withId ( R.id.link_signup ) ).perform ( click () );
        onView ( withId ( R.id.input_name ) ).perform ( typeText ( "test" ) );
        onView ( withId ( R.id.input_address ) ).perform ( typeText ( "test address" ) );
        onView ( withId ( R.id.input_email ) ).perform ( typeText ( "test@test.com" ) );
        onView ( withId ( R.id.input_mobile ) ).perform ( typeText ( "9876543210" ) );

        ViewInteraction password = onView ( withId ( R.id.input_password ) ).perform ( scrollTo () );
        password.perform ( typeText ( "test@123" ) );

        ViewInteraction reEnterPassword = onView ( withId ( R.id.input_reEnterPassword ) ).perform ( scrollTo () );
        reEnterPassword.perform ( typeText ( "test@123" ), closeSoftKeyboard () );

        onView ( withId ( R.id.btn_signup ) ).perform ( click () );
        onView ( isRoot () ).perform ( waitFor ( 3000 ) );
        onView ( withText ( "Hello world!" ) ).check ( matches ( isDisplayed () ) );
    }

    @Test
    public void clickOnSetting() {
        openActionBarOverflowOrOptionsMenu ( getInstrumentation ().getTargetContext () );
        onView ( withText ( "Settings" ) ).perform ( click () );

    }
}
