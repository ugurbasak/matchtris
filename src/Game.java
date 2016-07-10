import java.util.Random;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
import java.awt.Image;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.awt.image.BufferedImage;

public class Game extends JPanel implements Runnable {

    private final int B_WIDTH = 350;
    private final int B_HEIGHT = 350;
    private final int DELAY = 25;
    private Thread animator;


    public final String KEY_LEFT_ARROW = "VK_LEFT";
    public final String KEY_RIGHT_ARROW = "VK_RIGHT";
    public final String KEY_DOWN_ARROW = "VK_DOWN";
    public final String KEY_UP_ARROW = "VK_UP";
    public final String KEY_SOFTKEY1 = "VK_ENTER";
    public final String KEY_SOFTKEY2 = "VK_SPACE";

    public void initGame() {
        System.out.println("Game::InitGame");
        setKeyBindings();
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        setDoubleBuffered(true);
    }

    private void setKeyBindings() {
        ActionMap actionMap = getActionMap();
        int condition = JComponent.WHEN_IN_FOCUSED_WINDOW;
        InputMap inputMap = getInputMap(condition);

        //Use the constants KEY_KEFT_ARROW ...
        String vkLeft = "VK_LEFT";
        String vkRight = "VK_RIGHT";
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), vkLeft);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), vkRight);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "VK_UP");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "VK_DOWN");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "VK_ENTER");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "VK_SPACE");

        actionMap.put(vkLeft, new KeyAction(vkLeft));
        actionMap.put(vkRight, new KeyAction(vkRight));
        actionMap.put("VK_UP", new KeyAction("VK_UP"));
        actionMap.put("VK_DOWN", new KeyAction("VK_DOWN"));
        actionMap.put("VK_ENTER", new KeyAction("VK_ENTER"));
        actionMap.put("VK_SPACE", new KeyAction("VK_SPACE"));
    }

    @
    Override
    public void addNotify() {
        super.addNotify();

        animator = new Thread(this);
        animator.start();
    }

    @
    Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        myPaint(g, notall);
    }

    private void cycle() {
        //Any updates
    }

    @
    Override
    public void run() {

        long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();

        while (true) {

            cycle();
            repaint();

            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = DELAY - timeDiff;

            if (sleep < 0) {
                sleep = 2;
            }

            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                System.out.println("Interrupted: " + e.getMessage());
            }

            beforeTime = System.currentTimeMillis();
        }
    }


    private Font font;
    //private MatchTris myMidlet;
    public static int GameMode = 1;
    private Board board = null;


    

    private long lastDraw;
    private final long speed = 250;

    public MatchStone matchstone = null;

    private boolean notall = true;

    //private DirectGraphics dg;
    int[] transparency = {
        0xcc2C3A90, 
        0x332C3A90
    };
    public boolean Key = false;
    private String action = "VK_LEFT";
    private int KeyMove = 0;
    public static boolean falling = false;
    private int falling_times = 0;

    public static boolean GAMEOVER = true;
    public static int Level = 6;
    public static int puan = 0;

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
    private long timePressed = 0; //about when a key is pressed or released
    public static int flashing = 0;
    private int menuPosition = 0;

    private int[] HighScores;
    private int Continue = 0;

    //private RecordStore rsMatchTris = null;


    //private final int NoOfRecords = 11; //toplma record store kay?d?
    //1 tane contiue 20x10 tanede save game icin toplam 11 + 201 = 212tane
    /*RecordStorelar?n final int isimleri olsun sonra degistirirsin istersen*/
    //private final int NoOfRecords = 212; //toplma record store kay?d?
    //MatchStone ile 215 oldu + 2 CellX ve CellY
    //private final int NoOfRecords = 217; //toplma record store kay?d?
    private final int NoOfRecords = 218; //toplma record store kay?d?
    private final int LEVEL = 1, HIGHSCORES = 2, ISCONTINUE = 12, LOADTHEM = 13;
    private int transCell = 0; //Oyun bitince ekran? transparan yapacak

    public Game() {
        initGame();
        font = new Font("TimesRoman", Font.PLAIN, 12);
        board = new Board();
        //Initialization
        HighScores = new int[10];
        lastDraw = System.currentTimeMillis();

        /*
        Loading and saving game can be impelemented later   
                try {
                    //about record store
                    byte[] dummy = "0".getBytes();
                    rsMatchTris = RecordStore.openRecordStore("MatchTris",true);
                    if(rsMatchTris.getNumRecords() != NoOfRecords){
                        rsMatchTris.closeRecordStore();
                        RecordStore.deleteRecordStore("MatchTris");
                        rsMatchTris = RecordStore.openRecordStore("MatchTris",true);
                        for(int i=0; i<NoOfRecords; i++)
                            rsMatchTris.addRecord(dummy,0,dummy.length);
                        System.out.println("Added");
                    }
                    else{
                        //load i?lemlerini yap burada
                        Level = GetIntRecord(rsMatchTris,LEVEL);
                        for(int i=0; i<10; i++)
                            HighScores[i] = GetIntRecord(rsMatchTris,HIGHSCORES+i);
                        System.out.println("Level "+Level);
                        for(int i=0; i<10; i++)
                            System.out.println("HighScores "+HighScores[i]);
                        Continue = GetIntRecord(rsMatchTris,ISCONTINUE);
                    }
                    System.out.println("You have "+rsMatchTris.getNumRecords()+" records");
                    rsMatchTris.closeRecordStore();
                } catch (RecordStoreFullException e) {
                    e.printStackTrace();
                } catch (RecordStoreNotFoundException e) {
                    e.printStackTrace();
                } catch (RecordStoreException e) {
                    e.printStackTrace();
                } */
        //}


        //if(dg==null) {
        //  dg=DirectUtils.getDirectGraphics(g);
        //}

        //g.setFont(font); UBASAK
    }

    /* This code looks like old or new keypress code 
            if(!GAMEOVER){
                switch(GameMode){
                    case 0:
                    myPaint(g,notall);
                    if(Key){
                        switch(action) {
                        }
                    }
                    break;

                    
                    case 2:
                        DrawMenu(g,1,true,0);
                    break;
                    
                    case 3:
                    //asd
                        DrawMenu(g,2,true,0);
                    break;

                }               
            }
            else{
                switch(GameMode){
                    //SPLASH
                    case 1:
                        g.drawImage(splash,0,0);
                    break;
                    //Main menu
                    case 2:
                        g.drawImage(splash,0,0);
                        if(Continue==1){
                            DrawMenu(g,1,false,0);
                        }
                        else{
                            DrawMenu(g,0,false,0);
                        }
                    break;
                    
                    case 0:
                        drawAll(g);
                        int xy[][] ={ {startX,startX+6*10,startX+6*10,startX},
                                      {128-startY-transCell*6,128-startY-transCell*6,128-startY,128-startY} };
                        dg.fillPolygon(xy[0],0,xy[1],0,4,0x88888888);
                        //g.drawString("GAME OVER",0,60);
                        if(transCell!=20)
                            transCell++;
                        else{
                            g.setColor(0x000000);
                            g.drawString("GAME OVER",0,60);
                        }
                    break;
                }
            }
        } */

    private class KeyAction extends AbstractAction {
        public KeyAction(String actionCommand) {
            putValue(ACTION_COMMAND_KEY, actionCommand);
        }

        @
        Override
        public void actionPerformed(ActionEvent actionEvt) {
                String keyCode = actionEvt.getActionCommand();
                System.out.println(GameMode + " - " + GAMEOVER + " - " + keyCode + " pressed");
                action = keyCode;
                if (!GAMEOVER) {
                    if (timePressed == 0)
                        timePressed = System.currentTimeMillis();
                    switch (GameMode) {
                        case 0:
                            switch (keyCode) {
                                case KEY_LEFT_ARROW:
                                    if ((System.currentTimeMillis() - timePressed) >= 40) {
                                        board.move(-1, matchstone);
                                        notall = false;
                                        repaint();
                                        notall = true;
                                        timePressed = System.currentTimeMillis();
                                    }
                                    break;

                                case KEY_RIGHT_ARROW:
                                    if ((System.currentTimeMillis() - timePressed) >= 40) {
                                        board.move(1, matchstone);
                                        notall = false;
                                        repaint();
                                        notall = true;
                                        timePressed = System.currentTimeMillis();
                                    }
                                    break;

                                case KEY_DOWN_ARROW:
                                    if ((System.currentTimeMillis() - timePressed) >= 10) {
                                        board.move(0, matchstone);
                                        notall = false;
                                        repaint();
                                        notall = true;
                                        timePressed = System.currentTimeMillis();
                                    }
                                    break;
                                case KEY_UP_ARROW:
                                    matchstone.Rotate();
                                    notall = false;
                                    repaint();
                                    notall = true;
                                    break;
                                case KEY_SOFTKEY2:
                                    //System.out.println("Oyun durduruldu!");
                                    //SaveGame();
                                    GameMode = 2;
                                    break;
                                default:
                                    Key = true;
                                    KeyMove = 0;
                                    break;
                            }
                            break;
                            //Game is going on but flashing cause of Letting it down!
                        case 1:
                            //Need to implement this againg UBASAK
                            /*
                    drawAll(g);
                    if(flashing%2==0){
                        for(int i=0; i<10; i++){
                            for(int j=0; j<20; j++){
                                if(TBTest[i][j]==1){
                                    g.setClip(0,0,BOARD_WIDTH,BOARD_HEIGHT);
                                    g.drawImage(back,i*6+startX,j*6+startY);
                                }               
                            }
                        }
                    } */
                            if (++flashing == 10) {
                                GameMode = 0;
                                board.LetItDown();
                                falling = false;
                                board.CheckIsFull();
                            }
                            break;
                        case 2:
                            switch (keyCode) {
                                case KEY_DOWN_ARROW:
                                    menuPosition++;
                                    menuPosition = menuPosition % MenuModes[1].length;
                                    break;

                                case KEY_UP_ARROW:
                                    menuPosition--;
                                    if (menuPosition < 0) menuPosition += MenuModes[1].length;
                                    break;

                                case KEY_SOFTKEY1:
                                    if (menuPosition == 0) {
                                        System.out.println("Continue");
                                        GAMEOVER = false;
                                        GameMode = 0;
                                    } else if (menuPosition == 1) {
                                        board.Score();
                                        GAMEOVER = true;
                                        GameMode = 2;
                                        //Continue=0;
                                    } else if (menuPosition == 3) {
                                        /*  try {
                                                myMidlet.destroyApp(true);
                                            } catch (MIDletStateChangeException e) {
                                                e.printStackTrace();
                                            }*/
                                        GameMode = 3;
                                        menuPosition = 0;
                                    }
                            }
                            break;

                        case 3:
                            //System.out.println("menuPosition="+menuPosition);
                            switch (keyCode) {
                                case KEY_DOWN_ARROW:
                                    menuPosition = (menuPosition + 1) % 2;
                                    break;
                                case KEY_UP_ARROW:
                                    menuPosition = (menuPosition + 1) % 2;
                                    break;
                                case KEY_SOFTKEY1:
                                    /*try {
                                        if(menuPosition==0) myMidlet.destroyApp(true);
                                        else                myMidlet.destroyApp(false);
                                    } catch (MIDletStateChangeException e) {
                                        e.printStackTrace();
                                    }*/
                                    //UBASAK
                                    break;
                                    //}
                            }
                            break;
                    }
                } else { //GAMEOVER = true
                    switch (GameMode) {
                        //SPLASH
                        case 1:
                            GameMode = 2;
                            break;

                            //Main menu
                        case 2:
                            switch (keyCode) {
                                case KEY_DOWN_ARROW:
                                    menuPosition++;
                                    //menuPosition=menuPosition%MenuModes[0].length;
                                    menuPosition = menuPosition % MenuModes[Continue].length;
                                    break;

                                case KEY_UP_ARROW:
                                    menuPosition--;
                                    //if(menuPosition<0) menuPosition+=MenuModes[0].length;
                                    if (menuPosition < 0) menuPosition += MenuModes[Continue].length;
                                    break;

                                case KEY_LEFT_ARROW:
                                    if (MenuModes[0][menuPosition] == 6) {
                                        Level--;
                                        if (Level <= 4) Level = 8;
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

                                case KEY_RIGHT_ARROW:
                                    if (MenuModes[0][menuPosition] == 6) {
                                        Level++;
                                        //if(Level==9) Level=3;
                                        if (Level >= 9) Level = 5;
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

                                case KEY_SOFTKEY1:
                                    if (MenuModes[Continue][menuPosition] == 5) { //EXIT
                                        //try {
                                        //  myMidlet.destroyApp(true);
                                        //} catch (MIDletStateChangeException e1) {
                                        //  e1.printStackTrace();
                                        //}
                                        //UBASAK
                                    } else if (MenuModes[Continue][menuPosition] == 3) { //HELP

                                    } else if (MenuModes[Continue][menuPosition] == 0) { //NEW GAME
                                        System.out.println("Starts");
                                        NewGame();
                                    } else if (MenuModes[Continue][menuPosition] == 1) { //CONTINUE
                                        SaveLoad(false);
                                        System.out.println("Loaded");
                                        GAMEOVER = false;
                                        GameMode = 0;
                                        Continue = 0;
                                    } else if (MenuModes[Continue][menuPosition] == 2) { //FINISH
                                    } else if (MenuModes[Continue][menuPosition] == 4) { //ABOUT
                                    }
                                    /*                          if(menuPosition == 0){ 
                                                                    System.out.println("Starts");
                                                                    NewGame();
                                                                }
                                                                else if(menuPosition == 4){
                                                                    //myMidlet.notifyDestroyed();
                                                                    try {
                                                                        myMidlet.destroyApp(true);
                                                                    } catch (MIDletStateChangeException e1) {
                                                                        e1.printStackTrace();
                                                                    }
                                                                }
                                    */
                                    break;
                            }
                            break;

                        case 0:
                            //g.drawString("GAME OVER",0,60);
                            //System.out.println("Case 0");
                            if (KEY_SOFTKEY1 == keyCode) {
                                System.out.println("Game Over");
                                GameMode = 2;
                            }
                            break;
                    }
                }
            }
            /*
                    @Override
                protected void keyReleased(int keyCode) {
                    //System.out.println("Key "+Key);
                    //System.out.println("Key released "+getGameAction(keyCode));
                    timePressed = 0;
                    Key = false;
                    KeyMove = 0;
                }

                        @Override
                protected void keyRepeated(int keyCode) {
                    //int action = getGameAction(keyCode);
                    action = keyCode;
                    if(!Key){
                        Key=true;
                        System.out.println("false olmus");
                    }
                    if(!GAMEOVER){
                        //if((System.currentTimeMillis() - timePressed)>=50)
                        //  timePressed = System.currentTimeMillis();
                        if(GameMode == 0){
                            //switch (action) {
                            switch (keyCode) {
                            case KEY_UP_ARROW:
                                keyPressed(keyCode);
                            break;
                            default:
                                break;
                            }
                        }
                    }
                } */
    } //TAdapter

    private void myPaint(Graphics g, boolean all) {
        if (all) paintBoard(g);
        else board.drawAll(g);
    }

    private void paintBoard(Graphics g) {
        board.drawAll(g);
        g.setClip(0, 0, Board.BOARD_WIDTH, Board.BOARD_HEIGHT);
        if (!GAMEOVER) {
            board.CheckGameOver(matchstone);
            if(matchstone != null)
                matchstone.DrawShape(g, Board.balls, Board.CELL_SIZE);
            if (System.currentTimeMillis() - lastDraw >= speed) {
                if (board.Check(matchstone)) {
                    board.Update(matchstone);
                    //matchstone = null;
                    matchstone = new MatchStone();
                    //Key = false;
                    board.CheckIsFull();
                } else {
                    matchstone.CellY++;
                }
                lastDraw = System.currentTimeMillis();
            }
        }
    }

    private void DrawMenu(Graphics g, int MenuMode, boolean full, int trans) {
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

        //dg.fillPolygon(full ? xy[1][0] : xy[0][0],0,full ? xy[1][1] :xy[0][1],0,4,transparency[trans]);
        //dg.fillPolygon(full ? xy[1][0] :xy[0][0],0,full ? xy[1][1] :xy[0][1],0,4,transparency[MenuMode]);
        //dg.fillPolygon(xy[MenuMode][0],0,xy[MenuMode][1],0,4,transparency[MenuMode]);

        for (int i = 0; i < MenuModes[MenuMode].length; i++) {
            if (i == menuPosition) g.setColor(Color.red);
            else g.setColor(Color.white);
            if (MenuModes[MenuMode][i] == 6) {
                //int x = font.stringWidth(strMenu[MenuModes[MenuMode][i]]+" "+(Level-4));  
                int x = 10; //UBASAK
                g.drawString(strMenu[MenuModes[MenuMode][i]] + " " + (Level - 4), (128 - x) / 2, 40 + i * 15);

            } else {
                int x = 10; //UBASAK
                //int x = font.stringWidth(strMenu[MenuModes[MenuMode][i]]);    
                g.drawString(strMenu[MenuModes[MenuMode][i]], (128 - x) / 2, 40 + i * 15);
            }

            if (MenuMode == 2) {
                g.setColor(Color.white);
                g.drawString("Do you want to", 20, 10);
                g.drawString("save your game?", 20, 20);
            }
        }
    }

    private void NewGame() {
        GAMEOVER = false;
        GameMode = 0;
        board = new Board();
        lastDraw = System.currentTimeMillis();
        puan = 0;
        matchstone = new MatchStone();
        if (matchstone.CellY != 0) System.out.println("uyar? CellY 0 degil");
        matchstone.CellY = 0;
        transCell = 0;
    }

    /*  
    private int GetIntRecord(RecordStore rs, int no){
        int x=0;
        try {
            byte[] temp = rs.getRecord(no);
            String temp_str = new String(temp,0,temp.length);
            x=Integer.parseInt(temp_str);
        } catch (RecordStoreNotOpenException e) {
            e.printStackTrace();
        } catch (InvalidRecordIDException e) {
            e.printStackTrace();
        } catch (RecordStoreException e) {
            e.printStackTrace();
        }
        return x;
    }
    
    private void SetRecord(RecordStore rs,int no,int var){
        byte[] temp =( (String)(""+var) ).getBytes();
        try {
            rs.setRecord(no,temp,0,temp.length);
        } catch (RecordStoreNotOpenException e) {
            e.printStackTrace();
        } catch (InvalidRecordIDException e) {
            e.printStackTrace();
        } catch (RecordStoreFullException e) {
            e.printStackTrace();
        } catch (RecordStoreException e) {
            e.printStackTrace();
        }
    }*/

    

    

    public void SaveLoad(boolean Save) {
        if (!Save) matchstone = new MatchStone();
        long timex = System.currentTimeMillis();
        /*
        try {
            rsMatchTris = RecordStore.openRecordStore("MatchTris",true);
            for(int i=0; i<200; i++){
                if(Save)
                    SetRecord(rsMatchTris,LOADTHEM+i,TetrisBoard[i/20][i%20]);
                else
                    TetrisBoard[i/20][i%20] = GetIntRecord(rsMatchTris,LOADTHEM+i);
            }
            if(Save) SetRecord(rsMatchTris,ISCONTINUE,1);
            for(int i=0; i<3; i++){
                if(Save)
                    SetRecord(rsMatchTris,213+i,matchstone.type[i]);
                else
                    matchstone.type[i]= GetIntRecord(rsMatchTris,213+i);
            }
            if(Save){
                SetRecord(rsMatchTris,216,matchstone.CellX);
                SetRecord(rsMatchTris,217,matchstone.CellY);
                SetRecord(rsMatchTris,218,puan);
            }
            else{
                matchstone.CellX = GetIntRecord(rsMatchTris,216);
                matchstone.CellY = GetIntRecord(rsMatchTris,217);
                puan = GetIntRecord(rsMatchTris,218);
                SetRecord(rsMatchTris,ISCONTINUE,0);
            }
            rsMatchTris.closeRecordStore();
        } catch (RecordStoreFullException e) {
            e.printStackTrace();
        } catch (RecordStoreNotFoundException e) {
            e.printStackTrace();
        } catch (RecordStoreException e) {
            e.printStackTrace();
        } */
        timex = System.currentTimeMillis() - timex;
        System.out.println("timepassed in ms " + timex + " in sec " + (timex / 1000));
    }
}
