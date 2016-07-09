import java.util.Random;

/*
import javax.microedition.lcdui.Font;
#import javax.microedition.lcdui.Graphics;
#import javax.microedition.lcdui.Image;
#import javax.microedition.midlet.MIDletStateChangeException;
#import javax.microedition.rms.InvalidRecordIDException;
#import javax.microedition.rms.RecordStore;
#import javax.microedition.rms.RecordStoreException;
#import javax.microedition.rms.RecordStoreFullException;
#import javax.microedition.rms.RecordStoreNotFoundException;
#import javax.microedition.rms.RecordStoreNotOpenException;

#import com.nokia.mid.ui.DirectGraphics;
#import com.nokia.mid.ui.DirectUtils;
#import com.nokia.mid.ui.
*/
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
import java.awt.Image;

public class Game extends Applet implements Runnable {
//public class Game extends 
	private Font font;
	private MatchTris myMidlet;
	private int GameMode=1;
	public static Random random;

    public final int KEY_LEFT_ARROW = 1;
    public final int KEY_RIGHT_ARROW = 2;
    public final int KEY_DOWN_ARROW = 3;
	public final int KEY_UP_ARROW = 4;
    public final int KEY_SOFTKEY1 = 5;
    public final int KEY_SOFTKEY2 = 6;
	
	public Image balls =null;
	public Image border = null;
	public Image back = null;
	public Image splash = null;
	public Image menu = null;
	public Image blue_fonts = null;
	public Image black = null;
	public Image blue = null;
	public Image imgPuan = null;
	//0 1 ... 9 rakamlar?n?n geni?likleri
	//private final int[] WidthOfFonts = { 0,9,16,26,35,44,53,62,71,81,90 };
	private final int[] WidthOfFonts = { 0,8,12,20,28,36,44,52,60,68,76 };
	
	public int[][] TetrisBoard;
	public int[][] TBTest;
	private long lastDraw;
	private final long speed=250;
	public static final int startX = 4;
	public static final int startY = 4;
	public MatchStone matchstone=null;
	
	private boolean notall=true;

	//private DirectGraphics dg;
	int[] transparency = { 0xcc2C3A90,0x332C3A90 };
	public boolean Key=false;
	private int action=0;
	private int KeyMove=0;
	private boolean falling=false;
	private int falling_times = 0;
	
	boolean GAMEOVER = true;
	public static int Level=6;
	private int puan=0;

	private String[] strMenu = {"New Game","Continue","Finish Game",
							"Help","About","Exit","Level","YES","NO"};
	private int[][] MenuModes = {
		{ 0,6,3,4,5  },
		{ 1,2,3,5},
		{ 7,8}
	};
	private long timePressed = 0; //about when a key is pressed or released
	private int flashing=0;
	private int menuPosition=0;

	private int[] HighScores;
	private int Continue = 0;
	
	//private RecordStore rsMatchTris = null;
	

    //private final int NoOfRecords = 11; //toplma record store kay?d?
	//1 tane contiue 20x10 tanede save game icin toplam 11 + 201 = 212tane
	/*RecordStorelar?n final int isimleri olsun sonra degistirirsin istersen*/
	//private final int NoOfRecords = 212; //toplma record store kay?d?
	//MatchStone ile 215 oldu + 2 CellX ve CellY
	//private final int NoOfRecords = 217; //toplma record store kay?d?
	private final int NoOfRecords = 218; //toplma record store kay?d?
	private final int LEVEL=1,HIGHSCORES=2,ISCONTINUE=12,LOADTHEM=13;
	private int transCell=0; //Oyun bitince ekran? transparan yapacak
	
