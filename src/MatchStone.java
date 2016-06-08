import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class MatchStone {

	public int CellX;
	public int CellY;

	public int[] type;
	
	public MatchStone() {
		CellX = 4;
		CellY = 0;
		type = new int[3];
		for(int i=0; i<3; i++)
			type[i] = Math.abs(Game.random.nextInt())%Game.Level+1; 
	}
	
	public void DrawShape(Graphics g,Image image){
		for(int i=0; i<3; i++){
			if(this.CellY+i>=0){
				g.setClip(Game.startX+this.CellX*6,Game.startY+this.CellY*6+i*6,6,6);
				g.drawImage(image,Game.startX+this.CellX*6-(this.type[i]-1)*6,Game.startY+this.CellY*6+i*6,16|4);
			}
			//else System.out.println("Dont draw "+(this.CellY+i));
		}
	}
	
	public void Rotate(){
		int temp[] = new int[3];
		for(int i=0; i<3; i++)
			temp[i] = this.type[(i+1)%3];
		System.arraycopy(temp,0,this.type,0,3);
	}
}