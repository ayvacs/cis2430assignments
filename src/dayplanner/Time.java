package dayplanner;


/**
 * <p>
 * Represents an instant in time with minute-level precision. This is not a
 * time-zone-aware instant and has no seconds or daylight-saving rules.
 * </p>
 * <p>
 * Each component is stored as a plain integer and validated against the
 * following ranges:
 * </p>
 * <ul>
 * <li><b>year</b>: 1–9999</li>
 * <li><b>month</b>: 1–12</li>
 * <li><b>day</b>: 1 to the maximum days in the given month (respects leap years
 * for February; see {@link #maxDaysInMonth(int year, int month)})</li>
 * <li><b>hour</b>: 0–23</li>
 * <li><b>minute</b>: 0–59</li>
 * </ul>
 * <p>
 * The string form produced by {@link #toString()} is
 * <code>YYYY/MM/DD HH:MM</code>, e.g. <code>2026/09/15 14:30</code>.
 * </p>
 * <p>
 * There are also static methods for dealing with Time instances.
 * </p>
 */
public class Time implements Comparable<Object> {
    /*
     * Static Constants
     */

    /** Minimum allowable year (1). */
    private static final int MIN_YEAR = 1;
    /** Minimum allowable month (1, January). */
    private static final int MIN_MONTH = 1;
    /** Minimum allowable day (1). */
    private static final int MIN_DAY = 1;
    /** Minimum allowable hour (0). */
    private static final int MIN_HOUR = 0;
    /** Minimum allowable minute (0). */
    private static final int MIN_MINUTE = 0;

    /** Maximum allowable year (9999). */
    private static final int MAX_YEAR = 9999;
    /** Maximum allowable month (12, December). */
    private static final int MAX_MONTH = 12;
    // There is no MAX_DAY because the highest possible
    // day is variable, depending on the month.
    /** Maximum allowable hour (23). */
    private static final int MAX_HOUR = 23;
    /** Maximum allowable minute (59). */
    private static final int MAX_MINUTE = 59;



    /*
     * Instance Variables
     */

    /** The year component (1–9999). */
    private int year;
    /** The month component (1–12). */
    private int month;
    /** The day component (1 to max days in month). */
    private int day;
    /** The hour component (0–23). */
    private int hour;
    /** The minute component (0–59). */
    private int minute;



    /*
     * Static Methods
     */
    
    /**
     * Determines whether the specified year is a leap year.
     * Code adapted from <a href=
     * "https://www.programiz.com/cpp-programming/examples/leap-year">Programiz</a>.
     * 
     * @param year The year to check.
     * @return <code>true</code> if the year is a leap year, <code>false</code>
     *         otherwise.
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
     * 
     * @param year  The year.
     * @param month The month (1-12).
     * @return The maximum number of days in that month, or -1 if the month is
     *         invalid.
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
     * Returns an informative error message if any time component is invalid, or
     * <code>null</code> if valid.
     * 
     * @param year   The year.
     * @param month  The month.
     * @param day    The day.
     * @param hour   The hour.
     * @param minute The minute.
     * @return Description of the error, or <code>null</code> if all values are
     *         valid.
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
     * 
     * @param year   The year (must be positive, starting from 1).
     * @param month  The month (1-12).
     * @param day    The day (1 to max days in month).
     * @param hour   The hour (0-23).
     * @param minute The minute (0-59).
     * @return <code>true</code> if all values are valid, <code>false</code>
     *         otherwise.
     */
    public static boolean isValidTime(int year, int month, int day, int hour, int minute) {
        return getValidationError(year, month, day, hour, minute) == null;
    }



    /*
     * Constructors
     */

    /**
     * Constructs a <code>Time</code> initialized to minimum allowed values
     * (0001/01/01 00:00).
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
     * <p>
     * Constructs a <code>Time</code> with specified components.
     * </p>
     * 
     * @param year   The year (1-9999).
     * @param month  The month (1-12).
     * @param day    The day (1 to max days in month).
     * @param hour   The hour (0-23).
     * @param minute The minute (0-59).
     * @throws IllegalArgumentException If any component is outside its valid range.
     */
    public Time(int year, int month, int day, int hour, int minute)
    throws IllegalArgumentException {
        this();
        setYear(year);
        setMonth(month);
        setDay(day);
        setHour(hour);
        setMinute(minute);
    }

