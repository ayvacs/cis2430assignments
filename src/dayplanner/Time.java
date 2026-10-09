package dayplanner;


/**
 * Represents a specific instance in time, with minute precision.
 * In all methods of this class, the following representations are used:
 * <ul>
 * <li>A year is represented by an integer, i.e. <pre>1970.</pre></li>
 * <li>A month is represented by an integer, i.e. <pre>1 = January.</pre></li>
 * <li>A day is represented by an integer; i.e. <pre>1 = 1</pre> (the first day of the month).</li>
 * <li>An hour is represented by an integer; i.e. <pre>23 = 23:xx = 11:xx pm.</pre></li>
 * <li>A minute is represented by an integer; i.e. <pre>1 = xx:01.</pre></li>
 * </ul>
 */
public class Time implements Comparable<Time> {
    private static final int MIN_YEAR = 1;
    private static final int MIN_MONTH = 1;
    private static final int MIN_DAY = 1;
    private static final int MIN_HOUR = 0;
    private static final int MIN_MINUTE = 0;

    private static final int MAX_YEAR = 9999;
    private static final int MAX_MONTH = 12;
    // There is no MAX_DAY because the highest possible
    // day is variable, depending on the month.
    private static final int MAX_HOUR = 23;
    private static final int MAX_MINUTE = 59;



    private int year;   // Required
    private int month;  // Required
    private int day;    // Required
    private int hour;   // Required
    private int minute; // Required



    /**
     * Determines whether the given <code>year</code> is a leap year.
     * Code adapted from <a href="https://www.programiz.com/cpp-programming/examples/leap-year">Programiz</a>.
     * @return Whether or not <code>Year</code> is a leap year.
     * @param year The year to check.
     */
    public static boolean isLeapYear(int year) {
        if (year % 400 == 0)
            return true;
        else if (year % 100 == 0)
            return false;
        else if (year % 4 == 0)
            return true;

        return false;
    }

    /**
     * Calculates the maximum number of days in the given month and year.
     * @param year The year.
     * @param month The month (1-12).
     * @return The maximum number of days in that month, or -1 if the month is invalid.
     */
    public static int maxDaysInMonth(int year, int month) {
        if (month < MIN_MONTH || month > MAX_MONTH)
            return -1;

        if (month == 2)
            return isLeapYear(year) ? 29 : 28;

        return switch (month) {
            case 1, 3, 5, 7, 8, 10, 12 -> 31;
            default -> 30;
        };
    }

    /**
     * Returns an informative error message if any time component is invalid, or <code>null</code> if valid.
     * @param year The year.
     * @param month The month.
     * @param day The day.
     * @param hour The hour.
     * @param minute The minute.
     * @return Description of the error, or <code>null</code> if all values are valid.
     */
    public static String getValidationError(int year, int month, int day, int hour, int minute) {
        if (year < MIN_YEAR || year > MAX_YEAR)
            return "Year (" + year + ") must be a positive integer between " + MIN_YEAR + " and " + MAX_YEAR + ".";
        if (month < MIN_MONTH || month > MAX_MONTH)
            return "Month (" + month + ") must be between " + MIN_MONTH + " and " + MAX_MONTH + ".";
        int maxDays = maxDaysInMonth(year, month);
        if (day < MIN_DAY || day > maxDays)
            return "Day (" + day + ") is invalid: month " + month + " has max " + maxDays + " days.";
        if (hour < MIN_HOUR || hour > MAX_HOUR)
            return "Hour (" + hour + ") must be between " + MIN_HOUR + " and " + MAX_HOUR + ".";
        if (minute < MIN_MINUTE || minute > MAX_MINUTE)
            return "Minute (" + minute + ") must be between " + MIN_MINUTE + " and " + MAX_MINUTE + ".";
        return null;
    }

    /**
     * Validates whether the specified date and time values represent a valid time.
     * @param year The year (must be positive, starting from 1).
     * @param month The month (1-12).
     * @param day The day (1 to max days in month).
     * @param hour The hour (0-23).
     * @param minute The minute (0-59).
     * @return <code>true</code> if all values are valid, <code>false</code> otherwise.
     */
    public static boolean isValidTime(int year, int month, int day, int hour, int minute) {
        return getValidationError(year, month, day, hour, minute) == null;
    }



    /**
     * Instantiates a new <code>Time</code> with no initial fields.
     * Each field defaults to the minimum allowed values.
     */
    public Time() {
        // Assume default values
        this.year = MIN_YEAR;
        this.month = MIN_MONTH;
        this.day = MIN_DAY;
        this.hour = MIN_HOUR;
        this.minute = MIN_MINUTE;
    }

    /**
     * Instantiates a new <code>Time</code> with each required field specified.
     * If any field is not valid, it defaults to its minimum allowed value.
     * @param year The new <code>Time</code>'s year.
     * @param month The new <code>Time</code>'s month.
     * @param day The new <code>Time</code>'s day.
     * @param hour The new <code>Time</code>'s hour.
     * @param minute The new <code>Time</code>'s minute.
     */
    public Time(int year, int month, int day, int hour, int minute) {
        // Assume default values
        this();

        setYear(year);
        setMonth(month);
        setDay(day);
        setHour(hour);
        setMinute(minute);
    }

