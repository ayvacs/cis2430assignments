package dayplanner;


import java.util.Iterator;


/**
 * Represents a collection of Activities.
 * Implementation of standard arrays, with some helpful methods.
 * <code>Type</code> determines the single type of activity this list accepts; i.e. <code>OtherActivity</code>.
 */
public class ActivityList<Type> implements Iterable<Type> {
    private static final int DEFAULT_CAPACITY = 256;



    private int capacity;   // Maximum possible length of this list
    private int length;     // Current length of this list
    private Type[] array;



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
    @SuppressWarnings("unchecked") // make the compiler shut up about typecasting
    public ActivityList(int capacity) {
        if (capacity < 1)
            capacity = DEFAULT_CAPACITY;
        this.capacity = capacity;

        // cant create a generic array so cast it instead.
        this.array = (Type[]) new Object[capacity];
    }



    /**
     * Determine whether this <code>ActivityList</code> is full.
     * @return Boolean (if <code>true</code>, no more elements can be added).
     */
    public boolean isFull() {
        return length == capacity;
    }

    /**
     * Determine whether this <code>ActivityList</code> is empty.
     * @return Boolean (if <code>true</code>, there are no elements in the list).
     */
    public boolean isEmpty() {
        return length == 0;
    }

    /**
     * Append a new <code>Activity</code> to the end of this list.
     * If full, do nothing.
     * @param activity The new <code>Activity</code> to append.
     */
    public void append(Type activity) {
        if (isFull())
            return;

        array[length] = activity;
        length++;
    }



    /**
     * Return an <code>Iterator</code> that allows you to easily iterate through this <code>ActivityList</code>.
     * For example:
     * 
     * <pre>
     * for (Activity a : activityList) {
     *     System.out.println(a.toString());
     * }
     * </pre>
     * @return <code>Iterator</code> representation of this list.
     */
    public Iterator<Type> iterator() {
        return new Iterator<Type>() {
            private int index = 0;

            public boolean hasNext() {
                return index < length;
            }

            public Type next() {
                Type ret = array[index];
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
            for (Type a: this)
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
    public boolean equals(ActivityList<Type> other) {
        return toString().equals(other.toString());
    }
}
