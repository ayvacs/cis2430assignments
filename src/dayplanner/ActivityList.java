package dayplanner;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Iterator;


/**
 * <p>
 * A fixed-capacity, array-backed list of {@link Activity} objects. Extends
 * {@link AbstractList} to ensure methods are familiar. The backing array never
 * grows or resizes.
 * </p>
 * <p>
 * Example usage:
 * </p>
 * 
 * <pre>
 * ActivityList activities = new ActivityList(2); // max capacity is 2
 * activities.append(new HomeActivity("Laundry", start, end));
 * activities.sort(); // orders by start time
 * </pre>
 */
public class ActivityList extends AbstractList<Activity> {
    private static final int DEFAULT_CAPACITY = 256;



    private int capacity;   // Maximum possible length of this list (the list COULD POSSIBLY hold this many elements)
    private int length;     // Current length of this list (the list IS CURRENTLY holding this many elements)
    private Activity[] array;



    /*
     * Constructors
     */
    
    /**
     * Constructs an <code>ActivityList</code> with the default capacity (256).
     */
    public ActivityList() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Constructs an <code>ActivityList</code> with the specified capacity.
     * 
     * @param capacity Maximum number of activities this list can hold.
     * @throws IllegalArgumentException If capacity is negative.
     */
    public ActivityList(int capacity) throws IllegalArgumentException {
        if (capacity < 0)
            throw new IllegalArgumentException("Capacity cannot be negative");

        this.capacity = capacity;
        this.length = 0;
        this.array = new Activity[capacity];
    }

    /**
     * Constructs an <code>ActivityList</code> by loading activities from a RAS
     * file.
     * 
     * @param dirName  The directory containing the RAS file.
     * @param fileName The name of the RAS file.
     * @throws IllegalArgumentException If either argument is null or empty. If the
     *                                  file does not exist, construction succeeds
     *                                  with an empty list.
     */
    public ActivityList(String dirName, String fileName) throws IllegalArgumentException {
        this();

        if (dirName == null   || fileName == null
         || dirName.isEmpty() || fileName.isEmpty())
            throw new IllegalArgumentException("Directory or file name was null or empty.");

        try {

            appendActivitiesFromFile(dirName, fileName);

        } catch (FileNotFoundException e) {

            /**
             * If the file is not found that means the user never saved data before. We
             * don't need to do anything here, just start this list as one with the default
             * capacity (which was the first line in this method anyways)
             */
            System.out.println("There was no list found at " + dirName + "/" + fileName + "; starting with an empty list");

        } catch (Exception e) {

            System.out.println(e.toString());
            e.printStackTrace();

        }
    }



    /**
     * Utility method for the constructor {@link #ActivityList(String, String)} that
     * reads through a RAS file, instantiates all the activities in it, and appends
     * them to this list.
     * 
     * @param dirName  The directory containing the RAS file.
     * @param fileName The name of the RAS file.
     * @throws FileNotFoundException          if the file does not exist.
     * @throws ArrayIndexOutOfBoundsException if the file is greater than the
     *                                        allotted array size.
     * @throws IOException                    if there was an error reading to the
     *                                        file.
     */
    public void appendActivitiesFromFile(String dirName, String fileName)
    throws FileNotFoundException, ArrayIndexOutOfBoundsException, IOException {
        if (!FileUtils.exists(dirName, fileName))
            throw new FileNotFoundException(dirName + ", " + fileName);

        for (String line : FileUtils.readLines(dirName, fileName)) {
            if (isFull())
                throw new ArrayIndexOutOfBoundsException("Cannot continue, file greater than allotted aray size");
            else if (line != null && !line.isEmpty())
                append(activityFromRAS(line));
        }
    }



    /*
     * Serialization
     */

    /**
     * <p>
     * Encodes this activity list into RAS (Readable Activity Serial) format.
     * </p>
     * <p>
     * Each activity is serialized on its own line, making the output directly
     * suitable for writing to a <code>.ras</code> file.
     * </p>
     * <p>
     * For example, a list containing one home and one school activity produces a
     * String identical in format to the following:
     * </p>
     * 
     * <pre>
     * &lt;HomeActivity,Morning Run,2026/01/01 07:00,2026/01/01 08:00,NIL&gt;
     * &lt;SchoolActivity,Study Session,2026/10/07 14:00,2026/10/07 17:00,Midterm prep - data structures&gt;
     * </pre>
     * 
     * @return A newline-separated RAS representation of all activities.
     */
    public String toRAS() {
        String ret = "";
        String tmp;

        for (Activity a : this) {
            try {
                tmp = a.toRAS();
            } catch (Exception e) {
                tmp = e.toString();
            }

            ret += tmp + "\n";
        }

        return ret;
    }