	public Game(MatchTris myMidlet){
		this.myMidlet=myMidlet;
		random = new Random();
		font=new Font("TimesRoman", Font.PLAIN, 12);
			
		//////////initialize
		TetrisBoard = new int[10][20];
		TBTest = new int[10][20];
		HighScores = new int[10];

		//yeni bir tas yaratmak icin
		//matchstone = new MatchStone();
		//burda yaratma
		
		for(int i=0; i<10; i++){
			for(int j=0; j<20; j++){
				TetrisBoard[i][j]=0;
				TBTest[i][j]=0;
			}
		}
		lastDraw = System.currentTimeMillis();
	
/*
Loading and saving game can be impelemented later	
		try {
			//about record store
			byte[] dummy = "0".getBytes();
			rsMatchTris = RecordStore.openRecordStore("MatchTris",true);
			if(rsMatchTris.getNumRecords() != NoOfRecords){
				rsMatchTris.closeRecordStore();
				RecordStore.deleteRecordStore("MatchTris");
				rsMatchTris = RecordStore.openRecordStore("MatchTris",true);
				for(int i=0; i<NoOfRecords; i++)
					rsMatchTris.addRecord(dummy,0,dummy.length);
				System.out.println("Added");
			}
			else{
				//load i?lemlerini yap burada
				Level = GetIntRecord(rsMatchTris,LEVEL);
				for(int i=0; i<10; i++)
					HighScores[i] = GetIntRecord(rsMatchTris,HIGHSCORES+i);
				System.out.println("Level "+Level);
				for(int i=0; i<10; i++)
					System.out.println("HighScores "+HighScores[i]);
				Continue = GetIntRecord(rsMatchTris,ISCONTINUE);
			}
			System.out.println("You have "+rsMatchTris.getNumRecords()+" records");
			rsMatchTris.closeRecordStore();
		} catch (RecordStoreFullException e) {
			e.printStackTrace();
		} catch (RecordStoreNotFoundException e) {
			e.printStackTrace();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		} */
	//}


		//if(dg==null) {
        //	dg=DirectUtils.getDirectGraphics(g);
		//}
        
        //g.setFont(font); UBASAK
}

/* This code looks like old or new keypress code 
		if(!GAMEOVER){
			switch(GameMode){
				case 0:
				myPaint(g,notall);
				if(Key){
					switch(action) {
						case KEY_LEFT_ARROW:
							if((System.currentTimeMillis() - timePressed)>=40){
								move(-1);
								notall=false;
								repaint();
								notall=true;
								timePressed = System.currentTimeMillis();
							}
						break;
	
						case KEY_RIGHT_ARROW:
							if((System.currentTimeMillis() - timePressed)>=40){
								move(1);
								notall=false;
								repaint();
								notall=true;
								timePressed = System.currentTimeMillis();
							}
						break;

						case KEY_DOWN_ARROW:
							if((System.currentTimeMillis() - timePressed)>=10){
								move(0);
								notall=false;
								repaint();
								notall=true;
								timePressed = System.currentTimeMillis();
							}
						break;
					}
				}
				break;

				//Game is going on but flashing cause of Letting it down!
				case 1:
					drawAll(g);
					if(flashing%2==0){
						for(int i=0; i<10; i++){
							for(int j=0; j<20; j++){
								if(TBTest[i][j]==1){
									g.setClip(0,0,128,128);
									g.drawImage(back,i*6+startX,j*6+startY);
								}				
							}
						}
					}
					if(++flashing == 10){
						GameMode = 0;
						LetItDown();
						falling=false;
						CheckIsFull();
					}
				break;
				
				case 2:
					DrawMenu(g,1,true,0);
				break;
				
				case 3:
				//asd
					DrawMenu(g,2,true,0);
				break;

			}				
		}
		else{
			switch(GameMode){
				//SPLASH
				case 1:
					g.drawImage(splash,0,0);
				break;
				//Main menu
				case 2:
					g.drawImage(splash,0,0);
					if(Continue==1){
						DrawMenu(g,1,false,0);
					}
					else{
						DrawMenu(g,0,false,0);
					}
				break;
				
				case 0:
					drawAll(g);
					int xy[][] ={ {startX,startX+6*10,startX+6*10,startX},
								  {128-startY-transCell*6,128-startY-transCell*6,128-startY,128-startY} };
					dg.fillPolygon(xy[0],0,xy[1],0,4,0x88888888);
					//g.drawString("GAME OVER",0,60);
					if(transCell!=20)
						transCell++;
					else{
						g.setColor(0x000000);
						g.drawString("GAME OVER",0,60);
					}
				break;
			}
		}
	} */

