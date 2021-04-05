//MODEL
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Vector;

public class Picture {
    private final int imgSize = 800;
    private BufferedImage picture;

    private Vector<Vector<BufferedImage>> cells;
    private int n;

    public int getSize(){
        return imgSize;
    }

    public void setImg(BufferedImage imgToLoad){
        if(imgToLoad.getWidth() > imgToLoad.getHeight()){   //landscape pic
            int cropWidth = (imgSize * imgToLoad.getHeight()) / imgSize;
            BufferedImage subImg = imgToLoad.getSubimage((imgToLoad.getWidth() - cropWidth) / 2, 0, cropWidth, imgToLoad.getHeight());

            BufferedImage resized = new BufferedImage(imgSize, imgSize, subImg.getType());
            Graphics2D g = resized.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(subImg, 0, 0, imgSize, imgSize, 0, 0, subImg.getWidth(), subImg.getHeight(), null);
            g.dispose();

            picture = resized;
        }
        else{   //portrait pic
            int cropHeight = (imgSize * imgToLoad.getWidth()) / imgSize;
            BufferedImage subImg = imgToLoad.getSubimage(0, (imgToLoad.getHeight() - cropHeight) / 2, imgToLoad.getWidth(), cropHeight);

            BufferedImage resized = new BufferedImage(imgSize, imgSize, subImg.getType());
            Graphics2D g = resized.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(subImg, 0, 0, imgSize, imgSize, 0, 0, subImg.getWidth(), subImg.getHeight(), null);
            g.dispose();

            picture = resized;
        }
    }

    public void dissectImg(int n){
        this.n = n;
        int step = imgSize/n;
        cells = new Vector<>();

        cells.setSize(n);
        for(int i = 0; i < n; i++){
            cells.set(i, new Vector<>());
            cells.get(i).setSize(n);
            for(int j = 0; j < n; j++){
                cells.get(i).set(j, picture.getSubimage(j*step, i*step, step, step));
            }
        }
    }

    public BufferedImage getImgAtCell(int index){
        int i = index / n;
        int j = index % n;
        return cells.get(i).get(j);
    }

    public BufferedImage getImg(){
        return picture;
    }
}
