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
	public Image bar = null;
	public Image baralt = null;
	
	public Image rakam = null;
	public Image BigBall = null;

	//0 1 ... 9 rakamlar?n?n geni?likleri
	//private final int[] WidthOfFonts = { 0,9,16,26,35,44,53,62,71,81,90 };
	//private final int[] WidthOfFonts = { 0,8,12,20,28,36,44,52,60,68,76 }; //eski blue'nun
	private final int[] WidthOfFonts = { 0,10,20,30,40,50,60,70,80,90,100 };
	
	public int[][] TetrisBoard;
	public int[][] TBTest;
	private long lastDraw;
	//private final long speed=250;
	private final long speed=500;
	public static final int startX = 4;
	public static final int startY = 4;
	public MatchStone matchstone=null;
	public MatchStone next = null;
	
	private boolean notall=true;

	private DirectGraphics dg;
	int[] transparency = { 0xcc2C3A90,0x332C3A90 };
	public boolean Key=false;
	private int action=0;
	private boolean falling=false;
	private int falling_times = 0;
	
	boolean GAMEOVER = true;
	public static int Level=5;
	private int puan=0;
	private int ToBonus;//bonusun c?kmas?na ne kadar kald?g?

	private String[] strMenu = {"New Game","Continue","Finish Game",
							"Help","About","Exit","Level","YES","NO","High Scores","Ses Acik","Titresim Acik","Ayarlar"};
							//3		4		5		6		7	8		9			10		11			12
	private int[][] MenuModes = {
		//{ 0,6,3,4,5  },
		//{ 0,6,9,3,4,5  },
		//{ 0,6,9,10,11,3,4,5  },
		{ 0,9,12,3,4,5  },//6,10,11
		{ 1,2,3,10,11,5},//finish game
		{ 7,8},
		{6,10,11}//ayarlar
	};
	private long timePressed = 0; //about when a key is pressed or released
	private int flashing=0;
	private int menuPosition=0;

	private int[] HighScores;
	private int Continue = 0;
	
	private RecordStore rsMatchTris = null;
	private final int NoOfRecords = 227; //toplma record store kay?d?
	private final int LEVEL=1,HIGHSCORES=2,ISCONTINUE=12,LOADTHEM=13;
	private int transCell=0; //Oyun bitince ekran? transparan yapacak
	
	private boolean BonusPosition=false;
	
	private int BonusX;
	private int BonusY;
	private int flying=0;
	private int color;
	private int BonusLimit=250;//The number of balls to achive bonus
	private boolean newscore=false;
	private int score_sira=0;
	private boolean ayarlar=false;
	
	private int ses=1;
	private int  titresim=1;

	private int save_game_true = 0;
	private boolean backfill=false;
	private boolean doldur=false;

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
				Continue = GetIntRecord(rsMatchTris,ISCONTINUE);
				//ses ve titresim
				ses = GetIntRecord(rsMatchTris,224);
				titresim = GetIntRecord(rsMatchTris,225);
				if(titresim==1)	strMenu[11] = "Titresim Acik";
				else strMenu[11] = "Titresim Kapali";
				if(ses==1)	strMenu[10] = "Ses Acik";
				else strMenu[10] = "Ses Kapali";
				save_game_true = GetIntRecord(rsMatchTris,226);
				System.out.println("save_game_true="+save_game_true);
				if(save_game_true==1)	Continue=1;
				else					Continue=0;
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
						if(action == FullCanvas.KEY_LEFT_ARROW || action== FullCanvas.KEY_RIGHT_ARROW || action == FullCanvas.KEY_NUM4 || action == FullCanvas.KEY_NUM6){
							//if(matchstone.CellY<2)
							if(matchstone.CellY==0)
								keyPressed(action);
							else if(System.currentTimeMillis()-timePressed>100){
								flying++;
								if(flying==2){
									keyPressed(action);
									flying=0;
								}
							}
						}
						else if((action==FullCanvas.KEY_DOWN_ARROW || action == FullCanvas.KEY_NUM5)&& System.currentTimeMillis()-timePressed>10)
							keyPressed(action);
					}
				break;

				//Game is going on but flashing cause of Letting it down!
				case 1:
					drawAll(g);
					if(flashing%2==0){
					//if(flashing%10==0){
						g.setClip(0,0,this.getWidth(),this.getHeight());
						for(int i=0; i<10; i++){
							for(int j=0; j<20; j++){
								if(TBTest[i][j]==1){
									g.drawImage(back,i*6+startX,j*6+startY,16|4);
								}				
							}
						}
					}
					//if(++flashing == 50){
					if(++flashing == 10){
						GameMode = 0;
						LetItDown();
						//Order();
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
				
				case 4://Help
					drawAll(g);
					matchstone.DrawShape(g,balls);
					int xy_[][] ={	{0,128,128,0}, {0,0,128,128} };		
					dg.fillPolygon(xy_[0],0,xy_[1],0,4,0xcc2C3A90);
					g.setClip(0,0,this.getWidth(),this.getHeight());
					g.setColor(0xffffff);
					g.drawString("Amac: taslardan yatay,",0,0+10,16|4);
					g.drawString("dikey veya capraz olarak",0,10+10,16|4);
					g.drawString("en azindan 3lu gruplar",0,20+10,16|4);
					g.drawString("olusturmak...",0,30+10,16|4);	 
					g.drawString("Yukari ok veya 8 degistir",0,50+10,16|4);
					g.drawString("Asagi ok veya 5 asagi git",0,60+10,16|4);
					g.drawString("Sol ok veya 4 ile sola git",0,70+10,16|4);
					g.drawString("Sag ok veya 6 ile saga git",0,80+10,16|4);
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
					if(ayarlar){
						DrawMenu(g,3,true,0);
					}
					else{
						if(Continue==1){
							//DrawMenu(g,1,false,0);
							DrawMenu(g,1,true,0);
						}
						else{
							//DrawMenu(g,0,false,0);
							DrawMenu(g,0,true,0);
						}
					}
				break;
				
				case 3://Help
					g.drawImage(splash,0,0,16|4);
					int xy_[][] ={	{0,128,128,0}, {0,0,128,128} };		
					dg.fillPolygon(xy_[0],0,xy_[1],0,4,0xcc2C3A90);
					
					g.setColor(0xffffff);
					g.drawString("Amac: taslardan yatay,",0,0+10,16|4);
					g.drawString("dikey veya capraz olarak",0,10+10,16|4);
					g.drawString("en azindan 3lu gruplar",0,20+10,16|4);
					g.drawString("olusturmak...",0,30+10,16|4);	 
					g.drawString("Yukari ok veya 8 degistir",0,50+10,16|4);
					g.drawString("Asagi ok veya 5 asagi git",0,60+10,16|4);
					g.drawString("Sol ok veya 4 ile sola git",0,70+10,16|4);
					g.drawString("Sag ok veya 6 ile saga git",0,80+10,16|4);
				break;

				case 4://About
					g.drawImage(splash,0,0,16|4);
					int xy__[][] ={	{0,128,128,0}, {0,0,128,128} };		
					dg.fillPolygon(xy__[0],0,xy__[1],0,4,0xcc2C3A90);
	
					g.setColor(0xffffff);
					g.drawString("Seuba Software Solutions",1,10,16|4);
					g.drawString("MatchTris ver:1.37",1,30,16|4);
					g.drawString("Programlama:",1,50,16|4);
					g.drawString("		 Ugur Basak",1,60,16|4);
					g.drawString("Grafik tasarim:",1,70,16|4);
					g.drawString(" 		 Onur Basak",1,80,16|4);	 
					g.drawString("Muzik:",1,90,16|4);
					g.drawString("		 Ugur Basak",1,100,16|4);
				break;
				
				case 5://High Scores
					g.drawImage(splash,0,0,16|4);
					int xy___[][] ={	{0,128,128,0}, {0,0,128,128} };		
					dg.fillPolygon(xy___[0],0,xy___[1],0,4,0xcc2C3A90);
					//scores
					g.setColor(0xff0000);
					g.drawString("Sira			Puan",25,5,16|4);
					g.setColor(0xffffff);
					g.drawLine(15,14,110,14);
					for(int i=0; i<10; i++){
						if(newscore && i==score_sira){
							g.setColor(0x00ff00);
							g.drawString(""+(i+1)+". 			"+HighScores[i],25,18+i*10,16|4);
							g.setColor(0xffffff);
						}
						else g.drawString(""+(i+1)+". 			"+HighScores[i],25,18+i*10,16|4);						
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
						g.setClip(0,0,128,128);
						g.drawString("GAME OVER",5,60,16|4);
					}

/*					drawAll(g);
					int xy[][] ={ {startX,startX+6*10,startX+6*10,startX},
								  {128-startY-transCell*6,128-startY-transCell*6,128-startY,128-startY} };
					dg.fillPolygon(xy[0],0,xy[1],0,4,0x88888888);
					if(transCell!=20 && doldur==false){
						transCell++;
						if(transCell==20){
							transCell=0;
							//backfill=
							doldur=true;
						}
					}
					else{
						g.setColor(0x000000);
						g.setClip(0,0,this.getWidth(),this.getHeight());
						if(backfill){
							g.fillRect(0,0,128,128);
							g.setColor(0xffffff);
							g.drawString("GAME OVER",30,50,16|4);
						}
						else if(!backfill){
							if(transCell!=64){
								for(int i=0; i<transCell; i++){
									g.drawLine(0,i,128,i);
									g.drawLine(0,128-i,128,128-i);
								}
								//transCell++;
								transCell+=1;
								if(transCell==64){
									transCell=0;
									backfill=true;
								}
							}

						}
					}*/
				break;
			}
		}
	}
	
	protected void keyPressed(int keyCode){
		action = keyCode;
		Key=true;//yeni
		
		if(!GAMEOVER){
			if(timePressed==0)	timePressed = System.currentTimeMillis();
			switch(GameMode){
				case 0:
					switch(keyCode){
						case FullCanvas.KEY_UP_ARROW:
						case FullCanvas.KEY_NUM2:
							matchstone.Rotate();
							notall=false;
							repaint();
							notall=true;
							break;

						case FullCanvas.KEY_LEFT_ARROW:
						case FullCanvas.KEY_NUM4:
							move(-1);
							notall=false;
							repaint();
							notall=true;
						break;

						case FullCanvas.KEY_RIGHT_ARROW:
						case FullCanvas.KEY_NUM6:
								move(1);
								notall=false;
								repaint();
								notall=true;
						break;

						case FullCanvas.KEY_DOWN_ARROW:
						case FullCanvas.KEY_NUM5:
								move(0);
								notall=false;
								repaint();
								notall=true;
						break;

						case FullCanvas.KEY_SOFTKEY2:
							GameMode=2;
						break;
					}
					break;
					case 2:
						switch(keyCode){
							case FullCanvas.KEY_DOWN_ARROW:
							case FullCanvas.KEY_NUM5:
								menuPosition++;
								menuPosition=menuPosition%MenuModes[1].length;
							break;
								
							case FullCanvas.KEY_UP_ARROW:
							case FullCanvas.KEY_NUM2:
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
									//GameMode = 2;
									GameMode = 5;
								}
								else if(menuPosition == 2){
									GameMode = 4;
								}
								else if(menuPosition == 3){
									//System.out.println("ses");
									ses=(ses+1)%2;
									if(ses==1)	strMenu[10] = "Ses Acik";
									else strMenu[10] = "Ses Kapali";
									try {
										rsMatchTris = RecordStore.openRecordStore("MatchTris",true);
										SetRecord(rsMatchTris,224,ses);
										rsMatchTris.closeRecordStore();
									} catch (RecordStoreFullException e1) {
										e1.printStackTrace();
									} catch (RecordStoreNotFoundException e1) {
										e1.printStackTrace();
									} catch (RecordStoreException e1) {
										e1.printStackTrace();
									}


								}
								else if(menuPosition == 4){
									//System.out.println("titresim");
									titresim=(titresim+1)%2;
									if(titresim==1)	strMenu[11] = "Titresim Acik";
									else strMenu[11] = "Titresim Kapali";
									
									//SetRecord(rsMatchTris,225,titresim);
									try {
										rsMatchTris = RecordStore.openRecordStore("MatchTris",true);
										SetRecord(rsMatchTris,225,titresim);
										rsMatchTris.closeRecordStore();
									} catch (RecordStoreFullException e1) {
										e1.printStackTrace();
									} catch (RecordStoreNotFoundException e1) {
										e1.printStackTrace();
									} catch (RecordStoreException e1) {
										e1.printStackTrace();
									}
								}
								else if(menuPosition == 5){
								   GameMode = 3;
								   menuPosition=0;
								}
						}
					break;

					case 3:
						switch(keyCode){
							case FullCanvas.KEY_DOWN_ARROW:
							case FullCanvas.KEY_NUM5:
								menuPosition=(menuPosition+1)%2; 
							break;
							case FullCanvas.KEY_UP_ARROW:
							case FullCanvas.KEY_NUM2:
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
					
					case 4://help
						//GameMode=0;					
						GameMode=2;
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
						case FullCanvas.KEY_NUM5:
							menuPosition++;
							if(ayarlar) menuPosition=menuPosition%MenuModes[3].length;
							else menuPosition=menuPosition%MenuModes[Continue].length;
						break;
						
						case FullCanvas.KEY_UP_ARROW:
						case FullCanvas.KEY_NUM2:
							menuPosition--;
							if(menuPosition<0){
								if(ayarlar) menuPosition+=MenuModes[3].length;
								else menuPosition+=MenuModes[Continue].length;
							}
						break;
						
						case FullCanvas.KEY_LEFT_ARROW:
						case FullCanvas.KEY_NUM4:
							//if(MenuModes[0][menuPosition] == 6){
							if(MenuModes[0][menuPosition] == 0){
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
						case FullCanvas.KEY_NUM6:
							//if(MenuModes[0][menuPosition] == 6){
							if(MenuModes[0][menuPosition] == 0){
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
							if(!ayarlar){
								if(MenuModes[Continue][menuPosition]==0){//NEW GAME
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
									try {
										rsMatchTris = RecordStore.openRecordStore("MatchTris",true);
										SetRecord(rsMatchTris,226,0);
										rsMatchTris.closeRecordStore();
									} catch (RecordStoreFullException e1) {
										e1.printStackTrace();
									} catch (RecordStoreNotFoundException e1) {
										e1.printStackTrace();
									} catch (RecordStoreException e1) {
										e1.printStackTrace();
									}

								}
								else if(MenuModes[Continue][menuPosition]==3){//HELP
									//System.out.println("GameMode="+GameMode);
									GameMode = 3;							
								}
								else if(MenuModes[Continue][menuPosition]==4){//ABOUT
									GameMode = 4;
								}
								else if(MenuModes[Continue][menuPosition]==5){//EXIT
									try {
										myMidlet.destroyApp(false);
									} catch (MIDletStateChangeException e1) {
										e1.printStackTrace();
									}
								}
								else if(MenuModes[Continue][menuPosition]==9){//High Scores
									GameMode=5;
								}
								else if(MenuModes[Continue][menuPosition]==12){//Ayarlar
									//GameMode=5;
									ayarlar=true;
									menuPosition=0;
								}
							}
							else{
								if(MenuModes[3][menuPosition]==10){//ses
									//System.out.println("ses");
									ses=(ses+1)%2;
									if(ses==1)	strMenu[10] = "Ses Acik";
									else strMenu[10] = "Ses Kapali";
									//SetRecord(rsMatchTris,224,ses);
									try {
										rsMatchTris = RecordStore.openRecordStore("MatchTris",true);
										SetRecord(rsMatchTris,224,ses);
										rsMatchTris.closeRecordStore();
									} catch (RecordStoreFullException e1) {
										e1.printStackTrace();
									} catch (RecordStoreNotFoundException e1) {
										e1.printStackTrace();
									} catch (RecordStoreException e1) {
										e1.printStackTrace();
									}

								}
								else if(MenuModes[3][menuPosition]==11){//titresim
									//System.out.println("titresim");
									titresim=(titresim+1)%2;
									if(titresim==1)	strMenu[11] = "Titresim Acik";
									else strMenu[11] = "Titresim Kapali";
									//SetRecord(rsMatchTris,224,titresim);
									try {
										rsMatchTris = RecordStore.openRecordStore("MatchTris",true);
										SetRecord(rsMatchTris,225,titresim);
										rsMatchTris.closeRecordStore();
									} catch (RecordStoreFullException e1) {
										e1.printStackTrace();
									} catch (RecordStoreNotFoundException e1) {
										e1.printStackTrace();
									} catch (RecordStoreException e1) {
										e1.printStackTrace();
									}


								}
							}
						break;
						
						case FullCanvas.KEY_SOFTKEY2:
							if(ayarlar){
								ayarlar=false;
								menuPosition=0;
							}			
						break;
					}
				break;
				
				case 3://help
					//if(keyCode == FullCanvas.KEY_SOFTKEY2)
					if(keyCode == FullCanvas.KEY_SOFTKEY2 || keyCode == FullCanvas.KEY_SOFTKEY1)
						GameMode=2;				
				break;

				case 4://about
					//if(keyCode == FullCanvas.KEY_SOFTKEY2)
					if(keyCode == FullCanvas.KEY_SOFTKEY2 || keyCode == FullCanvas.KEY_SOFTKEY1)
						GameMode=2;				
				break;
				
				case 5://high scores
					//if(keyCode == FullCanvas.KEY_SOFTKEY2){
					if(keyCode == FullCanvas.KEY_SOFTKEY2 || keyCode == FullCanvas.KEY_SOFTKEY1){
						GameMode=2;
						newscore=false;						
					}
				
				break;

				
				case 0:
				if(FullCanvas.KEY_SOFTKEY1 == keyCode){
					System.out.println("Game Over");
					//GameMode = 2;
					GameMode=5;
				}
				break;
			}
		}
	}
	
	protected void keyReleased(int keyCode) {
		timePressed = 0;
		flying=0;
		Key = false;
	}

	private void myPaint(Graphics g,boolean all){
		if(all)			paintBoard(g);
		else			drawAll(g);			
	}

	private void paintBoard(Graphics g){
		drawAll(g);

		g.setClip(0,0,this.getWidth(),this.getHeight());
		if(!GAMEOVER){
			if(Check())	CheckGameOver();
			if(GAMEOVER){
				matchstone.DrawShape(g,balls);
				return;
			}
			matchstone.DrawShape(g,balls);

			if(System.currentTimeMillis()-lastDraw >= speed){
				if(Check()){
					Update();
					matchstone = next;

					if(TetrisBoard[4][0]!=0){
						if(!CheckIsFull()){
							Score();
							GAMEOVER=true;
							return;
						}
					}
					else if(TetrisBoard[4][1]!=0){
						matchstone.CellY = -2;
						Update();
						if(!CheckIsFull()){
							System.out.println("game over -2");
							Score();
							GAMEOVER=true;
							return;
						}
					}
						
					else if(TetrisBoard[4][2]!=0){
						matchstone.CellY = -1;
						Update();
						if(!CheckIsFull()){
							System.out.println("game over -1");
							Score();
							GAMEOVER=true;
							return;
						}
					}

					/*if(ToBonus<1){
						ToBonus+=100;
						//next=new MatchStone(true);
						//matchstone=new MatchStone(true);
						next=new MatchStone(false);
					}
					else	next = new MatchStone(false);*/
					next=new MatchStone();;

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
		g.setClip(0,0,this.getWidth(),this.getHeight());
		g.setColor(0x808080);
		g.fillRect(0,0,128,128);
		
		for(int i=0; i<20; i++){
			for(int j=0; j<20; j++){
				g.drawImage(back,4+i*6,4+j*6,16|4);
			}
		}
		
		for(int i=0; i<10; i++){
			for(int j=0; j<20; j++){
				if(TetrisBoard[i][j]!=0){
						g.setClip(startX+i*6,startY+j*6,6,6);
						g.drawImage(balls,startX+i*6-(TetrisBoard[i][j]-1)*6,startY+j*6,16|4);
				}
				/*else{
					g.setClip(0,0,this.getWidth(),this.getHeight());
					g.drawImage(back,i*6+startX,j*6+startY,16|4);
				}*/				
			}
		}
		g.setClip(0,0,this.getWidth(),this.getHeight());

		DrawBorder(g);

		DrawNumber(g,puan,71,10);
		
		for(int i=0; i<3; i++){
			g.setClip(78,55+i*16,16,16);
			g.drawImage(BigBall,78-16*(next.type[i]-1),55+i*16,16|4);
		}

		
		//bar 90 pixel den olusuyo
		//int bonus_h = (100-ToBonus)*9/10;
		g.setClip(0,0,this.getWidth(),this.getHeight());
		g.drawImage(baralt,108,34,16|4);
		int bonus_h = (BonusLimit-ToBonus)*90/BonusLimit;
		
		g.setClip(108,124-bonus_h,16,bonus_h);
		g.drawImage(bar,108,34,16|4);
	}
	
	private void move(int direction){
		if(!GAMEOVER && GameMode==0 && matchstone.CellY>=0){
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
					if(!GAMEOVER)	CheckGameOver();
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
		if(matchstone.type[0]==9){
			BonusPosition=true;
			BonusX=matchstone.CellX;
			BonusY=matchstone.CellY;
			if(BonusY!=17){
			for(int i=0; i<3; i++)
				matchstone.type[i]=TetrisBoard[BonusX][BonusY+3];
			}
		}
		for(int i=0; i<3; i++){
			if(!(GAMEOVER && matchstone.CellY+i<0)){
				if(matchstone.CellY+i>=0)
				TetrisBoard[matchstone.CellX][matchstone.CellY+i] = matchstone.type[i];
			}
		}
	}
	
	private boolean CheckIsFull(){
		if(BonusPosition){
			color=9;
			if(BonusY!=17)
			color = TetrisBoard[BonusX][BonusY+3];
			for(int i=0; i<10; i++){
				for(int j=19; j>=0; j--){
					if(TetrisBoard[i][j]==color)
						TBTest[i][j]=1;
				}
			}
			falling = true;
		}
		{
		///////////////////////////////
		///	x
		///	x
		///	x
		/// kontrolü //////////////////
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
		}
		if(falling){
			GameMode = 1;
			flashing = 0;
		}
		else	return false;
		return true;
	}
	
	private void LetItDown(){
		if(BonusPosition){
			int bonus_points=0;
			boolean still_down=true;
			while(still_down){
				still_down=false;
				for(int i=0; i<10; i++){
					for(int j=19; j>=0; j--){
						TBTest[i][j]=0;
						if(TetrisBoard[i][j]==color){
							for(int k=j; k>=0; k--){
								if(k-1<0) TetrisBoard[i][k] = 0;
								else TetrisBoard[i][k] = TetrisBoard[i][k-1];
							}
							bonus_points++;
							still_down=true;
						}			
					}
				}
			}
			BonusPosition=false;
			puan+=bonus_points*(Level-4)*(Level-4);
			ToBonus-=bonus_points;

		}
		else{
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
						//ToBonus-=((Level-4)*(Level-4))*NoOfMatch;
						ToBonus-=NoOfMatch;
					}				
				}
			}
			
		}
		
		if(ToBonus<1){
			next.type[0]=9;
			next.type[1]=9;
			next.type[2]=9;
			//matchstone.type[0]=9;
			//matchstone.type[1]=9;
			//matchstone.type[2]=9;
			//ToBonus+=100;
			ToBonus+=BonusLimit;
			
		}
	}
	
	private void CheckGameOver(){
		//System.out.println("CheckGameOver");
		synchronized(TetrisBoard){
			if(System.currentTimeMillis()-lastDraw >= (speed-50)){
				if(matchstone.CellY<0){
					System.out.println("CheckGameOver");
					Update();
					System.out.println("Updated and isfull? CheckGameOver");
					if(!CheckIsFull()){
						System.out.println("GameOver CheckGameOver");
						GAMEOVER=true;
						Update();
						Score();
					}
					//else{
					//	System.out.println("Extra");
					//	extra=true;
					//}
				}
			}			
		}
	}
	
	private void DrawMenu(Graphics g,int MenuMode,boolean full,int trans){
		int starting = (128-MenuModes[MenuMode].length*15)/2;
		//System.out.println("menu mode="+starting);	

		g.setClip(0,0,this.getWidth(),this.getHeight());
		
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
				//g.drawString(strMenu[MenuModes[MenuMode][i]]+" "+(Level-4),(128-x)/2,40+i*15,16|4);
				g.drawString(strMenu[MenuModes[MenuMode][i]]+" "+(Level-4),(128-x)/2,starting+i*15,16|4);
					
			}
			else{
				int x = font.stringWidth(strMenu[MenuModes[MenuMode][i]]);	
				//g.drawString(strMenu[MenuModes[MenuMode][i]],(128-x)/2,40+i*15,16|4);
				g.drawString(strMenu[MenuModes[MenuMode][i]],(128-x)/2,starting+i*15,16|4);
			}

			if(MenuMode==2){
				g.setColor(0xffffff);
				g.drawString("Do you want to",20,10,16|4);				g.drawString("save your game?",20,20,16|4);	
			}
		}
	}
	
	private void NewGame(){
		save_game_true=0;
		try {
			rsMatchTris = RecordStore.openRecordStore("MatchTris",true);
			SetRecord(rsMatchTris,ISCONTINUE,0);
			rsMatchTris.closeRecordStore();
		} catch (RecordStoreFullException e) {
			e.printStackTrace();
		} catch (RecordStoreNotFoundException e) {
			e.printStackTrace();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		}
		
		backfill=false;
		doldur=true;
		
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
		//ToBonus=100;
		ToBonus=BonusLimit;
		
		matchstone = new MatchStone();
		BonusPosition=false;
		matchstone.CellY=0;
		transCell=0;

		next = new MatchStone();
		newscore=false;
/*		//for testing only
		TetrisBoard[4][19]=1;
		TetrisBoard[4][18]=2;
		TetrisBoard[4][17]=3;
		TetrisBoard[4][16]=4;
		TetrisBoard[4][15]=5;
		TetrisBoard[4][14]=6;
		TetrisBoard[4][13]=7;
		TetrisBoard[4][12]=8;
		TetrisBoard[4][11]=1;
		TetrisBoard[4][10]=2;
		TetrisBoard[4][9] =3;
		TetrisBoard[4][8] =4;
		TetrisBoard[4][7] =5;
		TetrisBoard[4][6] =6;
		TetrisBoard[4][5] =7;
		TetrisBoard[4][4] =2;
		TetrisBoard[4][3] =2;
		TetrisBoard[4][2] =1;
		//TetrisBoard[4][1] =1;
		//TetrisBoard[4][0] =3;
		
		//next.type[0]=9;
		//next.type[1]=9;
		//next.type[2]=9;
		next.type[0]=1;
		next.type[1]=1;
		next.type[2]=1;


		matchstone.type[0]=1;
		matchstone.type[1]=2;
		matchstone.type[2]=1;
		
		matchstone.CellX=0;
		matchstone.CellY=0;*/
		//next.type[4]=1;
		save_game_true=1;
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
			score_sira=j;
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
		newscore = true;
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
				if(Save)	SetRecord(rsMatchTris,LOADTHEM+i,TetrisBoard[i/20][i%20]);
				else		TetrisBoard[i/20][i%20] = GetIntRecord(rsMatchTris,LOADTHEM+i);
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
			//if(save_game_true==1){
				if(Save){
					System.out.println("save_game_true="+save_game_true);
					SetRecord(rsMatchTris,226,save_game_true);
				}
			//}

			
			if(Save){
				SetRecord(rsMatchTris,216,matchstone.CellX);
				SetRecord(rsMatchTris,217,matchstone.CellY);
				SetRecord(rsMatchTris,218,puan);
				SetRecord(rsMatchTris,227,ToBonus);
			}
			else{
				matchstone.CellX = GetIntRecord(rsMatchTris,216);
				matchstone.CellY = GetIntRecord(rsMatchTris,217);
				puan = GetIntRecord(rsMatchTris,218);
				SetRecord(rsMatchTris,ISCONTINUE,0);
				ToBonus = GetIntRecord(rsMatchTris,227);
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

	private void DrawBorder(Graphics g){
		///1. sol üst
		///2. sa? üst
		///3. sol alt
		///4. sa? alt
		///5. üst ba?lant?
		///6. alt ba?lant?
		///7. sol ba?lant?
		///8. sa? ba?lant?
		///9. dik boru
		//10.  yatay boru
		
		int wh=4;
		for(int i=0; i<32; i++){
			g.setClip(0+i*wh,0,wh,wh);//yatay borular ust
			g.drawImage(border,0+i*wh-9*wh,0,16|4);
			
			g.setClip(0+i*wh,128-wh,wh,wh);//yatay borular alt
			g.drawImage(border,0+i*wh-9*wh,128-wh,16|4);
		}
		
		for(int i=17; i<32; i++){
			g.setClip(0+i*wh,30,wh,wh);//yatay borular ust
			g.drawImage(border,0+i*wh-9*wh,30,16|4);
		}
		
		for(int i=0; i<32; i++){//dikey
			g.setClip(0,0+i*wh,wh,wh);
			g.drawImage(border,0-8*wh,0+i*wh,16|4);
			
			g.setClip(64,0+i*wh,wh,wh);
			g.drawImage(border,64-8*wh,0+i*wh,16|4);
			
			g.setClip(128-wh,0+i*wh,wh,wh);
			g.drawImage(border,128-wh-8*wh,0+i*wh,16|4);
		}
		
		for(int i=8; i<32; i++){
			g.setClip(128-24,0+i*wh,wh,wh);
			g.drawImage(border,128-24-8*wh,0+i*wh,16|4);
		}
		
		
		g.setClip(0,0,wh,wh);//sol ust
		g.drawImage(border,0,0,16|4);
		
		g.setClip(124,0,wh,wh);//sag ust
		g.drawImage(border,124-wh,0,16|4);
		
		g.setClip(0,124,wh,wh);//sol alt
		g.drawImage(border,0-2*wh,124,16|4);
		
		g.setClip(124,124,wh,wh);//sag alt
		g.drawImage(border,124-3*wh,124,16|4);
		
		g.setClip(64,0,wh,wh);//ust orta
		g.drawImage(border,64-4*wh,0,16|4);
		
		g.setClip(64,124,wh,wh);//alt orta
		g.drawImage(border,64-5*wh,124,16|4);

		g.setClip(104,30,wh,wh);//ust orta
		g.drawImage(border,104-4*wh,30,16|4);
		
		g.setClip(104,124,wh,wh);//alt orta
		g.drawImage(border,104-5*wh,124,16|4);

		g.setClip(64,30,wh,wh);//sol orta
		g.drawImage(border,64-6*wh,30,16|4);
		
		g.setClip(124,30,wh,wh);//sag orta
		g.drawImage(border,124-7*wh,30,16|4);
	}
	
	private void DrawNumber(Graphics g,int number, int x, int y){
		String no =""+number;
		int length = no.length();
		int[] ints = new int[5];

		for(int i=5-length; i<5; i++)
			ints[i] = Integer.parseInt(no.substring(i-5+length,i-5+length+1));
		
		int boyut = 9;
		
		for(int i=0; i<5; i++){
			g.setClip(x+i*(boyut+1),y,boyut,14);
			g.drawImage(rakam,x+i*(boyut+1)-ints[i]*boyut,y,16|4);
		}
		
		g.setClip(0,0,this.getWidth(),this.getHeight());
	}
}