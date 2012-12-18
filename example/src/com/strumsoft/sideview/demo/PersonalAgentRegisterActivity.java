package com.strumsoft.sideview.demo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.strumsoft.sideview.demo.util.FormatsHelper;



/**
 *
 * @author smishra
 */
public class PersonalAgentRegisterActivity extends SherlockActivity {
  
  
  private EditText editTextName ;
  private EditText editTextUsernameEmail ;
  private EditText editTextPassword ;
  private EditText editTextPasswordRepeat ;
  private TextView textViewResponse ;
  private InputMethodManager inputMethodManager = null;
  
  private Intent NEXT_INTENT ;
  private PersonalAgentApplication APPLICATION ;
   
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //setContentView(R.layout.pa_register);
    
    ActionBar actionBar = getSupportActionBar();
    actionBar.show();
    setContentView(R.layout.activity_register);        
    APPLICATION                     = ( PersonalAgentApplication) getApplication() ;
    
    editTextName                    = ( EditText)  findViewById( R.id.nameEditText) ;
    editTextUsernameEmail      = ( EditText)  findViewById( R.id.emailEditText ) ;
    editTextPassword               = ( EditText)  findViewById( R.id.passwordEditText) ;
    editTextPasswordRepeat     = ( EditText)  findViewById( R.id.passwordRepeatEditText) ;
    textViewResponse               = ( TextView)  findViewById( R.id.response) ;
    
        if (inputMethodManager == null) {
            inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(editTextName.getWindowToken(),
                    InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
  }
  
  
  
  @Override
    protected void onStart() {
        super.onStart();
        if (inputMethodManager != null) {

            inputMethodManager.hideSoftInputFromWindow(editTextName.getWindowToken(),
                    InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
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
  protected void onPause() {
    super.onPause();
  }

  @Override
  protected void onStop() {
    super.onStop();
  }
  
  @Override
  protected void onDestroy() {
    super.onDestroy();
  }
  
  
    @Override
  public void finish() {
    super.finish();
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
  }
  
  
  public void handleRegister ( View view ) {
    Log.d ( TAG, "handleRegister()..." + editTextPassword + editTextUsernameEmail) ;
    
    theUsernameEmail = editTextUsernameEmail.getText().toString() ;
    thePassword = editTextPassword.getText().toString() ;
    thePasswordRepeat =  editTextPasswordRepeat.getText().toString() ;
    
    Log.d ( TAG, "handleRegister() " + theUsernameEmail + " | " + thePassword ) ;
    
    if ( (theUsernameEmail == null || thePassword == null) || ( theUsernameEmail.length()< PersonalAgentApplication.MINIMUM_LENGTH_EMAIL || thePassword.length()< PersonalAgentApplication.MINIMUM_LENGTH_PASSWORD )) {
      Log.d ( TAG, "handleRegister returning due to insufficient data" ) ;
      textViewResponse.setText ("Please provide your Name, Email & Password (minimum password length is 7)" ) ;

      textViewResponse.setTextColor( Color.RED);
      return ;
    }
    
    if ( ! FormatsHelper.isValidEmailFormat( theUsernameEmail)) {
      
      Log.d ( TAG, "handleRegister returning due to email Format issue" ) ;
      textViewResponse.setText ("Email needs to be in a valid format such as yourid@yourmail.com ") ;
      textViewResponse.setTextColor( Color.RED);
      
      return ;
    }
    
    
    if  ( !  thePassword.equals( thePasswordRepeat) ) {
      Log.d ( TAG, "handleRegister returning due to password mismatch " ) ;
      textViewResponse.setText ("Please ensure  that passwords   entered twice match and are at least 4 characters long") ;
      textViewResponse.setTextColor( Color.RED);
      return ;
    }
   
    progressDialog = ProgressDialog.show ( this,  "Processing  Sign Up ..." , "Please wait ...", true, true  ) ;
    
    
    
    
           
    
    registerAsyncTask = new RegisterAsyncTask() ;
    registerAsyncTask.execute() ;
    
  } 
  
  
  
  public void handleNavigateToLogin ( View view )  {
    
    Intent loginIntent = new Intent ( this, PersonalAgentLoginActivity.class) ;
    //loginIntent.setFlags( (Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP) ); 
    loginIntent.setFlags( (Intent.FLAG_ACTIVITY_CLEAR_TOP )) ; 
    startActivity ( loginIntent) ;
    finish();
  }
  
  
  
  private class RegisterAsyncTask extends AsyncTask<Void, Void, String> {
    @Override
    protected String doInBackground ( Void... arg0 ) {
      return REGISTER_SUCCESS ;
    }  
    
   @Override
   protected void onPostExecute(String result) {
     super.onPostExecute(result);

     if ( progressDialog != null ) {
       progressDialog.dismiss();
     }
     
     if (result.equals(REGISTER_SUCCESS)) {
       PreferenceManager.getDefaultSharedPreferences(PersonalAgentRegisterActivity.this).edit().putBoolean(PersonalAgentApplication.PREFERENCE_SIGN_UP_DONE, true).commit();
       
      
       
       NEXT_INTENT = new Intent(APPLICATION, PersonalAgentLoginActivity.class);
       NEXT_INTENT.putExtra(  PersonalAgentApplication.STATE_RIGHT_AFTER_SIGN_UP_FLAG, true ) ;
       
       NEXT_INTENT.setFlags((Intent.FLAG_ACTIVITY_CLEAR_TOP));
       NEXT_INTENT.putExtra(PersonalAgentApplication.PREFERENCE_USERNAME, theUsernameEmail);
       NEXT_INTENT.putExtra(PersonalAgentApplication.PREFERENCE_PASSWORD, thePassword);     
       startActivity(NEXT_INTENT);
       finish();
     } else {
       Toast.makeText(PersonalAgentRegisterActivity.this, "Try again", 1000).show();
     }
   }
  }
  
  private String theUsernameEmail ;
  private String thePassword ;
  private String thePasswordRepeat  ;
  private ProgressDialog progressDialog ;
  
  
  private static final  String REGISTER_SUCCESS = "REGISTER_SUCCESS" ;
  private static final  String REGISTER_FAILED = "REGISTER_FAILED" ;
  
  
  private RegisterAsyncTask registerAsyncTask ;
  private static final String TAG = PersonalAgentRegisterActivity.class.getName() ;
}
