package com.pucp.odiparpackback.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
  public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
  public static final SimpleDateFormat dateFormat  = new SimpleDateFormat(DATE_TIME_FORMAT);
  
  public static String formatDate(Date date) {
    try {
      return dateFormat.format(date);
    } catch (Exception e) {
      return null;
    }
  }
  
  public static Date parseDate(String date) {
    try {
      return dateFormat.parse(date);
    } catch (Exception e) {
      return null;
    }
  }
}
