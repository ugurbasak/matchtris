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
		//myLoader.totalelements=11;
		myLoader.totalelements=9;
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
		myGame.splash= LoadImage("/images/splash.png");
		//myGame.menu= LoadImage("/images/menu.png");
		//myGame.blue_fonts= LoadImage("/images/blue_fonts.png");
		myGame.black= LoadImage("/images/black.png");
		myGame.blue= LoadImage("/images/blue.png");
		myGame.imgPuan= LoadImage("/images/puan.png");

		Display.getDisplay(this).setCurrent(myGame);
	}

	protected void pauseApp() {
		System.out.println("PAUSE");
	}

	protected void destroyApp(boolean b) throws MIDletStateChangeException {
		System.out.println("DESTROY");
		System.out.println("here and try to exit b="+b);
		if(b) myGame.SaveLoad(true);
		System.out.println("exit");
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