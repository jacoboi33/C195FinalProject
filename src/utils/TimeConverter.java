package utils;

import javax.swing.text.DateFormatter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeConverter {

    public static Calendar stringToCalendar(String strDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = sdf.parse(strDate);
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static String utcDateConverter(String utc) {
        DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ZoneId localZoneID = ZoneId.systemDefault();
        ZoneId utcZoneID = ZoneId.of("UTC");
        LocalDateTime utcDate = LocalDateTime.parse(utc, dtFormatter);
        ZonedDateTime localZoneStart = utcDate.atZone(utcZoneID).withZoneSameInstant(localZoneID);
        return localZoneStart.format(dtFormatter);
    }

    public static String getStartAndEndDate(LocalDate date, LocalTime time) {
        ZoneId zoneId = ZoneId.of(TimeZone.getDefault().getID());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("UTC"));
        ZonedDateTime localDateTime = ZonedDateTime.of(date, time, zoneId);
        return dateTimeFormatter.format(localDateTime);
    }

    public static LocalDate getLocalDate(String date) {
        System.out.println(date);

        DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.println(date.substring(0, 10));
        return LocalDate.parse(date.substring(0, 10), dtFormatter);
    }

    public static LocalTime getLocalTime(String time) {
        DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
//        System.out.println(time.substring(11, 19));
        return LocalTime.parse(time.substring(11, 19), dtFormatter);
    }

    public static LocalTime getLocalTime2(String time) {
        DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
//        System.out.println(time.substring(0, 8));
        return LocalTime.parse(time.substring(0, 8), dtFormatter);
    }


}
