package ca.dal.cs.athletemonitor.athletemonitor;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ca.dal.cs.athletemonitor.athletemonitor.testhelpers.TestingHelper;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.lang.Thread.sleep;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.is;

/**
 * Espresso Test for the CreateTeam Activity.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class CreateTeamActivityTest {
    /**
     * Test user for this test set
     */
    private static User testUser;

    /**
     * Intent for launching activity
     */
    private static Intent intent = new Intent();

    @Rule
    public IntentsTestRule<CreateTeamActivity> mActivityRule =
            new IntentsTestRule(CreateTeamActivity.class, false, false);

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
     *
     * @throws Exception General exceptions
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
     * Test that the proper button and fields exist.
     *
     * @throws Exception General exceptions
     */
    @Test
    public void testProperFieldsExist() throws Exception {
        onView(withId(R.id.newTeamName));
        onView(withId(R.id.newTeamMotto));
        onView(withId(R.id.submitTeamButton));
    }

    /**
     * Test that the submit new team button is disabled if the fields are invalid
     *
     * @throws Exception General exceptions
     */
    @Test
    public void testSubmitButtonDisabled(){
        onView(withId(R.id.submitTeamButton)).check(matches(not(isEnabled())));
    }

    /**
     * Test that the submit new team button is enabled if the fields are valid
     *
     * @throws Exception General exceptions
     */
    @Test
    public void testSubmitButtonEnabled() throws Exception {
        onView(withId(R.id.newTeamName)).perform(typeText("testteam"));
        onView(withId(R.id.newTeamMotto)).perform(typeText("testmotto"), closeSoftKeyboard());
        onView(withId(R.id.submitTeamButton)).check(matches(isEnabled()));
    }

    /**
     * Tests that creating a team when clicking create team button
     *
     * @throws Exception General exception
     */
    @Test
    public void testOnCreateTeamButtonClick() throws Exception {
        final Team testTeam = TestingHelper.createTestTeam(testUser.getUsername());
        onView(withId(R.id.newTeamName)).perform(typeText(testTeam.getName()));
        onView(withId(R.id.newTeamMotto)).perform(typeText(testTeam.getMotto()), closeSoftKeyboard());
        onView(withId(R.id.submitTeamButton)).perform(click());

        intended(hasComponent(TeamActivity.class.getName()));
        onData(is(withText(testTeam.getName())));
    }
}
