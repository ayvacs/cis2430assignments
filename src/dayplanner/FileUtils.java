package dayplanner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


/**
 * <p>
 * Handles the application's file reads, writes, and deletion to separate these
 * persistance details from the main code.
 * </p>
 * <p>
 * The purpose of this class is to enable easy file operations based on a
 * directory and file name, to cut down on boilerplate code in application
 * classes.
 * </p>
 */
public class FileUtils {
    /**
     * Default constructor (not used because all methods are static).
     */
    private FileUtils() {}


    /**
     * Reads every line from the named file.
     *
     * @param dirName  Directory containing the file.
     * @param fileName Name of the file.
     * @return The file's lines in order.
     * @throws IOException If the file cannot be read.
     */
    public static ArrayList<String> readLines(String dirName, String fileName) throws IOException {
        ArrayList<String> lines = new ArrayList<>();
        File file = new File(dirName, fileName);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null)
                lines.add(line);
        }

        return lines;
    }


    /**
     * Reads and trims the first line of the named file.
     *
     * @param dirName  Directory containing the file.
     * @param fileName Name of the file.
     * @return The trimmed first line.
     * @throws IOException If the file cannot be read.
     */
    public static String readFirstLine(String dirName, String fileName) throws IOException {
        // i hate how java makes you use
        // 10 trillion classes for everything
        File f = new File(dirName, fileName);
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);

        String str = br.readLine().trim();
        br.close();
        return str;
    }


    /**
     * Writes text to a file, creating its parent directories when needed.
     *
     * @param dirName Directory where the file will be saved.
     * @param fileName Name of the target file.
     * @param contents Text to write.
     * @throws IOException If the file cannot be written.
     */
    public static void writeText(String dirName, String fileName, String contents) throws IOException {
        File file = new File(dirName, fileName);
        File parent = file.getParentFile();

        if (parent != null)
            parent.mkdirs();

        FileWriter fw = new FileWriter(file);
        fw.write(contents);
        fw.close();
    }


    /**
     * Checks whether the named file exists.
     * 
     * @param dirName  Directory where the specified file is found.
     * @param fileName Name of the specified file.
     * @return <code>true</code> if the file exists, <code>false</code> otherwise.
     */
    public static boolean exists(String dirName, String fileName) {
        return new File(dirName, fileName).exists();
    }


    /**
     * Deletes the named file if it exists.
     * 
     * @param dirName  Directory where the specified file is found.
     * @param fileName Name of the specified file.
     */
    public static void delete(String dirName, String fileName) {
        new File(dirName, fileName).delete();
    }
}
