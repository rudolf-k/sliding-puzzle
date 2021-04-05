//CONTROLLER
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class AppFrame extends JFrame {
    private final SidePanel sidePanel;
    private final Picture picture;

    private PictureFrame picFrame;
    private PastScoresFrame hScoreFrame;

    public AppFrame(){
        setLayout(null);
        setTitle("Sliding puzzle");

        picture = new Picture();
        GamePanel gamePanel = new GamePanel(picture);
        sidePanel = new SidePanel(gamePanel);
        add(gamePanel);
        add(sidePanel);

        setupMenuBar();

        setBounds(100, 100, picture.getSize()+200, picture.getSize() + 60);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        setResizable(false);

        addWindowListener(new WindowAdapter() { //Saves the score files on close.
            @Override
            public void windowClosing(WindowEvent e) {
                gamePanel.setGameState(0);
                sidePanel.updateScoreFiles();
                super.windowClosing(e);
            }
        });
    }

    private void setupMenuBar(){
        JMenuBar menuBar = new JMenuBar();

        JMenu menuFile = new JMenu("File");
        JMenuItem menuFileLoad = new JMenuItem("Load");
        menuFileLoad.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();

            if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
                try{
                    picture.setImg(ImageIO.read(fileChooser.getSelectedFile()));
                    sidePanel.imgLoaded();
                } catch (IOException exc){
                    exc.printStackTrace();
                }
            }
        });
        menuFile.add(menuFileLoad);

        JMenuItem menuFileExit = new JMenuItem("Exit");
        menuFileExit.addActionListener(e -> {
            sidePanel.updateScoreFiles();
            System.exit(0);
        });
        menuFile.add(menuFileExit);

        menuBar.add(menuFile);


        JMenu menuView = new JMenu("View");
        JMenuItem menuViewScores = new JMenuItem("Scores");
        menuViewScores.addActionListener(e -> {
            if(hScoreFrame != null)
                hScoreFrame.dispose();
            hScoreFrame = new PastScoresFrame(sidePanel.getSelectedDifficulty());
        });
        menuView.add(menuViewScores);

        JMenuItem menuViewFullImage = new JMenuItem("Full image");
        menuViewFullImage.addActionListener(e -> {
            if(picFrame != null)
                picFrame.dispose();
            picFrame = new PictureFrame(picture);
        });
        menuView.add(menuViewFullImage);
        menuBar.add(menuView);

        setJMenuBar(menuBar);
    }
}
