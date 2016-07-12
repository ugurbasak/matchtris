import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Menu {

    public int currentmode = 0;
    public int menuPosition = 0;
    public int trans = 0;

    public Menu() { 
    }

    public void openMenu(int menumode) {
        currentmode = menumode;
    }

    public void draw(Graphics g, int mode) {
        this.currentmode = mode;
        g.setClip(0, 0, Constants.BOARD_WIDTH, Constants.BOARD_HEIGHT);
        int x = Constants.BOARD_WIDTH  / 2 - 50;
        int y = Constants.BOARD_HEIGHT / 2 - 10;
        int length = this.getLength(this.currentmode);
        g.setColor(Color.black);
        g.fillRect(x-5, y-20, 100, length * 15 + 10);
        for (int i = 0; i < length; i++) {
            if (i == menuPosition) g.setColor(Color.red);
            else g.setColor(Color.white);
            int val = this.getValue(this.currentmode, i); 
            String text = Constants.strMenu[val];
            if (val == 6 ) {
                text += " " + (Game.Level - 4);
            }
            g.drawString(text, x, y + i * 15);

            if (this.currentmode == 2) {
                g.setColor(Color.white);
                g.drawString("Do you want to", Constants.BOARD_WIDTH/3, Constants.BOARD_HEIGHT/3);
                g.drawString("save your game?", Constants.BOARD_WIDTH/3, Constants.BOARD_HEIGHT/3 + 20);
            }
        }
    }

    public int getValue(int mode, int index) {
        return Constants.MenuModes[mode][index];
    }

    public int getLength(int mode) {
        return Constants.MenuModes[mode].length;
    }

    public void navigate(String keyCode) {
        switch (keyCode) {
            case Constants.KEY_DOWN_ARROW:
                menuPosition++;
                menuPosition = menuPosition % this.getLength(this.currentmode);
                break;
            case Constants.KEY_UP_ARROW:
                menuPosition--;
                if (menuPosition < 0) menuPosition += this.getLength(this.currentmode);
                break;
            case Constants.KEY_LEFT_ARROW:
                if (this.getCommand() == 6 ) {
                    Game.Level--;
                    if (Game.Level <= 4) Game.Level = 8;
                }
                //store.setScore(Game.Level);
                break;
            case Constants.KEY_RIGHT_ARROW:
                if (this.getCommand() == 6 ) {
                    Game.Level++;
                    if (Game.Level >= 9) Game.Level = 5;
                }
                //store.setScore(Game.Level);
                break;
        }
    }

    public int getCommand() {
        return this.getValue(currentmode, menuPosition);
    }
}
