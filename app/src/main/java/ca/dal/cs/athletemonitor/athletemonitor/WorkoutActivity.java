package ca.dal.cs.athletemonitor.athletemonitor;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class WorkoutActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    private Spinner spinner;
    private Button submitButton;
    private Workout currentWorkout;
    private WorkoutExercise currentExercise;
    private List<Workout> workouts;
    private LinearLayout layout;
    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
        Log.d("myTag", "Created!");
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);




        AccountManager.getUser(getIntent().getExtras().getString("username"), new AccountManager.UserObjectListener() {
            @Override
            public void onUserPopulated(User user) {
                // Get the layout to add exercises to
                layout = findViewById(R.id.workoutLinearLayout);

                //boolean alternateColor = false;

                // Get the user's list of exercises
                workouts = user.getUserWorkouts();


                spinner = (Spinner) findViewById (R.id.spinner);
                ArrayAdapter<Workout> adapter = new ArrayAdapter<Workout>(getApplicationContext(),
                        android.R.layout.simple_spinner_item, workouts);
                adapter.setDropDownViewResource
                        (android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setSelection(position);

                submitButton = findViewById(R.id.submitDataButton);
                submitButton.setClickable(false);

                // Iterate and add exercises to screen
                /*
                for (Workout workout : workouts) {
                    // Build a new TextView for this exercise
                    TextView workoutText = new TextView(WorkoutActivity.this);
                    workoutText.setText(workout.getName());
                    workoutText.setTextSize(28);
                    workoutText.setPadding(0, 30, 0, 30);

                    if (alternateColor) workoutText.setBackgroundColor(Color.LTGRAY);

                    workoutText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 4, 0, 0);

                    workoutText.setLayoutParams(params);

                    // Add a click listener to show more information
                    //workoutText.setOnClickListener(new WorkoutActivity.DialogOnClickListener(workout));
                    // Add the text view to the screen
                    layout.addView(workoutText);

                    alternateColor = !alternateColor;
                }
                */
            }
        });
    }
    //}

    public void onItemSelected (AdapterView<?> parent, View view, int pos, long id) {
        currentWorkout = workouts.get(pos);
        position = pos;
        boolean isCompleted = currentWorkout.isCompleted();
        final ArrayList<WorkoutExercise> exerciseList = currentWorkout.getExercises();

        for (int i = 0; i< exerciseList.size(); i++) {
            currentExercise = exerciseList.get(i);
            // Build a new TextView for this exercise
            TextView exerciseText = new TextView(WorkoutActivity.this);
            exerciseText.setText(currentExercise.getName());
            exerciseText.setTextSize(28);
            exerciseText.setPadding(0, 10, 0, 10);

            //if (alternateColor) workoutText.setBackgroundColor(Color.LTGRAY);

            exerciseText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 4, 0, 0);

            exerciseText.setLayoutParams(params);

            // Add a click listener to show more information
            //workoutText.setOnClickListener(new WorkoutActivity.DialogOnClickListener(workout));
            // Add the text view to the screen
            layout.addView(exerciseText);

            //RelativeLayout exerciseDataLayout = new RelativeLayout(WorkoutActivity.this);
            EditText inputData = new EditText(WorkoutActivity.this);
            TextView outOfField = new TextView(WorkoutActivity.this);

            inputData.setTextSize(28);
            inputData.setPadding(0, 10, 0, 10);
            inputData.setTag("workoutExercise" + i);

            outOfField.setTextSize(28);
            outOfField.setPadding(0, 10, 0, 10);

            outOfField.setText("/ " + currentExercise.getTime() + " " + currentExercise.getTimeUnit());

            if (!isCompleted) {
                inputData.setText(currentExercise.getData());
                inputData.setBackgroundColor(Color.LTGRAY);
                inputData.setEnabled(false);
            }

            layout.addView(inputData);
            layout.addView(outOfField);
        }

        if (!isCompleted) {
            submitButton.setClickable(true);
        } else {
            // Create on click listener for submit button
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get the logged in user instance
                    AccountManager.getUser(getIntent().getExtras().getString("username"), new AccountManager.UserObjectListener() {
                        @Override
                        public void onUserPopulated(User user) {
                            if (user == null) {
                                throw new IllegalStateException("Not logged in");
                            }

                            for (int i=0; i<exerciseList.size(); i++) {
                                // Get the exercise data from the fields
                                currentExercise = exerciseList.get(i);
                                TextView exerciseView = (TextView) layout.findViewWithTag("workoutExercise" + i);
                                String data = exerciseView.getText().toString();
                                int dataValue = 0;
                                try {
                                    dataValue = Integer.parseInt(data);
                                    if (dataValue >= currentExercise.getTime())
                                    {
                                        exerciseView.setBackgroundColor(Color.GREEN);
                                    }
                                    else
                                    {
                                        exerciseView.setBackgroundColor(Color.RED);
                                    }
                                    exerciseView.setEnabled(false);
                                    currentExercise.setData(dataValue);
                                }
                                catch(Exception e) {

                                }


                                //user.getUserWorkouts().get(position).getExercises().get(i).setData(dataValue);
                            }
                            currentWorkout.setCompleted(true);
                            AccountManager.updateUser(user);
                        }
                    });
                }
            });
        }
    }

    public void onNothingSelected (AdapterView<?> parent)
    {

    }

}



