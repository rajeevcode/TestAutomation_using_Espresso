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

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

@RunWith ( AndroidJUnit4.class )
@SmallTest
@FixMethodOrder ( MethodSorters.NAME_ASCENDING )
public class LoginTest {
    @Rule
    public ActivityTestRule <MainActivity> mActivityRule = new ActivityTestRule <> (
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

        try {
            Thread.sleep ( 3000 );
        } catch (Exception e) {
            e.printStackTrace ();
        }

        onView ( withText ( "Hello world!" ) ).check ( matches ( isDisplayed () ) );
        onView ( withId ( R.id.btn_logout ) ).perform ( click () );

    }

    @Test
    public void testLogin() {
        onView ( withId ( R.id.input_email ) ).perform ( typeText ( "test@test.com" ) );
        ViewInteraction inoutPassword = onView ( withId ( R.id.input_password ) ).perform ( scrollTo () );
        inoutPassword.perform ( typeText ( "test@123" ) );
        onView ( withId ( R.id.btn_login ) ).perform ( click () );
        onView ( isRoot () ).perform ( waitFor ( 3000 ) );
        onView ( withText ( "Hello world!" ) ).check ( matches ( isDisplayed () ) );
    }

    @Test
    public void testLoginFailedWithWrongPassword() {
        onView ( withId ( R.id.input_email ) ).perform ( typeText ( "test@test.com" ) );
        ViewInteraction inoutPassword = onView ( withId ( R.id.input_password ) ).perform ( scrollTo () );
        inoutPassword.perform ( typeText ( "test@12345" ) );
        onView ( withId ( R.id.btn_login ) ).perform ( click () );
        onView ( withId ( R.id.input_password ) ).check ( matches ( hasErrorText ( "enter a valid email address or password" ) ) );
        onView ( withText ( "Login failed" ) ).inRoot ( withDecorView ( not ( is ( mActivityRule.getActivity ().getWindow ().getDecorView () ) ) ) ).check ( matches ( isDisplayed () ) );
    }

    @Test
    public void testLoginFailedWithBlankPassword() {
        onView ( withId ( R.id.input_email ) ).perform ( typeText ( "test@test.com" ) );
        onView ( withId ( R.id.btn_login ) ).perform ( click () );
        onView ( withId ( R.id.input_password ) ).check ( matches ( hasErrorText ( "between 4 and 10 alphanumeric characters" ) ) );
        onView ( withText ( "Login failed" ) ).inRoot ( withDecorView ( not ( is ( mActivityRule.getActivity ().getWindow ().getDecorView () ) ) ) ).check ( matches ( isDisplayed () ) );
    }

    @Test
    public void testLoginFailedWithInvalidEmail() {
        onView ( withId ( R.id.input_email ) ).perform ( typeText ( "test" ) );
        onView ( withId ( R.id.btn_login ) ).perform ( click () );
        onView ( withId ( R.id.input_email ) ).check ( matches ( hasErrorText ( "enter a valid email address" ) ) );
        onView ( withText ( "Login failed" ) ).inRoot ( withDecorView ( not ( is ( mActivityRule.getActivity ().getWindow ().getDecorView () ) ) ) ).check ( matches ( isDisplayed () ) );
    }

    @Test
    public void testReLogin() {
        onView ( withId ( R.id.link_signup ) ).perform ( click () );
        onView ( withId ( R.id.input_name ) ).perform ( closeSoftKeyboard () );
        onView ( withId ( R.id.link_login ) ).perform ( scrollTo (), click () );
        onView ( withId ( R.id.input_email ) ).perform ( typeText ( "test@test.com" ) );
        ViewInteraction inoutPassword = onView ( withId ( R.id.input_password ) ).perform ( scrollTo () );
        inoutPassword.perform ( typeText ( "test@123" ) );
        onView ( withId ( R.id.btn_login ) ).perform ( click () );
        onView ( isRoot () ).perform ( waitFor ( 3000 ) );
        onView ( withText ( "Hello world!" ) ).check ( matches ( isDisplayed () ) );
    }
}