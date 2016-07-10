import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
import java.awt.Image;
import java.io.FileReader;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.JApplet;
import javax.swing.JFrame;
import java.awt.*; 
import java.awt.event.*;
import java.awt.geom.Line2D;
import javax.swing.JApplet;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class MatchTris extends JFrame {

	public MatchTris() {
        initUI();
    }

    private void initUI() {
        Game game = new Game();
        loading(game); 
        add(game);
        
        setResizable(false);
        pack();
        
        setTitle("MatchTris");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                MatchTris ex = new MatchTris();
                ex.setVisible(true);
            }
        });
    }
	
	public void loading(Game myGame) {
	    System.out.println("Initialization started");
		myGame.balls = LoadImage("images/balls.png");
		myGame.back  = LoadImage("images/back.png");
		myGame.border= LoadImage("images/border.png");
		myGame.splash= LoadImage("images/splash.png");
		myGame.menu= LoadImage("images/menu.png");
		myGame.blue_fonts= LoadImage("images/blue_fonts.png");
		myGame.black= LoadImage("images/black.png");
		myGame.blue= LoadImage("images/blue.png");
		myGame.imgPuan= LoadImage("images/puan.png");
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
}
