import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Menu {
    private String[] strMenu = {
        "New Game", "Continue", "Finish Game",
        "Help", "About", "Exit", "Level", "YES", "NO"
    };
    private int[][] MenuModes = {
        {
            0, 6, 3, 4, 5
        }, {
            1, 2, 3, 5
        }, {
            7, 8
        }
    };

    public int currentmode = 0;
    public int menuPosition = 0; //Need to move menuPosition from Game.java to here
    public boolean isEnabled = false;
    public boolean isFull = false;
    public int trans = 0;
    //private List myList;
    //private Form myForm;
    //private StringItem myStringItem;
    //private Command cmdBack;
    //private Command cmdMenu;
    //private Command cmdOk;
    //private MatchTris myMidlet;

    public Menu( /*MatchTris myMidlet*/ ) {
        //this.myMidlet=myMidlet;
        //cmdOk=new Command("Tamam",Command.SCREEN,1);
        //cmdMenu=new Command("Menu",Command.BACK,1);
        //cmdBack=new Command("Geri",Command.BACK,1);
    }

    public void openMenu(int menumode) {

        currentmode = menumode;

        switch (menumode) {

            case 0:
                /* Must be a menu but it can be re-implemented
                myList = null;
                myList = new List("Shoot Me", List.IMPLICIT);
                myList.append("Yeni oyun", null);
                myList.append("Ayarlar", null);
                myList.append("Yardim", null);
                myList.append("Test", null);
                myList.append("Kapat", null);
                myList.addCommand(cmdOk);
                myList.setCommandListener(this);
                Display.getDisplay(myMidlet).setCurrent(myList);
            */
                break;
        }
    }

    public void commandAction( /*Command c, Displayable d*/ ) {

        /*      if(c==cmdOk){
                    switch(currentmode){
                        case 0:
                            if(myList.getSelectedIndex()==0){
                                
                            }
                            else if(myList.getSelectedIndex()==1){
                                
                            }
                            else if(myList.getSelectedIndex()==2){
                                
                            }
                            else if(myList.getSelectedIndex()==3){
                                
                            }
                            else if(myList.getSelectedIndex()==4){
                                
                            }
                        break;
                    }
                }else if(c==cmdBack){
                    
                }else if(c==cmdMenu){
                    openMenu(0);
                } */
    }

    public void draw(Graphics g) {
        if( !this.isEnabled ) {
            return;
        }
        //g.setFont(font);
        g.setClip(0, 0, Board.BOARD_WIDTH, Board.BOARD_HEIGHT);

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
        };

        for (int i = 0; i < MenuModes[this.currentmode].length; i++) {
            if (i == menuPosition) g.setColor(Color.red);
            else g.setColor(Color.white);
            if (MenuModes[this.currentmode][i] == 6) {
                //int x = font.stringWidth(strMenu[MenuModes[MenuMode][i]]+" "+(Level-4));  
                int x = 10; //UBASAK
                g.drawString(strMenu[MenuModes[this.currentmode][i]] + " " + (Game.Level - 4), (128 - x) / 2, 40 + i * 15);

            } else {
                int x = 10; //UBASAK
                //int x = font.stringWidth(strMenu[MenuModes[MenuMode][i]]);    
                g.drawString(strMenu[MenuModes[this.currentmode][i]], (128 - x) / 2, 40 + i * 15);
            }

            if (this.currentmode == 2) {
                g.setColor(Color.white);
                g.drawString("Do you want to", 20, 10);
                g.drawString("save your game?", 20, 20);
            }
        }

        this.isEnabled = false;
    }

    public void draw(int menuPosition, int mode, boolean full, int trans) {
        this.currentmode = mode;
        this.isFull = full;
        this.trans = trans;
        this.menuPosition = menuPosition;
        this.isEnabled = true;
    }
    
    public int getValue(int mode, int index) {
        return MenuModes[mode][index];
    }

    public int getLength(int mode) {
        return MenuModes[mode].length;
    }
}
