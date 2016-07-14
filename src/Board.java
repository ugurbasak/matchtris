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
    public int[][] TBTest;
    private boolean isFlashingEnabled = false;
    private Images images = null;
    public Board(Images images) {
        //Initialization
        this.images = images;
        TetrisBoard = new int[10][20];
        TBTest = new int[10][20];

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 20; j++) {
                TetrisBoard[i][j] = 0;
                TBTest[i][j] = 0;
            }
        }
    }

    public void drawAll(Graphics g) {
        this.drawBackground(g);
        this.drawCells(g);
        this.drawBorder(g);
        this.drawScore(g);
    }

    private void drawBackground(Graphics g) {
        g.setClip(0, 0, Constants.BOARD_WIDTH, Constants.BOARD_HEIGHT);
        g.setColor(Color.gray);
        g.fillRect(0, 0, Constants.BOARD_WIDTH, Constants.BOARD_HEIGHT);
        //g.drawImage(this.splash, 0, 0, Constants.BOARD_WIDTH, Constants.BOARD_HEIGHT, null);
     }

    private void drawCells(Graphics g) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 20; j++) {
                int x_v = Constants.startX + i * Constants.CELL_SIZE;
                int y_v = Constants.startY + j * Constants.CELL_SIZE;
                BufferedImage image = this.getCellImage(i, j);
                g.drawImage(image, x_v, y_v, Constants.CELL_SIZE, Constants.CELL_SIZE, null);
            }
        }
    }

    private BufferedImage getCellImage(int i, int j) {
        if (this.isFilled(i, j) && isValidCellForRendering(i, j) ) {
            return images.getSprite(images.balls, this.getValue(i, j));
        } else {
            return images.back;
        }
    }

    private boolean isValidCellForRendering(int i, int j) {
        if( !isFlashingEnabled ) {
            return true;
        }

        if ( this.TBTest[i][j] == 1 ) {
            return false;
        } else {
            return true;
        }
    }

    private void drawBorder(Graphics g) {
        g.setClip(0, 0, Constants.BOARD_WIDTH, Constants.BOARD_HEIGHT);
        for (int i = 0; i < Constants.BOARD_WIDTH / 4; i++) {
            g.drawImage(images.border, Constants.BOARD_WIDTH / 2, i * 4, null);
            g.drawImage(images.border, 0, i * 4, null);
        }
        for (int i = 0; i < Constants.BOARD_WIDTH / 8; i++) {
            g.drawImage(images.border, i * 4, Constants.BOARD_WIDTH - 4 , null);
            g.drawImage(images.border, i * 4, 0, null);
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
        for (int i = 0; i < 10; i++) {
            for (int j = 19; j >= 0; j--) {
                if (this.isFilled(i, j)) {
                    int color = this.getValue(i, j);
                    this.checkToUp(i, j, color);
                    this.checkToRight(i, j, color);
                    this.checkToRightHorizontal(i, j, color);
                    this.checkToLeftHorizontal(i, j, color);
                }
            }
        }
    }

    private boolean isInRange(int x, int y) {
        if( x < 0 || x >=10) {
            return false;
        }

        if( y < 0 || y >=20) {
            return false;
        }

        return true;
    }

    private void check(int i, int j, int dx, int dy, int color) {
        try {
            int matches = this.getMatches(i, j, dx, dy, color);
            this.setMatches(i, j, dx, dy, matches);
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println(i + " - " + j + " " + ex);
            throw ex;
        }
    }

    private void setMatches(int i, int j, int dx, int dy, int matches) {
        if (matches >= 3) {
            for (int m = 0; m < matches; m++) {
                TBTest[i + (dx * m)][j + (dy * m)] = 1;
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
        for (int i = 0; i < 10; i++) {
            for (int j = 19; j >= 0; j--) {
                if (TBTest[i][j] == 1) {
                    int matches = 1;
                    while (true) {
                        if (j - matches >= 0) {
                            if (TBTest[i][j] == TBTest[i][j - matches])
                                matches++;
                            else break;
                        } else break;
                    }
                    //System.out.println("1st end");
                    /*
                    int k=0;
                    while(true){
                        System.out.println("in here");
                        if(TetrisBoard[i][j-matches-k]==0)
                            break;
                        k++;
                    }
                    System.out.println("k = "+k);
                    */
                    /*
                    //int l=k;
                    //int l=0;
                    //while(l!=k){
                        //TetrisBoard[i][j-l] = TetrisBoard[i][j-matches-l];
                        //l++;
                    //}
                    for(int l=0; l<k+1; l++){
                        TetrisBoard[i][j-l] = TetrisBoard[i][j-matches-l];
                    }
                    //for(int p=0; p<matches; p++){
                    //  TetrisBoard[i][j-matches-k-p-1]=0;
                    //}
                    System.out.println("last control");
                    for(int m=0; m<matches; m++){
                        System.out.println(j-matches-k+m-1);
                        TetrisBoard[i][j-matches-k+m-1]=0;
                    }
                    System.out.println("last control finished");
                    */
                    //System.out.println("matches "+NoOfMatch+" j= "+j);
                    int m = 0;
                    //if(matches==3)
                    //System.out.println("2nd start");
                    while (true) {
                        if (j - m < 0) break;
                        if ((j - m - matches) < 0)
                            TetrisBoard[i][j - m] = 0;
                        else
                            TetrisBoard[i][j - m] = this.getValue(i, j, 0, -1 * (matches + m));
                        if (j - m - 1 >= 0) {
                            if ( !this.isFilled(i, j, 0, -1 * (m + 1) )  || (j - m) == 0) break;
                        }
                        //yeni eklenti
                        //if((k+matches-1)==m) break;
                        /////////
                        m++;
                    }
                    //System.out.println("2nd end");
                    //System.out.println("m = "+m);

                    for (int u = 0; u < matches; u++)
                        TBTest[i][j - u] = 0;
                    //puan+=((Level-2)*(Level-2))*matches;
                    Game.puan += ((Game.Level - 4) * (Game.Level - 4)) * matches;
                }
            }
        }
        /*for(int i=0; i<10; i++){
            for(int j=19; j>=0; j--){
                if(TBTest[i][j]==1) puan+=(Level-2)*(Level-2);
                TBTest[i][j]=0;
            }
        }*/
        //System.out.println("All Made Zero");

        /*
        11O22
         534
        5 3 4
        */
        /*
        11044
        22033
         576
        5 7 6
        */
    }

    public boolean Check(MatchStone matchstone) {
        //if(matchstone.CellX<0 || matchstone.CellX>9) System.out.println("x de hata "+matchstone.CellX);
        if (matchstone.CellY + 2 == 19 || this.isFilled(matchstone, 0, 3))
            return true;
        return false;
    }

    public void Update(MatchStone matchstone) {
        if( Game.GAMEOVER ) {
            return;
        }

        int x = matchstone.CellX;
        int y = matchstone.CellY;

        for (int i = 0; i < 3; i++) {
            if ( y + i >= 0 ) {
                TetrisBoard[x][y + i] = matchstone.type[i];
            }
        }
    }

    public void CheckGameOver(MatchStone matchstone) {
        synchronized(TetrisBoard) {
            int i = -1;
            if (this.isFilled(4,0)) {
                i = 0;
            } else if (this.isFilled(4,1)) {
                i = 1;
            } else if (this.isFilled(4,2)) {
                i = 2;
            }
            if (i != -1) {
                Game.GAMEOVER = true;
                matchstone.CellY = -3 + i;
                Update(matchstone);
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
                System.out.println("GAME OVER!");
                Score();
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
            System.out.println("GameMode =" + Game.GameMode + " GAMEOVER =" + Game.GAMEOVER);
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
        if (matchstone.CellX == 9 || this.isFilled(matchstone, 1, i) ) {
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
        if (Game.GameMode == Constants.GAME_MODE_STANDARD && Game.GAMEOVER == true) {
            System.out.println("The error is at this point. What error?");
        }

        if (Game.GAMEOVER || Game.GameMode != Constants.GAME_MODE_STANDARD) {
            return;
        }

        boolean increase = true;
        if (direction == 1) {
            for (int i = 0; i < 3; i++) {
                if( this.checkRight(matchstone, i) ) {
                    increase = false;
                }
            }
            if (increase) matchstone.CellX++;
        } else if (direction == -1) {
            for (int i = 0; i < 3; i++) {
                if (this.checkLeft(matchstone, i)) {
                    increase = false;
                }
            }
            if (increase) matchstone.CellX--;
        } else if (direction == 0) {
            if (this.Check(matchstone)) {
                //Update();
                //matchstone = new MatchStone();
                //CheckIsFull();
                if (!Game.GAMEOVER) {
                    this.CheckGameOver(matchstone);
                }
            } else {
                matchstone.CellY++;
            }
        }
    }

    public void setFlashing(boolean isFlashingEnabled) {
        this.isFlashingEnabled = isFlashingEnabled;
    }
}
