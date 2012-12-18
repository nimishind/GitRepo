package com.strumsoft.sideview.demo.util;
import android.util.Patterns ;
/**
 *
 * @author smishra
 */
public class FormatsHelper {
  
  public static boolean isValidEmailFormat (CharSequence email) {
  
    if ( email == null )
      return  false ;
    
    return Patterns.EMAIL_ADDRESS.matcher(email).matches(); 
  
  }
}
