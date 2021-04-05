import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class SidePanel extends JPanel {
    private final GamePanel gamePanel;
    private TimeController timeController;

    private JLabel currentScoreL;
    private JLabel lastScoreL;
    private JLabel hsTextL;
    private JLabel hsScoreL;

    private JLabel diff;
    private JRadioButton buttonEasy;
    private JRadioButton buttonNormal;
    private JRadioButton buttonMedium;
    private JRadioButton buttonHard;
    private JRadioButton buttonExpert;
    private JRadioButton buttonInsane;

    private JButton buttonStart;
    private JButton buttonReset;

    private Clip soundtrack;

    public SidePanel(GamePanel gamePanel){
        this.gamePanel = gamePanel;
        setLayout(null);
        setBounds(800, 0, 200, 800);

        setup();
    }

    private void setup(){
        setupScoreUI();
        setupDifficultyButtons();
        setupStartResetButtons();
        updateScoreText();
        setupAudio();

        timeController = new TimeController(this.gamePanel, this, currentScoreL);
    }

    private void setupScoreUI(){
        JLabel timeL = new JLabel("Time:");
        timeL.setFont(new Font("Arial", Font.BOLD, 15));
        timeL.setBounds(10, 40, 100, 50);
        currentScoreL = new JLabel("0:0:0");
        currentScoreL.setFont(new Font("Arial", Font.PLAIN, 14));
        currentScoreL.setBounds(80, 40, 100, 50);
        add(timeL);
        add(currentScoreL);

        JLabel lastTimeL = new JLabel("Last score:");
        lastTimeL.setBounds(10, 100, 100, 50);
        lastScoreL = new JLabel("0:0:0");
        lastScoreL.setBounds(125, 101, 100, 50);
        lastScoreL.setFont(new Font("Arial", Font.PLAIN, lastScoreL.getFont().getSize()));
        add(lastTimeL);
        add(lastScoreL);

        hsTextL = new JLabel("Highscore(4x4):");
        hsTextL.setBounds(10, 125, 140, 50);
        hsScoreL = new JLabel();
        hsScoreL.setBounds(125, 126, 100, 50);
        hsScoreL.setFont(new Font("Arial", Font.PLAIN, hsScoreL.getFont().getSize()));
        add(hsTextL);
        add(hsScoreL);
    }

    private void setupDifficultyButtons(){
        diff = new JLabel("DIFFICULTY");
        diff.setBounds(10, 250, 100, 50);
        add(diff);

        buttonEasy = new JRadioButton("Easy (3x3)");
        buttonNormal = new JRadioButton("Normal (4x4)");
        buttonMedium = new JRadioButton("Medium (5x5)");
        buttonHard = new JRadioButton("Hard (6x6)");
        buttonExpert = new JRadioButton("Expert (7x7)");
        buttonInsane = new JRadioButton("Insane (10x10)");
        buttonEasy.setBounds(10, 300, 200, 30);
        buttonNormal.setBounds(10, 330, 200, 30);
        buttonMedium.setBounds(10, 360, 200, 30);
        buttonHard.setBounds(10, 390, 200, 30);
        buttonExpert.setBounds(10, 420, 200, 30);
        buttonInsane.setBounds(10, 450, 200, 30);
        buttonEasy.addActionListener(e -> updateScoreText());
        buttonNormal.addActionListener(e -> updateScoreText());
        buttonMedium.addActionListener(e -> updateScoreText());
        buttonHard.addActionListener(e -> updateScoreText());
        buttonExpert.addActionListener(e -> updateScoreText());
        buttonInsane.addActionListener(e -> updateScoreText());

        ButtonGroup diffButtons = new ButtonGroup();
        diffButtons.add(buttonEasy);
        diffButtons.add(buttonNormal);
        diffButtons.add(buttonMedium);
        diffButtons.add(buttonHard);
        diffButtons.add(buttonExpert);
        diffButtons.add(buttonInsane);

        add(buttonEasy);
        add(buttonNormal);
        add(buttonMedium);
        add(buttonHard);
        add(buttonExpert);
        add(buttonInsane);

        buttonNormal.setSelected(true);
    }

    private void setupStartResetButtons(){
        buttonStart = new JButton("Start");
        buttonStart.setBounds(30, 530, 100, 30);
        add(buttonStart);
        buttonStart.setEnabled(false);
        buttonStart.addActionListener(e -> {
            gamePanel.startGame(getSelectedDifficulty());
            setDifficultyEnabled(false);
            buttonStart.setEnabled(false);

            new Thread(timeController).start();
        });

        buttonReset = new JButton("Reset");
        buttonReset.setBounds(40, 570, 80, 20);
        add(buttonReset);
        buttonReset.setEnabled(false);
        buttonReset.addActionListener(e -> {
            gamePanel.setGameState(1);
            settingsPhase();
        });
    }

    private void setupAudio(){
        AudioInputStream inputStream;

        try {
            inputStream = AudioSystem.getAudioInputStream(new File("Audio/soundtrack.wav"));
            soundtrack = AudioSystem.getClip();
            soundtrack.open(inputStream);
            soundtrack.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }

        setupAudioUI();
    }

    private void setupAudioUI(){
        JLabel soundtrackL = new JLabel("Music:");
        soundtrackL.setBounds(10, 630, 100, 30);
        add(soundtrackL);

        JCheckBox soundtrackCheck = new JCheckBox();
        soundtrackCheck.setBounds(100, 630, 200, 30);
        soundtrackCheck.setSelected(true);
        soundtrackCheck.addActionListener(e -> {
            if(soundtrackCheck.isSelected())
                soundtrack.loop(Clip.LOOP_CONTINUOUSLY);
            else
                soundtrack.stop();
        });
        add(soundtrackCheck);


        JLabel soundL = new JLabel("Sound Effects:");
        soundL.setBounds(10, 655, 100, 30);
        add(soundL);

        JCheckBox soundCheck = new JCheckBox();
        soundCheck.setBounds(100, 655, 200, 30);
        soundCheck.setSelected(true);
        soundCheck.addActionListener(e -> gamePanel.setSound(soundCheck.isSelected()));
        add(soundCheck);
    }

    public void imgLoaded(){
        buttonReset.setEnabled(true);
        gamePanel.setGameState(1);
        settingsPhase();
    }

    private void updateScoreText(){
        hsTextL.setText("Highscore(" + getSelectedDifficulty() + "x" + getSelectedDifficulty() + "):");
        hsScoreL.setText(Scores.getHighScore(getSelectedDifficulty()));
    }

    private void setDifficultyEnabled(boolean b){
        diff.setEnabled(b);
        buttonEasy.setEnabled(b);
        buttonNormal.setEnabled(b);
        buttonMedium.setEnabled(b);
        buttonHard.setEnabled(b);
        buttonExpert.setEnabled(b);
        buttonInsane.setEnabled(b);
    }

    public int getSelectedDifficulty(){
        if(buttonEasy.isSelected())
            return 3;
        if(buttonNormal.isSelected())
            return 4;
        if(buttonMedium.isSelected())
            return 5;
        if(buttonHard.isSelected())
            return 6;
        if(buttonExpert.isSelected())
            return 7;
        if(buttonInsane.isSelected())
            return 10;

        return 0;
    }

    public void settingsPhase(){
        gamePanel.repaint();
        setDifficultyEnabled(true);
        buttonStart.setEnabled(true);
        currentScoreL.setText("0:0:0");
    }

    public void finishPhase(){
        Scores.addScore(currentScoreL.getText(), getSelectedDifficulty());
        lastScoreL.setText(currentScoreL.getText());
        hsScoreL.setText(Scores.getHighScore(getSelectedDifficulty()));
    }

    public void updateScoreFiles(){
        for(int i = 3; i <= 7; i++)
            Scores.saveSession(i);

        Scores.saveSession(10);
    }
}
