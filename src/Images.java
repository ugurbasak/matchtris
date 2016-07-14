import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Images {

    public BufferedImage balls = null;
    public BufferedImage border = null;
    public BufferedImage back = null;
    public BufferedImage splash = null;

    public Images() {
        this.init();
    }

    public BufferedImage getSprite(BufferedImage image, int index) {
        int image_size = Constants.CELL_SIZE;//6;
        return image.getSubimage((index-1)*image_size,0,image_size, image_size);
    }

    public void init() {
        System.out.println("Initialization started");
        if(this.back == null)
            this.back = loadBackground(); //LoadImage("images/back.png");
        if(this.balls == null)
            this.balls = loadBalls(); //LoadImage("images/balls.png");
        if(this.border == null)
            this.border = LoadImage("images/border.png");
        if(this.splash == null)
            this.splash = LoadImage("images/splash.png");
    }

    private BufferedImage loadBalls() {
        BufferedImage image = new BufferedImage(Constants.CELL_SIZE * Constants.MAX_BALLS, Constants.CELL_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D)image.getGraphics();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f));
        g.fillRect(0,0,Constants.CELL_SIZE * Constants.MAX_BALLS, Constants.CELL_SIZE);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        //If there will be more than 8 balls, this code must be modified
        Color[] colors = {Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN, Color.ORANGE, Color.MAGENTA, Color.CYAN, Color.BLACK };
        for(int i=0; i<Constants.MAX_BALLS; i++) {
            int x = Constants.CELL_SIZE * i;
            int y = 0;
            int diameter = Constants.CELL_SIZE;
            if(i < 8)
                g.setColor(colors[i]);
            else
                g.setColor(new Color(  (float) (Utilities.random.nextFloat() / 2f + 0.5), (float) (Utilities.random.nextFloat() / 2f + 0.5), (float)(Utilities.random.nextFloat() / 2f + 0.5)) );
            Ellipse2D.Double circle = new Ellipse2D.Double(x, y, diameter, diameter);
            g.fill(circle);
        }
        return image;
    }

    private BufferedImage loadBackground() {
        BufferedImage image = new BufferedImage(Constants.CELL_SIZE, Constants.CELL_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D)image.getGraphics();
        int step = 256 / Constants.CELL_SIZE;
        for(int i=0; i<Constants.CELL_SIZE; i++) {
            int val = step * i;
            g.setColor(new Color(val, val, val, 128));
            g.drawLine(0, i, Constants.CELL_SIZE, i); 
        }
        return image;
    }

    private BufferedImage LoadImage(String str) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(str));
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        System.out.println("Load Image for " + str);
        return img; //UBASAK find a way to return a real image
    }
}
