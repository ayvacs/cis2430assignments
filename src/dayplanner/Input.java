package dayplanner;


import java.util.Scanner;
import java.io.InputStream;


public class Input {
    private final Scanner SCANNER;


    public Input(InputStream in) {
        SCANNER = new Scanner(in);
    }


    /*private void clearNewline() {
        SCANNER.nextLine();
    }*/


    public int readInt(){
        System.out.print("> ");

        // from Defensive Programming Examples
        // int input = SCANNER.nextInt();
        int input = 0;
        String line;
        do {
            line = SCANNER.nextLine();
            if (line.matches("[-+]?[0-9]+")) {
                input = Integer.parseInt(line);
                break;
            } else {
                System.out.print("Invalid integer. Try again:\n> ");
            }
        } while (true);
        
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
        System.out.print("Y/N > ");

        char input;
        boolean ret = false;
        do {
            input = Character.toUpperCase(readString().charAt(0));
            if (input == 'Y') {
                ret = true;
                break;
            }
            else if (input == 'N')
                break;
            else
                System.out.print("Invalid boolean. Try typing Yes or No.\nY/N > ");
        } while (true);
        
        System.out.println(" ");
        return ret;
    }


    public Time readTime(){
        System.out.print("> ");

        String input = SCANNER.nextLine();
        Time time = new Time(input);
        
        System.out.println(" ");
        return time;
    }
}