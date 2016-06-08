import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class MatchTris extends MIDlet{
		
	private Loader myLoader;
	private GameLooper myGame_looper;
	public Game myGame;
	public Menu myMenu;


	protected void startApp() throws MIDletStateChangeException {
		myLoader = new Loader(this);
		myLoader.totalelements=6;
		Display.getDisplay(this).setCurrent(myLoader);
		
		myGame = new Game(this);
		myLoader.currentstep++;
		myLoader.repaint();
		
		myMenu = new Menu(this);
		myLoader.currentstep++;
		myLoader.repaint();
		
		myGame_looper = new GameLooper(this,myGame);
		myLoader.currentstep++;
		myLoader.repaint();
		
		myGame.balls = LoadImage("/images/balls.png");
		myGame.back  = LoadImage("/images/back.png");
		myGame.border= LoadImage("/images/border.png");
		
		Display.getDisplay(this).setCurrent(myGame);
	}

	protected void pauseApp() {
	}

	protected void destroyApp(boolean b) throws MIDletStateChangeException {
		notifyDestroyed();
	}
	public Image LoadImage(String str) {
		try {
			myLoader.currentstep++;
			myLoader.repaint();
			return Image.createImage(str);
		} catch (Exception err) {
			Alert myAlert=new Alert("NO!",str,null,AlertType.INFO);
			Display.getDisplay(this).setCurrent(myAlert);
			return null;
		} catch (OutOfMemoryError e) {
			Alert myAlert=new Alert("NO!",str,null,AlertType.INFO);
			Display.getDisplay(this).setCurrent(myAlert);
			return null;
		}
	}

}