	protected void keyPressed(int keyCode){
		//System.out.println("Key pressed "+getGameAction(keyCode));
		action = keyCode;
		//System.out.println(action);
		if(!GAMEOVER){
			if(timePressed==0)
				 timePressed = System.currentTimeMillis();
			switch(GameMode){
				case 0:
					switch(keyCode){
						case KEY_UP_ARROW:
							matchstone.Rotate();
							notall=false;
							repaint();
							notall=true;
							break;
						case KEY_SOFTKEY2:
							//System.out.println("Oyun durduruldu!");
							//SaveGame();
							GameMode=2;
						break;
						default:
							Key = true;
							KeyMove = 0;
							break;
					}
					break;
					case 2:
						switch(keyCode){
							case KEY_DOWN_ARROW:
								menuPosition++;
								menuPosition=menuPosition%MenuModes[1].length;
							break;
								
							case KEY_UP_ARROW:
								menuPosition--;
								if(menuPosition<0) menuPosition+=MenuModes[1].length;
							break;
								
							case KEY_SOFTKEY1:
								if(menuPosition == 0){ 
									System.out.println("Continue");
									GAMEOVER=false;
									GameMode = 0;
								}
								else if(menuPosition == 1){
									Score();
									GAMEOVER = true;
									GameMode = 2;
									//Continue=0;
								}
								else if(menuPosition == 3){
								/*	try {
										myMidlet.destroyApp(true);
									} catch (MIDletStateChangeException e) {
										e.printStackTrace();
									}*/
								   GameMode = 3;
								   menuPosition=0;
								}
						}
					break;

					case 3:
					//System.out.println("menuPosition="+menuPosition);
						switch(keyCode){
							case KEY_DOWN_ARROW:
								menuPosition=(menuPosition+1)%2; 
							break;
							case KEY_UP_ARROW:
								menuPosition=(menuPosition+1)%2;
							break;
							case KEY_SOFTKEY1:
								/*try {
									if(menuPosition==0)	myMidlet.destroyApp(true);
									else				myMidlet.destroyApp(false);
								} catch (MIDletStateChangeException e) {
									e.printStackTrace();
								}*/
//UBASAK
							break;
							//}
						}
				break;
			}
		}
		else{//GAMEOVER = true
			switch(GameMode){
				//SPLASH
				case 1:
					GameMode = 2;
				break;
				
				//Main menu
				case 2:
					switch(keyCode){
						case KEY_DOWN_ARROW:
							menuPosition++;
							//menuPosition=menuPosition%MenuModes[0].length;
							menuPosition=menuPosition%MenuModes[Continue].length;
						break;
						
						case KEY_UP_ARROW:
							menuPosition--;
							//if(menuPosition<0) menuPosition+=MenuModes[0].length;
							if(menuPosition<0) menuPosition+=MenuModes[Continue].length;
						break;
						
						case KEY_LEFT_ARROW:
							if(MenuModes[0][menuPosition] == 6){
								Level--;
								if(Level<=4) Level=8;
							}
							/*try {
								rsMatchTris = RecordStore.openRecordStore("MatchTris",true);
								SetRecord(rsMatchTris,LEVEL,Level);
							} catch (RecordStoreFullException e) {
								e.printStackTrace();
							} catch (RecordStoreNotFoundException e) {
								e.printStackTrace();
							} catch (RecordStoreException e) {
								e.printStackTrace();
							} */
						break;
						
						case KEY_RIGHT_ARROW:
							if(MenuModes[0][menuPosition] == 6){
								Level++;
								//if(Level==9) Level=3;
								if(Level>=9) Level=5;
							}
							/*try {
								rsMatchTris = RecordStore.openRecordStore("MatchTris",true);
								SetRecord(rsMatchTris,LEVEL,Level);
							} catch (RecordStoreFullException e) {
								e.printStackTrace();
							} catch (RecordStoreNotFoundException e) {
								e.printStackTrace();
							} catch (RecordStoreException e) {
								e.printStackTrace();
							}*/
						break;
						
						case KEY_SOFTKEY1:
							if(MenuModes[Continue][menuPosition]==5){//EXIT
								//try {
								//	myMidlet.destroyApp(true);
								//} catch (MIDletStateChangeException e1) {
								//	e1.printStackTrace();
								//}
//UBASAK
							}
							else if(MenuModes[Continue][menuPosition]==3){//HELP
								
							}
							else if(MenuModes[Continue][menuPosition]==0){//NEW GAME
								System.out.println("Starts");
								NewGame();
							}
							else if(MenuModes[Continue][menuPosition]==1){//CONTINUE
								SaveLoad(false);
								System.out.println("Loaded");
								GAMEOVER=false;
								GameMode=0;
								Continue=0;
							}
							else if(MenuModes[Continue][menuPosition]==2){//FINISH
							}
							else if(MenuModes[Continue][menuPosition]==4){//ABOUT
							}
/*							if(menuPosition == 0){ 
								System.out.println("Starts");
								NewGame();
							}
							else if(menuPosition == 4){
								//myMidlet.notifyDestroyed();
								try {
									myMidlet.destroyApp(true);
								} catch (MIDletStateChangeException e1) {
									e1.printStackTrace();
								}
							}
*/							
						break;
					}
				break;
				
				case 0:
				//g.drawString("GAME OVER",0,60);
				//System.out.println("Case 0");
				if(KEY_SOFTKEY1 == keyCode){
					System.out.println("Game Over");
					GameMode = 2;
				}
				break;
			}
		}
	}
	
