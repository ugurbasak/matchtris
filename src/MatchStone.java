import java.applet.*;
import java.awt.*;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class MatchStone {

    private final int BALL_IMAGE_SIZE = Constants.CELL_SIZE; //if you want to use and image use its size like 6 for current image
    
    public int CellX;
    public int CellY;
    public int[] type;

    public MatchStone() {
        this.setNextCoordinates();
        this.initTypes();
    }

    private void setNextCoordinates() {
        CellX = 12;
        CellY = 5;
    }

    public void initCoordinates() {
        CellX = 4;
        CellY = -1 * Constants.BALL_LENGTH;//0;
    }

    private void initTypes() {
        type = new int[Constants.BALL_LENGTH];
        for (int i = 0; i < Constants.BALL_LENGTH; i++) {
            type[i] = getRandomColor();
        }
    }

    private int getRandomColor() {
        return getRandom() % Game.Level + 1;
    }

    private int getRandom() {
        return Math.abs( Utilities.random.nextInt() );
    }

    private BufferedImage getSprite(BufferedImage image, int index) {
        return image.getSubimage( (index-1) * BALL_IMAGE_SIZE, 0, BALL_IMAGE_SIZE, BALL_IMAGE_SIZE);
    }

    public void DrawShape(Graphics g, BufferedImage image, int cell_size) {
        for (int i = 0; i < Constants.BALL_LENGTH; i++) {
            if (this.CellY + i >= 0) {
                BufferedImage imagex = getSprite(image, this.type[i]);
                g.drawImage(imagex, Constants.startX + this.CellX * cell_size, Constants.startY + (this.CellY + i) * cell_size, cell_size, cell_size, null);
            }
        }
    }

    public void Rotate() {
        int temp[] = new int[Constants.BALL_LENGTH];
        for (int i = 0; i < Constants.BALL_LENGTH; i++) {
            temp[i] = this.type[(i + 1) % Constants.BALL_LENGTH];
        }
        System.arraycopy(temp, 0, this.type, 0, Constants.BALL_LENGTH);
    }
}
