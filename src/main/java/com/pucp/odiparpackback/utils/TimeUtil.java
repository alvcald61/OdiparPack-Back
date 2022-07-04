package com.pucp.odiparpackback.utils;

import com.pucp.odiparpackback.response.ErrorResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeUtil {
  public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

  public static String formatDate(Date date) {
    Locale localePeru = new Locale("es", "pe");
    TimeZone.setDefault(TimeZone.getTimeZone("America/Lima"));
    SimpleDateFormat formatDate = new SimpleDateFormat(DATE_TIME_FORMAT, localePeru);
    return formatDate.format(date);
  }

  public static Date parseDate(String date) throws ParseException {
    Locale localePeru = new Locale("es", "pe");
    TimeZone.setDefault(TimeZone.getTimeZone("America/Lima"));
    SimpleDateFormat formatDate = new SimpleDateFormat(DATE_TIME_FORMAT, localePeru);
    return formatDate.parse(date);
  }


  public static ErrorResponse validateDates(String date) {
    if (!date.matches(RegexPattern.DATE_FORMAT)) {
      return new ErrorResponse(String.format(Message.INCORRECT_FORMAT, "startDate"));
    }
    return null;
  }
}
