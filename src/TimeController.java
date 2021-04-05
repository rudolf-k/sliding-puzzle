import javax.swing.*;

public class TimeController implements Runnable {
    private final GamePanel gamePanel;
    private final SidePanel sidePanel;
    private final Stopwatch stopwatch;
    private final JLabel time;

    public TimeController(GamePanel gp, SidePanel sp, JLabel t){
        stopwatch = new Stopwatch();
        gamePanel = gp;
        sidePanel = sp;
        time = t;
    }

    @Override
    public void run() {
        stopwatch.start();
        while(gamePanel.getGameState() == 2){
            time.setText(stopwatch.getElapsedTimeMinutes() + ":" + stopwatch.getElapsedTimeSeconds() + ":" + stopwatch.getElapsedTimeMillis());
        }
        if(gamePanel.getGameState() == 3){
            sidePanel.finishPhase();
        }
    }
}
