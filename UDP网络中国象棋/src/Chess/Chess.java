package Chess;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import javax.swing.JPanel;
public class Chess {
	public static final short REDP=1;
	public static final short BLACKP=0;
	public short player;
	public String typeName;
	public int x,y;
	private Image chessImage;
	private int leftX=28,leftY=20;
	
	public Chess(short player, String typeName,int x,int y) {
		this.player=player;
		this.typeName=typeName;
		this.x=x;
		this.y=y;
		if(player==REDP) {
			switch(typeName) {
			case "帅":
				chessImage=Toolkit.getDefaultToolkit().getImage("image/chess7.png");
				break;
			case "仕":
				chessImage = Toolkit.getDefaultToolkit().getImage("image/chess8.png");
				break;
			case "相":
				chessImage = Toolkit.getDefaultToolkit().getImage("image/chess9.png");
				break;
			case "马":
				chessImage = Toolkit.getDefaultToolkit().getImage("image/chess10.png");
				break;
			case "车":
				chessImage = Toolkit.getDefaultToolkit().getImage("image/chess11.png");
				break;
			case "炮":
				chessImage = Toolkit.getDefaultToolkit().getImage("image/chess12.png");
				break;
			case "兵":
				chessImage = Toolkit.getDefaultToolkit().getImage("image/chess13.png");
				break;		
			}
		}
			else {
				switch(typeName) {
				case "将":
					chessImage = Toolkit.getDefaultToolkit().getImage("image/chess0.png");
					break;
				case "士":
					chessImage = Toolkit.getDefaultToolkit().getImage("image/chess1.png");
					break;
				case "象":
					chessImage = Toolkit.getDefaultToolkit().getImage("image/chess2.png");
					break;
				case "马":
					chessImage = Toolkit.getDefaultToolkit().getImage("image/chess3.png");
					break;
				case "车":
					chessImage = Toolkit.getDefaultToolkit().getImage("image/chess4.png");
					break;
				case "炮":
					chessImage = Toolkit.getDefaultToolkit().getImage("image/chess5.png");
					break;
				case "卒":
					chessImage = Toolkit.getDefaultToolkit().getImage("image/chess6.png");
					break;
				}
			}
		}
	
	public void setPos(int x,int y) {
		this.x=x;
		this.y=y;
	}
	public void ReversePos() {
		x=9-x;
		y=8-y;
	}
	protected void paint(Graphics g,JPanel i) {
		g.drawImage(chessImage,leftX+y*62,leftY+x*57,40,40,(ImageObserver)i);
	}
	public void DrawSelectedChess(Graphics g) {
		g.drawRect(leftX+y*62,leftY+x*57,40,40);
	}
}