	protected void keyReleased(int keyCode) {
		//System.out.println("Key "+Key);
		//System.out.println("Key released "+getGameAction(keyCode));
		timePressed = 0;
		Key = false;
		KeyMove = 0;
	}
	protected void keyRepeated(int keyCode) {
		//int action = getGameAction(keyCode);
		action = keyCode;
		if(!Key){
			Key=true;
			System.out.println("false olmus");
		}
		if(!GAMEOVER){
			//if((System.currentTimeMillis() - timePressed)>=50)
			//	timePressed = System.currentTimeMillis();
			if(GameMode == 0){
				//switch (action) {
				switch (keyCode) {
				case KEY_UP_ARROW:
					keyPressed(keyCode);
				break;
				default:
					break;
				}
			}
		}
	}


	private void myPaint(Graphics g,boolean all){
		if(all)			paintBoard(g);
		else			drawAll(g);			
	}

	private void paintBoard(Graphics g){
		drawAll(g);

		g.setClip(0,0,128,128);
		if(!GAMEOVER){
			CheckGameOver();

			matchstone.DrawShape(g,balls);
			if(System.currentTimeMillis()-lastDraw >= speed){
				if(Check()){
					Update();
					//matchstone = null;
					matchstone = new MatchStone();
					//Key = false;
					CheckIsFull();
				}
				else{
					matchstone.CellY++;
				}
				lastDraw = System.currentTimeMillis();
			}
		}
	}
	

	private void drawAll(Graphics g){
		g.setClip(0,0,128,128);
		g.setColor(Color.gray);
		g.fillRect(0,0,128,128);
		for(int i=0; i<10; i++){
			for(int j=0; j<20; j++){
				if(TetrisBoard[i][j]!=0){
						g.setClip(startX+i*6,startY+j*6,6,6);
						g.drawImage(balls,startX+i*6-(TetrisBoard[i][j]-1)*6,startY+j*6, null);
				}
				else{
					g.setClip(0,0,128,128);
					g.drawImage(back,i*6+startX,j*6+startY, null);
				}				
			}
		}
		g.setClip(0,0,128,128);
		for(int i=0; i<32; i++){
			g.drawImage(border,64,i*4, null);
			g.drawImage(border,0,i*4, null);
		}
		for(int i=0; i<16; i++){
			g.drawImage(border,i*4,124, null);
			g.drawImage(border,i*4,0, null);
		}
		/*g.setColor(0x000000);
		g.drawString("Puan",80,5);
		g.drawString(""+puan,80,15);
		*/
		g.drawImage(imgPuan,80,5, null);
		DrawString(g,puan,80,30);
	}
	
