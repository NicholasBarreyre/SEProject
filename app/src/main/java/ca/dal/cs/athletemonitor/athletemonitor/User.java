package ca.dal.cs.athletemonitor.athletemonitor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the account information of a user, including all data associated with
 * functionality of the app.
 */
public class User implements Serializable {
    private String username; //login username and unique identifier
    private String password; //login password

    // Initialize exercises list
    private List<Exercise> userExercises = new ArrayList<Exercise>();
    private List<Workout> userWorkouts = new ArrayList<Workout>();
    private List<Team> userTeams = new ArrayList<Team>();
    private List<Goal> userGoals = new ArrayList<Goal>();

    /**
     * No args constructor used primarily for database based initialization of user objects
     */
    public User() { /* intentionally left blank */ }

    /**
     * Constructs a user instance with the given username and password
     *
     * @param username Username of the user
     * @param password Password of the user
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Retrieves the list of teams associated with this user
     *
     * @return List of teams
     */
    public List<Team> getUserTeams() { return this.userTeams;}

    /**
     * Adds the specified team to this users list of teams
     *
     * @param team Team to add to user
     */
    public void addUserTeam(Team team) { this.userTeams.add(team); }

    public void removeUserTeam(Team team) {this.userTeams.remove(team);}
    /*public void removeUserTeam(String teamName) {
            for (Team team : userTeams) {
            if(team.getName()==teamName) {
                userTeams.remove(team);
            }
        }
        }*/
    public List<Exercise> getUserExercises() {
        return userExercises;
    }

    public void addUserExercise(Exercise exercise){
        userExercises.add(exercise);
    }

    public List<Workout> getUserWorkouts() { return userWorkouts; }

    public void addUserWorkout(Workout workout) { userWorkouts.add(workout); }

    /**
     * Retrieves the user's goal's
     *
     * @return List of user's goals
     */
    public List<Goal> getUserGoals(){ return userGoals; }

    /**
     * Add's a goal to the user's list of goals
     *
     * @param goal
     */
    public void addUserGoal(Goal goal){ userGoals.add(goal); }
    /**
     * Retrieves the username of the user
     *
     * @return User's current username
     */
    public String getUsername() { return this.username; }

    /**
     * Retrieves the password of the user
     *
     * @return User's current password
     */
    public String getPassword() { return this.password; }

    /**
     * Sets the password of the user
     *
     * @param password Desired password
     */
    public void setPassword(String password) { this.password = password; }

    /**
     * Sets the user's username
     *
     * @param username Desired username
     */
    public void setUsername(String username) { this.username = username; }
}
