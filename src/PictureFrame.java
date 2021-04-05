import javax.swing.*;
import java.awt.*;

public class PictureFrame extends JFrame {
    public PictureFrame(Picture pic){
        setLayout(null);
        setTitle("Full image");

        PicturePanel panel = new PicturePanel(pic);
        add(panel);

        setBounds(250, 200, pic.getSize(), pic.getSize());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        setResizable(false);
    }

    private static class PicturePanel extends JPanel{
        Picture picture;

        protected PicturePanel(Picture p){
            this.picture = p;

            setLayout(null);
            setBounds(0, 0, picture.getSize(), picture.getSize());
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(picture.getImg(), 0, 0, null);
        }
    }
}
