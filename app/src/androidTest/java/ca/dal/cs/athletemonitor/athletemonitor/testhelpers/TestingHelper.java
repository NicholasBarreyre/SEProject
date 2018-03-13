package ca.dal.cs.athletemonitor.athletemonitor.testhelpers;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import ca.dal.cs.athletemonitor.athletemonitor.AccountManager;
import ca.dal.cs.athletemonitor.athletemonitor.Exercise;
import ca.dal.cs.athletemonitor.athletemonitor.User;
import ca.dal.cs.athletemonitor.athletemonitor.listeners.BooleanResultListener;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * This class encapsulates common methods, listeners, and object creation requirements used in
 * unit and instrumented tests.
 */
public class TestingHelper {

    public static final Exercise testExercise1 = new Exercise("exercise 1", "description", 5, TimeUnit.MINUTES);
    public static final Exercise testExercise2 = new Exercise("exercise 2", "description", 5, TimeUnit.SECONDS);
    public static final Exercise testExercise3 = new Exercise("exercise 3", "description", 5, TimeUnit.HOURS);

    /**
     * Creates a BooleanResultListener with default behaviour of asserting a true result as true
     *
     * @return Listener with assertTrue behaviour
     */
    public static BooleanResultListener assertTrueBooleanResult() {
        return new BooleanResultListener() {
            @Override
            public void onResult(boolean result) {
                assertTrue("True value expected, seen False...", result);
            }
        };
    }

    /**
     * Creates a BooleanResultListener with default behaviour of asserting a false result
     *
     * @return Listener with assertTrue behaviour
     */
    public static BooleanResultListener assertFalseBooleanResult() {
        return new BooleanResultListener() {
            @Override
            public void onResult(boolean result) {
                assertFalse("False value expected, seen True...", result);
            }
        };
    }

    /**
     * Test helper method to generate the standard testing user account
     *
     * @return Pre-determined user object with known information for testing purposes
     */
    public static User createTestUser() {
        int randomNum = (int) (Math.random() * 1000);

        return new User("test_user" + randomNum, "test_password");
    }

    /**
     * Test helper to authenticate the testuser
     */
    public static void authTestUser(){
        AccountManager.authenticate(new User("testuser", "abc"), null);
    }

    /**
     * Test helper to reset the testuser's exercise list
     */
    public static void resetTestUserExercises(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersReference = database.getReference("users/testuser");
        usersReference.child("userExercises").setValue(null);
    }

    public static void resetTestTeam(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference teamsReference = database.getReference("teams");
        teamsReference.child("testteam").setValue(null);
    }

    /**
     * Test helper to reset the testuser's workout list
     */
    public static void resetTestUserWorkouts(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersReference = database.getReference("users/testuser");
        usersReference.child("userWorkouts").setValue(null);
    }

    /**
     * Test helper to add test exercises to testuser
     */
    public static void addTestUserExercises(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersReference = database.getReference("users/testuser");

        ArrayList<Exercise> testExercises = new ArrayList<>();
        testExercises.add(testExercise1);
        testExercises.add(testExercise2);
        testExercises.add(testExercise3);
        usersReference.child("userExercises").setValue(testExercises);
    }
}