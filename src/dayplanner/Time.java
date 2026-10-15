package dayplanner;


/**
 * Represents an instance in time with minute-level precision.
 * Encapsulates year, month (1-12), day (1-31), hour (0-23), and minute (0-59).
 */
public class Time implements Comparable<Time> {
    /*
     * Static Constants
     */

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



    /*
     * Instance Variables
     */

    private int year;   // Required
    private int month;  // Required
    private int day;    // Required
    private int hour;   // Required
    private int minute; // Required



    /*
     * Static Methods
     */
    
    /**
     * Determines whether the specified year is a leap year.
     * Code adapted from <a href="https://www.programiz.com/cpp-programming/examples/leap-year">Programiz</a>.
     * @param year The year to check.
     * @return <code>true</code> if the year is a leap year, <code>false</code> otherwise.
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



    /*
     * Constructors
     */

    /**
     * Constructs a <code>Time</code> initialized to minimum allowed values (0001/01/01 00:00).
     */
    public Time() {
        // Assume default values
        setYear(MIN_YEAR);
        setMonth(MIN_MONTH);
        setDay(MIN_DAY);
        setHour(MIN_HOUR);
        setMinute(MIN_MINUTE);
    }

    /**
     * Constructs a <code>Time</code> with specified components.
     * Invalid components default to their minimum allowed values.
     * @param year The year.
     * @param month The month.
     * @param day The day.
     * @param hour The hour.
     * @param minute The minute.
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
     * Constructs a <code>Time</code> by parsing a string in YYYY/MM/DD HH:MM format.
     * Invalid components default to their minimum allowed values.
     * @param formatString String in YYYY/MM/DD HH:MM format.
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
                    setYear(y);
                    setMonth(m);
                    setDay(d);
                    setHour(h);
                    setMinute(min);
                } else {
                    System.out.println("One or more fields were not properly entered; defaulting to " + toString() + ".");
                }
            } catch (Exception e) {
                System.out.println("One or more fields were not properly entered; defaulting to " + toString() + ".");
            }
        }
    }



    /*
     * Mutators
     */

    /**
     * Sets the year if within the valid range (1-9999).
     * @param year The new year.
     */
    public void setYear(int year) {
        if (year >= MIN_YEAR && year <= MAX_YEAR)
            this.year = year;
    }

    /**
     * Sets the month if within the valid range (1-12).
     * @param month The new month.
     */
    public void setMonth(int month) {
        if (month >= MIN_MONTH && month <= MAX_MONTH)
            this.month = month;
    }

    /**
     * Sets the day if within the valid range for the current month and year.
     * @param day The new day.
     */
    public void setDay(int day) {
        int maxDays = maxDaysInMonth(getYear(), getMonth());
        
        // If valid, populate the field.
        if (day >= MIN_DAY && day <= maxDays)
            this.day = day;
    }

    /**
     * Sets the hour if within the valid range (0-23).
     * @param hour The new hour.
     */
    public void setHour(int hour) {
        if (hour >= MIN_HOUR && hour <= MAX_HOUR)
            this.hour = hour;
    }

    /**
     * Sets the minute if within the valid range (0-59).
     * @param minute The new minute.
     */
    public void setMinute(int minute) {
        if (minute >= MIN_MINUTE && minute <= MAX_MINUTE)
            this.minute = minute;
    }



    /*
     * Accessors
     */

    /**
     * Gets the year.
     * @return The year.
     */
    public int getYear() {
        return this.year;
    }

    /**
     * Gets the month.
     * @return The month (1-12).
     */
    public int getMonth() {
        return this.month;
    }

    /**
     * Gets the day.
     * @return The day of the month.
     */
    public int getDay() {
        return this.day;
    }

    /**
     * Gets the hour.
     * @return The hour (0-23).
     */
    public int getHour() {
        return this.hour;
    }

    /**
     * Gets the minute.
     * @return The minute (0-59).
     */
    public int getMinute() {
        return this.minute;
    }



    /*
     * Object Overrides
     */

    /**
     * Returns a string representation of this time in YYYY/MM/DD HH:MM format.
     * @return Formatted time string.
     */
    @Override
    public String toString() {
        return String.format("%04d/%02d/%02d %02d:%02d",
            getYear(), getMonth(), getDay(),
            getHour(), getMinute());
    }

    /**
     * Compares this time with another time.
     * @param other The other time to compare with.
     * @return A negative integer, zero, or a positive integer as this time precedes, equals, or follows the specified time.
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
     * Determines whether this time is equal to another <code>Time</code>.
     * @param other The other time to compare with.
     * @return <code>true</code> if both times represent the same moment, <code>false</code> otherwise.
     */
    public boolean equals(Time other) {
        if (other == null)
            return false;
        return compareTo(other) == 0;
    }

    /**
     * Determines whether this time is equal to another object.
     * @param other The object to compare with.
     * @return <code>true</code> if the objects are equal, <code>false</code> otherwise.
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
