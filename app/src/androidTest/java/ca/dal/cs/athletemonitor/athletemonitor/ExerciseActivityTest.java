package ca.dal.cs.athletemonitor.athletemonitor;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.LinearLayout;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

/**
 * Espresso Test for the exercise Activity.
 */
@RunWith(AndroidJUnit4.class)
public class ExerciseActivityTest {
    @Rule
    public IntentsTestRule<ExerciseActivity> mActivityRule =
            new IntentsTestRule(ExerciseActivity.class);

    @BeforeClass
    public static void setupUser(){
        new UserManager().login("user", "pass");
    }

    /**
     * Test that the button to create an exercise exist.
     * @throws Exception
     */
    @Test
    public void testHasCreateButton() throws Exception {
        //Try to get the button.
        onView(withId(R.id.createExerciseButton));
    }

    /**
     * Test that the button to transfer to the create exercise activity works.
     * @throws Exception
     */
    @Test
    public void testGoToExerciseButton() throws Exception {
        //Try to click the button.
        onView(withId(R.id.createExerciseButton)).perform(click());
        intended(hasComponent(CreateExerciseActivity.class.getName()));
    }

    @Test
    public void testCreateAndViewExercise(){
        // Click the create exercise button
        onView(withId(R.id.createExerciseButton)).perform(click());

        // Enter exercise details
        onView(withId(R.id.newExerciseName)).perform(typeText("Run"));
        onView(withId(R.id.newExerciseDescription)).perform(typeText("Run outside"));
        onView(withId(R.id.newExerciseTime)).perform(typeText("5"), closeSoftKeyboard());

        // Click submit button
        onView(withId(R.id.newExerciseSubmitButton)).perform(click());

        // Check that the new exercise is there
        onView(withParent(withId(R.id.exerciseLinearLayout))).check(matches(withText("Run")));
    }
}