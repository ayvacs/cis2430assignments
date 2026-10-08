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
 * Represents a collection of Activities.
 * Implementation of standard arrays, with some helpful methods.
 */
public class ActivityList
extends AbstractList<Activity>
{
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
     * @param capacity The new <code>ActivityList</code>'s capacity.
     * @throws IllegalArgumentException when <code>capacity</code> is negative.
     */
    public ActivityList(int capacity) {
        if (capacity < 0)
            throw new IllegalArgumentException("Capacity cannot be negative");

        this.capacity = capacity;
        this.array = new Activity[capacity];
    }

    /**
     * Instantiate a new <code>ActivityList</code> from a RAS list saved to a file.
     * @param dirName The name of the directory where the RAS file lives.
     * @param fileName The name of the RAS file.
     */
    public ActivityList(String dirName, String fileName) {
        this();

        try {
            // Get the file instance
            File file = new File(dirName + "/" + fileName);
            BufferedReader fr = new BufferedReader(
                new FileReader(file));

            String line;

            while ((line = fr.readLine()) != null)
                append(activityFromRAS(line));

            fr.close();
        }
        catch (FileNotFoundException e) {
            // if the file is not found - thats fine
            // that just means the user never
            // saved data before
            // so we can exit now without doing anything
            System.out.println("There was no list found at " + dirName + "/" + fileName + ".");
            return;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    /**
     * Parse the type tag from a RAS string and instantiate the correct <code>Activity</code> subclass.
     * @param ras A valid RAS entry string.
     * @return The appropriate <code>Activity</code> subclass instance.
     */
    private static Activity activityFromRAS(String ras) {
        // remove the outer angled brackets
        String inner = ras.substring(1, ras.length() - 1);
        // tokenize and grab token 0
        String type = inner.split(",")[0];

        switch (type) {
            case "HomeActivity":   return new HomeActivity(ras);
            case "SchoolActivity": return new SchoolActivity(ras);
            case "OtherActivity":  return new OtherActivity(ras);
            default:               return new Activity(ras);
        }
    }



    /**
     * Get the Activity at the specified index.
     * @param index Index to look at.
     * @return Activity at the specified index.
     * @throws IndexOutOfBoundsException If <code>index</code> is negative or greater than the size.
     */
    @Override
    public Activity get(int index) {
        if (index < 0 || index >= size())
            throw new IndexOutOfBoundsException("Index " + index + ", Size" + size());

        return array[index];
    }

    
    /**
     * Return the current size (number of elements) of this <code>ActivityList</code>.
     * @return Integer corresponding to the current number of elements
     */
    @Override
    public int size() {
        return length;
    }

    /**
     * Replace the Activity at the specified index.
     * @param index Index to put element.
     * @param activity Activity to put there.
     * @throws IndexOutOfBoundsException If <code>index</code> is negative or greater than the size.
     * @return The old Activity located at <code>index</code>.
     */
    @Override
    public Activity set(int index, Activity activity) {
        if (index < 0 || index >= size())
            throw new IndexOutOfBoundsException("Index " + index + ", Size" + size());

        Activity old = get(index);
        array[index] = activity;
        return old;
    }

    /**
     * Insert the Activity to the specified index and shift all elements behind it, if any, back by one.
     * @param index Index to insert element.
     * @param activity Activity to put there.
     * @throws IndexOutOfBoundsException If <code>index</code> is negative or greater than the size.
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
     * Remove the Activity at the specified index, shifting all elements behind it forward by one.
     * @param index Index of the element to remove.
     * @throws IndexOutOfBoundsException If <code>index</code> is negative or greater than or equal to the size.
     * @return The removed Activity.
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
     * @return The <code>Activity</code> that was just appended, or <code>null</code> when the list is full.
     */
    public Activity append(Activity activity) {
        if (isFull())
            return null;

        array[length] = activity;
        length++;
        
        return activity;
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
     * Encodes this instance into <code>RAS</code> <b>(Readable Activity Serial)</b>, a text format that allows it to be written to text files.
     * <code>RAS</code> lists follow the following format:
     * <pre>&lt;entry...&gt;<br>&lt;entry...&gt;<br>&lt;entry...&gt;</pre>
     * where <code>&lt;entry...&gt;</code> is one single RAS entry (which represents one activity).
     * For more details, see <code>Activity.toRAS()</code> or <code>README.md</code>.
     * @return A <code>RAS</code> representation of this <code>ActivityList</code>.
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
     * Attempt to save the <code>ActivityList</code> to the specified file in RAS format.
     * Overwrites any existing content in the file.
     * @param dirName Name of the directory where the file will be saved.
     * @param fileName Name of the file where content will be saved.
     * @return Boolean indicating if there were any errors. (<code>true</code> = success, <code>false</code> = failure)
     */
    public boolean saveToRAS(String dirName, String fileName) {
        // Get the File instance
        File file = new File(dirName + "/" + fileName);

        // Create the directory if it doesnt exist
        File parent = file.getParentFile();
        if (parent != null)
            parent.mkdirs();

        try {
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
     * @return Whether or not this <code>ActivityList</code> equals <code>other</code>.
     * @param other The other <code>ActivityList</code> to compare with.
     */
    public boolean equals(ActivityList other) {
        return toString().equals(other.toString());
    }

    /**
     * Sort the ActivityList in-place in ascending order by starting time (activities that start earlier appear earlier).
     */
    public void sort() {
        Arrays.sort(array);
    }
}