	public void move(int direction){
		if(!GAMEOVER && GameMode==0){
			boolean increase = true;
			if(direction == 1){
				for(int i=0; i<3; i++){
					if(matchstone.CellX == 9 || TetrisBoard[matchstone.CellX+1][matchstone.CellY+i]!=0)
						increase = false;
				}
				if(increase) matchstone.CellX++;
			}
			else if(direction == -1){
				for(int i=0; i<3; i++){
					if(matchstone.CellX == 0 || TetrisBoard[matchstone.CellX-1][matchstone.CellY+i]!=0)
						increase = false;
				}
				if(increase) matchstone.CellX--;
			}
			else if(direction == 0){
				if(Check()){
	//				Update();
	//				matchstone = new MatchStone();
	//				CheckIsFull();
					if(!GAMEOVER){
						CheckGameOver();
	  				}
				}
				else{
					matchstone.CellY++;
				}
			}
		}
		else if(GameMode==0 && GAMEOVER==true)
			System.out.println("Hata Buradaym?s");
	}
	private boolean Check(){
		//if(matchstone.CellX<0 || matchstone.CellX>9) System.out.println("x de hata "+matchstone.CellX);
		if(matchstone.CellY+2 == 19 || TetrisBoard[matchstone.CellX][matchstone.CellY+3]!=0)
			return true;
		return false;
	}
	
	private void Update(){
		for(int i=0; i<3; i++){
			if(!(GAMEOVER && matchstone.CellY+i<0))
			TetrisBoard[matchstone.CellX][matchstone.CellY+i] = matchstone.type[i];
		}
	}
	
