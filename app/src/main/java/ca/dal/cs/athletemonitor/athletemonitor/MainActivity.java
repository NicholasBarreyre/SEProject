package ca.dal.cs.athletemonitor.athletemonitor;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.support.v7.widget.Toolbar;

/**
 * This class describes the main screen where users can choose an activity
 * to interact with the application.
 */
public class MainActivity extends AppCompatActivity {

    private User activeUser = null;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        //Initialize toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton runNow = findViewById(R.id.featureBtn);
        runNow.setImageResource(R.drawable.ic_directions_run_black_24dp);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        findViewById(R.id.featureBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent instantRecordingIntent = new Intent(MainActivity.this, MapsActivity.class);
                instantRecordingIntent.putExtras(getIntent().getExtras());
                instantRecordingIntent.putExtra("instantRecord", true);
                startActivityForResult(instantRecordingIntent, 1);
            }
        });

        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);

                        Intent nextIntent;

                        switch (menuItem.getItemId()){
                            case R.id.goToRecordWorkoutActivity:
                                nextIntent = new Intent(MainActivity.this, MapsActivity.class);
                                break;
                            case R.id.goToExerciseActivity:
                                nextIntent = new Intent(MainActivity.this, ExerciseActivity.class);
                                break;
                            case R.id.goToGoalsActivity:
                                nextIntent = new Intent(MainActivity.this, GoalsActivity.class);
                                break;
                            case R.id.goToTeamActivity:
                                nextIntent = new Intent(MainActivity.this, TeamActivity.class);
                                break;
                            case R.id.goToUserInfoActivity:
                                nextIntent = new Intent(MainActivity.this, UserInformationActivity.class);
                                break;
                            case R.id.goToWorkoutActivity:
                                nextIntent = new Intent(MainActivity.this, WorkoutActivity.class);
                                break;
                            case R.id.btnSignOut:
                                logOutButtonHandler(navigationView);
                                nextIntent = null;
                                break;
                            default:
                                nextIntent = new Intent(MainActivity.this, MainActivity.class);
                        }

                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        if(nextIntent != null){
                            nextIntent.putExtra("user", activeUser);
                            startActivityForResult(nextIntent,1);
                        }

                        return true;
                    }
                });

        // retrieve the extras passed by the intent, if there is a username then the user is logged
        // in.  If username doesn't exist, go to sign in.
        final Bundle extras = getIntent().getExtras();
        if (extras == null || !extras.containsKey("user")) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        } else {
            activeUser = (User) extras.getSerializable("user");
        }

        ((Switch)findViewById(R.id.onlineToggleSwitch)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AccountManager.setOnline(isChecked);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Take user to the log in page and update Firebase's online_users node
     * @param view
     */
    public void logOutButtonHandler(View view){
        if(!AccountManager.isOnline()){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            builder.setMessage(R.string.logout_while_offline_warning)
                    .setTitle("Warning")
                    .setPositiveButton(R.string.logout_while_offline_warning_save, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AccountManager.setOnline(true);
                            logout();
                        }
                    })
                    .setNegativeButton(R.string.logout_while_offline_warning_quit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            logout();
                        }
                    })
                    .create()
                    .show();
        } else {
            logout();
        }
    }

    private void logout() {
        //Start by taking the user offline in Firebase
        AccountManager.setUserLoginState(activeUser.getUsername(), false);

        // Return to login activity
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 1){
            /* Returning from workout activity */
        }
    }
}
