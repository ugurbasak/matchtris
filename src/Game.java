import java.util.Random;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;

import com.nokia.mid.ui.DirectGraphics;
import com.nokia.mid.ui.DirectUtils;
import com.nokia.mid.ui.FullCanvas;

public class Game extends FullCanvas{
	private Font font;
	private MatchTris myMidlet;
	private int GameMode=1;
	public static Random random;
	
	public Image balls =null;
	public Image border = null;
	public Image back = null;
	public Image splash = null;
	//public Image menu = null;
	//public Image blue_fonts = null;
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
	public MatchStone next = null;
	
	private boolean notall=true;

	private DirectGraphics dg;
	int[] transparency = { 0xcc2C3A90,0x332C3A90 };
	public boolean Key=false;
	private int action=0;
	private int KeyMove=0;
	private boolean falling=false;
	private int falling_times = 0;
	
	boolean GAMEOVER = true;
	public static int Level=5;
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
	
	private RecordStore rsMatchTris = null;
	private final int NoOfRecords = 221; //toplma record store kay?d?
	private final int LEVEL=1,HIGHSCORES=2,ISCONTINUE=12,LOADTHEM=13;
	private int transCell=0; //Oyun bitince ekran? transparan yapacak
	private boolean extra=false;
	
	
	public Game(MatchTris myMidlet){
		this.myMidlet=myMidlet;
		random = new Random();
		font=Font.getFont(Font.FACE_SYSTEM,Font.STYLE_PLAIN,Font.SIZE_SMALL);
			
		//////////initialize
		TetrisBoard = new int[10][20];
		TBTest = new int[10][20];
		HighScores = new int[10];

		for(int i=0; i<10; i++){
			for(int j=0; j<20; j++){
				TetrisBoard[i][j]=0;
				TBTest[i][j]=0;
			}
		}
		lastDraw = System.currentTimeMillis();
		
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
				SetRecord(rsMatchTris,LEVEL,5);
				System.out.println("Added");
			}
			else{
				//load i?lemlerini yap burada
				Level = GetIntRecord(rsMatchTris,LEVEL);
				for(int i=0; i<10; i++)
					HighScores[i] = GetIntRecord(rsMatchTris,HIGHSCORES+i);
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
		}
	}
	
	protected void paint(Graphics g) {
		if(dg==null)	dg=DirectUtils.getDirectGraphics(g);
		g.setFont(font);

		if(!GAMEOVER){
			switch(GameMode){
				case 0:
				myPaint(g,notall);
				if(Key){
					switch(action) {
						case FullCanvas.KEY_LEFT_ARROW:
							if((System.currentTimeMillis() - timePressed)>=40){
								move(-1);
								notall=false;
								repaint();
								notall=true;
								timePressed = System.currentTimeMillis();
							}
						break;
	
						case FullCanvas.KEY_RIGHT_ARROW:
							if((System.currentTimeMillis() - timePressed)>=40){
								move(1);
								notall=false;
								repaint();
								notall=true;
								timePressed = System.currentTimeMillis();
							}
						break;

						case FullCanvas.KEY_DOWN_ARROW:
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
									g.drawImage(back,i*6+startX,j*6+startY,16|4);
								}				
							}
						}
					}
					if(++flashing == 10){
						GameMode = 0;
						LetItDown();
						Order();
						falling=false;
						CheckIsFull();
					}
				break;
				
				case 2:
					DrawMenu(g,1,true,0);
				break;
				
				case 3:
					DrawMenu(g,2,true,0);
				break;

			}				
		}
		else{
			switch(GameMode){
				//SPLASH
				case 1:
					g.drawImage(splash,0,0,16|4);
				break;
				//Main menu
				case 2:
					g.drawImage(splash,0,0,16|4);
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
					if(transCell!=20)
						transCell++;
					else{
						g.setColor(0x000000);
						g.drawString("GAME OVER",0,60,16|4);
					}
				break;
			}
		}
	}
	
	protected void keyPressed(int keyCode){
		action = keyCode;
		if(!GAMEOVER){
			if(timePressed==0)
				 timePressed = System.currentTimeMillis();
			switch(GameMode){
				case 0:
					switch(keyCode){
						case FullCanvas.KEY_UP_ARROW:
							matchstone.Rotate();
							notall=false;
							repaint();
							notall=true;
							break;
						case FullCanvas.KEY_SOFTKEY2:
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
							case FullCanvas.KEY_DOWN_ARROW:
								menuPosition++;
								menuPosition=menuPosition%MenuModes[1].length;
							break;
								
							case FullCanvas.KEY_UP_ARROW:
								menuPosition--;
								if(menuPosition<0) menuPosition+=MenuModes[1].length;
							break;
								
							case FullCanvas.KEY_SOFTKEY1:
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
								   GameMode = 3;
								   menuPosition=0;
								}
						}
					break;

					case 3:
						switch(keyCode){
							case FullCanvas.KEY_DOWN_ARROW:
								menuPosition=(menuPosition+1)%2; 
							break;
							case FullCanvas.KEY_UP_ARROW:
								menuPosition=(menuPosition+1)%2;
							break;
							case FullCanvas.KEY_SOFTKEY1:
								try {
									if(menuPosition==0)	myMidlet.destroyApp(true);
									else				myMidlet.destroyApp(false);
								} catch (MIDletStateChangeException e) {
									e.printStackTrace();
								}
							break;
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
						case FullCanvas.KEY_DOWN_ARROW:
							menuPosition++;
							menuPosition=menuPosition%MenuModes[Continue].length;
						break;
						
						case FullCanvas.KEY_UP_ARROW:
							menuPosition--;
							if(menuPosition<0) menuPosition+=MenuModes[Continue].length;
						break;
						
						case FullCanvas.KEY_LEFT_ARROW:
							if(MenuModes[0][menuPosition] == 6){
								Level--;
								if(Level<=4) Level=8;
							}
							try {
								rsMatchTris = RecordStore.openRecordStore("MatchTris",true);
								SetRecord(rsMatchTris,LEVEL,Level);
							} catch (RecordStoreFullException e) {
								e.printStackTrace();
							} catch (RecordStoreNotFoundException e) {
								e.printStackTrace();
							} catch (RecordStoreException e) {
								e.printStackTrace();
							}
						break;
						
						case FullCanvas.KEY_RIGHT_ARROW:
							if(MenuModes[0][menuPosition] == 6){
								Level++;
								if(Level>=9) Level=5;
							}
							try {
								rsMatchTris = RecordStore.openRecordStore("MatchTris",true);
								SetRecord(rsMatchTris,LEVEL,Level);
							} catch (RecordStoreFullException e) {
								e.printStackTrace();
							} catch (RecordStoreNotFoundException e) {
								e.printStackTrace();
							} catch (RecordStoreException e) {
								e.printStackTrace();
							}
						break;
						
						case FullCanvas.KEY_SOFTKEY1:
							if(MenuModes[Continue][menuPosition]==5){//EXIT
								try {
									myMidlet.destroyApp(false);
								} catch (MIDletStateChangeException e1) {
									e1.printStackTrace();
								}
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
								Score();
								GAMEOVER = true;
								GameMode = 2;
								Continue=0;
							}
							else if(MenuModes[Continue][menuPosition]==4){//ABOUT
						}
						break;
					}
				break;
				
				case 0:
				if(FullCanvas.KEY_SOFTKEY1 == keyCode){
					System.out.println("Game Over");
					GameMode = 2;
				}
				break;
			}
		}
	}
	
	protected void keyReleased(int keyCode) {
		timePressed = 0;
		Key = false;
		KeyMove = 0;
	}
	protected void keyRepeated(int keyCode) {
		action = keyCode;
		if(!Key){
			Key=true;
			System.out.println("false olmus");
		}
		if(!GAMEOVER){
			if(GameMode == 0){
				switch (keyCode) {
				case FullCanvas.KEY_UP_ARROW:
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
			if(Check())	CheckGameOver();

			matchstone.DrawShape(g,balls);

			if(System.currentTimeMillis()-lastDraw >= speed){
				if(Check()){
					Update();
					matchstone = next;
					next = new MatchStone();

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
		g.setColor(0x808080);
		g.fillRect(0,0,128,128);
		for(int i=0; i<10; i++){
			for(int j=0; j<20; j++){
				if(TetrisBoard[i][j]!=0){
						g.setClip(startX+i*6,startY+j*6,6,6);
						g.drawImage(balls,startX+i*6-(TetrisBoard[i][j]-1)*6,startY+j*6,16|4);
				}
				else{
					g.setClip(0,0,128,128);
					g.drawImage(back,i*6+startX,j*6+startY,16|4);
				}				
			}
		}
		g.setClip(0,0,128,128);
		for(int i=0; i<32; i++){
			g.drawImage(border,64,i*4,16|4);
			g.drawImage(border,0,i*4,16|4);
		}
		for(int i=0; i<16; i++){
			g.drawImage(border,i*4,124,16|4);
			g.drawImage(border,i*4,0,16|4);
		}

		g.drawImage(imgPuan,80,5,16|4);
		DrawString(g,puan,80,30);
		
		for(int i=0; i<3; i++){
			g.setClip(100,60+i*6,6,6);
			g.drawImage(balls,100-6*(next.type[i]-1),60+i*6,16|4);
		}
	}
	
	public void move(int direction){
		if(!GAMEOVER && GameMode==0){
			boolean increase = true;
			if(direction == 1){
				for(int i=0; i<3; i++){
					if(matchstone.CellY+i>=0)//yeni ekledim
					if(matchstone.CellX == 9 || TetrisBoard[matchstone.CellX+1][matchstone.CellY+i]!=0)
						increase = false;
				}
				if(increase) matchstone.CellX++;
			}
			else if(direction == -1){
				for(int i=0; i<3; i++){
					if(matchstone.CellY+i>=0)//yeni ekledim
					if(matchstone.CellX == 0 || TetrisBoard[matchstone.CellX-1][matchstone.CellY+i]!=0)
						increase = false;
				}
				if(increase) matchstone.CellX--;
			}
			else if(direction == 0){
				if(Check()){
					if(!GAMEOVER){
						CheckGameOver();
					}
				}
				else
					matchstone.CellY++;
			}
		}
		else if(GameMode==0 && GAMEOVER==true)
			System.out.println("Hata Buradaym?s");
	}
	private boolean Check(){
		if(matchstone.CellY+2 == 19 || TetrisBoard[matchstone.CellX][matchstone.CellY+3]!=0)
			return true;
		return false;
	}
	
	private void Update(){
		for(int i=0; i<3; i++){
			if(!(GAMEOVER && matchstone.CellY+i<0)){
				if(matchstone.CellY+i>=0)
				TetrisBoard[matchstone.CellX][matchstone.CellY+i] = matchstone.type[i];
			}
		}
	}
	
	private boolean CheckIsFull(){
///////////////////////////////
///	x
///	x
///	x
/// kontrolü //////////////////
		//System.out.println("Checking to up");
		for(int i=0; i<10; i++){
			for(int j=19; j>=0; j--){
				if(TetrisBoard[i][j]!=0){
					int color = TetrisBoard[i][j];
					int NoOfMatch = 1;
					while(true){
						if(j-NoOfMatch>=0){
							if(TetrisBoard[i][j] == TetrisBoard[i][j-NoOfMatch])
								NoOfMatch++;
							else
								break;
						}
						else break;
					}
					if(NoOfMatch>=3){
						for(int m=0; m<NoOfMatch; m++)
							TBTest[i][j-m]=1;
						falling = true;
					}
				}
			}
		}
///////////////////////////////////////////////////
///////////////////////////////
///	
///	x x x
///	
/// kontrolü //////////////////
	  	for(int i=0; i<10; i++){
		  	for(int j=19; j>=0; j--){
				if(TetrisBoard[i][j]!=0){
					int color = TetrisBoard[i][j];
					int NoOfMatch = 1;
					while(true){
						if(i+NoOfMatch<10){
							if(TetrisBoard[i][j] == TetrisBoard[i+NoOfMatch][j])
								NoOfMatch++;
							else	break;
						}
						else break;
					}
					if(NoOfMatch>=3){
						for(int m=0; m<NoOfMatch; m++)
							TBTest[i+m][j]=1;
						falling = true;
					}
				}
		  	}
	  	}
///////////////////////////////////////////////////
///////////////////////////////
///	  x
///  x	
///	x
/// kontrolü //////////////////
		for(int i=0; i<10; i++){
			for(int j=19; j>=0; j--){
				if(TetrisBoard[i][j]!=0){
					int color = TetrisBoard[i][j];
					int NoOfMatch = 1;
					while(true){
						if(i+NoOfMatch<10 && j-NoOfMatch>=0){
							if(TetrisBoard[i][j] == TetrisBoard[i+NoOfMatch][j-NoOfMatch])
							NoOfMatch++;
							else break;
						}
						else break;
					}
					if(NoOfMatch>=3){
						for(int m=0; m<NoOfMatch; m++)
							TBTest[i+m][j-m]=1;
						falling = true;
					}
				}
			}
		}
///////////////////////////////////////////////////
///////////////////////////////
///	x
///  x	
///	  x
/// kontrolü //////////////////
	    for(int i=0; i<10; i++){
		  for(int j=19; j>=0; j--){
			  if(TetrisBoard[i][j]!=0){
				  int color = TetrisBoard[i][j];
				  int NoOfMatch = 1;
				  while(true){
					  if(i-NoOfMatch>=0 && j-NoOfMatch>=0){
						  if(TetrisBoard[i][j] == TetrisBoard[i-NoOfMatch][j-NoOfMatch])
						  NoOfMatch++;
						  else break;
					  }
					  else break;
				  }
				  if(NoOfMatch>=3){
					  for(int m=0; m<NoOfMatch; m++){
						  TBTest[i-m][j-m]=1;
					  }
					  falling = true;
				  }
			  }
		  }
    	}
		if(falling){
			GameMode = 1;
			flashing = 0;
		}
		else	return false;
		return true;
	}
	
	private void LetItDown(){
		for(int i=0; i<10; i++){
			for(int j=19; j>=0; j--){
				if(TBTest[i][j]==1){
					int NoOfMatch = 1;
					while(true){
						if(j-NoOfMatch>=0){
							if(TBTest[i][j] == TBTest[i][j-NoOfMatch])
								NoOfMatch++;
							else break;
						}
						else break;
					}
					int m=0;
					while(true){
						if(j-m<0) break;
						if((j-m-NoOfMatch)<0)
							TetrisBoard[i][j-m]=0;							
						else
							TetrisBoard[i][j-m] = TetrisBoard[i][j-NoOfMatch-m];
						if(j-m-1>=0){
							if(TetrisBoard[i][j-m-1]==0 || (j-m)==0 ) break;
						}
						m++;
					}
					for(int u=0; u<NoOfMatch; u++)
						TBTest[i][j-u]=0;
					puan+=((Level-4)*(Level-4))*NoOfMatch;
				}				
			}
		}
	}
	
	private void CheckGameOver(){
		synchronized(TetrisBoard){
			if(System.currentTimeMillis()-lastDraw >= (speed-50)){
				if(matchstone.CellY<0){
					Update();
					if(!CheckIsFull()){
						GAMEOVER=true;
						Update();
						Score();
					}
					else{
						extra=true;
					}
				}
			}
		}
	}
	
	private void DrawMenu(Graphics g,int MenuMode,boolean full,int trans){
		g.setClip(0,0,128,128);

		int xy[][][] ={ 
			{ {0,128,128,0}, {32,32,128,128} },
			{ {0,128,128,0}, {0,0,128,128} }		
		};
 
		dg.fillPolygon(full ? xy[1][0] : xy[0][0],0,full ? xy[1][1] :xy[0][1],0,4,transparency[trans]);

		for(int i=0; i<MenuModes[MenuMode].length; i++){
			if(i==menuPosition) g.setColor(0xff0000);
			else g.setColor(0xffffff);
			if(MenuModes[MenuMode][i] == 6){
				int x = font.stringWidth(strMenu[MenuModes[MenuMode][i]]+" "+(Level-4));	
				g.drawString(strMenu[MenuModes[MenuMode][i]]+" "+(Level-4),(128-x)/2,40+i*15,16|4);
					
			}
			else{
				int x = font.stringWidth(strMenu[MenuModes[MenuMode][i]]);	
				g.drawString(strMenu[MenuModes[MenuMode][i]],(128-x)/2,40+i*15,16|4);
			}

			if(MenuMode==2){
				g.setColor(0xffffff);
				g.drawString("Do you want to",20,10,16|4);				g.drawString("save your game?",20,20,16|4);	
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

		if(matchstone.CellY!=-2) System.out.println("uyar? CellY 0 degil");
		matchstone.CellY=-2;

		transCell=0;

		next = new MatchStone();		
	}
	
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
	}
	
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
			}
		}
	}
	
	private void DrawString(Graphics g,int number,int x,int y){
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
			g.drawImage(blue,x+distance[i]+i-WidthOfFonts[ints[i]],y,16|4);
		}
		g.setClip(0,0,128,128);
	}
	
	public void SaveLoad(boolean Save){
		if(!Save) {
			matchstone = new MatchStone();
			next = new MatchStone();
		}
		long timex = System.currentTimeMillis();
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
				if(Save){
					SetRecord(rsMatchTris,213+i,matchstone.type[i]);
					SetRecord(rsMatchTris,221+i,next.type[i]);
				}
				else{
					matchstone.type[i]= GetIntRecord(rsMatchTris,213+i);
					next.type[i]= GetIntRecord(rsMatchTris,221+i);
				}
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
		}
		timex = System.currentTimeMillis() - timex;
		System.out.println("timepassed in ms "+timex+" in sec "+(timex/1000));
	}
	
	private void Order(){
		if(extra){
			if(matchstone.CellY<0){
				int here =0;
				boolean fall=false;
				for(int j=0; j<20; j++){
					if(TetrisBoard[matchstone.CellX][j]!=0){
						fall=true;
						here=j-1;
						break;
					}
				}
				for(int i=0; i<(matchstone.CellY*(-1)); i++){
					TetrisBoard[matchstone.CellX][here+i]=matchstone.type[(matchstone.CellY*(-1))-i-1];
				}
			}
			matchstone = next;
			next = new MatchStone();
			extra=false;
		}
	}
}