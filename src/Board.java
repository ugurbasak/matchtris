import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Board {

    public int[][] TetrisBoard;
    private boolean[][] TBTest;
    private boolean isFlashingEnabled = false;
    private Images images = null;
    private int transCell = 0; //When the game ends using this propert board will be covered with a transparent layer

    public Board(Images images) {
        //Initialization
        this.images = images;
        this.transCell = 0;
        this.isFlashingEnabled = false;
        TetrisBoard = new int[Constants.BOARD_COLUMNS][Constants.BOARD_ROWS];
        TBTest = new boolean[Constants.BOARD_COLUMNS][Constants.BOARD_ROWS];

        for (int i = 0; i < Constants.BOARD_COLUMNS; i++) {
            for (int j = 0; j < Constants.BOARD_ROWS; j++) {
                TetrisBoard[i][j] = 0;
                TBTest[i][j] = false;
            }
        }
    }

    public void drawAll(Graphics g) {
        this.drawBackground(g);
        this.drawCells(g);
        this.drawBorder(g);
        this.drawScore(g);
        this.drawTransparentLayer(g);
    }

    private void drawBackground(Graphics g) {
        g.setClip(0, 0, Constants.BOARD_WIDTH, Constants.BOARD_HEIGHT);
        g.setColor(Color.gray);
        g.fillRect(0, 0, Constants.BOARD_WIDTH, Constants.BOARD_HEIGHT);
        //g.drawImage(this.splash, 0, 0, Constants.BOARD_WIDTH, Constants.BOARD_HEIGHT, null);
     }

    private void drawCells(Graphics g) {
        for (int i = 0; i < Constants.BOARD_COLUMNS; i++) {
            for (int j = 0; j < Constants.BOARD_ROWS; j++) {
                int x_v = Constants.startX + i * Constants.CELL_SIZE;
                int y_v = Constants.startY + j * Constants.CELL_SIZE;
                BufferedImage image = this.getCellImage(i, j);
                g.drawImage(image, x_v, y_v, Constants.CELL_SIZE, Constants.CELL_SIZE, null);
            }
        }
    }

    private BufferedImage getCellImage(int i, int j) {
        if (this.isFilled(i, j) && isValidCellForRendering(i, j) ) {
            return images.getSprite(images.get("balls"), this.getValue(i, j));
        } else {
            return images.get("back");
        }
    }

    private boolean isValidCellForRendering(int i, int j) {
        if( !isFlashingEnabled ) {
            return true;
        }

        if ( this.TBTest[i][j] ) {
            return false;
        } else {
            return true;
        }
    }

    private void drawBorder(Graphics g) {
        g.setClip(0, 0, Constants.BOARD_WIDTH, Constants.BOARD_HEIGHT);
        for (int i = 0; i < Constants.BOARD_WIDTH / 4; i++) {
            g.drawImage(images.get("border"), Constants.BOARD_WIDTH / 2, i * 4, null);
            g.drawImage(images.get("border"), 0, i * 4, null);
        }
        for (int i = 0; i < Constants.BOARD_WIDTH / 8; i++) {
            g.drawImage(images.get("border"), i * 4, Constants.BOARD_WIDTH - 4 , null);
            g.drawImage(images.get("border"), i * 4, 0, null);
        }
    }

    private void drawScore(Graphics g) {
        g.setColor(Color.black);
        g.drawString("Points", (10 + 2) * Constants.CELL_SIZE, 50);
        g.setColor(Color.blue);
        g.drawString(Integer.toString(Game.puan), (10 + 2) * Constants.CELL_SIZE, 70);
    }

    public boolean CheckIsFull() {
        this.check();

        if (Game.falling) {
            // The first code block means flashing is active, can make this code parametric.
            if (true) {
                Game.GameMode = Constants.GAME_MODE_SPLASH;
                Game.flashing = 0;
            } else {
                this.handleFalling();
            }
        } else {
            return false;
        }
        return true;
    }

    public void handleFalling() {
        LetItDown();
        Game.falling = false;
        CheckIsFull();
    }

    private void check() {
        for (int i = 0; i < Constants.BOARD_COLUMNS; i++) {
            for (int j = Constants.BOARD_ROWS - 1; j >= 0; j--) {
                if (!this.isFilled(i, j)) {
                    continue;
                }
                int color = this.getValue(i, j);
                this.checkToUp(i, j, color);
                this.checkToRight(i, j, color);
                this.checkToRightHorizontal(i, j, color);
                this.checkToLeftHorizontal(i, j, color);
            }
        }
    }

    private boolean isInRange(int x, int y) {
        if( x < 0 || x >=Constants.BOARD_COLUMNS) {
            return false;
        }

        if( y < 0 || y >=Constants.BOARD_ROWS) {
            return false;
        }

        return true;
    }

    private void check(int i, int j, int dx, int dy, int color) {
        try {
            int matches = this.getMatches(i, j, dx, dy, color);
            this.setMatches(i, j, dx, dy, matches);
        } catch (ArrayIndexOutOfBoundsException ex) {
            Logger.error(i + " - " + j + " " + ex);
            throw ex;
        }
    }

    private void setMatches(int i, int j, int dx, int dy, int matches) {
        if (matches >= Constants.BALL_LENGTH) {
            for (int m = 0; m < matches; m++) {
                TBTest[i + (dx * m)][j + (dy * m)] = true;
            }
            Game.falling = true;
        }
    }

    private int getMatches(int i, int j, int dx, int dy, int color) {
        int matches = 1;
        while ( this.isInRange(i + (dx * matches), j + (dy * matches) ) &&
                this.getValue(i, j) == this.getValue(i, j, dx * matches, dy * matches) ) {
            matches++;
        }
        return matches;
    }

    private void checkToUp(int i, int j, int color) {
        this.check(i, j, 0, -1, color);
    }

    private void checkToRight(int i, int j, int color) {
        this.check(i, j, 1, 0, color);
    }

    private void checkToRightHorizontal(int i, int j, int color) {
        this.check(i, j, 1, -1, color);
    }

    private void checkToLeftHorizontal(int i, int j, int color) {
        this.check(i, j, -1, -1, color);
    }

    private void LetItDown() {
        for (int i = 0; i < Constants.BOARD_COLUMNS; i++) {
            for (int j = Constants.BOARD_ROWS - 1; j >= 0; j--) {
                if ( !this.TBTest[i][j] ) {
                    continue;
                }
                int matches = this.getMatchesCount(i, j);
                this.letItDown(i, j, matches);
                this.clearChecks(i, j, matches);
                this.updateScore(matches);
            }
        }
    }

    private int getMatchesCount(int i, int j) {
        int matches = 1;
        while (j - matches >= 0 && this.TBTest[i][j] == this.TBTest[i][j - matches] ) {
            matches++;
        }
        return matches;
    }

    private void letItDown(int i, int j, int matches) {
        int m = 0;
        while (j - m >= 0) {
            int value = 0;
            if ((j - m - matches) >= 0) {
                value = this.getValue(i, j, 0, -1 * (matches + m));
            }
            TetrisBoard[i][j - m] = value;
            if (j - m - 1 >= 0 && ( !this.isFilled(i, j, 0, -1 * (m + 1) ) || (j - m) == 0) ) {
                break;
            }
            m++;
        }
    }

    private void clearChecks(int i, int j, int matches) {
        for (int u = 0; u < matches; u++) {
            this.TBTest[i][j - u] = false;
        }
    }

    private void updateScore(int matches) {
        Game.puan += ((Game.Level - 4) * (Game.Level - 4)) * matches;
    }

    public boolean Check(MatchStone matchstone) {
        if (matchstone.CellY + Constants.BALL_LENGTH == Constants.BOARD_ROWS || this.isFilled(matchstone, 0, Constants.BALL_LENGTH)) {
            return true;
        } else {
            return false;
        }
    }

    public void Update(MatchStone matchstone) {
        if( Game.GAMEOVER ) {
            return;
        }

        int x = matchstone.CellX;
        int y = matchstone.CellY;

        for (int i = 0; i < Constants.BALL_LENGTH; i++) {
            if ( y + i >= 0 ) {
                TetrisBoard[x][y + i] = matchstone.type[i];
            }
        }
    }

    public void CheckGameOver(MatchStone matchstone) {
        synchronized(TetrisBoard) {
            if (this.isFilled(4, 0)) {
                Game.GAMEOVER = true;
                Logger.debug("GAME OVER!");
                Score();
                /*
                if(CheckIsFull()){
                    System.out.println("here and not finished");
                    GAMEOVER=false;
                    matchstone = new MatchStone();
                    //Key = false;
                    CheckIsFull();
                }
                else{
                    System.out.println("GAME OVER!");
                    Score();
                }
                */
            }
        }
    }

    public void Score() {
        synchronized(TetrisBoard) {
            //Score Kay?t islemleri
            
            /*
            int j = 0;
            for (j = 1; j < 10; j++) {
                if (puan > HighScores[0]) {
                    j = 0;
                    break;
                } else if (puan > HighScores[j]) break;
            }
            System.out.println("my j =" + j);
            */

            /*
            if(j!=10){
                for(int n=9; n>=j; n--){
                    if(n!=j)    HighScores[n] = HighScores[n-1];
                    else        HighScores[j] = puan;
                }
                try {
                    rsMatchTris = RecordStore.openRecordStore("MatchTris",true);
                    for(int ii=0; ii<10; ii++){
                        System.out.println("Record "+ii+" = "+HighScores[ii] );
                        SetRecord(rsMatchTris,HIGHSCORES+ii,HighScores[ii]);
                    }
                    rsMatchTris.closeRecordStore();
                    System.out.println("Closed");
                } catch (RecordStoreFullException e) {
                    e.printStackTrace();
                } catch (RecordStoreNotFoundException e) {
                    e.printStackTrace();
                } catch (RecordStoreException e) {
                    e.printStackTrace();
                }
            } */
            Logger.debug("GameMode =" + Game.GameMode + " GAMEOVER =" + Game.GAMEOVER);
        }
    }

    private int getValue(int x, int y, int dx, int dy) {
        return this.TetrisBoard[x + dx][y + dy];
    }

    private int getValue(int x, int y) {
        return this.getValue(x, y, 0, 0);
    }
    // x  : current x value
    // y  : current y value
    // dx : expected diff x value
    // dy : expected fiff y value
    private boolean isFilled(int x, int y, int dx, int dy) {
        return this.getValue(x, y, dx, dy) != 0;
    }

    private boolean isFilled(int x, int y) {
        return this.isFilled(x, y, 0, 0);
    }

    private boolean isFilled(MatchStone matchStone, int dx, int dy) {
        return this.isFilled(matchStone.CellX, matchStone.CellY, dx, dy);
    }

    private boolean isFilled(MatchStone matchStone) {
        return this.isFilled(matchStone, 0, 0);
    }

    private boolean checkRight(MatchStone matchstone, int i) {
        if (matchstone.CellX == (Constants.BOARD_COLUMNS - 1) || this.isFilled(matchstone, 1, i) ) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkLeft(MatchStone matchstone, int i) {
        if (matchstone.CellX == 0 || this.isFilled(matchstone, -1, i) ) {
            return true;
        } else {
            return false;
        }
    }

    public void move(int direction, MatchStone matchstone) {
        if (Game.GAMEOVER && Game.GameMode == Constants.GAME_MODE_STANDARD) {
            Logger.error("The error is at this point. What error?");
        }

        if (Game.GAMEOVER || Game.GameMode != Constants.GAME_MODE_STANDARD) {
            return;
        }

        //If it is still fully or partly iin the invisible area discard move action
        if( matchstone.CellY < 0 ) {
            return;
        }

        boolean increase = true;
        if (direction == 1) {
            for (int i = 0; i < Constants.BALL_LENGTH; i++) {
                if( this.checkRight(matchstone, i) ) {
                    increase = false;
                }
            }
            if (increase) matchstone.CellX++;
        } else if (direction == -1) {
            for (int i = 0; i < Constants.BALL_LENGTH; i++) {
                if (this.checkLeft(matchstone, i)) {
                    increase = false;
                }
            }
            if (increase) matchstone.CellX--;
        } else if (direction == 0) {
            if (this.Check(matchstone)) {
                this.CheckGameOver(matchstone);
            } else {
                matchstone.CellY++;
            }
        }
    }

    public void setFlashing(boolean isFlashingEnabled) {
        this.isFlashingEnabled = isFlashingEnabled;
    }

    public void drawTransparentLayer(Graphics g) {
        if( !Game.GAMEOVER ) {
            return;
        }

        if(Game.GameMode != Constants.GAME_MODE_STANDARD) {
            return;
        }

        if( transCell + 1 < Constants.CELL_SIZE * Constants.BOARD_ROWS ) {
            transCell+=Constants.CELL_SIZE;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        g2d.setColor(Color.black);
        g2d.fillRect(Constants.startX, 0, Constants.CELL_SIZE * 10, Constants.startY + transCell);
        Logger.debug("TransCell " + transCell);

        if(transCell >= Constants.CELL_SIZE * Constants.BOARD_ROWS ) {
            Logger.debug("Game is really over");//Do really something
            g.setColor(Color.black);
            g.drawString("Game Over please click press key to continue", 2 * Constants.CELL_SIZE, 4 * Constants.CELL_SIZE);
        }
    }
}
