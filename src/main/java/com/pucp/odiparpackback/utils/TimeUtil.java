package com.pucp.odiparpackback.utils;

import com.pucp.odiparpackback.response.ErrorResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
  public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
  public static final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);

  public static String formatDate(Date date) {
    return dateFormat.format(date);
  }

  public static Date parseDate(String date) throws ParseException {
    return dateFormat.parse(date);
  }


  public static ErrorResponse validateDates(String date) {
    if (!date.matches(RegexPattern.DATE_FORMAT)) {
      return new ErrorResponse(String.format(Message.INCORRECT_FORMAT, "startDate"));
    }
    return null;
  }
}