    /**
     * <p>
     * Copy constructor. Constructs a new <code>Time</code> that is a duplicate
     * of the given <code>Time</code>, preventing co-references that could cause
     * privacy leaks.
     * </p>
     *
     * @param other The <code>Time</code> to copy.
     * @throws IllegalArgumentException If {@code other} is {@code null}.
     */
    public Time(Time other)
    throws IllegalArgumentException {
        if (other == null)
            throw new IllegalArgumentException("Cannot copy a null Time.");
        this.year   = other.year;
        this.month  = other.month;
        this.day    = other.day;
        this.hour   = other.hour;
        this.minute = other.minute;
    }

    /**
     * Constructs a <code>Time</code> by parsing a string in YYYY/MM/DD HH:MM
     * format.
     * 
     * @param formatString String containing at least five numeric components, such
     *                     as {@code 2026/09/15 14:30}; separators may be slash,
     *                     colon, comma, or whitespace.
     * @throws IllegalArgumentException If the string is null, empty, has fewer than
     *                                  5 components, or contains invalid values.
     */
    public Time(String formatString)
    throws IllegalArgumentException {
        if (formatString == null || formatString.trim().isEmpty())
            throw new IllegalArgumentException("Time string cannot be null or empty.");

        // Split up components by slashes, colons, commas, or whitespace
        String[] components = formatString.trim().split("[/:,\\s]+");

        if (components.length < 5)
            throw new IllegalArgumentException("Invalid time format: expected at least 5 components in '" + formatString + "'.");

        try {
            int y = Integer.parseInt(components[0]);
            int m = Integer.parseInt(components[1]);
            int d = Integer.parseInt(components[2]);
            int h = Integer.parseInt(components[3]);
            int min = Integer.parseInt(components[4]);

            String error = getValidationError(y, m, d, h, min);
            if (error != null)
                throw new IllegalArgumentException(error);

            this.year = y;
            this.month = m;
            this.day = d;
            this.hour = h;
            this.minute = min;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Time components must be integers in '" + formatString + "'.", e);
        }
    }



    /*
     * Mutators
     */

    /**
     * Sets the year if within the valid range (1-9999).
     * 
     * @param year The new year.
     * @throws IllegalArgumentException If year is not between 1 and 9999, or if
     *                                  the current day becomes invalid for the new year.
     */
    public void setYear(int year)
    throws IllegalArgumentException {
        if (year < MIN_YEAR || year > MAX_YEAR)
            throw new IllegalArgumentException("Year (" + year + ") must be a positive integer between " + MIN_YEAR + " and " + MAX_YEAR + ".");
        int maxDays = maxDaysInMonth(year, getMonth());
        if (getDay() > maxDays)
            throw new IllegalArgumentException("Day (" + getDay() + ") is invalid for month " + getMonth() + " in year " + year + ".");
        this.year = year;
    }

    /**
     * Sets the month if within the valid range (1-12).
     * 
     * @param month The new month.
     * @throws IllegalArgumentException If month is not between 1 and 12, or if
     *                                  the current day becomes invalid for the new month.
     */
    public void setMonth(int month)
    throws IllegalArgumentException {
        if (month < MIN_MONTH || month > MAX_MONTH)
            throw new IllegalArgumentException("Month (" + month + ") must be between " + MIN_MONTH + " and " + MAX_MONTH + ".");
        int maxDays = maxDaysInMonth(getYear(), month);
        if (getDay() > maxDays)
            throw new IllegalArgumentException("Day (" + getDay() + ") is invalid for month " + month + " in year " + getYear() + ".");
        this.month = month;
    }

    /**
     * Sets the day if within the valid range for the current month and year. Set
     * year and month first when changing to a date with a different month length.
     * 
     * @param day The new day.
     * @throws IllegalArgumentException If day is not between 1 and the maximum
     *                                  days for the current month and year.
     */
    public void setDay(int day)
    throws IllegalArgumentException {
        int maxDays = maxDaysInMonth(getYear(), getMonth());
        if (day < MIN_DAY || day > maxDays)
            throw new IllegalArgumentException("Day (" + day + ") is invalid: month " + getMonth() + " has max " + maxDays + " days.");
        this.day = day;
    }

