package dayplanner;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Iterator;


/**
 * Represents a list of <code>Activity</code> objects.
 * Extends <code>AbstractList</code> to provide standard collection operations.
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
     * @param capacity The initial capacity.
     * @throws IllegalArgumentException If capacity is negative.
     */
    public ActivityList(int capacity) {
        if (capacity < 0)
            throw new IllegalArgumentException("Capacity cannot be negative");

        this.capacity = capacity;
        this.length = 0;
        this.array = new Activity[capacity];
    }

    /**
     * Constructs an <code>ActivityList</code> by loading activities from a RAS file.
     * @param dirName The directory containing the RAS file.
     * @param fileName The name of the RAS file.
     * @throws IllegalArgumentException If either argument is null or empty.
     */
    public ActivityList(String dirName, String fileName) {
        this();

        if (dirName == null || fileName == null || dirName.isEmpty() || fileName.isEmpty())
            throw new IllegalArgumentException("One or more of the two arguments was null or empty:\n"
                + "(dirName=" + dirName + ")\n"
                + "(dirName=" + fileName + ")");

        BufferedReader br;
        FileReader fr;
        File file = new File(dirName + "/" + fileName);

        try {

            fr = new FileReader(file);
            br = new BufferedReader(fr);

            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    Activity a = activityFromRAS(line);

                    if (isFull()) {
                        br.close();
                        throw new ArrayIndexOutOfBoundsException("Full ("+fileName+"),("+a.toString()+")");
                    }
                    else {
                        append(a);
                    }
                }
            }
            
            br.close();

        } catch (FileNotFoundException e) {
            // if the file is not found - thats fine
            // that just means the user never saved data before.
            // doing nothing starts this list as one
            // with DEFAULT_CAPACITY
            System.out.println("There was no list found at " + dirName + "/" + fileName + "; starting with an empty list");
        } catch (Exception e) {
            // if there was any other exception
            // then something else went wrong
            e.printStackTrace();
        }
    }



    /*
     * Serialization
     */

    /**
     * Encodes this activity list into RAS (Readable Activity Serial) format.
     * Each activity is serialized on its own line, making the output directly suitable for writing to a <code>.ras</code> file.
     * <br>
     * For example, a list containing one home and one school activity produces a String identical in format to the following:
     * <pre>
     * &lt;HomeActivity,Morning Run,2026/01/01 07:00,2026/01/01 08:00,NIL&gt;
     * &lt;SchoolActivity,Study Session,2026/10/07 14:00,2026/10/07 17:00,Midterm prep - data structures&gt;
     * </pre>
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
     * Saves the activity list to a file in RAS format.
     * @param dirName Directory where the file will be saved.
     * @param fileName Name of the target file.
     * @return <code>true</code> if saved successfully, <code>false</code> otherwise.
     */
    public boolean saveToRAS(String dirName, String fileName) {
        try {
            // Get the File instance
            File file = new File(dirName + "/" + fileName);

            // Create the directory if it doesnt exist
            File parent = file.getParentFile();
            if (parent != null)
                parent.mkdirs();

            FileWriter writer = new FileWriter(file);
            writer.write(toRAS());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Parses a RAS entry string and instantiates the appropriate <code>Activity</code> subclass.
     * The first token (between the opening <code>&lt;</code> and the first comma) is used as the
     * class discriminator: <code>HomeActivity</code>, <code>SchoolActivity</code>, or
     * <code>OtherActivity</code> each map to their respective constructors.
     * Any unrecognized type falls back to a plain <code>Activity</code> instance.
     * @param ras A valid RAS entry string.
     * @return The instantiated <code>Activity</code> subclass.
     */
    private static Activity activityFromRAS(String ras) {
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
     * @param index The index of the activity to retrieve.
     * @return The activity at the specified index.
     * @throws IndexOutOfBoundsException If index is out of bounds.
     */
    @Override
    public Activity get(int index) {
        if (index < 0 || index >= size())
            throw new IndexOutOfBoundsException("Index " + index + ", Size" + size());

        return array[index];
    }
    
    /**
     * Returns the number of activities in this list.
     * @return The number of activities.
     */
    @Override
    public int size() {
        return length;
    }

    /**
     * Replaces the activity at the specified index.
     * @param index Index of the activity to replace.
     * @param activity The new activity to place at the index.
     * @return The activity previously at the specified index.
     * @throws IndexOutOfBoundsException If index is out of bounds.
     */
    @Override
    public Activity set(int index, Activity activity) {
        if (index < 0 || index >= size())
            throw new IndexOutOfBoundsException("Index " + index + ", Size " + size());

        Activity old = get(index);
        array[index] = activity;
        return old;
    }

    /**
     * Inserts the activity at the specified index, shifting subsequent elements right.
     * @param index Index at which the activity is to be inserted.
     * @param activity The activity to insert.
     * @throws IndexOutOfBoundsException If index is out of bounds.
     */
    public void add(int index, Activity activity) {
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
     * Removes the activity at the specified index, shifting subsequent elements left.
     * @param index Index of the activity to remove.
     * @return The activity that was removed.
     * @throws IndexOutOfBoundsException If index is out of bounds.
     */
    @Override
    public Activity remove(int index) {
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
     * @return <code>true</code> if the list is full, <code>false</code> otherwise.
     */
    public boolean isFull() {
        return length == capacity;
    }

    /**
     * Determines whether this list contains no activities.
     * @return <code>true</code> if the list is empty, <code>false</code> otherwise.
     */
    public boolean isEmpty() {
        return length == 0;
    }

    /**
     * Appends an activity to the end of this list.
     * @param activity The activity to append.
     * @return The appended activity, or <code>null</code> if the list is full.
     */
    public Activity append(Activity activity) {
        if (isFull())
            return null;

        array[length] = activity;
        length++;
        
        return activity;
    }

    /**
     * Returns an iterator over the activities in this list, enabling for-each iteration.
     * For example:
     * <pre>
     * for (Activity a : activityList) {
     *     System.out.println(a.getTitle());
     * }
     * </pre>
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
     */
    public void sort() {
        Arrays.sort(array, 0, length);
    }



    /*
     * Object Overrides
     */
    
    /**
     * Returns a string representation of this activity list.
     * @return A string representation of this activity list.
     */
    @Override
    public String toString() {
        String str = "---\n";

        if (isEmpty()) {
            str += "(empty)\n";
        } else {
            for (Activity a: this)
                if (a == null)
                    str += "(null)\n";
                else
                    str += a.toString() + "\n";
        }

        str += "---";
        return str;
    }

    /**
     * Determines whether this activity list is equal to another <code>ActivityList</code>.
     * @param other The other <code>ActivityList</code> to compare with.
     * @return <code>true</code> if both lists contain equal activities in the same order, <code>false</code> otherwise.
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
     * @param other The object to compare with.
     * @return <code>true</code> if the objects are equal, <code>false</code> otherwise.
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
