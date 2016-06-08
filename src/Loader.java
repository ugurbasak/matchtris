import javax.microedition.lcdui.Graphics;

import com.nokia.mid.ui.FullCanvas;

public class Loader extends FullCanvas{

	private final MatchTris myMidlet;
	public int totalelements=0;
	public int currentstep=0;

	Loader(MatchTris myMidlet){
		this.myMidlet = myMidlet;
		
	}

	protected void paint(Graphics g) {
		g.setColor(0x000000);
		g.fillRect(0,0,176,208);
		g.setColor(0xffaa00);
		g.drawString("Yukleniyor...",88,104,Graphics.BASELINE|Graphics.HCENTER);
		g.setColor(0xff0000);
		g.drawLine(38,150,138,150);
		g.setColor(0x00ff00);
		g.drawLine(38,150,38+currentstep*(100/totalelements),150);
		g.drawString(""+Runtime.getRuntime().freeMemory(),88,175,Graphics.BASELINE|Graphics.HCENTER);
		g.drawString("Tl:"+Runtime.getRuntime().totalMemory()/1000+"K Fr:"+Runtime.getRuntime().freeMemory()/1000+"K",88,195,Graphics.BASELINE|Graphics.HCENTER);
	}
}
