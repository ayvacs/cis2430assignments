package dayplanner;


public class Time {
    private static final int MIN_YEAR = 1970;
    private static final int MAX_YEAR = MIN_YEAR + 200;



    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;



    public Time() {
        // Assume default values
        this.year = MIN_YEAR;
        this.month = 1;
        this.day = 1;
        this.hour = 1;
        this.minute = 1;
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
        if (month >= 1 && month <= 12)
            this.month = month;
    }

    public void setDay(int day) {
        if (day >= 1 && day <= 31)
            this.day = day;
    }

    public void setHour(int hour) {
        if (hour == 24)
            setHour(0);
        if (hour >= 0 && hour <= 23)
            this.hour = hour;
    }

    public void setMinute(int minute) {
        if (minute == 60)
            setMinute(0);
        if (minute >= 0 && minute <= 59)
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
