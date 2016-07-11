import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Menu {

    public int currentmode = 0;
    public int menuPosition = 0; //Need to move menuPosition from Game.java to here
    public int trans = 0;

    public Menu() { 
    }

    public void openMenu(int menumode) {
        currentmode = menumode;
    }

    public void draw(Graphics g, int mode) {
        this.currentmode = mode;
        g.setClip(0, 0, Constants.BOARD_WIDTH, Constants.BOARD_HEIGHT);
        /*
        int xy[][][] = {
            {
                {
                    0, Board.BOARD_WIDTH, Board.BOARD_HEIGHT, 0
                }, {
                    32, 32, Board.BOARD_WIDTH, Board.BOARD_HEIGHT
                }
            }, {
                {
                    0, Board.BOARD_WIDTH, Board.BOARD_HEIGHT, 0
                }, {
                    0, 0, Board.BOARD_WIDTH, Board.BOARD_HEIGHT
                }
            }
        };*/

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
                g.drawString("Do you want to", 20, 10);
                g.drawString("save your game?", 20, 20);
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
        //default behavior is for gameover && GameMode=2
        // !gameover 3
        //menuPosition = (menuPosition + 1) % 2;
        // !gameover 2
        //case KEY_DOWN_ARROW:
        //menuPosition++;
        //menuPosition = menuPosition % this.getLength(1);
        //case KEY_UP_ARROW:
        //menuPosition--;
        //if (menuPosition < 0) menuPosition += this.getLength(1);
                                    
        switch (keyCode) {

            case Constants.KEY_DOWN_ARROW:
                menuPosition++;
                menuPosition = menuPosition % this.getLength(Game.Continue);
                break;

            case Constants.KEY_UP_ARROW:
                menuPosition--;
                if (menuPosition < 0) menuPosition += this.getLength(Game.Continue);
                break;

            case Constants.KEY_LEFT_ARROW:
                if (this.getValue(0, menuPosition) == 6) {
                    Game.Level--;
                    if (Game.Level <= 4) Game.Level = 8;
                }
                /*try {
                    rsMatchTris = RecordStore.openRecordStore("MatchTris",true);
                    SetRecord(rsMatchTris,LEVEL,Level);
                } catch (RecordStoreFullException e) {
                    e.printStackTrace();
                } catch (RecordStoreNotFoundException e) {
                    e.printStackTrace();
                } catch (RecordStoreException e) {
                    e.printStackTrace();
                } */

                break;

            case Constants.KEY_RIGHT_ARROW:
                if (this.getValue(0, menuPosition) == 6) {
                    Game.Level++;
                    //if(Level==9) Level=3;
                    if (Game.Level >= 9) Game.Level = 5;
                }
                /*try {
                    rsMatchTris = RecordStore.openRecordStore("MatchTris",true);
                    SetRecord(rsMatchTris,LEVEL,Level);
                } catch (RecordStoreFullException e) {
                    e.printStackTrace();
                } catch (RecordStoreNotFoundException e) {
                    e.printStackTrace();
                } catch (RecordStoreException e) {
                    e.printStackTrace();
                }*/
                break;
        }
    }

    public int getCommand() {
        return this.getValue(currentmode, menuPosition);
    }
}
