//import javax.microedition.lcdui.Graphics;

//import com.nokia.mid.ui.FullCanvas;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Loader extends Applet {

	private final MatchTris myMidlet;
	public int totalelements=0;
	public int currentstep=0;

	Loader(MatchTris myMidlet){
		this.myMidlet = myMidlet;
		
	}

    @Override
	public void paint(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0,0,176,208);
		g.setColor(Color.orange);
		g.drawString("Yukleniyor...",88,104);
		g.setColor(Color.red);
		g.drawLine(38,150,138,150);
		g.setColor(Color.green);
		g.drawLine(38,150,38+currentstep*(100/totalelements),150);
		g.drawString(""+Runtime.getRuntime().freeMemory(),88,175);
		g.drawString("Tl:"+Runtime.getRuntime().totalMemory()/1000+"K Fr:"+Runtime.getRuntime().freeMemory()/1000+"K",88,195);
	}
}
