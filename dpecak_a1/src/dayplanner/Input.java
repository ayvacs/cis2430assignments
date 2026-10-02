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
        
        System.out.println(" ");
        return input;
    }


    public String readString(){
        System.out.print("> ");

        String input = SCANNER.nextLine();
        input = input.trim();
        
        System.out.println(" ");
        return input;
    }


    public boolean readBool(){
        System.out.print("Y/n > ");

        String input = readString();
        boolean bool = (Character.toUpperCase(input.charAt(0)) == 'Y');
        
        System.out.println(" ");
        return bool;
    }


    public Time readTime(){
        System.out.print("> ");

        String input = SCANNER.nextLine();
        Time time = new Time(input);
        
        System.out.println(" ");
        return time;
    }
}