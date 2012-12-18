package com.strumsoft.sideview.demo ;

import android.app.Application;

/**
 *
 * @author smishra
 */
public class PersonalAgentApplication extends Application  {
  
  private String appId ;
  
  private String     username ;
  private String     password ;
  private boolean  autoLoginFlag  ;
  

  @Override
  public void onCreate() {
    super.onCreate();
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
  }
  
  
  void setApplicationInstallationIdentifier ( String id ) {
    appId = id ;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public boolean isAutoLoginFlag() {
    return autoLoginFlag;
  }

  public void setAutoLoginFlag(boolean autoLoginFlag) {
    this.autoLoginFlag = autoLoginFlag;
  }
  
  
  
  // some STATE constant lvalues / intent extras lvalues :
  public static final String STATE_RIGHT_AFTER_SIGN_UP_FLAG =     "state.right_after_sign_up" ; // signup -- > Login, pre-populate 
  
  
  // some MINIMUM_LENGTHS constants :
  public static final int MINIMUM_LENGTH_EMAIL = 7 ;
  public static final int MINIMUM_LENGTH_PASSWORD = 7 ;
  
  
  
  /** preferences  lvalues */
  public static final String  PREFERENCE_APPLICATION_IDENTIFIER         =   "applicationIdentifier" ;
  public static final String  PREFERENCE_USERNAME                       =   "username" ;
  public static final String  PREFERENCE_PASSWORD                       =   "password" ;
  public static final String  PREFERENCE_AUTO_LOGIN_FLAG                =   "rememberPassword" ;
  public static final String  PREFERENCE_IS_FIRST_RUN_DONE              =   "isFirstRunFlag" ;
  public static final String  PREFERENCE_SIGN_UP_DONE                   =   "signup.done" ;
  public static final String  PREFERENCE_ENABLE_LOCATION                =   "enable.location" ;
  public static final String  PREFERENCE_ENABLE_APP                     =   "enable.app" ;
  public static final String  PREFERENCE_ENABLE_CLICKS                  =   "enable.click" ;
}