    /**
     * Saves the activity list to a file in RAS format. Parent directories are
     * created when needed.
     * 
     * @param dirName  Directory where the file will be saved.
     * @param fileName Name of the target file.
     * @return {@code true} if the write succeeds, or {@code false} if an I/O error
     *         occurs.
     */
    public boolean saveToRAS(String dirName, String fileName) {
        try {
            FileUtils.writeText(dirName, fileName, toRAS());
        } catch (IOException e) {
            System.out.println("Error writing to " + dirName + "/" + fileName);
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * <p>
     * Parses a RAS entry string and instantiates the appropriate
     * <code>Activity</code> subclass.
     * </p>
     * <p>
     * The first token (between the opening <code>&lt;</code> and the first comma)
     * is used as the class discriminator: <code>HomeActivity</code>,
     * <code>SchoolActivity</code>, or <code>OtherActivity</code> each map to their
     * respective constructors. Any unrecognized type falls back to a plain
     * {@code Activity}.
     * </p>
     * <p>
     * Values are comma-delimited and are not escaped, so commas inside values are
     * unsupported.
     * </p>
     * 
     * @param ras One serialized entry, such as
     *            {@code <SchoolActivity,Lab,2026/10/20 14:30,2026/10/20 16:30,NIL>}.
     * @return The instantiated <code>Activity</code> subclass.
     */
    public static Activity activityFromRAS(String ras) {
        // remove the outer angled brackets
        String inner = ras.substring(1, ras.length() - 1);
        // tokenize and grab token 0 (classname)
        String type = inner.split(",")[0];

        switch (type) {
            case "HomeActivity":   return new HomeActivity(ras);
            case "SchoolActivity": return new SchoolActivity(ras);
            case "OtherActivity":  return new OtherActivity(ras);
            default:               return new Activity(ras);
        }
    }



    /*
     * Instance Methods
     */

    /**
     * Gets the activity at the specified index.
     * 
     * @param index The index of the activity to retrieve.
     * @return The activity at the specified index.
     * @throws IndexOutOfBoundsException If {@code index < 0} or
     *                                   {@code index >= size()}.
     */
    @Override
    public Activity get(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size())
            throw new IndexOutOfBoundsException("Index " + index + ", Size" + size());

        return array[index];
    }
    
    /**
     * Returns the number of activities in this list.
     * 
     * @return The number of activities.
     */
    @Override
    public int size() {
        return length;
    }

    /**
     * Replaces the activity at the specified index.
     * 
     * @param index    Index of the activity to replace.
     * @param activity The new activity to place at the index.
     * @return The activity previously at the specified index.
     * @throws IndexOutOfBoundsException If index is out of bounds.
     */
    @Override
    public Activity set(int index, Activity activity) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size())
            throw new IndexOutOfBoundsException("Index " + index + ", Size " + size());

        Activity old = get(index);
        array[index] = activity;
        return old;
    }

    /**
     * <p>
     * Inserts the activity at the specified index, shifting subsequent elements
     * right.
     * </p>
     * <p>
     * Ensure the list is not full before calling; this fixed-capacity
     * implementation cannot accommodate an insertion when all slots are occupied.
     * </p>
     * 
     * @param index    Index at which the activity is to be inserted.
     * @param activity The activity to insert.
     * @throws IndexOutOfBoundsException If index is out of bounds.
     */
    public void add(int index, Activity activity) throws IndexOutOfBoundsException {
        if (index < 0 || index > size())
            throw new IndexOutOfBoundsException("Index " + index + ", Size " + size());

        // If the array is full then we don't need to shift any elements
        if (size() != capacity)
            System.arraycopy(
                array, index,
                array, index + 1,
                size() - index);

        array[index] = activity;
        length++;
    }

    /**
     * Removes the activity at the specified index, shifting subsequent elements
     * left.
     * 
     * @param index Index of the activity to remove.
     * @return The activity that was removed.
     * @throws IndexOutOfBoundsException If index is out of bounds.
     */
    @Override
    public Activity remove(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size())
            throw new IndexOutOfBoundsException("Index " + index + ", Size " + size());

        Activity old = get(index);

        // Shift all elements after index forward by one
        System.arraycopy(
            array, index + 1,
            array, index,
            size() - index - 1);

        array[length - 1] = null;
        length--;

        return old;
    }

    /**
     * Determines whether this list has reached its maximum capacity.
     * 
     * @return <code>true</code> if the list is full, <code>false</code> otherwise.
     */
    public boolean isFull() {
        return length == capacity;
    }

    /**
     * Determines whether this list contains no activities.
     * 
     * @return <code>true</code> if the list is empty, <code>false</code> otherwise.
     */
    public boolean isEmpty() {
        return length == 0;
    }

    /**
     * Appends an activity to the end of this list.
     * 
     * @param activity The activity to append.
     * @return The same activity reference after insertion, or {@code null} if full.
     *         Check for {@code null} to detect a full list before using the result.
     */
    public Activity append(Activity activity) {
        if (isFull())
            return null;

        array[length] = activity;
        length++;
        
        return activity;
    }

    /**
     * Returns an iterator over the activities in this list, enabling for-each
     * iteration. For example:
     * 
     * <pre>
     * for (Activity a : activityList) {
     *     System.out.println(a.getTitle());
     * }
     * </pre>
     * 
     * @return An iterator over the elements in this list.
     */
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
     * Sorts the activities in-place in chronological order by start time.
     * Activities with equal start times compare equally and may appear in either
     * relative order.
     */
    public void sort() {
        Arrays.sort(array, 0, length);
    }



    /*
     * Object Overrides
     */

    /**
     * Returns a string representation of this activity list.
     * 
     * @return A string representation of this activity list.
     */
    @Override
    public String toString() {
        String str = "---\n";

        if (isEmpty()) {
            str += "(empty)\n";
        } else {
            for (Activity a: this)
                if (a == null || a.toString().isBlank())
                    str += "(null)\n";
                else
                    str += a.toString() + "\n";
        }

        str += "---";
        return str;
    }

    /**
     * Determines whether this activity list is equal to another
     * <code>ActivityList</code>.
     * 
     * @param other The other <code>ActivityList</code> to compare with.
     * @return <code>true</code> if both lists contain equal activities in the same
     *         order, <code>false</code> otherwise.
     */
    public boolean equals(ActivityList other) {
        if (other == null || size() != other.size())
            return false;

        for (int i = 0; i < size(); i++) {
            if (!get(i).equals(other.get(i)))
                return false;
        }

        return true;
    }

    /**
     * Determines whether this activity list is equal to another object.
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
        return equals((ActivityList) other);
    }
}
