package dayplanner;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;


/**
 * Represents a collection of Activities.
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

        // cant create a generic array so cast it instead.
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
        // check the first token inside the angle brackets to get the type tag
        String inner = ras.substring(1, ras.length() - 1);
        String type = inner.split(",")[0];

        switch (type) {
            case "HomeActivity":   return new HomeActivity(ras);
            case "SchoolActivity": return new SchoolActivity(ras);
            case "OtherActivity":  return new OtherActivity(ras);
            default:               return new Activity(ras);
        }
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
    public void append(Activity activity) {
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
     * @return Boolean indicating if there were any errors.
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

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @return Whether or not this <code>ActivityList</code> equals <code>other</code>.
     * @param other The other <code>ActivityList</code> to compare with.
     */
    public boolean equals(ActivityList other) {
        return toString().equals(other.toString());
    }
}
