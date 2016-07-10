import java.applet.*;
import java.awt.*;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class MatchStone {

    public int CellX;
    public int CellY;

    public int[] type;

    public MatchStone() {
        CellX = 4;
        CellY = 0;
        type = new int[3];
        for (int i = 0; i < 3; i++)
            type[i] = Math.abs(Game.random.nextInt()) % Game.Level + 1;
    }

    private BufferedImage getSprite(BufferedImage image, int index) {
        //System.out.println("Index is " + index);
        int image_size = 6;
        return image.getSubimage((index-1)*image_size,0,image_size, image_size);
    }

    public void DrawShape(Graphics g, BufferedImage image, int cell_size) {
        for (int i = 0; i < 3; i++) {
            if (this.CellY + i >= 0) {
                BufferedImage imagex = getSprite(image, this.type[i]);
                g.drawImage(imagex, Game.startX+this.CellX*cell_size, Game.startY + (this.CellY + i) * cell_size, cell_size, cell_size, null);
            }
            //else System.out.println("Dont draw "+(this.CellY+i));
        }
    }

    public void Rotate() {
        int temp[] = new int[3];
        for (int i = 0; i < 3; i++)
            temp[i] = this.type[(i + 1) % 3];
        System.arraycopy(temp, 0, this.type, 0, 3);
    }
}
