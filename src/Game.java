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

    private Thread animator;

    public void initGame() {
        System.out.println("Game::InitGame");
        setKeyBindings();
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(Constants.B_WIDTH, Constants.B_HEIGHT));
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
            sleep = Constants.DELAY - timeDiff;

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
    public static int GameMode = 2;
    private Board board = null;
    private Menu menu = null;
    public MatchStone matchstone = null;

    private long lastDraw;
    private boolean notall = true;

    public boolean Key = false;
    private String action = "VK_LEFT";
    private int KeyMove = 0;
    public static boolean falling = false;
    private int falling_times = 0;

    public static boolean GAMEOVER = true;
    public static int Level = 6;
    public static int puan = 0;

    private long timePressed = 0; //about when a key is pressed or released
    public static int flashing = 0;

    private int[] HighScores;
    public static int Continue = 0;

    //private RecordStore rsMatchTris = null;

    private int transCell = 0; //Oyun bitince ekran? transparan yapacak

    public Game() {
        initGame();
        font = new Font("TimesRoman", Font.PLAIN, 20);
        board = new Board();
        menu = new Menu();
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
                }               
            }
            else{
                switch(GameMode){
                    
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
                System.out.println(GameMode + " - " + GAMEOVER + " - " + menu.menuPosition + " - " + keyCode + " pressed");
                action = keyCode;
                if (!GAMEOVER) {
                    if (timePressed == 0)
                        timePressed = System.currentTimeMillis();
                    switch (GameMode) {
                        case 0:
                            switch (keyCode) {
                                case Constants.KEY_LEFT_ARROW:
                                    if ((System.currentTimeMillis() - timePressed) >= 40) {
                                        board.move(-1, matchstone);
                                        notall = false;
                                        repaint();
                                        notall = true;
                                        timePressed = System.currentTimeMillis();
                                    }
                                    break;

                                case Constants.KEY_RIGHT_ARROW:
                                    if ((System.currentTimeMillis() - timePressed) >= 40) {
                                        board.move(1, matchstone);
                                        notall = false;
                                        repaint();
                                        notall = true;
                                        timePressed = System.currentTimeMillis();
                                    }
                                    break;

                                case Constants.KEY_DOWN_ARROW:
                                    if ((System.currentTimeMillis() - timePressed) >= 10) {
                                        board.move(0, matchstone);
                                        notall = false;
                                        repaint();
                                        notall = true;
                                        timePressed = System.currentTimeMillis();
                                    }
                                    break;
                                case Constants.KEY_UP_ARROW:
                                    matchstone.Rotate();
                                    notall = false;
                                    repaint();
                                    notall = true;
                                    break;
                                case Constants.KEY_SOFTKEY2:
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
                                case Constants.KEY_DOWN_ARROW:
                                case Constants.KEY_UP_ARROW:
                                    menu.navigate(keyCode);
                                    break;

                                case Constants.KEY_SOFTKEY1:
                                    if (menu.menuPosition == 0) {
                                        System.out.println("Continue");
                                        GAMEOVER = false;
                                        GameMode = 0;
                                    } else if (menu.menuPosition == 1) {
                                        board.Score();
                                        GAMEOVER = true;
                                        GameMode = 2;
                                        //Continue=0;
                                    } else if (menu.menuPosition == 3) {
                                        System.exit(0);
                                        //we are exiting but there are some flows, check this. UBASAK
                                        GameMode = 3;
                                        menu.menuPosition = 0;
                                    }
                            }
                            menu.draw(1,true,0);

                            break;

                        case 3:
                            switch (keyCode) {
                                case Constants.KEY_DOWN_ARROW:
                                case Constants.KEY_UP_ARROW:
                                    menu.navigate(keyCode);
                                    break;
                                case Constants.KEY_SOFTKEY1:
                                    System.exit(0);
                                    break;
                            }
                            menu.draw(2,true,0);
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
                                case Constants.KEY_DOWN_ARROW:
                                case Constants.KEY_UP_ARROW:
                                case Constants.KEY_LEFT_ARROW:
                                case Constants.KEY_RIGHT_ARROW:
                                    menu.navigate(keyCode);
                                    break;
                                case Constants.KEY_SOFTKEY1:
                                    int menuValue = menu.getValue(Continue, menu.menuPosition);
                                    if (menuValue == 5) { //EXIT
                                        System.exit(0);
                                    } else if (menuValue == 3) { //HELP

                                    } else if (menuValue == 0) { //NEW ONE
                                        System.out.println("Starts");
                                        NewGame();
                                    } else if (menuValue == 1) { //CONTINUE
                                        SaveLoad(false);
                                        System.out.println("Loaded");
                                        GAMEOVER = false;
                                        GameMode = 0;
                                        Continue = 0;
                                    } else if (menuValue == 2) { //FINISH
                                    } else if (menuValue == 4) { //ABOUT
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
                               if(Continue==1){
                                    menu.draw(1,false,0);
                                }
                                else{
                                    menu.draw(0,false,0);
                                }
                            break;

                        case 0:
                            //g.drawString("GAME OVER",0,60);
                            //System.out.println("Case 0");
                            if (Constants.KEY_SOFTKEY1 == keyCode) {
                                System.out.println("Game Over");
                                GameMode = 2;
                            }
                            break;
                    }
                }
            }
            /* UBASAK - For J2ME applications I was using keyreleased and keyrepeated events, check if they are necessary.

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
                            case Constants.KEY_UP_ARROW:
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
        g.setFont(font);
        if (all) paintBoard(g);
        else board.drawAll(g);
        drawSplash(g);
        if(GameMode == 2 || GameMode == 3)
            menu.draw(g);
    }

    private void paintBoard(Graphics g) {
        board.drawAll(g);
        g.setClip(0, 0, Constants.BOARD_WIDTH, Constants.BOARD_HEIGHT);
        if (!GAMEOVER) {
            board.CheckGameOver(matchstone);
            if(matchstone != null)
                matchstone.DrawShape(g, Board.balls, Constants.CELL_SIZE);
            if (System.currentTimeMillis() - lastDraw >= Constants.speed) {
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

    private void drawSplash(Graphics g) {
        if( GameMode != 2 ) return;
        g.drawImage(Board.splash,0,0, Constants.BOARD_WIDTH, Constants.BOARD_HEIGHT,null);        
    }

    private void NewGame() {
        GAMEOVER = false;
        GameMode = 0;
        board = new Board();
        lastDraw = System.currentTimeMillis();
        puan = 0;
        matchstone = new MatchStone();
        if (matchstone.CellY != 0) {
            System.out.println("WARNING : cell_y is not ZERO");
        }
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
