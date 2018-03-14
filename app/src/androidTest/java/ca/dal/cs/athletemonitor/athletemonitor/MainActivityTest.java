package ca.dal.cs.athletemonitor.athletemonitor;


import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ca.dal.cs.athletemonitor.athletemonitor.listeners.BooleanResultListener;
import ca.dal.cs.athletemonitor.athletemonitor.testhelpers.TestingHelper;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.lang.Thread.sleep;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Espresso Test for the Main Activity.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    /**
     * Test user for this test set
     */
    private static User testUser;
    private static Intent intent = new Intent();

    @ClassRule
    public static IntentsTestRule<MainActivity> mActivityRule =
            new IntentsTestRule(MainActivity.class, false, false);

    /**
     * Set up test environment for this test set
     *
     * @throws Exception General exceptions
     */
    @BeforeClass
    public static void setupTestEnvironment() throws Exception {
        testUser = TestingHelper.createTestUser();
        TestingHelper.setupTestEnvironment(intent, testUser);
    }

    /**
     * Clean up test environment after this test set has run
     * @throws Exception
     */
    @AfterClass
    public static void cleanupEnvironment() throws Exception {
        AccountManager.setUserLoginState(testUser.getUsername(), false);
        AccountManager.deleteUser(testUser, null);
    }

    /**
     * Initializes and starts the activity before each test is run
     */
    @Before
    public void launchActivity() throws Exception {
        sleep(250);
        mActivityRule.launchActivity(intent);
    }

    /**
     * Test that the button to transfer to the exercise activity works.
     * @throws Exception
     */
    @Test
    public void testGoToExerciseButton() throws Exception {
        //Try to click the button.
        onView(withId(R.id.goToExerciseActivityButton)).perform(click());
        intended(hasComponent(ExerciseActivity.class.getName()));
    }

    @Test
    public void testGoToUserInfoButton() {
        onView(withId(R.id.goToUserInfo)).perform(click());
        intended(hasComponent(UserInformationActivity.class.getName()));
    }

    /**
     * Test that the sign out button works.
     * @throws Exception
     */
    @Test
    public void testSignOutButton() throws Exception {

        //Create a test user
        User testUser = TestingHelper.createTestUser();

        //Create an entry in the online_users node (in Firebase)
        AccountManager.setUserLoginState(testUser.getUsername(), false);

        onView(withId(R.id.btnSignOut)).perform(click());

        //check that Firebase has been updated
        AccountManager.isLoggedIn(testUser, TestingHelper.assertFalseBooleanResult());

        //check that Login Activity has been started
        intended(hasComponent(LoginActivity.class.getName()));
    }

    @Test
    public void testSignOutButtonOffline() throws Exception {
        AccountManager.setOnline(false);

        onView(withId(R.id.btnSignOut)).perform(click());

        onView(withText(R.string.logout_while_offline_warning)).check(matches(isDisplayed()));
        onView(withText(R.string.logout_while_offline_warning_save)).perform(click());

        assertTrue(AccountManager.isOnline());

        //check that Login Activity has been started
        intended(hasComponent(LoginActivity.class.getName()));
    }

    /**
     * Test that the button to create a new team transfers to the new team activity.
     * @throws Exception
     */
    @Test
    public void testTeamButton() throws Exception {
        // Try to click the button.
        onView(withId(R.id.teamButton)).perform(click());
        intended(hasComponent(TeamActivity.class.getName()));
    }

    /**
     * Test that the online/offline toggle switch toggles offline status
     */
    @Test
    public void testOnlineToggleSwitch() throws Exception {
        sleep(1000);
        assertTrue(AccountManager.isOnline());
        onView(withId(R.id.onlineToggleSwitch)).perform(click());
        assertFalse(AccountManager.isOnline());
    }
}