    /**
     * Instantiates a new <code>Time</code> based on the given <code>formatString</code>.
     * @param formatString Format <code>String</code> of the format <code>YYYY/MM/DD HH:MM</code>. Input is validated against specified minimum and maximum values; if any field is not valid, it defaults to its minimum allowed value. Note: This parameter's required format matches the return format of <code>toString()</code> exactly.
     */
    public Time(String formatString) {
        // Assume default values
        this();

        if (formatString == null || formatString.trim().isEmpty())
            return;

        // Split up components by slashes, colons, commas, or whitespace
        String[] components = formatString.trim().split("[/:,\\s]+");

        if (components.length >= 5) {
            try {
                int y = Integer.parseInt(components[0]);
                int m = Integer.parseInt(components[1]);
                int d = Integer.parseInt(components[2]);
                int h = Integer.parseInt(components[3]);
                int min = Integer.parseInt(components[4]);

                if (isValidTime(y, m, d, h, min)) {
                    this.year = y;
                    this.month = m;
                    this.day = d;
                    this.hour = h;
                    this.minute = min;
                } else {
                    System.out.println("One or more fields were not properly entered; defaulting to " + toString() + ".");
                }
            } catch (Exception e) {
                System.out.println("One or more fields were not properly entered; defaulting to " + toString() + ".");
            }
        }
    }


    /**
     * Attempt to set the specified <code>year</code>.
     * Subject to input validation based on the allowed range of years. If the new field is not valid, the operation cancels without affecting the instance.
     * Must be <code>1</code> through <code>9999</code> inclusive.
     * @param year The new year.
     */
    public void setYear(int year) {
        if (year >= MIN_YEAR && year <= MAX_YEAR)
            this.year = year;
    }

    /**
     * Attempt to set the specified <code>month</code>.
     * Subject to input validation based on the allowed range of months. If the new field is not valid, the operation cancels without affecting the instance.
     * Must be <code>1</code> through <code>12</code> inclusive.
     * @param month The new month.
     */
    public void setMonth(int month) {
        if (month >= MIN_MONTH && month <= MAX_MONTH)
            this.month = month;
    }

    /**
     * Attempt to set the specified <code>day</code>.
     * Subject to input validation based on the allowed range of days. If the new field is not valid, the operation cancels without affecting the instance.
     * Must be <code>1</code> through <code>31</code> inclusive, depending on the month and leap year rules.
     * @param day The new day.
     */
    public void setDay(int day) {
        int maxDays = maxDaysInMonth(getYear(), getMonth());
        
        // If valid, populate the field.
        if (day >= MIN_DAY && day <= maxDays)
            this.day = day;
    }

    /**
     * Attempt to set the specified <code>hour</code>.
     * Subject to input validation based on the allowed range of hours. If the new field is not valid, the operation cancels without affecting the instance.
     * Must be <code>0</code> through <code>23</code> inclusive.
     * @param hour The new hour.
     */
    public void setHour(int hour) {
        if (hour >= MIN_HOUR && hour <= MAX_HOUR)
            this.hour = hour;
    }

    /**
     * Attempt to set the specified <code>minute</code>.
     * Subject to input validation based on the allowed range of minutes.
     * If the new field is not valid, the operation cancels without affecting the instance.
     * Must be <code>0</code> through <code>59</code> inclusive.
     * @param minute The new minute.
     */
    public void setMinute(int minute) {
        if (minute >= MIN_MINUTE && minute <= MAX_MINUTE)
            this.minute = minute;
    }



    /** @return This <code>Time</code>'s <b>year</b> field as an integer. */
    public int getYear()   { return this.year;   }

    /** @return This <code>Time</code>'s <b>month</b> field as an integer. */
    public int getMonth()  { return this.month;  }

    /** @return This <code>Time</code>'s <b>day</b> field as an integer. */
    public int getDay()    { return this.day;    }

    /** @return This <code>Time</code>'s <b>hour</b> field as an integer. */
    public int getHour()   { return this.hour;   }

    /** @return This <code>Time</code>'s <b>minute</b> field as an integer. */
    public int getMinute() { return this.minute; }



    /**
     * @return A <code>String</code> representation of this <code>Time</code> of the format <code>YYYY/MM/DD HH:MM</code>.
     */
    public String toString() {
        return String.format("%04d/%02d/%02d %02d:%02d",
            getYear(), getMonth(), getDay(),
            getHour(), getMinute());
    }

    /**
     * Compare two <code>Time</code> instances.
     * @return An integer indicating the relationship between the two instances: A value of <code>0</code> indicates they are equal; a negative value indicates the calling <code>Time</code> <b>precedes</b> the argument; and a positive value indicates the calling <code>Time</code> <b>follows</b> the argument.
     * @param other The other <code>Time</code> to compare with.
     */
    @Override
    public int compareTo(Time other) {
        if (other == null)
            throw new NullPointerException("Cannot compare Time to null");

        if (getYear() != other.getYear())
            return getYear() - other.getYear();
        
        if (getMonth() != other.getMonth())
            return getMonth() - other.getMonth();
        
        if (getDay() != other.getDay())
            return getDay() - other.getDay();
        
        if (getHour() != other.getHour())
            return getHour() - other.getHour();

        return getMinute() - other.getMinute();
    }

    /**
     * @return Whether or not this <code>Time</code> equals <code>other</code>.
     * @param other The other <code>Time</code> to compare with.
     */
    public boolean equals(Time other) {
        if (other == null)
            return false;
        return compareTo(other) == 0;
    }

    /**
     * @return Whether or not this <code>Time</code> equals <code>other</code>.
     * @param other The object to compare with.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (other == null || getClass() != other.getClass())
            return false;
        return equals((Time) other);
    }
}
