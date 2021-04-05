//VIEW
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

public class GamePanel extends JPanel {
    private final Picture picture;
    private int n, step;
    private int startX, startY;

    private final int nrOfSwaps = 100;  //nrOfSwaps MUST BE EVEN for the puzzle to be solvable

    Vector<Vector<Integer>> cells;

    private int gameState = 0;  //0 - no img loaded;    1 - img loaded;     2 - game;       3 - game finished;

    private boolean sound = true;
    private Clip moveSound;
    private Clip finishSound;

    public GamePanel(Picture p){
        picture = p;
        setBounds(0, 0, picture.getSize(), picture.getSize());

        setupSoundFX();
    }

    public void setGameState(int state){
        this.gameState = state;
    }
    public int getGameState(){
        return this.gameState;
    }

    public void setSound(boolean sound) {
        this.sound = sound;
    }

    private void setupSoundFX(){
        AudioInputStream inputStream;

        try {
            inputStream = AudioSystem.getAudioInputStream(new File("Audio/move.wav"));
            moveSound = AudioSystem.getClip();
            moveSound.open(inputStream);

            inputStream = AudioSystem.getAudioInputStream(new File("Audio/finish.wav"));
            finishSound = AudioSystem.getClip();
            finishSound.open(inputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void startGame(int difficulty){
        gameState = 2;
        this.n = difficulty;
        this.step = picture.getSize() / n;
        picture.dissectImg(n);

        generatePuzzle();
        repaint();
        gamePhase();
    }

    private void generatePuzzle(){
        ArrayList<Integer> randomizer = new ArrayList<>();
        for(int i = 0; i < (n*n-1); i++)
            randomizer.add(i);

        Random rand = new Random();
        for(int i = 0; i < nrOfSwaps; i++){
            int a = rand.nextInt(n*n-1);
            int b = a;
            while(b == a)
                b = rand.nextInt(n*n-1);

            int t = randomizer.get(a);
            randomizer.set(a, randomizer.get(b));
            randomizer.set(b, t);
        }

        cells = new Vector<>();
        cells.setSize(n);
        int next = 0;
        for(int i = 0; i < n; i++){
            cells.set(i, new Vector<>());
            cells.get(i).setSize(n);
            for(int j = 0; j < n; j++){
                if(i == n-1 && j == n-1)
                    cells.get(i).set(j, -1);
                else{
                    cells.get(i).set(j, randomizer.get(next));
                    next++;
                }
            }
        }
    }

    private void gamePhase(){
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mouseClicked(e);

                startX = e.getX();
                startY = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);

                Point start = coordToCell(new Point(startX, startY));
                Point dest = coordToCell(new Point(e.getX(), e.getY()));

                if(validMove(start, dest)){
                    swapCells(start, dest);
                    repaint();

                    if(sound) {
                        moveSound.setMicrosecondPosition(0);
                        moveSound.start();
                    }

                    if(gameOver()){
                        gameState = 3;

                        moveSound.setMicrosecondPosition(0);
                        finishSound.start();
                    }
                }

                startX = -1;
                startY = -1;
            }
        });
    }

    private boolean validMove(Point start, Point dest){
        if(gameState != 2)      //not in game phase
            return false;

        if(cells.get(dest.x).get(dest.y) != -1) //not empty
            return false;

        if(start.x < 0 || start.y < 0)
            return false;

        if(Math.abs(start.x - dest.x) == 1){    //Up / Down
            if(Math.abs(start.y - dest.y) > 0)
                return false;
        }

        if(Math.abs(start.y - dest.y) == 1){    //Left / Right
            if(Math.abs(start.x - dest.x) > 0)
                return false;
        }

        return true;
    }

    private Point coordToCell(Point p){     //x = i     y = j
        if(p.x < 0 || p.y < 0)
            return new Point(-1, -1);

        return new Point(p.y / step, p.x / step);
    }

    private void swapCells(Point x, Point y){
        int a = cells.get(x.x).get(x.y);
        int b = cells.get(y.x).get(y.y);
        cells.get(x.x).set(x.y, b);
        cells.get(y.x).set(y.y, a);
    }

    private boolean gameOver(){
        //////
        //    for(int i = 0; i < n; i++)
        //        System.out.println(cells.get(i));
        //    System.out.println();
        //////

        int expectedVal;
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                expectedVal = (i+j) + i*(n-1);
                if(i == n-1 && j == n-1)
                    expectedVal = -1;
                if(cells.get(i).get(j) != expectedVal) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(gameState == 0)
            return;
        if(gameState == 1)
            g.drawImage(picture.getImg(), 0, 0, null);
        else if(gameState > 1){
            for(int i = 0; i < n; i++){
                for(int j = 0; j < n; j++){
                    if(cells.get(i).get(j) != -1){
                        g.drawImage(picture.getImgAtCell(cells.get(i).get(j)), j*step, i*step, null);
                    }
                    else{
                        g.setColor(new Color(238, 238, 238));
                        g.drawRect(j*step, i*step, step, step);
                    }
                }
            }
        }
    }
}
