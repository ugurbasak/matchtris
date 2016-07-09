//import javax.microedition.lcdui.Command;
//import javax.microedition.lcdui.CommandListener;
//import javax.microedition.lcdui.Display;
//import javax.microedition.lcdui.Displayable;
//import javax.microedition.lcdui.Form;
//import javax.microedition.lcdui.List;
//import javax.microedition.lcdui.StringItem;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Menu { // implements CommandListener{

	public int currentmode=0;
	
	//private List myList;
	//private Form myForm;
	//private StringItem myStringItem;
	//private Command cmdBack;
	//private Command cmdMenu;
	//private Command cmdOk;
	//private MatchTris myMidlet;
	
	public Menu(/*MatchTris myMidlet*/){
		//this.myMidlet=myMidlet;
		//cmdOk=new Command("Tamam",Command.SCREEN,1);
		//cmdMenu=new Command("Menu",Command.BACK,1);
		//cmdBack=new Command("Geri",Command.BACK,1);
	}
	
	public void openMenu(int menumode){

		currentmode=menumode;
		
		switch(menumode){
			
			case 0:
				/* Must be a menu but it can be re-implemented
                myList = null;
				myList = new List("Shoot Me", List.IMPLICIT);
				myList.append("Yeni oyun", null);
				myList.append("Ayarlar", null);
				myList.append("Yardim", null);
				myList.append("Test", null);
				myList.append("Kapat", null);
				myList.addCommand(cmdOk);
				myList.setCommandListener(this);
				Display.getDisplay(myMidlet).setCurrent(myList);
            */
			break;
		}
	}
	
	public void commandAction(/*Command c, Displayable d*/) {
		
/*		if(c==cmdOk){
			switch(currentmode){
				case 0:
					if(myList.getSelectedIndex()==0){
						
					}
					else if(myList.getSelectedIndex()==1){
						
					}
					else if(myList.getSelectedIndex()==2){
						
					}
					else if(myList.getSelectedIndex()==3){
						
					}
					else if(myList.getSelectedIndex()==4){
						
					}
				break;
			}
		}else if(c==cmdBack){
			
		}else if(c==cmdMenu){
			openMenu(0);
		} */
	}
	
}
