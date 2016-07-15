import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.Applet;
import java.util.Random;
import java.io.FileReader;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.swing.JApplet;
import javax.swing.JFrame;
import java.awt.geom.Line2D;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class MatchTris extends JFrame {

    public MatchTris() {
        initUI();
    }

    private void initUI() {
        Game game = new Game();
        add(game);

        setResizable(false);
        pack();

        setTitle("MatchTris");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        for(int i=0; i<args.length; i++) {
            Constants.LOG_LEVEL = Integer.parseInt(args[i]);
        }

        EventQueue.invokeLater(new Runnable() {@
            Override
            public void run() {
                MatchTris ex = new MatchTris();
                ex.setVisible(true);
            }
        });
    }

    
}
