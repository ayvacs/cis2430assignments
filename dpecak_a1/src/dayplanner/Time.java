package dayplanner;


public class Time {
    private static final int MIN_YEAR = 1970;
    private static final int MIN_MONTH = 1;
    private static final int MIN_DAY = 1;
    private static final int MIN_HOUR = 0;
    private static final int MIN_MINUTE = 0;

    private static final int MAX_YEAR = MIN_YEAR + 200;
    private static final int MAX_MONTH = 12;
    private static final int MAX_DAY = 31;
    private static final int MAX_HOUR = 23;
    private static final int MAX_MINUTE = 59;



    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;



    private static boolean isLeapYear(int year) {
        // https://www.programiz.com/cpp-programming/examples/leap-year

        if (year % 400 == 0)
            return true;
        else if (year % 100 == 0)
            return false;
        else if (year % 4 == 0)
            return true;

        return false;
    }



    public Time() {
        // Assume default values
        this.year = MIN_YEAR;
        this.month = MIN_MONTH;
        this.day = MIN_DAY;
        this.hour = MIN_HOUR;
        this.minute = MIN_MINUTE;
    }

    public Time(int year, int month, int day, int hour, int minute) {
        // Assume default values
        this();

        setYear(year);
        setMonth(month);
        setDay(day);
        setHour(hour);
        setMinute(minute);
    }

    public Time(String formatString) {
        // Assume default values
        this();

        // Remove all spaces
        String fs = formatString.trim().replaceAll(" ", "");

        // Split up the 5 components by slashes, colons, and commas
        String[] components = fs.split("[/:,]");

        // Populate attributes (parseInt defaults to NaN if no integer is found, which our setter methods refuse to operate on, thus avoiding any exceptions.)
        setYear(Integer.parseInt(components[0]));
        setMonth(Integer.parseInt(components[1]));
        setDay(Integer.parseInt(components[2]));
        setHour(Integer.parseInt(components[3]));
        setMinute(Integer.parseInt(components[4]));
    }



    public void setYear(int year) {
        if (year >= MIN_YEAR && year <= MAX_YEAR)
            this.year = year;
    }

    public void setMonth(int month) {
        if (month >= MIN_MONTH && month <= MAX_MONTH)
            this.month = month;
    }

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

    public void setHour(int hour) {
        if (hour >= MIN_HOUR && hour <= MAX_HOUR)
            this.hour = hour;
    }

    public void setMinute(int minute) {
        if (minute >= MIN_MINUTE && minute <= MAX_MINUTE)
            this.minute = minute;
    }



    public int getYear()   { return this.year;   }
    public int getMonth()  { return this.month;  }
    public int getDay()    { return this.day;    }
    public int getHour()   { return this.hour;   }
    public int getMinute() { return this.minute; }



    public String toString() {
        return String.format("%d/%d/%d %d:%d",
            getYear(), getMonth(), getDay(),
            getHour(), getMinute());
    }

    public boolean equals(Time other) {
        return toString().equals(other.toString());
    }
}
