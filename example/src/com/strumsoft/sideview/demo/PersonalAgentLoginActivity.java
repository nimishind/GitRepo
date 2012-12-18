package com.strumsoft.sideview.demo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;

import com.strumsoft.sideview.demo.switchs.Switchs;
import com.strumsoft.sideview.demo.util.FormatsHelper;

public class PersonalAgentLoginActivity extends SherlockActivity {
    // android ui and functional elements
    private EditText editTextUsername;
    private EditText editTextPassword;
    private Switchs rememberPasswordSwitch;
    private TextView textViewResponse;
    private InputMethodManager inputMethodManager;
    private Intent NEXT_INTENT;
    private Intent SIGN_UP_OR_FORGOT_PASSWORD_INTENT;
    private PersonalAgentApplication APPLICATION;
    private SharedPreferences sharedPreferences;
    private TextView lableSignUpOrForgotPassword;
    // plain data elements
    private String theUsernameEmail;
    private String thePassword;
   
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.header_bg));
        actionBar.show();
        APPLICATION = (PersonalAgentApplication) getApplication();
 
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(APPLICATION);

        APPLICATION.setUsername(sharedPreferences.getString(PersonalAgentApplication.PREFERENCE_USERNAME,
                null));
        APPLICATION.setPassword(sharedPreferences.getString(PersonalAgentApplication.PREFERENCE_PASSWORD,
                null));
        APPLICATION.setAutoLoginFlag(sharedPreferences.getBoolean(
                PersonalAgentApplication.PREFERENCE_AUTO_LOGIN_FLAG, false));

        editTextUsername = (EditText) findViewById(R.id.emailAddressLogin);
        editTextPassword = (EditText) findViewById(R.id.passwordEditText);
        rememberPasswordSwitch = (Switchs) findViewById(R.id.rememberPasswordSwitch);
        lableSignUpOrForgotPassword = (TextView) findViewById(R.id.signUpLabel);
        textViewResponse = (TextView) findViewById(R.id.response);
        boolean switchPreviousState = sharedPreferences.getBoolean(
                PersonalAgentApplication.PREFERENCE_AUTO_LOGIN_FLAG, true);
        rememberPasswordSwitch.setChecked(switchPreviousState);

        // method to handle the post signup signin
        postSignupPrepopulateIfApplicable();

        if (sharedPreferences.getBoolean(PersonalAgentApplication.PREFERENCE_SIGN_UP_DONE, false)) {
            // lableSignUpOrForgotPassword.setText(R.string.forgot_password_btn_label);
        } else {
            lableSignUpOrForgotPassword.setText(R.string.sign_up_label);
        }

        // initial state of the switch is a function of any previous setting, defaults to true

        // NEXT_INTENT = new Intent ( this, PersonalAgentMainActivity.class) ; // this needs to be
        // conditional, is this the first run ?

        boolean isFirstRun = !sharedPreferences.getBoolean(
                PersonalAgentApplication.PREFERENCE_IS_FIRST_RUN_DONE, false);

        if (isFirstRun) {
            // this activity should never be called.
            // NEXT_INTENT = new Intent ( this, FirstRunOptInMainOverview.class) ;

        } else {
            // to do set as appropriate
            // NEXT_INTENT = new Intent ( this, OptInMainControlActivity.class) ;

        }

        // turn off the keyboard since it obscures the view, the big picture view of what the view is all
        // about...
        if (inputMethodManager == null) {
            inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(editTextUsername.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);

        }

        if (sharedPreferences.getBoolean(PersonalAgentApplication.PREFERENCE_SIGN_UP_DONE, false)) {
            // lableSignUpOrForgotPassword.setText(R.string.forgot_password_btn_label);
            // SIGN_UP_OR_FORGOT_PASSWORD_INTENT = new Intent ( this, ForgotPasswordActivity.class) ;

        } else {
            lableSignUpOrForgotPassword.setText(R.string.sign_up_label);
            SIGN_UP_OR_FORGOT_PASSWORD_INTENT = new Intent(this, PersonalAgentRegisterActivity.class);

        }

        if (APPLICATION.isAutoLoginFlag()) {
            autoLogin();
        }

    }

    /**
     * this method takes case of pre population of the username/password and response message (blue) Your
     * account has been created... You may login
     */
    private void postSignupPrepopulateIfApplicable() {

        if (!getIntent().getBooleanExtra(PersonalAgentApplication.STATE_RIGHT_AFTER_SIGN_UP_FLAG, false)) {
            return;
        }

        String usernameFromIntent = getIntent().getStringExtra(PersonalAgentApplication.PREFERENCE_USERNAME);
        String PasswordFromIntent = getIntent().getStringExtra(PersonalAgentApplication.PREFERENCE_PASSWORD);
        editTextUsername.setText(usernameFromIntent != null ? usernameFromIntent : "");
        editTextPassword.setText(PasswordFromIntent != null ? PasswordFromIntent : "");
        textViewResponse.setText(POST_SIGN_UP_MESSAGE);
        textViewResponse.setTextColor(Color.BLUE);
        rememberPasswordSwitch.setChecked(true);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // getSupportMenuInflater().inflate(R.menu.activity_login, menu);
        return true;
    }

    private void autoLogin() {

        if (APPLICATION.getUsername() != null && APPLICATION.getPassword() != null) {
            Log.d(TAG, "attemptAutoLogin() has available  username/password for  attempting autologin");
            autoLoginBasedAttempt = true;

            editTextUsername.setText(APPLICATION.getUsername());
            editTextPassword.setText(APPLICATION.getPassword());

            // as these are not initialized next time ( 2 run ) auto login will not work as they will be resed
            // after succesfull login. to null, null , false
            theUsernameEmail = APPLICATION.getUsername();
            thePassword = APPLICATION.getPassword();
            rememberPasswordToEnableAutoLogin = true;// its a auto logging procedure

            progressDialog = ProgressDialog.show(this, "Logging in", "Please wait ...", true, true);

            loginAsyncTask = new LoginAsyncTask();
            loginAsyncTask.execute();
        } else {
            Log.d(TAG, "attemptAutoLogin()  NO available username/password...");
        }

    }

    public void handleLogin(View view) {
        Log.d(TAG, "handleLogin()..." + editTextPassword + editTextUsername);

        theUsernameEmail = editTextUsername.getText().toString();
        thePassword = editTextPassword.getText().toString();
        rememberPasswordToEnableAutoLogin = rememberPasswordSwitch.isChecked();
        Log.d(TAG, "handleLogin() " + theUsernameEmail + " | " + thePassword + " | "
                + rememberPasswordToEnableAutoLogin);

        if ((theUsernameEmail == null || editTextPassword == null)
                || (theUsernameEmail.length() < USERNAME_EMAIL_MINIMUM_LENGTH || thePassword.length() < PASSWORD_MINIMUM_LENGTH)) {
            Log.d(TAG, "handleLogin returning due to insufficient data");
            textViewResponse.setText("Please enter email and password (minimum password length is 7)");
            textViewResponse.setTextColor(Color.RED);
            return;
        }

        if (!FormatsHelper.isValidEmailFormat(theUsernameEmail)) {

            Log.d(TAG, "handleRegister returning due to email Format issue");
            textViewResponse.setText("Email needs to be in a valid format such as yourid@yourmail.com");
            textViewResponse.setTextColor(Color.RED);

            return;
        }

        progressDialog = ProgressDialog.show(this, "Signing IN", "Please wait ...", true, true);
        APPLICATION.setUsername(theUsernameEmail);
        APPLICATION.setPassword(thePassword);

        loginAsyncTask = new PersonalAgentLoginActivity.LoginAsyncTask();
        loginAsyncTask.execute();

    }

    public void handleSolicitSignupOrForgotPassword(View view) {

        startActivity(SIGN_UP_OR_FORGOT_PASSWORD_INTENT);
        Log.d(TAG, "handleSolicitSignup()...");

    }

    public void handleRememberPasswordToggle(View view) {

        boolean passwordRememberToggle = ((ToggleButton) view).isChecked();

        if (passwordRememberToggle) {
            //
            // enable
            Log.d(TAG, "onToggleClicked()... ON");

        } else {
            // disable
            //
            Log.d(TAG, "onToggleClicked()... OFF");
        }
    }

    private class LoginAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... arg0) {

            // loginResult = PersonalAgentClient.attemptLogin() ;
            // temporarily @TODO figure out call to back end for pa indep login
            long loopCount = Integer.MAX_VALUE / 50;
            for (long l = 0; l < loopCount; ++l) {

            }

            loginResult = true; // TODO call PA Login/strong or as determined

            Log.d(TAG, "LoginAsyncTask::doInBackground()... loginResult=" + loginResult);

            if (loginResult == false) {
                return LOGIN_FAILED;
            } else {

                // meaningful to save only a working username/password combination

                Log.d(TAG, "LoginAsyncTask::doInBackground() " + theUsernameEmail + " | " + thePassword
                        + " | " + rememberPasswordToEnableAutoLogin);

                sharedPreferences.edit()
                        .putString(PersonalAgentApplication.PREFERENCE_USERNAME, theUsernameEmail).commit();
                sharedPreferences.edit().putString(PersonalAgentApplication.PREFERENCE_PASSWORD, thePassword)
                        .commit();
                sharedPreferences
                        .edit()
                        .putBoolean(PersonalAgentApplication.PREFERENCE_AUTO_LOGIN_FLAG,
                                rememberPasswordToEnableAutoLogin).commit();
                // if logged in directly then can be considered that sign up was done.
                sharedPreferences.edit().putBoolean(PersonalAgentApplication.PREFERENCE_SIGN_UP_DONE, true)
                        .commit();
                APPLICATION.setUsername(theUsernameEmail);
                APPLICATION.setPassword(thePassword);

                Log.d(TAG,
                        "LoginAsyncTask::doInBackground() after saving preferences : "
                                + sharedPreferences.getString(PersonalAgentApplication.PREFERENCE_USERNAME,
                                        null)
                                + " | "
                                + sharedPreferences.getString(PersonalAgentApplication.PREFERENCE_PASSWORD,
                                        null)
                                + " | "
                                + sharedPreferences.getBoolean(
                                        PersonalAgentApplication.PREFERENCE_AUTO_LOGIN_FLAG, false));

                return LOGIN_SUCCEEDED;
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // loginButton.setEnabled( true);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }

            if (LOGIN_FAILED.equals(result)) {
                if (!autoLoginBasedAttempt) {

                    textViewResponse.setText(LOGIN_FAILED_MESSAGE);
                    textViewResponse.setTextColor(Color.RED);
                } else {
                    autoLoginBasedAttempt = false;
                }

                return;
            } else {
                // login succeeded

                NEXT_INTENT.setFlags((Intent.FLAG_ACTIVITY_CLEAR_TOP));
                startActivity(NEXT_INTENT);
                finish();
            }

        }

    }

    // some variables relating to back end calls
    private LoginAsyncTask loginAsyncTask;
    private ProgressDialog progressDialog;
    private boolean rememberPasswordToEnableAutoLogin;
    private boolean autoLoginBasedAttempt;
    private static boolean loginResult;

    // constants :
    private static final int USERNAME_EMAIL_MINIMUM_LENGTH = 7;
    private static final int PASSWORD_MINIMUM_LENGTH = 7;

    // signing in right after the signup,
    private static String POST_SIGN_UP_MESSAGE = "\nYou are now successfully  signed Up !  Please proceed with signing into  your Personal Agent.";

    private static final String LOGIN_SUCCEEDED = "LOGIN_SUCCEEDED";
    private static final String LOGIN_FAILED = "LOGIN_FAILED";
    private static final String LOGIN_FAILED_MESSAGE = "Login failed, please verify  username,  password and network access";
    private static final String TAG = PersonalAgentLoginActivity.class.getName();

}