import java.io.*;
import java.util.Scanner;

public class Scores {

    private Scores() { }

    public static String getHighScore(int difficulty){
        String hs = "0:0:0";

        File input = new File("scores" + difficulty + ".txt");
        try {
            Scanner scanner = new Scanner(input);
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();

                if(isBetter(hs, line)){
                    hs = line;
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return hs;
    }

    public static boolean isBetter(String current, String line){  //is 'line' better than 'current'
        if(line.length() > 0 && Character.isDigit(line.charAt(0))){
            if(current.equals("0:0:0")){
                return true;
            }

            String[] parts = current.split(":");
            long t1 = Integer.parseInt(parts[0]) * 60000 + Integer.parseInt(parts[1]) * 1000 + Integer.parseInt(parts[1]);

            parts = line.split(":");
            long t2 = Integer.parseInt(parts[0]) * 60000 + Integer.parseInt(parts[1]) * 1000 + Integer.parseInt(parts[1]);

            return t2 < t1;
        }
        else {
            return false;
        }
    }

    public static void addScore(String score, int difficulty){
        try {
            File input = new File("scores" + difficulty + ".txt");
            Scanner scanner = new Scanner(input);
            PrintWriter output = new PrintWriter(new FileWriter("temp.txt"));

            output.write("Current session:\n");
            output.write(score + "\n");

            scanner.nextLine();
            while(scanner.hasNextLine()){
                output.write(scanner.nextLine() + "\n");
            }
            scanner.close();
            if(input.delete())
                System.out.println("Deleting old '" + input.getName() + "'");

            output.close();
            if(new File("temp.txt").renameTo(new File(input.getName())))
                System.out.println("Updated '" + input.getName() + "'");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveSession(int difficulty){
        try {
            File input = new File("scores" + difficulty + ".txt");
            Scanner scanner = new Scanner(input);

            scanner.nextLine(); //"Current..."
            String line = scanner.nextLine();
            if(line.equals("")){
                scanner.close();
                System.out.println("'" + input.getName() + "' was not updated.");
                return;
            }

            PrintWriter output = new PrintWriter(new FileWriter("temp.txt"));
            output.write("Current session:\n\n");
            output.write("Previous sessions:\n");

            while(true){
                if(line.equals("")){
                    if(!scanner.hasNextLine())
                        break;

                    line = scanner.nextLine();
                    if(line.equals("Previous sessions:")) {
                        if(!scanner.hasNextLine())
                            break;
                        line = scanner.nextLine();
                    }
                }
                output.write(line + "\n");

                if(!scanner.hasNextLine())
                    break;
                line = scanner.nextLine();
            }
            scanner.close();
            output.close();

            if(input.delete())
                System.out.println("Deleting old '" + input.getName());

            if(new File("temp.txt").renameTo(new File(input.getName())))
                System.out.println("Updated '" + input.getName() + "'");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
