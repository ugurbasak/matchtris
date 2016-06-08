import java.util.Random;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import com.nokia.mid.ui.FullCanvas;

public class Game extends FullCanvas{

	private MatchTris myMidlet;
	private int GameMode=0;
	public static Random random;
	
	public Image balls =null;
	public Image border = null;
	public Image back = null;

	public int[][] TetrisBoard;
	public int[][] TBTest;
	private long lastDraw;
	private final long speed=250;
	public static final int startX = 4;
	public static final int startY = 4;
	public MatchStone matchstone=null;
	
	private boolean notall=true;

	public boolean Key=false;
	private int action=0;
	private int KeyMove=0;
	private boolean falling=false;
	private int falling_times = 0;
	
	boolean GAMEOVER = false;
	public static int Level=4;
	private int puan=0;

	public Game(MatchTris myMidlet){
		this.myMidlet=myMidlet;
		random = new Random();
		
		//////////initialize
		TetrisBoard = new int[10][20];
		TBTest = new int[10][20];

		//yeni bir tas yaratmak icin
		matchstone = new MatchStone();
		
		for(int i=0; i<10; i++){
			for(int j=0; j<20; j++){
				TetrisBoard[i][j]=0;
				TBTest[i][j]=0;
			}
		}
		lastDraw = System.currentTimeMillis();
	}
	
	protected void paint(Graphics g) {
		if(!GAMEOVER){
			switch(GameMode){
				case 0:
				myPaint(g,notall);
				if(Key){
					if(KeyMove%4 == 0){
						switch(action) {
							case LEFT:
								move(-1);
								notall=false;
								repaint();
								notall=true;
								break;
	
							case RIGHT:
								move(1);
								notall=false;
								repaint();
								notall=true;
								break;
							case DOWN:
								move(0);
								notall=false;
								repaint();
								notall=true;
								KeyMove=3;
								break;
						}
					}
				}
				KeyMove++;
				break;
			}
		}
		else{
			drawAll(g);
			g.drawString("GAME OVER",0,60,16|4);
		}
	}
	
	protected void keyPressed(int keyCode){
		//System.out.println("Key pressed "+getGameAction(keyCode));
		action = getGameAction(keyCode);
		if(!GAMEOVER){
			switch(GameMode){
				case 0:
				switch(action){
					case UP:
						matchstone.Rotate();
						notall=false;
						repaint();
						notall=true;
						break;
					default:
						Key = true;
						KeyMove = 0;
						break;
				}
				break;
			}
		}
	}
	
	protected void keyReleased(int keyCode) {
		System.out.println("Key released "+getGameAction(keyCode));
		Key = false;
		KeyMove = 0;
	}
	protected void keyRepeated(int keyCode) {
		int action = getGameAction(keyCode);
		if(!GAMEOVER){
			if(GameMode == 0){
				switch (action) {
				case UP:
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
					//if(!(falling && TBTest[i][j]==1) || falling_times%2==0){
						g.setClip(startX+i*6,startY+j*6,6,6);
						g.drawImage(balls,startX+i*6-(TetrisBoard[i][j]-1)*6,startY+j*6,16|4);
					//}
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
		//System.out.println("drawing puan ="+puan);
		g.setColor(0x000000);
		g.drawString("Puan",80,5,16|4);
		g.drawString(""+puan,80,15,16|4);
	}
	
	public void move(int direction){
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
/// kontrolü //////////////////
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
/// kontrolü //////////////////
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
/// kontrolü //////////////////
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
/// kontrolü //////////////////
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
			//System.out.println("----------------");
			LetItDown();
			//System.out.println("After LetItDown");
			falling=false;
			CheckIsFull();
			//System.out.println("After 2nd CheckIsFull");
		}
		else
			return false;
		return true;
	}
	
	private void LetItDown(){
		//System.out.println("1");
		for(int i=0; i<10; i++){
			for(int j=19; j>=0; j--){
				if(TBTest[i][j]==1){
					int NoOfMatch = 1;
					while(true){
						//System.out.println("2");
						if(j-NoOfMatch>=0){
							//System.out.println("i="+i+"j-NoOfMatch="+(j-NoOfMatch));
							if(TBTest[i][j] == TBTest[i][j-NoOfMatch])
								NoOfMatch++;
							else break;
						}
						//System.out.println("2 end");
					}
					int m=0;
					while(true){
						//System.out.println("3");
						//if((j-m-NoOfMatch)==0){
						if(j-m<0) break;
						if((j-m-NoOfMatch)<0){
							//System.out.println("j-m="+(j-m));
							TetrisBoard[i][j-m]=0;							
						}
						else{
						//0 -1
						//System.out.println("aa i="+i+"j-NoOfMatch-m="+(j-NoOfMatch-m));
						TetrisBoard[i][j-m] = TetrisBoard[i][j-NoOfMatch-m];
						//System.out.println("bb i="+i+"j-m-1="+(j-m-1));
						}
						if(j-m-1>=0){
						if(TetrisBoard[i][j-m-1]==0 || (j-m)==0 ) break;
						}
						m++;
						//System.out.println("3 end");
					}
					//System.out.println("and exit");
					//for(int m=0; m<NoOfMatch; m++)
					//	TBTest[i][j-m]=0;
				}				
			}
		}
		for(int i=0; i<10; i++){
			for(int j=19; j>=0; j--){
				if(TBTest[i][j]==1) puan+=(Level-3)*(Level-3);
				TBTest[i][j]=0;
			}
		}
		//System.out.println("1 end");
	}
	
	private void CheckGameOver(){
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
			//matchstone.DrawShape(g,balls);
			if(CheckIsFull()){
				System.out.println("here and not finished");
				GAMEOVER=false;
				matchstone = new MatchStone();
				CheckIsFull();
			}
			else{
			//Update();
			//System.out.println("son elemanlar"+TetrisBoard[4][0]);
			System.out.println("GAMEOVER");
			}
		}
	}
}