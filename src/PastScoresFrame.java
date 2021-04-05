import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class PastScoresFrame extends JFrame{
    public PastScoresFrame(int difficulty){
        setTitle("Past scores (" + difficulty + "x" + difficulty + ")");

        PastScoresPanel panel = new PastScoresPanel(difficulty);

        JScrollPane scrollPane = new JScrollPane(panel);
        add(scrollPane);


        setBounds(250, 200, 300, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        setResizable(false);
    }

    private static class PastScoresPanel extends JPanel {
        protected PastScoresPanel(int difficulty) {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBorder(new EmptyBorder(10, 0, 0, 0));

            File input = new File("scores" + difficulty + ".txt");
            try {
                Scanner scanner = new Scanner(input);

                while(scanner.hasNextLine()){
                    String line = scanner.nextLine();
                    JLabel toAdd = new JLabel(line);
                    toAdd.setAlignmentX(0.5f);

                    if(line.equals(""))
                        toAdd.setText(" ");

                    add(toAdd);
                }
                scanner.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            setVisible(true);
        }
    }
}