	private boolean CheckIsFull(){
///////////////////////////////
///	x
///	x
///	x
/// control //////////////////
		//System.out.println("Checking to up");
		for(int i=0; i<10; i++){
			for(int j=19; j>=0; j--){
				//if(TetrisBoard[i][j]!=0 && TBTest[i][j]==0){
				if(TetrisBoard[i][j]!=0){
					int color = TetrisBoard[i][j];
					int NoOfMatch = 1;
					while(true){
						if(j-NoOfMatch>=0){
							//if(j-NoOfMatch<0) System.out.println("j-NoOfMatch<0");
							//if(i<0)			  System.out.println("i<0");
							if(TetrisBoard[i][j] == TetrisBoard[i][j-NoOfMatch])
								NoOfMatch++;
							else
								break;
						}
						else break;
					}
					if(NoOfMatch>=3){
						//System.out.println("will check up "+NoOfMatch);
						for(int m=0; m<NoOfMatch; m++)
							TBTest[i][j-m]=1;
						falling = true;
						//System.out.println("yukari");							
					}
				}
			}
		}
///////////////////////////////////////////////////
///////////////////////////////
///	
///	x x x
///	
/// control //////////////////
  		//System.out.println("Checking to right");
	  	for(int i=0; i<10; i++){
		  	for(int j=19; j>=0; j--){
				if(TetrisBoard[i][j]!=0){
					int color = TetrisBoard[i][j];
					int NoOfMatch = 1;
					while(true){
						if(i+NoOfMatch<10){
							//if(j<0) System.out.println("j<0");
							//if(i+NoOfMatch<0)  System.out.println("i+NoOfMatch<0");

							if(TetrisBoard[i][j] == TetrisBoard[i+NoOfMatch][j])
								NoOfMatch++;
							else
								break;
						}
						else break;
					}
					if(NoOfMatch>=3){
						//System.out.println("will check right "+NoOfMatch);
						for(int m=0; m<NoOfMatch; m++)
							TBTest[i+m][j]=1;
						falling = true;
						//System.out.println("yana");
					}
				}
		  	}
	  	}
///////////////////////////////////////////////////
///////////////////////////////
///	  x
///  x	
///	x
/// control //////////////////
  		//System.out.println("Checking to right horizontal");
		for(int i=0; i<10; i++){
			for(int j=19; j>=0; j--){
				if(TetrisBoard[i][j]!=0){
					int color = TetrisBoard[i][j];
					int NoOfMatch = 1;
					while(true){
						if(i+NoOfMatch<10 && j-NoOfMatch>=0){
							//if(j-NoOfMatch<0) System.out.println("j-NoOfMatch");
							//if(i+NoOfMatch>9)  System.out.println("i+NoOfMatch>9");

							if(TetrisBoard[i][j] == TetrisBoard[i+NoOfMatch][j-NoOfMatch])
							NoOfMatch++;
							else break;
						}
						else break;
					}
					if(NoOfMatch>=3){
						//System.out.println("will check right hor"+NoOfMatch);
						for(int m=0; m<NoOfMatch; m++)
							TBTest[i+m][j-m]=1;
						falling = true;
						//System.out.println("sag capraz");
					}
				}
			}
		}
///////////////////////////////////////////////////
///////////////////////////////
///	x
///  x	
///	  x
/// control //////////////////
  		//System.out.println("Checking to left horizontal");
	    for(int i=0; i<10; i++){
		  for(int j=19; j>=0; j--){
			  if(TetrisBoard[i][j]!=0){
				  int color = TetrisBoard[i][j];
				  int NoOfMatch = 1;
				  while(true){
					  if(i-NoOfMatch>=0 && j-NoOfMatch>=0){
						//if(j-NoOfMatch<0) System.out.println("j-NoOfMatch");
						//if(i-NoOfMatch<0)  System.out.println("i+NoOfMatch<0");

						  if(TetrisBoard[i][j] == TetrisBoard[i-NoOfMatch][j-NoOfMatch])
						  NoOfMatch++;
						  else break;
					  }
					  else break;
				  }
				  if(NoOfMatch>=3){
					  //System.out.println("will check left hor"+NoOfMatch);
					  for(int m=0; m<NoOfMatch; m++){
						  //System.out.println("i-m="+(i-m)+" j-m="+(j-m));
						  TBTest[i-m][j-m]=1;
					  }
					  falling = true;
					  //System.out.println("sol capraz");
				  }
			  }
		  }
    	}
		if(falling){
/*			//System.out.println("----------------");
			LetItDown();
			//System.out.println("After LetItDown");
			falling=false;
			CheckIsFull();
			//System.out.println("After 2nd CheckIsFull");
*/			
			GameMode = 1;
			flashing = 0;
		}
		else
			return false;
		return true;
	}
	
