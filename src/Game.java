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
    //Common properties
    private Thread animator;

    //Instance properties
    private Font font;
    private Board board = null;
    private Menu menu = null;
    private Store store = null;
    private Images images = null;
    public MatchStone matchstone = null;
    private int[] HighScores;

    //Game state properties --needs heavy refactoring. UBASAK
    public static int GameMode = Constants.GAME_MODE_MENU;
    private long lastDraw;
    public static int flashing = 0;
    public boolean Key = false;
    private int KeyMove = 0;
    public static boolean falling = false;
    private int falling_times = 0;
    public static boolean GAMEOVER = true;
    public static int Level = 6;
    public static int puan = 0;
    private long timePressed = 0; //about when a key is pressed or released
    public static int Continue = 0;
    private int transCell = 0; //When the game ends using this propert board will be covered with a transparent layer

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

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), Constants.KEY_LEFT_ARROW);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), Constants.KEY_RIGHT_ARROW);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), Constants.KEY_UP_ARROW);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), Constants.KEY_DOWN_ARROW);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), Constants.KEY_SOFTKEY1);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), Constants.KEY_SOFTKEY2);

        actionMap.put(Constants.KEY_LEFT_ARROW, new KeyAction(Constants.KEY_LEFT_ARROW));
        actionMap.put(Constants.KEY_RIGHT_ARROW, new KeyAction(Constants.KEY_RIGHT_ARROW));
        actionMap.put(Constants.KEY_UP_ARROW, new KeyAction(Constants.KEY_UP_ARROW));
        actionMap.put(Constants.KEY_DOWN_ARROW, new KeyAction(Constants.KEY_DOWN_ARROW));
        actionMap.put(Constants.KEY_SOFTKEY1, new KeyAction(Constants.KEY_SOFTKEY1));
        actionMap.put(Constants.KEY_SOFTKEY2, new KeyAction(Constants.KEY_SOFTKEY2));
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
        paintGame(g);
    }

    private void cycle() {
        //Any updates
        if (!GAMEOVER) {
            if( GameMode == Constants.GAME_MODE_STANDARD ) {
                board.CheckGameOver(matchstone);
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
            } else if( GameMode == Constants.GAME_MODE_SPLASH) {
                if (++flashing == 10) {
                    GameMode = Constants.GAME_MODE_STANDARD;
                    board.setFlashing(false);
                    board.handleFalling();
                } else {
                    board.setFlashing( flashing % 2 == 0);
                }
            }
        }
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

    public Game() {
        initGame();
        font = new Font("TimesRoman", Font.PLAIN, 20);
        images = new Images();
        board = new Board(images);
        menu = new Menu();
        store = new Store();
        HighScores = new int[10];
        lastDraw = System.currentTimeMillis();
        timePressed = System.currentTimeMillis();
        store.loadScores();
    }

    private class KeyAction extends AbstractAction {
        public KeyAction(String actionCommand) {
            putValue(ACTION_COMMAND_KEY, actionCommand);
        }

        @
        Override
        public void actionPerformed(ActionEvent actionEvt) {
            String keyCode = actionEvt.getActionCommand();
            System.out.println("GameMode : " + GameMode + " GameOver : " + GAMEOVER + " KeyCode : " + keyCode + " pressed");
            if (GAMEOVER) {
                passiveGameActions(keyCode);
            } else {
                activeGameActions(keyCode);
            }
        }
    } //TAdapter

    private void activeGameActions(String keyCode) {
        if( GameMode == Constants.GAME_MODE_STANDARD) {
            switch (keyCode) {
                case Constants.KEY_LEFT_ARROW:
                    board.move(-1, matchstone);
                    break;
                case Constants.KEY_RIGHT_ARROW:
                    board.move(1, matchstone);
                    break;
                case Constants.KEY_DOWN_ARROW:
                    board.move(0, matchstone);
                    break;
                case Constants.KEY_UP_ARROW:
                    matchstone.Rotate();
                    break;
                case Constants.KEY_SOFTKEY1:
                case Constants.KEY_SOFTKEY2:
                    //Stop
                    //SaveGame();
                    GameMode = Constants.GAME_MODE_MENU;
                    Continue = 1;
                    break;
                default:
                    Key = true;
                    KeyMove = 0;
                    break;
            }
        } else if (GameMode == Constants.GAME_MODE_SPLASH) {
            //Game is going on but flashing cause of Letting it down!
            //Need to implement this againg UBASAK
        } else if (GameMode == Constants.GAME_MODE_MENU || GameMode == Constants.GAME_MODE_EXIT) {
            this.handleMenuEvents(keyCode);
        }
    }

    private void passiveGameActions(String keyCode) {
        if( GameMode == Constants.GAME_MODE_STANDARD) {
            //g.drawString("GAME OVER",0,60);
            System.out.println("Case 0");
            if (Constants.KEY_SOFTKEY1 == keyCode) {
                System.out.println("Game Over");
                GameMode = Constants.GAME_MODE_MENU;
            }
            //GameOver now it is time to draw the transparant part UBASAK
            /* This code looks like old or new keypress code 
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
            */
        } else if( GameMode == Constants.GAME_MODE_SPLASH) {
            GameMode = Constants.GAME_MODE_MENU;
        } else if (GameMode == Constants.GAME_MODE_MENU) {
            this.handleMenuEvents(keyCode);
        }
    }

    private void handleMenuEvents(String keyCode) {
        switch (keyCode) {
            case Constants.KEY_DOWN_ARROW:
            case Constants.KEY_UP_ARROW:
            case Constants.KEY_LEFT_ARROW:
            case Constants.KEY_RIGHT_ARROW:
                menu.navigate(keyCode);
                break;
            case Constants.KEY_SOFTKEY1:
            case Constants.KEY_SOFTKEY2:
                evaluateCommand();
                break;
        }
    }

    private void evaluateCommand() {
        int menuValue = menu.getCommand();
        if (menuValue == 0) { //NEW ONE
            System.out.println("Starts");
            NewGame();
        } else if (menuValue == 1) { //CONTINUE
            store.SaveLoad(false);
            System.out.println("Loaded");
            GAMEOVER = false;
            GameMode = Constants.GAME_MODE_STANDARD;
            Continue = 0;
        } else if (menuValue == 2) { //FINISH
            this.GameMode = Constants.GAME_MODE_MENU;
            this.GAMEOVER = true;
            this.Continue = 0;
        } else if (menuValue == 3) { //HELP
            board.Score();
            GAMEOVER = true;
            this.GameMode = Constants.GAME_MODE_MENU;
            //Continue=0;
        } else if (menuValue == 4) { //ABOUT

        } else if (menuValue == 5) { //EXIT
            if( GAMEOVER ) {
                System.exit(0);
            } else {
                //we are exiting but there are some flows, check this. UBASAK
                GameMode = Constants.GAME_MODE_EXIT;
                //menu.menuPosition = 0;
            }
        } else if(menuValue == 6) { //LEVE

        } else if(menuValue == 7) { //YES
            System.exit(0);
        } else if(menuValue == 8) { //NO
            GameMode = Constants.GAME_MODE_STANDARD;
        }
    }

    private void paintGame(Graphics g) {
        g.setFont(font);
        board.drawAll(g);
        paintBoard(g);
        drawSplash(g);
        drawMenu(g);
    }

    private void drawMenu(Graphics g) {
        if( GameMode == Constants.GAME_MODE_MENU || GameMode == Constants.GAME_MODE_EXIT) {
            int mode = GameMode - 1;
            if( Continue == 0 ) mode--;
            menu.draw(g, mode);
        }
    }

    private void paintBoard(Graphics g) {
        if( !GAMEOVER && matchstone != null ) {
            matchstone.DrawShape(g, images.balls, Constants.CELL_SIZE);
        }
    }

    private void drawSplash(Graphics g) {
        if( GameMode != Constants.GAME_MODE_MENU  ) return;
        g.drawImage(images.splash,0,0, Constants.BOARD_WIDTH, Constants.BOARD_HEIGHT,null);
    }

    private void NewGame() {
        GAMEOVER = false;
        GameMode = Constants.GAME_MODE_STANDARD;
        board = new Board(images);
        lastDraw = System.currentTimeMillis();
        puan = 0;
        matchstone = new MatchStone();
        if (matchstone.CellY != 0) {
            System.out.println("WARNING : cell_y is not ZERO");
        }
        matchstone.CellY = 0;
        transCell = 0;
    }
}
