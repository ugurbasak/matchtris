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
import java.io.FileReader;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;

public class Board {

    public int[][] TetrisBoard;
    public int[][] TBTest;

    public static BufferedImage balls = null;
    public static BufferedImage border = null;
    public static BufferedImage back = null;
    public static BufferedImage splash = null;
    public static BufferedImage menu = null;
    public static BufferedImage blue_fonts = null;
    public static BufferedImage black = null;
    public static BufferedImage blue = null;
    public static BufferedImage imgPuan = null;

    public Board() {
        //Initialization
        TetrisBoard = new int[10][20];
        TBTest = new int[10][20];

        //To create a new stone
        //matchstone = new MatchStone();
        //do not create here

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 20; j++) {
                TetrisBoard[i][j] = 0;
                TBTest[i][j] = 0;
            }
        }

        this.loading();
    }

    private BufferedImage getSprite(BufferedImage image, int index) {
        //System.out.println("Index is " + index);
        int image_size = 6;
        return image.getSubimage((index-1)*image_size,0,image_size, image_size);
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
    }

    private void drawCells(Graphics g) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 20; j++) {
                int x_v = Constants.startX + i * Constants.CELL_SIZE;
                int y_v = Constants.startY + j * Constants.CELL_SIZE;
                if (TetrisBoard[i][j] != 0) {
                    BufferedImage image = getSprite(balls, TetrisBoard[i][j]);
                    g.drawImage(image, x_v, y_v, Constants.CELL_SIZE, Constants.CELL_SIZE, null);
                } else {
                    g.setClip(0, 0, Constants.BOARD_WIDTH, Constants.BOARD_HEIGHT);
                    g.drawImage(back, x_v, y_v, Constants.CELL_SIZE, Constants.CELL_SIZE, null);
                }
            }
        }
    }

    private void drawBorder(Graphics g) {
        g.setClip(0, 0, Constants.BOARD_WIDTH, Constants.BOARD_HEIGHT);
        for (int i = 0; i < Constants.BOARD_WIDTH / 4; i++) {
            g.drawImage(border, Constants.BOARD_WIDTH / 2, i * 4, null);
            g.drawImage(border, 0, i * 4, null);
        }
        for (int i = 0; i < Constants.BOARD_WIDTH / 8; i++) {
            g.drawImage(border, i * 4, Constants.BOARD_WIDTH - 4 , null);
            g.drawImage(border, i * 4, 0, null);
        }
    }

    private void drawScore(Graphics g) {
        /*g.setColor(0x000000);
        g.drawString("Puan",80,5);
        g.drawString(""+puan,80,15);
        */
        g.drawImage(imgPuan, (10 + 2) * Constants.CELL_SIZE , 5, null);
        DrawString(g, Game.puan, (10 + 2) * Constants.CELL_SIZE, 30);
    }

    public boolean CheckIsFull() {
        this.checkToUp();
        this.checkToRight();
        this.checkRightHorizontal();
        this.checkLeftHorizontal();

        if (Game.falling) {
            /*          //System.out.println("----------------");
                        LetItDown();
                        //System.out.println("After LetItDown");
                        falling=false;
                        CheckIsFull();
                        //System.out.println("After 2nd CheckIsFull");
            */
            //Commenting for now UBASAK
            if (false) {
                Game.GameMode = 1;
                Game.flashing = 0;
            } else {
                LetItDown();
                Game.falling = false;
                CheckIsFull();
            }
        } else
            return false;
        return true;
    }

    private void checkToUp() {
        ///////////////////////////////
        /// x
        /// x
        /// x
        /// control //////////////////
        //System.out.println("Checking to up");
        for (int i = 0; i < 10; i++) {
            for (int j = 19; j >= 0; j--) {
                //if(TetrisBoard[i][j]!=0 && TBTest[i][j]==0){
                if (TetrisBoard[i][j] != 0) {
                    int color = TetrisBoard[i][j];
                    int NoOfMatch = 1;
                    while (true) {
                        if (j - NoOfMatch >= 0) {
                            //if(j-NoOfMatch<0) System.out.println("j-NoOfMatch<0");
                            //if(i<0)             System.out.println("i<0");
                            if (TetrisBoard[i][j] == TetrisBoard[i][j - NoOfMatch])
                                NoOfMatch++;
                            else
                                break;
                        } else break;
                    }
                    if (NoOfMatch >= 3) {
                        //System.out.println("will check up "+NoOfMatch);
                        for (int m = 0; m < NoOfMatch; m++)
                            TBTest[i][j - m] = 1;
                        Game.falling = true;
                        //System.out.println("yukari");                         
                    }
                }
            }
        }
    }

    private void checkToRight() {
        ///////////////////////////////////////////////////
        ///////////////////////////////
        /// 
        /// x x x
        /// 
        /// control //////////////////
        //System.out.println("Checking to right");
        for (int i = 0; i < 10; i++) {
            for (int j = 19; j >= 0; j--) {
                if (TetrisBoard[i][j] != 0) {
                    int color = TetrisBoard[i][j];
                    int NoOfMatch = 1;
                    while (true) {
                        if (i + NoOfMatch < 10) {
                            //if(j<0) System.out.println("j<0");
                            //if(i+NoOfMatch<0)  System.out.println("i+NoOfMatch<0");

                            if (TetrisBoard[i][j] == TetrisBoard[i + NoOfMatch][j])
                                NoOfMatch++;
                            else
                                break;
                        } else break;
                    }
                    if (NoOfMatch >= 3) {
                        //System.out.println("will check right "+NoOfMatch);
                        for (int m = 0; m < NoOfMatch; m++)
                            TBTest[i + m][j] = 1;
                        Game.falling = true;
                        //System.out.println("yana");
                    }
                }
            }
        }
    }

    private void checkRightHorizontal() {
        ///////////////////////////////////////////////////
        ///////////////////////////////
        ///   x
        ///  x  
        /// x
        /// control //////////////////
        //System.out.println("Checking to right horizontal");
        for (int i = 0; i < 10; i++) {
            for (int j = 19; j >= 0; j--) {
                if (TetrisBoard[i][j] != 0) {
                    int color = TetrisBoard[i][j];
                    int NoOfMatch = 1;
                    while (true) {
                        if (i + NoOfMatch < 10 && j - NoOfMatch >= 0) {
                            //if(j-NoOfMatch<0) System.out.println("j-NoOfMatch");
                            //if(i+NoOfMatch>9)  System.out.println("i+NoOfMatch>9");

                            if (TetrisBoard[i][j] == TetrisBoard[i + NoOfMatch][j - NoOfMatch])
                                NoOfMatch++;
                            else break;
                        } else break;
                    }
                    if (NoOfMatch >= 3) {
                        //System.out.println("will check right hor"+NoOfMatch);
                        for (int m = 0; m < NoOfMatch; m++)
                            TBTest[i + m][j - m] = 1;
                        Game.falling = true;
                        //System.out.println("sag capraz");
                    }
                }
            }
        }
    }

    private void checkLeftHorizontal() {
        ///////////////////////////////////////////////////
        ///////////////////////////////
        /// x
        ///  x  
        ///   x
        /// control //////////////////
        //System.out.println("Checking to left horizontal");
        for (int i = 0; i < 10; i++) {
            for (int j = 19; j >= 0; j--) {
                if (TetrisBoard[i][j] != 0) {
                    int color = TetrisBoard[i][j];
                    int NoOfMatch = 1;
                    while (true) {
                        if (i - NoOfMatch >= 0 && j - NoOfMatch >= 0) {
                            //if(j-NoOfMatch<0) System.out.println("j-NoOfMatch");
                            //if(i-NoOfMatch<0)  System.out.println("i+NoOfMatch<0");

                            if (TetrisBoard[i][j] == TetrisBoard[i - NoOfMatch][j - NoOfMatch])
                                NoOfMatch++;
                            else break;
                        } else break;
                    }
                    if (NoOfMatch >= 3) {
                        //System.out.println("will check left hor"+NoOfMatch);
                        for (int m = 0; m < NoOfMatch; m++) {
                            //System.out.println("i-m="+(i-m)+" j-m="+(j-m));
                            TBTest[i - m][j - m] = 1;
                        }
                        Game.falling = true;
                        //System.out.println("sol capraz");
                    }
                }
            }
        }
    }

    public void LetItDown() {
        for (int i = 0; i < 10; i++) {
            for (int j = 19; j >= 0; j--) {
                if (TBTest[i][j] == 1) {
                    int NoOfMatch = 1;
                    //System.out.println("1st start");
                    while (true) {
                        if (j - NoOfMatch >= 0) {
                            if (TBTest[i][j] == TBTest[i][j - NoOfMatch])
                                NoOfMatch++;
                            else break;
                        } else break;
                    }
                    //System.out.println("1st end");
                    /*                  int k=0;
                                        while(true){
                                            System.out.println("in here");
                                            if(TetrisBoard[i][j-NoOfMatch-k]==0)
                                                break;
                                            k++;
                                        }
                                        System.out.println("k = "+k);
                    */
                    /*                  //int l=k;
                                        //int l=0;
                                        //while(l!=k){
                                            //TetrisBoard[i][j-l] = TetrisBoard[i][j-NoOfMatch-l];
                                            //l++;
                                        //}
                                        for(int l=0; l<k+1; l++){
                                            TetrisBoard[i][j-l] = TetrisBoard[i][j-NoOfMatch-l];
                                        }
                                        //for(int p=0; p<NoOfMatch; p++){
                                        //  TetrisBoard[i][j-NoOfMatch-k-p-1]=0;
                                        //}
                                        System.out.println("last control");
                                        for(int m=0; m<NoOfMatch; m++){
                                            System.out.println(j-NoOfMatch-k+m-1);
                                            TetrisBoard[i][j-NoOfMatch-k+m-1]=0;
                                        }
                                        System.out.println("last control finished");
                    */
                    //System.out.println("NoOfMatch "+NoOfMatch+" j= "+j);
                    int m = 0;
                    //if(NoOfMatch==3)
                    //System.out.println("2nd start");
                    while (true) {
                        if (j - m < 0) break;
                        if ((j - m - NoOfMatch) < 0)
                            TetrisBoard[i][j - m] = 0;
                        else
                            TetrisBoard[i][j - m] = TetrisBoard[i][j - NoOfMatch - m];
                        if (j - m - 1 >= 0) {
                            if (TetrisBoard[i][j - m - 1] == 0 || (j - m) == 0) break;
                        }
                        //yeni eklenti
                        //if((k+NoOfMatch-1)==m) break;
                        /////////
                        m++;
                    }
                    //System.out.println("2nd end");
                    //System.out.println("m = "+m);

                    for (int u = 0; u < NoOfMatch; u++)
                        TBTest[i][j - u] = 0;
                    //puan+=((Level-2)*(Level-2))*NoOfMatch;
                    Game.puan += ((Game.Level - 4) * (Game.Level - 4)) * NoOfMatch;
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

    public void loading() {
        System.out.println("Initialization started");
        if(this.balls == null)
            this.balls = LoadImage("images/balls.png");
        if(this.back == null)
            this.back = LoadImage("images/back.png");
        if(this.border == null)
            this.border = LoadImage("images/border.png");
        if(this.splash == null)
            this.splash = LoadImage("images/splash.png");
        if(this.menu == null)
            this.menu = LoadImage("images/menu.png");
        if(this.blue_fonts == null)
            this.blue_fonts = LoadImage("images/blue_fonts.png");
        if(this.black == null)
            this.black = LoadImage("images/black.png");
        if(this.blue == null)
            this.blue = LoadImage("images/blue.png");
        if(this.imgPuan == null)
            this.imgPuan = LoadImage("images/puan.png");
    }

    public BufferedImage LoadImage(String str) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(str));
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        System.out.println("Load Image for " + str);
        return img; //UBASAK find a way to return a real image
    }

    private void DrawString(Graphics g, int number, int x, int y) {
        String no = "" + number;
        int[] ints = new int[no.length()];
        int[] distance = new int[ints.length + 1];
        distance[0] = 0;
        for (int i = 0; i < no.length(); i++)
            ints[i] = Integer.parseInt(no.substring(i, i + 1));
        for (int i = 0; i < ints.length; i++)
            distance[i + 1] = distance[i] + Constants.WidthOfFonts[ints[i] + 1] - Constants.WidthOfFonts[ints[i]];
        for (int i = 0; i < no.length(); i++) {
            g.setClip(x + distance[i] + i, y, distance[i + 1] - distance[i], 20);
            g.drawImage(blue, x + distance[i] + i - Constants.WidthOfFonts[ints[i]], y, null);
        }
        g.setClip(0, 0, Constants.BOARD_WIDTH, Constants.BOARD_HEIGHT);
    }

    public boolean Check(MatchStone matchstone) {
        //if(matchstone.CellX<0 || matchstone.CellX>9) System.out.println("x de hata "+matchstone.CellX);
        if (matchstone.CellY + 2 == 19 || TetrisBoard[matchstone.CellX][matchstone.CellY + 3] != 0)
            return true;
        return false;
    }

    public void Update(MatchStone matchstone) {
        for (int i = 0; i < 3; i++) {
            if (!(Game.GAMEOVER && matchstone.CellY + i < 0))
                TetrisBoard[matchstone.CellX][matchstone.CellY + i] = matchstone.type[i];
        }
    }

    public void CheckGameOver(MatchStone matchstone) {
        synchronized(TetrisBoard) {
            int i = -1;
            if (TetrisBoard[4][0] != 0) {
                i = 0;
            } else if (TetrisBoard[4][1] != 0) {
                i = 1;
            } else if (TetrisBoard[4][2] != 0) {
                i = 2;
            }
            if (i != -1) {
                Game.GAMEOVER = true;
                matchstone.CellY = -3 + i;
                Update(matchstone);
                /*              if(CheckIsFull()){
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

    public void move(int direction, MatchStone matchstone) {
        if (!Game.GAMEOVER && Game.GameMode == 0) {
            boolean increase = true;
            if (direction == 1) {
                for (int i = 0; i < 3; i++) {
                    if (matchstone.CellX == 9 || TetrisBoard[matchstone.CellX + 1][matchstone.CellY + i] != 0)
                        increase = false;
                }
                if (increase) matchstone.CellX++;
            } else if (direction == -1) {
                for (int i = 0; i < 3; i++) {
                    if (matchstone.CellX == 0 || TetrisBoard[matchstone.CellX - 1][matchstone.CellY + i] != 0)
                        increase = false;
                }
                if (increase) matchstone.CellX--;
            } else if (direction == 0) {
                if (this.Check(matchstone)) {
                    //              Update();
                    //              matchstone = new MatchStone();
                    //              CheckIsFull();
                    if (!Game.GAMEOVER) {
                        this.CheckGameOver(matchstone);
                    }
                } else {
                    matchstone.CellY++;
                }
            }
        } else if (Game.GameMode == 0 && Game.GAMEOVER == true) {
            System.out.println("The error is at this point. What error?");
        }
    }
}