    /**
     * Sets the hour if within the valid range (0-23).
     * 
     * @param hour The new hour.
     * @throws IllegalArgumentException If hour is not between 0 and 23.
     */
    public void setHour(int hour)
    throws IllegalArgumentException {
        if (hour < MIN_HOUR || hour > MAX_HOUR)
            throw new IllegalArgumentException("Hour (" + hour + ") must be between " + MIN_HOUR + " and " + MAX_HOUR + ".");
        this.hour = hour;
    }

    /**
     * Sets the minute if within the valid range (0-59).
     * 
     * @param minute The new minute.
     * @throws IllegalArgumentException If minute is not between 0 and 59.
     */
    public void setMinute(int minute)
    throws IllegalArgumentException {
        if (minute < MIN_MINUTE || minute > MAX_MINUTE)
            throw new IllegalArgumentException("Minute (" + minute + ") must be between " + MIN_MINUTE + " and " + MAX_MINUTE + ".");
        this.minute = minute;
    }



    /*
     * Accessors
     */

    /**
     * Gets the year.
     * 
     * @return The year.
     */
    public int getYear() {
        return this.year;
    }

    /**
     * Gets the month.
     * 
     * @return The month (1-12).
     */
    public int getMonth() {
        return this.month;
    }

    /**
     * Gets the day.
     * 
     * @return The day of the month.
     */
    public int getDay() {
        return this.day;
    }

    /**
     * Gets the hour.
     * 
     * @return The hour (0-23).
     */
    public int getHour() {
        return this.hour;
    }

    /**
     * Gets the minute.
     * 
     * @return The minute (0-59).
     */
    public int getMinute() {
        return this.minute;
    }



    /*
     * Object Overrides
     */

    /**
     * <p>
     * Returns a string representation of this time in <code>YYYY/MM/DD HH:MM</code>
     * format.
     * </p>
     * <p>
     * Month, day, hour, and minute are zero-padded to two digits; year is
     * zero-padded to four. For example, September 5, 2026 at 8:03 AM is formatted
     * as <code>2026/09/05 08:03</code>.
     * </p>
     * 
     * @return Formatted time string.
     */
    @Override
    public String toString() {
        return String.format("%04d/%02d/%02d %02d:%02d",
            getYear(), getMonth(), getDay(),
            getHour(), getMinute());
    }

    /**
     * <p>
     * Compares this time with another object chronologically.
     * </p>
     * <p>
     * Fields are compared in order: year, then month, then day, then hour, then
     * minute. The first field that differs determines the result.
     * </p>
     * 
     * @param other The other object to compare with.
     * @return A negative integer, zero, or a positive integer as this time
     *         precedes, equals, or follows {@code other}.
     * @throws NullPointerException If {@code other} is {@code null}.
     * @throws ClassCastException   If {@code other} is not an instance of
     *                              {@link Time}.
     */
    @Override
    public int compareTo(Object other)
    throws NullPointerException, ClassCastException {
        if (other == null)
            throw new NullPointerException("Cannot compare Time to null");
        if (!(other instanceof Time))
            throw new ClassCastException("Cannot compare Time to an object of class " + other.getClass().getName());

        Time o = (Time) other;

        if (getYear() != o.getYear())
            return getYear() - o.getYear();
        
        if (getMonth() != o.getMonth())
            return getMonth() - o.getMonth();
        
        if (getDay() != o.getDay())
            return getDay() - o.getDay();
        
        if (getHour() != o.getHour())
            return getHour() - o.getHour();

        return getMinute() - o.getMinute();
    }

    /**
     * Determines whether a time period from {@code start} to {@code end} is valid.
     * Uses {@link #compareTo(Object)} to verify that {@code start} is strictly
     * before {@code end}.
     * 
     * @param start The start time.
     * @param end   The end time.
     * @return {@code true} if both times are non-null and {@code start} is strictly
     *         before {@code end}; {@code false} otherwise.
     */
    public static boolean isValidPeriod(Time start, Time end) {
        if (start == null || end == null)
            return false;
        try {
            return start.compareTo(end) < 0;
        } catch (ClassCastException | NullPointerException e) {
            return false;
        }
    }

    /**
     * Determines whether this time is equal to another object.
     * 
     * @param other The object to compare with.
     * @return <code>true</code> if the objects are equal, <code>false</code>
     *         otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (other == null || getClass() != other.getClass())
            return false;
        return compareTo(other) == 0;
    }
}
