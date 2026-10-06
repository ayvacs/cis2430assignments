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
public class Time {
    private static final int MIN_YEAR = 1970;
    private static final int MIN_MONTH = 1;
    private static final int MIN_DAY = 1;
    private static final int MIN_HOUR = 0;
    private static final int MIN_MINUTE = 0;

    private static final int MAX_YEAR = 2170;
    private static final int MAX_MONTH = 12;
    private static final int MAX_DAY = 31;
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

        // Remove all double, triple, etc spaces
        formatString = formatString.trim().replaceAll("\\s{2,}", "");

        // Split up the 5 components by slashes, colons, and commas
        String[] components = formatString.split("[/:, ]");

        // Populate attributes (parseInt defaults to NaN if no integer is found, which our setter methods refuse to operate on, thus avoiding any exceptions.)
        setYear(Integer.parseInt(components[0]));
        setMonth(Integer.parseInt(components[1]));
        setDay(Integer.parseInt(components[2]));
        setHour(Integer.parseInt(components[3]));
        setMinute(Integer.parseInt(components[4]));
    }


    /**
     * Attempt to set the specified <code>year</code>.
     * Subject to input validation based on the allowed range of years. If the new field is not valid, the operation cancels without affecting the instance.
     * Must be <code>1970</code> through <code>2170</code> inclusive.
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
     * Must be <code>0</code> through <code>31</code> inclusive, except if the current month is February. Days in February must be <code>0</code> through <code>29</code> inclusive on leap years, and <code>0</code> through <code>28</code> inclusive on non-leap years.
     * @param day The new day.
     */
    public void setDay(int day) {
        int year = getYear();
        int month = getMonth();
        boolean isLeap = isLeapYear(year);
        boolean isFeb = month == 2;

        // First, determine whether the given day exceeds the number of days in the month.
        int thisMonthsMaxDay = -1;

        if (isFeb) {
            if (isLeap)
                thisMonthsMaxDay = 29;
            else
                thisMonthsMaxDay = 28;
        } else {
            if (switch (month) {
                case 1, 3, 5, 7, 8, 10, 12 -> true;
                default -> false; })
                thisMonthsMaxDay = 31;
            else
                thisMonthsMaxDay = 30;
        }
        
        // If valid, populate the field.
        if (day >= MIN_DAY && month <= thisMonthsMaxDay)
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
        return String.format("%d/%d/%d %d:%d",
            getYear(), getMonth(), getDay(),
            getHour(), getMinute());
    }

    /**
     * @return Whether or not this <code>Time</code> equals <code>other</code>.
     * @param other The other <code>Time</code> to compare with.
     */
    public boolean equals(Time other) {
        return toString().equals(other.toString());
    }

    /**
     * Compare two <code>Time</code> instances lexicographically.
     * @return An integer indicating the relationship between the two strings: A value of 0 indicates they are equal; a negative value indicates the calling <code>Time</code> <b>precedes</b> the argument; and a positive value indicates the calling <code>Time</code> <b>follows</b> the argument.
     * @param other The other <code>Time</code> to compare with.
     */
    public int compareTo(Time other) {
        return toString().compareTo(other.toString());
    }
}
