package dayplanner;


import java.util.Scanner;
import java.io.InputStream;


public class Input {
    private final Scanner SCANNER;

    public Input(InputStream in) {
        SCANNER = new Scanner(in);
    }

    private void clearNewline() {
        SCANNER.nextLine();
    }

    public int readInt(){
        System.out.print("> ");

        int input = SCANNER.nextInt();
        clearNewline();
        
        System.out.println();
        return input;
    }
}