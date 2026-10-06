package dayplanner;


import java.util.Iterator;


/**
 * Represents a collection of Activities of any type.
 * Implementation of standard arrays, with some helpful methods.
 */
public class ActivityList implements Iterable<Activity> {
    private static final int DEFAULT_CAPACITY = 256;



    private int capacity;   // Maximum possible length of this list
    private int length;     // Current length of this list
    private Activity[] array;



    /**
     * Instantiate a new <code>ActivityList</code> with the default capacity.
     */
    public ActivityList() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Instantiate a new <code>ActivityList</code> with the specified integer capacity.
     * @param capacity The new <code>ActivityList</code>'s capacity. If zero or negative, use the default instead.
     */
    public ActivityList(int capacity) {
        if (capacity < 1)
            capacity = DEFAULT_CAPACITY;
        this.capacity = capacity;
        this.array = new Activity[capacity];
    }



    /**
     * Determine whether this <code>ActivityList</code> is full.
     * @return Boolean (if <code>true</code>, no more elements can be added).
     */
    boolean isFull() {
        return length == capacity;
    }

    /**
     * Determine whether this <code>ActivityList</code> is empty.
     * @return Boolean (if <code>true</code>, there are no elements in the list).
     */
    boolean isEmpty() {
        return length == 0;
    }

    /**
     * Append a new <code>Activity</code> to the end of this list.
     * If full, do nothing.
     * @param activity The new <code>Activity</code> to append.
     */
    void append(Activity activity) {
        if (isFull())
            return;

        array[length] = activity;
        length++;
    }



    public Iterator<Activity> iterator() {
        return new Iterator<Activity>() {
            private int index = 0;

            public boolean hasNext() {
                return index < length;
            }

            public Activity next() {
                Activity ret = array[index];
                index++;
                return ret;
            }
        };
    }



    /**
     * @return A <code>String</code> representation of this <code>ActivityList</code>.
     */
    public String toString() {
        String ret = "---\n";

        if (isEmpty()) {
            ret += "(empty)\n";
        } else {
            for (Activity a: this)
                if (a == null)
                    ret += "(null)\n";
                else
                    ret += a.toString() + "\n";
        }

        ret += "---";
        return ret;
    }

    /**
     * @return Whether or not this <code>ActivityList</code> equals <code>other</code>.
     * @param other The other <code>ActivityList</code> to compare with.
     */
    public boolean equals(ActivityList other) {
        return toString().equals(other.toString());
    }
}
