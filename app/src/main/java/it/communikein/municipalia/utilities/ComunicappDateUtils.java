package it.communikein.municipalia.utilities;

import android.content.Context;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import it.communikein.municipalia.R;

public class ComunicappDateUtils {

    public static final DateFormat dateFormat =
            new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
    public static final DateFormat dateTimeFormat =
            new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.ITALY);


    private static long getNormalizedUtcMsForToday() {

        /*
         * This number represents the number of milliseconds that have elapsed since January
         * 1st, 1970 at midnight in the GMT time zone.
         */
        long utcNowMillis = System.currentTimeMillis();

        /*
         * This TimeZone represents the device's current time zone. It provides us with a means
         * of acquiring the offset for local time from a UTC time stamp.
         */
        TimeZone currentTimeZone = TimeZone.getDefault();

        /*
         * The getOffset method returns the number of milliseconds to add to UTC time to get the
         * elapsed time since the epoch for our current time zone. We pass the current UTC time
         * into this method so it can determine changes to account for daylight savings time.
         */
        long gmtOffsetMillis = currentTimeZone.getOffset(utcNowMillis);

        /*
         * UTC time is measured in milliseconds from January 1, 1970 at midnight from the GMT
         * time zone. Depending on your time zone, the time since January 1, 1970 at midnight (GMT)
         * will be greater or smaller. This variable represents the number of milliseconds since
         * January 1, 1970 (GMT) time.
         */
        long timeSinceEpochLocalTimeMillis = utcNowMillis + gmtOffsetMillis;

        /* This method simply converts milliseconds to days, disregarding any fractional days */
        long daysSinceEpochLocal = TimeUnit.MILLISECONDS.toDays(timeSinceEpochLocalTimeMillis);

        /*
         * Finally, we convert back to milliseconds. This time stamp represents today's date at
         * midnight in GMT time. We will need to account for local time zone offsets when
         * extracting this information from the database.
         */

        return TimeUnit.DAYS.toMillis(daysSinceEpochLocal);
    }

    public static Date getNormalizedUtcDateForToday() {
        long normalizedMilli = getNormalizedUtcMsForToday();
        return new Date(normalizedMilli);
    }

    /**
     * This method returns the number of days since the epoch (January 01, 1970, 12:00 Midnight UTC)
     * in UTC time from the current date.
     *
     * @param utcDate A date in milliseconds in UTC time.
     *
     * @return The number of days from the epoch to the date argument.
     */
    private static long elapsedDaysSinceEpoch(long utcDate) {
        return TimeUnit.MILLISECONDS.toDays(utcDate);
    }

    /**
     * This method will return the local time midnight for the provided normalized UTC date.
     *
     * @param normalizedUtcDate UTC time at midnight for a given date. This number comes from the
     *                          database
     *
     * @return The local date corresponding to the given normalized UTC date
     */
    private static long getLocalMidnightFromNormalizedUtcDate(long normalizedUtcDate) {
        /* The timeZone object will provide us the current user's time zone offset */
        TimeZone timeZone = TimeZone.getDefault();
        /*
         * This offset, in milliseconds, when added to a UTC date time, will produce the local
         * time.
         */
        long gmtOffset = timeZone.getOffset(normalizedUtcDate);
        return normalizedUtcDate - gmtOffset;
    }

    /**
     * Helper method to convert the database representation of the date into something to display
     * to users. As classy and polished a user experience as "1474061664" is, we can do better.
     * <p/>
     * The day string for forecast uses the following logic:
     * For today: "Today, June 8"
     * For tomorrow:  "Tomorrow
     * For the next 5 days: "Wednesday" (just the day name)
     * For all days after that: "Mon, Jun 8" (Mon, 8 Jun in UK, for example)
     *
     * @param context               Context to use for resource localization
     * @param normalizedUtcMidnight The date in milliseconds (UTC midnight)
     * @param showFullDate          Used to show a fuller-version of the date, which always
     *                              contains either the day of the week, today, or tomorrow, in
     *                              addition to the date.
     *
     * @return A user-friendly representation of the date such as "Today, June 8", "Tomorrow",
     * or "Friday"
     */
    public static String getFriendlyDateString(Context context, long normalizedUtcMidnight,
                                               boolean showFullDate, boolean showTime) {

        /*
         * NOTE: localDate should be localDateMidnightMillis and should be straight from the
         * database
         *
         * Since we normalized the date when we inserted it into the database, we need to take
         * that normalized date and produce a date (in UTC time) that represents the local time
         * zone at midnight.
         */
        long localDate = getLocalMidnightFromNormalizedUtcDate(normalizedUtcMidnight);

        /*
         * In order to determine which day of the week we are creating a date string for, we need
         * to compare the number of days that have passed since the epoch (January 1, 1970 at
         * 00:00 GMT)
         */
        long daysFromEpochToProvidedDate = elapsedDaysSinceEpoch(localDate);

        /*
         * As a basis for comparison, we use the number of days that have passed from the epoch
         * until today.
         */
        long daysFromEpochToToday = elapsedDaysSinceEpoch(System.currentTimeMillis());

        if (daysFromEpochToProvidedDate == daysFromEpochToToday || showFullDate) {
            /* Get readable date */
            String readableDate = getReadableDateString(localDate, showTime);

            /* Insert the word 'time', between date and time, based on the language of the phone */
            String result = readableDate.substring(0, readableDate.indexOf(" ")) + " ";
            result += context.getString(R.string.time) + " " +
                    readableDate.substring(readableDate.indexOf(" ") + 1);

            /*
             * If the date is yesterday, today or tomorrow, change the whole date with yesterday,
             * today and tomorrow respectively
             */
            if (daysFromEpochToProvidedDate - daysFromEpochToToday < 2 &&
                    daysFromEpochToProvidedDate - daysFromEpochToToday > -2) {
                /* Get the day name: yesterday, today, tomorrow */
                String dayName = getDayName(context, localDate);
                String localizedDayName = dateFormat.format(localDate);

                return result.replace(localizedDayName, dayName);
            } else {
                return result;
            }
        } else if (daysFromEpochToProvidedDate < daysFromEpochToToday + 7) {
            /* If the input date is less than a week in the future, just return the day name. */
            return getDayName(context, localDate);
        } else {
            /* If the input date is more than a week in the future, return the full date */
            return getReadableDateString(localDate, showTime);
        }
    }

    /**
     * Returns a date string in the format specified, which shows an abbreviated date without a
     * year.
     *
     * @param timeInMillis Time in milliseconds since the epoch (local time)
     *
     * @return The formatted date string
     */
    private static String getReadableDateString(long timeInMillis, boolean showTime) {
        Date date = new Date(timeInMillis);

        if (showTime)
            return dateTimeFormat.format(date);
        else
            return dateFormat.format(date);
    }

    /**
     * Given a day, returns just the name to use for that day.
     *   E.g "today", "tomorrow", "Wednesday".
     *
     * @param context      Context to use for resource localization
     * @param dateInMillis The date in milliseconds (UTC time)
     *
     * @return the string day of the week
     */
    private static String getDayName(Context context, long dateInMillis) {
        /*
         * If the date is today, return the localized version of "Today" instead of the actual
         * day name.
         */
        long daysFromEpochToProvidedDate = elapsedDaysSinceEpoch(dateInMillis);
        long daysFromEpochToToday = elapsedDaysSinceEpoch(System.currentTimeMillis());

        int daysAfterToday = (int) (daysFromEpochToProvidedDate - daysFromEpochToToday);

        switch (daysAfterToday) {
            case -1:
                return context.getString(R.string.yesterday);
            case 0:
                return context.getString(R.string.today);
            case 1:
                return context.getString(R.string.tomorrow);

            default:
                SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
                return dayFormat.format(dateInMillis);
        }
    }

}
