import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.Applet;
import java.util.Random;
import javax.swing.*;
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
    private MatchStone matchstone = null;
    private MatchStone nextStone = null;
    private int[] HighScores;

    //Game state properties --needs heavy refactoring. UBASAK
    public static int GameMode = Constants.GAME_MODE_MENU;
    private long lastDraw;
    public static int flashing = 0;
    public static boolean falling = false;
    public static boolean GAMEOVER = true;
    public static int Level = 6;
    public static int points = 0;

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
        if ( GAMEOVER ) {
            return;
        }

        if( GameMode == Constants.GAME_MODE_STANDARD ) {
            gameCycle();
        } else if( GameMode == Constants.GAME_MODE_SPLASH) {
            splashCycle();
        }
    }

    private void gameCycle() {
        board.CheckGameOver(matchstone);

        if (System.currentTimeMillis() - lastDraw < Constants.speed) {
            return;
        }

        if (board.Check(matchstone)) {
            board.Update(matchstone);
            updateStones();
            board.CheckIsFull();
        } else {
            matchstone.CellY++;
        }

        lastDraw = System.currentTimeMillis();
    }

    private void splashCycle() {
        if (++flashing == 10) {
            GameMode = Constants.GAME_MODE_STANDARD;
            board.setFlashing(false);
            board.handleFalling();
        } else {
            board.setFlashing( flashing % 2 == 0);
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
                Logger.error("Interrupted: " + e.getMessage());
            }

            beforeTime = System.currentTimeMillis();
        }
    }

    public Game() {
        this.initGame();
        this.newGame();
        this.GameMode = Constants.GAME_MODE_MENU;
    }

    public void initGame() {
        Logger.debug("Game::InitGame");
        setKeyBindings();
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(Constants.B_WIDTH, Constants.B_HEIGHT));
        setDoubleBuffered(true);
        font = new Font("TimesRoman", Font.PLAIN, 20);
        images = new Images();
        menu = new Menu();
        store = new Store();
        HighScores = new int[10];
        store.loadScores();
    }

    private void newGame() {
        GAMEOVER = false;
        GameMode = Constants.GAME_MODE_STANDARD;
        board = new Board(images);
        lastDraw = System.currentTimeMillis();
        points = 0;
        matchstone = new MatchStone(images.get("balls"));
        nextStone = new MatchStone(images.get("balls"));
        matchstone.initCoordinates();
        if (matchstone.CellY != 0) {
            Logger.warn("WARNING : cell_y is not ZERO");
        }
        log_info();
    }

    private class KeyAction extends AbstractAction {
        public KeyAction(String actionCommand) {
            putValue(ACTION_COMMAND_KEY, actionCommand);
        }

        @
        Override
        public void actionPerformed(ActionEvent actionEvt) {
            String keyCode = actionEvt.getActionCommand();
            Logger.debug("GameMode : " + GameMode + " GameOver : " + GAMEOVER + " KeyCode : " + keyCode + " pressed");
            if (GAMEOVER) {
                passiveGameActions(keyCode);
            } else {
                activeGameActions(keyCode);
            }
        log_info();
        }
    } //TAdapter

    private void activeGameActions(String keyCode) {
        if( GameMode == Constants.GAME_MODE_STANDARD) {
            this.handleGameEvents(keyCode);
        } else if (GameMode == Constants.GAME_MODE_SPLASH) {
            //Game is going on but flashing cause of Letting it down!
            //Need to implement this againg UBASAK
        } else if (GameMode == Constants.GAME_MODE_MENU || GameMode == Constants.GAME_MODE_EXIT || GameMode == Constants.GAME_MODE_CONTINUE) {
            this.handleMenuEvents(keyCode);
        }
    }

    private void handleGameEvents(String keyCode) {
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
                GameMode = Constants.GAME_MODE_CONTINUE;
                break;
        }
    }

    private void passiveGameActions(String keyCode) {
        if( GameMode == Constants.GAME_MODE_STANDARD) {
            if (Constants.KEY_SOFTKEY1 == keyCode) {
                Logger.debug("Game Over - Starting again");
                GameMode = Constants.GAME_MODE_MENU;
            }
        } else if( GameMode == Constants.GAME_MODE_SPLASH) {
            GameMode = Constants.GAME_MODE_MENU;
        } else if (GameMode == Constants.GAME_MODE_MENU || GameMode == Constants.GAME_MODE_CONTINUE) {
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
            Logger.debug("Starts");
            this.newGame();
        } else if (menuValue == 1) { //CONTINUE
            store.SaveLoad(false);
            Logger.debug("Loaded");
            GAMEOVER = false;
            GameMode = Constants.GAME_MODE_STANDARD;
        } else if (menuValue == 2) { //FINISH
            this.GameMode = Constants.GAME_MODE_MENU;
            this.GAMEOVER = true;
        } else if (menuValue == 3) { //HELP
            board.Score();
            GAMEOVER = true;
            this.GameMode = Constants.GAME_MODE_MENU;
        } else if (menuValue == 4) { //ABOUT

        } else if (menuValue == 5) { //EXIT
            if( GAMEOVER ) {
                System.exit(0);
            } else {
                GameMode = Constants.GAME_MODE_EXIT;
            }
        } else if(menuValue == 6) { //LEVE

        } else if(menuValue == 7) { //YES
            System.exit(0);
        } else if(menuValue == 8) { //NO
            GameMode = Constants.GAME_MODE_STANDARD;
        }
        log_info();
    }

    private void paintGame(Graphics g) {
        g.setFont(font);
        board.draw(g);
        paintStones(g);
        drawSplash(g);
        menu.draw(g);
    }

    private void paintStones(Graphics g) {
        if( GAMEOVER || matchstone == null || nextStone == null ) {
            return;
        }

        matchstone.draw(g);
        nextStone.draw(g);
    }

    private void drawSplash(Graphics g) {
        if( GameMode != Constants.GAME_MODE_MENU  ) return;
        g.drawImage(images.get("splash"), 0, 0, Constants.BOARD_WIDTH, Constants.BOARD_HEIGHT, null);
    }

    private void updateStones() {
        if( matchstone == null || nextStone == null ) {
            return;
        }
        MatchStone tempStone = matchstone;
        matchstone = nextStone;
        matchstone.initCoordinates();
        nextStone = tempStone;
        nextStone.initTypes();
        nextStone.setNextCoordinates(); 
    }

    private void log_info() {
        Logger.debug(String.format("GameOver : %s\tGameMode : %s", GAMEOVER, GameMode));
    }
}
