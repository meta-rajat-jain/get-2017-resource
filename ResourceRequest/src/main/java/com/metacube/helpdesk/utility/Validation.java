package com.metacube.helpdesk.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {
    
    public static boolean isNull(Object dataToBeCheck){
        boolean flag=false;
       if(dataToBeCheck==null){
           flag=true;
       }
       return flag;
    }
    
    public static boolean isEmpty(String dataToBeCheck){
        boolean flag=false;
       if(dataToBeCheck.length()==0){
           flag=true;
       }
       return flag;
    }
    
    public static boolean validateInput(String dataToBeCheck , String patternString){
        Pattern pattern;
        pattern = Pattern.compile(patternString);
        Matcher matcher;
        matcher=pattern.matcher(dataToBeCheck);
        return matcher.matches(); 
    }
}
