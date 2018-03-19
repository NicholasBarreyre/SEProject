package ca.dal.cs.athletemonitor.athletemonitor;

import ca.dal.cs.athletemonitor.athletemonitor.AccountManagerTest;
import ca.dal.cs.athletemonitor.athletemonitor.listeners.BooleanResultListener;
import ca.dal.cs.athletemonitor.athletemonitor.testhelpers.TestingHelper;

import android.accounts.Account;
import android.app.Activity;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static java.lang.Thread.sleep;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * UI Test for Login Activity
 */

public class LoginActivityUITest {
    private String usernameInputTestText = "username";
    private String passwordInputTestText = "password";

   @Rule
   public IntentsTestRule<LoginActivity> intentsTestRule = new IntentsTestRule<>(LoginActivity.class);

    /**
     * Test username with empty string
     *
     * @throws Exception Generic exception
     */
    @Test
    public void usernameEmptyTest() throws Exception {
        onView(withId(R.id.txtUsername)).check(matches(withText("")));
    }

    /**
     * Test username text box with input
     *
     * @throws Exception Generic exception
     */
    @Test
    public void usernameInputTextTest() throws Exception {
        onView(withId(R.id.txtUsername)).perform(typeText(usernameInputTestText));
        closeSoftKeyboard();
        onView(withId(R.id.txtUsername)).check(matches(withText(usernameInputTestText)));
    }

    /**
     * Test password with empty string
     *
     * @throws Exception Generic exception
     */
    @Test
    public void passwordEmptyTest() throws Exception {
        onView(withId(R.id.txtPassword)).check(matches(withText("")));
    }

    /**
     * Test password text box with input
     *
     * @throws Exception Generic exception
     */
    @Test
    public void passwordInputTextTest() throws Exception {
        onView(withId(R.id.txtPassword)).perform(typeText(passwordInputTestText));
        closeSoftKeyboard();
        onView(withId(R.id.txtPassword)).check(matches(withText(passwordInputTestText)));
    }

    /**
     * Test successful login when sign in button is clicked
     *
     * @throws Exception Generic exception
     */
    @Test
    public void signInSuccessTest() throws Exception {
        //generate a test user and add it to the user accounts list
        final User testUser = TestingHelper.createTestUser();

        //use the user information to populate the controls
        AccountManager.createUser(testUser, null);

        Log.d("signInSuccessTestDebug", "Test account created...populating controls");

        //use the user information to populate the controls
        onView(withId(R.id.txtUsername)).perform(typeText(testUser.getUsername()));
        closeSoftKeyboard();
        onView(withId(R.id.txtPassword)).perform(typeText(testUser.getPassword()));
        closeSoftKeyboard();

        Log.d("signInSuccessTestDebug", "Controls populated, performing click...");
        onView(withId(R.id.btnSignIn)).perform(click());

        //clean up test user
        AccountManager.setUserLoginState(testUser.getUsername(), false);
        AccountManager.deleteUser(testUser, TestingHelper.assertTrueBooleanResult());

        sleep(3000);

        intended(hasComponent(MainActivity.class.getName()));
    }

    /**
     * Test unsuccessful login due to non existing user
     *
     * @throws Exception Generic exception
     */
    @Test
    public void signInInvalidUserTest() throws Exception {
        //generate a test user and add it to the user accounts list
        final User testUser = TestingHelper.createTestUser();

        //ensure the test account does not exist
        AccountManager.deleteUser(testUser, new BooleanResultListener() {
            @Override
            public void onResult(boolean result) {
            }
        });

        Log.d("signInInvalidUserTest", "Populating controls");

        //use the user information to populate the controls
        onView(withId(R.id.txtUsername)).perform(typeText(testUser.getUsername()));
        closeSoftKeyboard();
        onView(withId(R.id.txtPassword)).perform(typeText(testUser.getPassword()));
        closeSoftKeyboard();

        Log.d("signInInvalidUserTest", "Controls populated, performing click...");
        onView(withId(R.id.btnSignIn)).perform(click());

        sleep(3000);

        onView(withId(R.id.lblMessage)).check(matches(withText(R.string.loginFailure)));
    }

    /**
     * Tests Register button click creates account
     *
     * @throws Exception
     */
    @Test
    public void accountRegisterSuccessTest() throws Exception {
        //create a test user to register
        final User testUser = TestingHelper.createTestUser();
        final Activity loginActivity = intentsTestRule.getActivity();

        //populate controls with login information
        onView(withId(R.id.txtUsername)).perform(typeText(testUser.getUsername()));
        closeSoftKeyboard();
        onView(withId(R.id.txtPassword)).perform(typeText(testUser.getPassword()));
        closeSoftKeyboard();

        onView(withId(R.id.btnRegister)).perform(click());
        sleep(1000);

        onView(withId(R.id.lblMessage)).check(matches(withText(R.string.accountCreated)));

        AccountManager.deleteUser(testUser, TestingHelper.assertTrueBooleanResult());
    }

    /**
     * Tests Register button click creates account could not create account
     *
     * @throws Exception
     */
    @Test
    public void accountRegisterFailureTest() throws Exception {
        //create a test user to register
        final User testUser = TestingHelper.createTestUser();
        final Activity loginActivity = intentsTestRule.getActivity();

        AccountManager.createUser(testUser, null);

        //populate controls with login information
        onView(withId(R.id.txtUsername)).perform(typeText(testUser.getUsername()));
        closeSoftKeyboard();
        onView(withId(R.id.txtPassword)).perform(typeText(testUser.getPassword()));
        closeSoftKeyboard();

        onView(withId(R.id.btnRegister)).perform(click());
        sleep(1000);

        onView(withId(R.id.lblMessage)).check(matches(withText(R.string.accountNotCreated)));

        AccountManager.deleteUser(testUser, TestingHelper.assertTrueBooleanResult());
    }
}