	private void LetItDown(){
		for(int i=0; i<10; i++){
			for(int j=19; j>=0; j--){
				if(TBTest[i][j]==1){
					int NoOfMatch = 1;
					//System.out.println("1st start");
					while(true){
						if(j-NoOfMatch>=0){
							if(TBTest[i][j] == TBTest[i][j-NoOfMatch])
								NoOfMatch++;
							else break;
						}
						else break;
					}
					//System.out.println("1st end");
/*					int k=0;
					while(true){
						System.out.println("in here");
						if(TetrisBoard[i][j-NoOfMatch-k]==0)
							break;
						k++;
					}
					System.out.println("k = "+k);
*/					
/*					//int l=k;
					//int l=0;
					//while(l!=k){
						//TetrisBoard[i][j-l] = TetrisBoard[i][j-NoOfMatch-l];
						//l++;
					//}
					for(int l=0; l<k+1; l++){
						TetrisBoard[i][j-l] = TetrisBoard[i][j-NoOfMatch-l];
					}
					//for(int p=0; p<NoOfMatch; p++){
					//	TetrisBoard[i][j-NoOfMatch-k-p-1]=0;
					//}
					System.out.println("last control");
					for(int m=0; m<NoOfMatch; m++){
						System.out.println(j-NoOfMatch-k+m-1);
						TetrisBoard[i][j-NoOfMatch-k+m-1]=0;
					}
					System.out.println("last control finished");
*/												
					//System.out.println("NoOfMatch "+NoOfMatch+" j= "+j);
					int m=0;
					//if(NoOfMatch==3)
					//System.out.println("2nd start");
					while(true){
						if(j-m<0) break;
						if((j-m-NoOfMatch)<0)
							TetrisBoard[i][j-m]=0;							
						else
							TetrisBoard[i][j-m] = TetrisBoard[i][j-NoOfMatch-m];
						if(j-m-1>=0){
							if(TetrisBoard[i][j-m-1]==0 || (j-m)==0 ) break;
						}
						//yeni eklenti
						//if((k+NoOfMatch-1)==m) break;
						/////////
						m++;
					}
					//System.out.println("2nd end");
					//System.out.println("m = "+m);
					
					for(int u=0; u<NoOfMatch; u++)
						TBTest[i][j-u]=0;
					//puan+=((Level-2)*(Level-2))*NoOfMatch;
					puan+=((Level-4)*(Level-4))*NoOfMatch;
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
	
	private void CheckGameOver(){
		synchronized(TetrisBoard){
			int i=-1;
			if(TetrisBoard[4][0]!=0){
				i=0;
			}
			else if(TetrisBoard[4][1]!=0){
				i=1;
			}
			else if(TetrisBoard[4][2]!=0){
				i=2;
			}
			if(i!=-1){
				GAMEOVER=true;
				matchstone.CellY=-3+i;
				Update();
/*				if(CheckIsFull()){
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
	
	private void DrawMenu(Graphics g,int MenuMode,boolean full,int trans){
		//g.setFont(font);
		g.setClip(0,0,128,128);

		int xy[][][] ={ 
			{ {0,128,128,0}, {32,32,128,128} },
			{ {0,128,128,0}, {0,0,128,128} }		
		};
 
		//dg.fillPolygon(full ? xy[1][0] : xy[0][0],0,full ? xy[1][1] :xy[0][1],0,4,transparency[trans]);
		
        //dg.fillPolygon(full ? xy[1][0] :xy[0][0],0,full ? xy[1][1] :xy[0][1],0,4,transparency[MenuMode]);
		//dg.fillPolygon(xy[MenuMode][0],0,xy[MenuMode][1],0,4,transparency[MenuMode]);

		for(int i=0; i<MenuModes[MenuMode].length; i++){
			if(i==menuPosition) g.setColor(Color.red);
			else g.setColor(Color.white);
			if(MenuModes[MenuMode][i] == 6){
				//int x = font.stringWidth(strMenu[MenuModes[MenuMode][i]]+" "+(Level-4));	
				int x = 10; //UBASAK
                g.drawString(strMenu[MenuModes[MenuMode][i]]+" "+(Level-4),(128-x)/2,40+i*15);
					
			}
			else{
				int x = 10; //UBASAK
				//int x = font.stringWidth(strMenu[MenuModes[MenuMode][i]]);	
				g.drawString(strMenu[MenuModes[MenuMode][i]],(128-x)/2,40+i*15);
			}

			if(MenuMode==2){
				g.setColor(Color.white);
				g.drawString("Do you want to",20,10);
                g.drawString("save your game?",20,20);	
			}
		}
	}
	
	private void NewGame(){
		GAMEOVER=false;
		GameMode = 0;
		for(int i=0; i<10; i++){
			for(int j=0; j<20; j++){
				TetrisBoard[i][j]=0;
				TBTest[i][j]=0;
			}
		}
		lastDraw = System.currentTimeMillis();
		puan=0;
		matchstone = new MatchStone();
		if(matchstone.CellY!=0) System.out.println("uyar? CellY 0 degil");
		matchstone.CellY=0;
		transCell=0;
	}

    /*	
	private int GetIntRecord(RecordStore rs, int no){
		int x=0;
		try {
			byte[] temp = rs.getRecord(no);
			String temp_str = new String(temp,0,temp.length);
			x=Integer.parseInt(temp_str);
		} catch (RecordStoreNotOpenException e) {
			e.printStackTrace();
		} catch (InvalidRecordIDException e) {
			e.printStackTrace();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		}
		return x;
	}
	
	private void SetRecord(RecordStore rs,int no,int var){
		byte[] temp =( (String)(""+var) ).getBytes();
		try {
			rs.setRecord(no,temp,0,temp.length);
		} catch (RecordStoreNotOpenException e) {
			e.printStackTrace();
		} catch (InvalidRecordIDException e) {
			e.printStackTrace();
		} catch (RecordStoreFullException e) {
			e.printStackTrace();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		}
	}*/
	
	private void Score(){
		synchronized(TetrisBoard){
			//Score Kay?t islemleri
			int j=0;
			for(j=1; j<10; j++){
				if(puan>HighScores[0]){
					j=0;
					break;
				}
				else if(puan>HighScores[j]) break;
			}
			System.out.println("my j ="+j);
            /*
			if(j!=10){
				for(int n=9; n>=j; n--){
					if(n!=j)	HighScores[n] = HighScores[n-1];
					else		HighScores[j] = puan;
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
			System.out.println("GameMode ="+GameMode+" GAMEOVER ="+GAMEOVER);
		}
	}
	
	private void DrawString(Graphics g,/*Image image,*/int number,int x,int y){
		String no =""+number;
		int[] ints = new int[no.length()];
		int[] distance = new int[ints.length+1];
		distance[0] = 0; 
		for(int i=0; i<no.length(); i++)
			ints[i] = Integer.parseInt(no.substring(i,i+1));
		for(int i=0; i<ints.length; i++)
			distance[i+1] = distance[i] + WidthOfFonts[ints[i]+1]-WidthOfFonts[ints[i]];
		for(int i=0; i<no.length(); i++){
			g.setClip(x+distance[i]+i,y,distance[i+1]-distance[i],20);
			g.drawImage(blue,x+distance[i]+i-WidthOfFonts[ints[i]],y, null);
		}
		g.setClip(0,0,128,128);
	}
	
	public void SaveLoad(boolean Save){
		if(!Save) matchstone = new MatchStone();
		long timex = System.currentTimeMillis();
		/*
        try {
			rsMatchTris = RecordStore.openRecordStore("MatchTris",true);
			for(int i=0; i<200; i++){
				if(Save)
					SetRecord(rsMatchTris,LOADTHEM+i,TetrisBoard[i/20][i%20]);
				else
					TetrisBoard[i/20][i%20] = GetIntRecord(rsMatchTris,LOADTHEM+i);
			}
			if(Save) SetRecord(rsMatchTris,ISCONTINUE,1);
			for(int i=0; i<3; i++){
				if(Save)
					SetRecord(rsMatchTris,213+i,matchstone.type[i]);
				else
					matchstone.type[i]= GetIntRecord(rsMatchTris,213+i);
			}
			if(Save){
				SetRecord(rsMatchTris,216,matchstone.CellX);
				SetRecord(rsMatchTris,217,matchstone.CellY);
				SetRecord(rsMatchTris,218,puan);
			}
			else{
				matchstone.CellX = GetIntRecord(rsMatchTris,216);
				matchstone.CellY = GetIntRecord(rsMatchTris,217);
				puan = GetIntRecord(rsMatchTris,218);
				SetRecord(rsMatchTris,ISCONTINUE,0);
			}
			rsMatchTris.closeRecordStore();
		} catch (RecordStoreFullException e) {
			e.printStackTrace();
		} catch (RecordStoreNotFoundException e) {
			e.printStackTrace();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		} */
		timex = System.currentTimeMillis() - timex;
		System.out.println("timepassed in ms "+timex+" in sec "+(timex/1000));
	}




 private Thread myThread;
    public boolean isRunnning = false;
        public void paint(Graphics g) {

        }

        public void init() {

        }

         public void run() {

        }

}
