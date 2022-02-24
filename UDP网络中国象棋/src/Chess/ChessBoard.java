package Chess;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Chess.Chess;
import Chess.GameClient;
import Chess.Node;
public class ChessBoard extends JPanel implements Runnable{
	public static final short REDP=1;
	public static final short BLACKP=0;
	public static Chess[] chess=new Chess[32];
	public static int[][] map=new int [10][9];
	public Image bufferImage;
	public static Chess firstChess=null;
	public Chess secondChess=null;
	private boolean isFirstClick=true;
	private int x1,x2,y1,y2;
	private static int tempX;
	private static int tempY;
	private boolean isMyTurn=true;
	public short LocalPlayer=REDP;
	private String message="";
	private boolean flag=false;
	private int otherPort=3003;
	private int receivePort=3004;
	public ArrayList<Node>list=new ArrayList<Node>();
	private String ip="127.0.0.1";
	
	private void initMap() {//棋盘初始化
		for(int i=0;i<10;i++) {
			for(int j=0;j<9;j++)
				map[i][j]=-1;
		}
	}
	public static Chess getFirstChess() {
		return firstChess;
	}
	public static int[][] getMap(){
		return map;
	}
	public ChessBoard(boolean isAI) {
		initMap();
		addMouseListener(new MouseAdapter() {
			int index1=-1,index2=-1,index3=-1,index4=-1;
			public void mouseClicked(MouseEvent e) {
				if(index2!=0&&index4!=16) {
						selectedChess(e);
						repaint();
				}
				else {
					if(index2==0)
					{
						JOptionPane.showConfirmDialog(null, "红方赢了","提示",JOptionPane.DEFAULT_OPTION);
					}
					else if(index4==16) {
						JOptionPane.showConfirmDialog(null, "黑方赢了","提示",JOptionPane.DEFAULT_OPTION);
					}
				}
			}
				private void selectedChess(MouseEvent e) {
					index1=-1;index2=-1;index3=-1;index4=-1;
					if(isFirstClick) {
						firstChess=analyse(e.getX(),e.getY());
						x1=tempX;
						y1=tempY;
						if(firstChess!=null)
						if(firstChess.player!=LocalPlayer)
							return ;
						isFirstClick=false;
					}
					else {
						secondChess=analyse(e.getX(),e.getY());
						x2=tempX;y2=tempY;
						if(secondChess!=null) {
							if(secondChess.player==LocalPlayer) {
								firstChess=secondChess;
								x1=tempX;
								y1=tempY;
								secondChess=null;
								repaint();
								return ;
							}
						}
						if(secondChess==null) {
							if(Ruler.IsAbleToMove(firstChess,x2,y2)) {
								index1=map[x1][y1];
								map[x1][y1]=-1;
								map[x2][y2]=index1;
								chess[index1].setPos(x2,y2);
								list.add(new Node(index1,x2,y2,x1,y1,-1));
								isFirstClick=true;
								repaint();
								AlphaBetaNode result=(new AlphaBetaAlgorithm()).search(ChessBoard.this);
								int toX=result.to[0],toY=result.to[1];
								int frX=result.from[0],frY=result.from[1];
								if(map[toX][toY]==-1)
								{
									int xx=map[frX][frY];
									index3=xx;
									map[frX][frY]=-1;
									map[toX][toY]=index3;
									chess[index3].setPos(toX,toY);
								}
								else {
									index3=map[frX][frY];
									index4=map[toX][toY];
									map[frX][frY]=-1;
									map[toX][toY]=index3;
									chess[index3].setPos(toX,toY);
									chess[index4]=null;
								}
								repaint();
							}
							return ;
					}
					if(secondChess!=null&&Ruler.IsAbleToMove(firstChess,x2,y2)) {
						index1=map[x1][y1];
						index2=map[x2][y2];
						map[x1][y1]=-1;
						map[x2][y2]=index1;
						chess[index1].setPos(x2,y2);
						chess[index2]=null;
						list.add(new Node(index1,x2,y2,x1,y1,index2));
						isFirstClick=true;
						repaint();
						if(index2==0)
						{
							JOptionPane.showConfirmDialog(null, "红方赢了","提示",JOptionPane.DEFAULT_OPTION);
							repaint();
							return ;
						}
						AlphaBetaNode result=(new AlphaBetaAlgorithm()).search(ChessBoard.this);
						int toX=result.to[0],toY=result.to[1];
						int frX=result.from[0],frY=result.from[1];
						if(map[toX][toY]==-1)
						{
							index3=map[frX][frY];
							map[frX][frY]=-1;
							map[toX][toY]=index3;
							chess[index3].setPos(toX,toY);
						}
						else {
							index3=map[frX][frY];
							index4=map[toX][toY];
							map[frX][frY]=-1;
							map[toX][toY]=index3;
							chess[index3].setPos(toX,toY);
							chess[index4]=null;
						}
						repaint();
					}
				}
			}
		});
	}
	public ChessBoard() {
		initMap();
		message="程序处于等待联机状态！";
		
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(isMyTurn==false) {
					message="现在该对方走棋";
					repaint();
					return ;
				}
				selectedChess(e);
				repaint();
			}
			
			private void selectedChess(MouseEvent e) {
				int index1,index2;
				if(isFirstClick) {
					firstChess=analyse(e.getX(),e.getY());
					x1=tempX;
					y1=tempY;
					if(firstChess.player!=LocalPlayer) {
						message="您无法操控对方棋子";
						return ;
					}
					isFirstClick=false;
				}
				else {
					secondChess=analyse(e.getX(),e.getY());
					x2=tempX;
					y2=tempY;
					if(secondChess!=null) {
						if(secondChess.player==LocalPlayer) {
							firstChess=secondChess;
							x1=tempX;
							y1=tempY;
							secondChess=null;
							return ;
						}
					}
					
					if(secondChess==null) {
						if(Ruler.IsAbleToMove(firstChess,x2,y2)) {
							index1=map[x1][y1];
							map[x1][y1]=-1;
							map[x2][y2]=index1;
							chess[index1].setPos(x2,y2);
							
							send("move"+"|"+index1+"|"+(9-x2)+"|"+(8-y2)+"|"+(9-x1)+"|"+(8-y1)+"|"+"-1|");
							list.add(new Node(index1,x2,y2,x1,y1,-1));
							isFirstClick=true;
							repaint();
							SetMyTurn(false);
						}
						else {
							message="不符合规则";
						}
						return ;
					}
					
					if(secondChess!=null&&Ruler.IsAbleToMove(firstChess,x2,y2)) {
						isFirstClick=true;
						index1=map[x1][y1];
						index2 = map[x2][y2];
						map[x1][y1] = -1;
						map[x2][y2] = index1;
						chess[index1].setPos(x2, y2);
						chess[index2] = null;
						repaint();
						send("move"+"|"+index1+"|"+(9-x2)+"|"+(8-y2)+"|"+(9-x1)+"|"+(8-y1)+"|"+index2+"|");
						list.add(new Node(index1,x2,y2,x1,y1,index2));//记录我方下棋信息
						if(index2 == 0){//被吃掉的是将
							message = "红方赢了";
							JOptionPane.showConfirmDialog(null, "红方赢了","提示",JOptionPane.DEFAULT_OPTION);
							//send
							send("succ"+"|"+"红方赢了"+"|");
							return ;
						}
						if(index2 == 16){//被吃掉的是帅
							message = "黑方赢了";
							JOptionPane.showConfirmDialog(null, "黑方赢了","提示",JOptionPane.DEFAULT_OPTION);
							//send
							send("succ"+"|"+"黑方赢了"+"|");
							return ;
						}
						SetMyTurn(false);//该对方了
					}else{//不能吃子
						message = "不能吃子";
					}
				}
			}
			
		
		});
	}
	public static void updateMap(int x,int y,int value) {
		map[x][y]=value;
	}
	public static Chess analyse(int x,int y) {
		int leftX=28,leftY=20;
		int index_x=-1,index_y=-1;
		for(int i=0;i<=9;i++) {
			for(int j=0;j<=8;j++) {
				Rectangle r=new Rectangle(leftX+j*62,leftY+i*57,40,40);
				if(r.contains(x,y)) {
					index_x=i;
					index_y=j;
					break;
				}
			}
		}
		tempX=index_x;
		tempY=index_y;
		if(index_x==-1&&index_y==-1)
			return null;
		if(map[index_x][index_y]==-1)return null;
		else return chess[map[index_x][index_y]];
	}
	private boolean isMyChess(int index) {
		if(index>=0&&index<=15&&LocalPlayer==BLACKP)return true;
		if(index>=16&&index>=31&&LocalPlayer==REDP)return true;
		return false;
	}
	
	private void SetMyTurn(boolean b) {
		isMyTurn=b;
		if(b)message="轮到你下棋了";
		else message="对方正在思考";
	}
	
	private void rebackChess(int index,int x,int y,int oldX,int oldY) {
		chess[index].setPos(oldX,oldY);
		map[oldX][oldY]=index;
		map[x][y]=-1;
	}
	
	private void resetChess(int index,int x,int y) {
		short temp;
		if(index<16)temp=BLACKP;
		else temp=REDP;
		String name=null;
		switch(index) {
		case 0:name="将";break;
		case 1:;
		case 2:name="士";break;
		case 3:;
		case 4:name="象";break;
		case 5:;
		case 6:name="马";break;
		case 7:;
		case 8:name="车";break;
		case 9:;
		case 10:name="炮";break;
		case 11:;
		case 12:;
		case 13:;
		case 14:;
		case 15:name="卒";break;
		case 16:name="帅";break;
		case 17:;
		case 18:name="仕";break;
		case 19:;
		case 20:name="相";break;
		case 21:;
		case 22:name="马";break;
		case 23:;
		case 24:name="车";break;
		case 25:;
		case 26:name="炮";break;
		case 27:;
		case 28:;
		case 29:;
		case 30:;
		case 31:name="兵";break;
		}
		
		chess[index]=new Chess(temp,name,x,y);
		map[x][y]=index;
	}
	
	public void startJoin(String ip,int otherPort,int receivePort) {
		flag=true;
		this.otherPort=otherPort;
		this.receivePort=receivePort;
		this.ip=ip;
		System.out.println("请连接到"+ip);
		send("join|");
		Thread  th=new Thread(this);
		th.start();
	}
	
	public void startNewGame(short player) {
		initMap();
		initChess();
		if(player==BLACKP)reverseBoard();
		repaint();
	}
	public void startNewGameWithAI(short player) {
		initMap();
		initChess();
		if(player==BLACKP)reverseBoard();
		repaint();
	}
	private void initChess() {
		//黑方
		chess[0] = new Chess(BLACKP,"将",0,4);
		map[0][4] = 0;
		chess[1] = new Chess(BLACKP,"士",0,3);
		map[0][3] = 1;
		chess[2] = new Chess(BLACKP,"士",0,5);
		map[0][5] = 2;
		chess[3] = new Chess(BLACKP,"象",0,2);
		map[0][2] = 3;
		chess[4] = new Chess(BLACKP,"象",0,6);
		map[0][6] = 4;
		chess[5] = new Chess(BLACKP,"马",0,1);
		map[0][1] = 5;
		chess[6] = new Chess(BLACKP,"马",0,7);
		map[0][7] = 6;
		chess[7] = new Chess(BLACKP,"车",0,0);
		map[0][0] = 7;
		chess[8] = new Chess(BLACKP,"车",0,8);
		map[0][8] = 8;
		chess[9] = new Chess(BLACKP,"炮",2,1);
		map[2][1] = 9;
		chess[10] = new Chess(BLACKP,"炮",2,7);
		map[2][7] = 10;
		for(int i=0;i<5;i++){
			chess[11+i] = new Chess(BLACKP,"卒",3,i*2);
			map[3][i*2] = 11+i;
		}
		
		//红方
		chess[16] = new Chess(REDP,"帅",9,4);
		map[9][4] = 16;
		chess[17] = new Chess(REDP,"仕",9,3);
		map[9][3] = 17;
		chess[18] = new Chess(REDP,"仕",9,5);
		map[9][5] = 18;
		chess[19] = new Chess(REDP,"相",9,2);
		map[9][2] = 19;
		chess[20] = new Chess(REDP,"相",9,6);
		map[9][6] = 20;
		chess[21] = new Chess(REDP,"马",9,1);
		map[9][1] = 21;
		chess[22] = new Chess(REDP,"马",9,7);
		map[9][7] = 22;
		chess[23] = new Chess(REDP,"车",9,0);
		map[9][0] = 23;
		chess[24] = new Chess(REDP,"车",9,8);
		map[9][8] = 24;
		chess[25] = new Chess(REDP,"炮",7,1);
		map[7][1] = 25;
		chess[26] = new Chess(REDP,"炮",7,7);
		map[7][7] = 26;

		for(int i=0;i<5;i++){
			chess[27+i] = new Chess(REDP,"兵",6,i*2);
			map[6][i*2] = 27+i;
		}
	}
	
	private void reverseBoard() {
		for(int i=0;i<32;i++)
			if(chess[i]!=null)
				chess[i].ReversePos();
		
		for(int i=0;i<5;i++)
			for(int j=0;j<9;j++)
			{
				int temp=map[i][j];
				map[i][j]=map[9-i][8-j];
				map[9-i][8-j]=temp;
			}
	}
	
	public void paint(Graphics g) {
		g.clearRect(0,0,this.getWidth(),this.getHeight());
		Image bachgroundImage=Toolkit.getDefaultToolkit().getImage("image/chessBoard.png");
		g.drawImage(bachgroundImage,0,0,600,600,this);
		for(int i=0;i<32;i++)
			if(chess[i]!=null)
				chess[i].paint(g,this);
		if(firstChess!=null)
			firstChess.DrawSelectedChess(g);
		if(secondChess!=null)
			secondChess.DrawSelectedChess(g);
		g.drawString(message,0,620);
	}
	
	
	
	public void send(String str) {
		DatagramSocket s = null;
		try{
			s = new DatagramSocket();
			byte[] buffer;
			buffer = new String(str).getBytes();
//			InetAddress ia = InetAddress.getLocalHost();//获取本机地址
			InetAddress ia = InetAddress.getByName(ip );//获取目标地址		
			System.out.println("请求连接的ip是"+ip);
			DatagramPacket dgp = new DatagramPacket(buffer, buffer.length,ia,otherPort);
			s.send(dgp);
			System.out.println("发送信息:"+str);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(s!=null){
				s.close();
			}
		}
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
			System.out.println("我是客户端，我绑定的端口是"+receivePort);
			DatagramSocket s = new DatagramSocket(receivePort);
			byte[] data = new byte[100];
			DatagramPacket dgp = new DatagramPacket(data, data.length);
			while(flag==true){
				s.receive(dgp);
				String strData = new String(data);
				String[] array = new String[6];
				array = strData.split("\\|");
				if(array[0].equals("join")){//对局被加入，我是黑方
					LocalPlayer = BLACKP;
					startNewGame(LocalPlayer);
					if(LocalPlayer==REDP){
						SetMyTurn(true);
					}else{
						SetMyTurn(false);
					}
					//发送联机成功信息
					send("conn|");

				}else if(array[0].equals("conn")){//我成功加入别人的对局，联机成功。我是红方
					LocalPlayer = REDP;
					startNewGame(LocalPlayer);
					if(LocalPlayer==REDP){
						SetMyTurn(true);
					}else{
						SetMyTurn(false);
					}

				}else if(array[0].equals("succ")){
					if(array[1].equals("黑方赢了")){
						if(LocalPlayer==REDP)
							JOptionPane.showConfirmDialog(null, "黑方赢了，你可以重新开始","你输了",JOptionPane.DEFAULT_OPTION);
						else
							JOptionPane.showConfirmDialog(null, "黑方赢了，你可以重新开始","你赢了",JOptionPane.DEFAULT_OPTION);						
					}
					if(array[1].equals("红方赢了")){
						if(LocalPlayer==REDP)
							JOptionPane.showConfirmDialog(null, "红方赢了，你可以重新开始","你赢了",JOptionPane.DEFAULT_OPTION);
						else
							JOptionPane.showConfirmDialog(null, "红方赢了，你可以重新开始","你输了",JOptionPane.DEFAULT_OPTION);						
					}
					message = "你可以重新开局";
					GameClient.buttonStart.setEnabled(true);//可以点击开始按钮了
					//
				}else if(array[0].equals("move")){
					//对方的走棋信息，move|棋子索引号|x|y|oldX|oldY|背驰棋子索引
					System.out.println("接受信息:"+array[0]+"|"+array[1]+"|"+array[2]+"|"+array[3]+"|"+array[4]+"|"+array[5]+"|"+array[6]+"|");
					int index = Short.parseShort(array[1]);
					x2 = Short.parseShort(array[2]);
					y2 = Short.parseShort(array[3]);
					//					String z = array[4];//对方上步走棋的棋谱信息
					//					message = x2 + ":" +y2;
					int oldX = Short.parseShort(array[4]);//棋子移动前所在行数
					int oldY = Short.parseShort(array[5]);//棋子移动前所在列数
					int eatChessIndex = Short.parseShort(array[6]);//被吃掉的棋子索引
					list.add(new Node(index,x2,y2,oldX,oldY,eatChessIndex));//记录下棋信息
					message = "对方将棋子\""+chess[index].typeName+"\"移动到了("+x2+","+y2+")\n现在该你走棋";
					Chess c = chess[index];
					x1 = c.x;
					y1 = c.y;

					index = map[x1][y1];

					int index2 = map[x2][y2];
					map[x1][y1] = -1;
					map[x2][y2] = index;
					chess[index].setPos(x2, y2);
					if(index2!=-1){// 如果吃了子，则取下被吃掉的棋子
						chess[index2] = null;
					}
					repaint();
					isMyTurn = true;
				}else if(array[0].equals("quit")){
					JOptionPane.showConfirmDialog(null, "对方退出了，游戏结束！","提示",JOptionPane.DEFAULT_OPTION);
					message = "对方退出了，游戏结束！";
					GameClient.buttonStart.setEnabled(true);//可以点击开始按钮了
				}else if(array[0].equals("lose")){
					JOptionPane.showConfirmDialog(null, "恭喜你，对方认输了！","你赢了",JOptionPane.DEFAULT_OPTION);
					SetMyTurn(false);
					GameClient.buttonStart.setEnabled(true);//可以点击开始按钮了
				}else if(array[0].equals("ask")){//对方请求悔棋

					String msg = "对方请求悔棋，是否同意？";
					int type = JOptionPane.YES_NO_OPTION;
					String title = "请求悔棋";
					int choice = 0;
					choice = JOptionPane.showConfirmDialog(null, msg,title,type);
					if(choice==1){//否,拒绝悔棋
						send("refuse|");
					}else if(choice == 0){//是,同意悔棋
						send("agree|");
						message = "同意了对方的悔棋，对方正在思考";
						SetMyTurn(false);//对方下棋

						Node temp = list.get(list.size()-1);//获取棋谱最后一步棋的信息
						list.remove(list.size()-1);//移除
						if(LocalPlayer==REDP){//假如我是红方

							if(temp.index>=16){//上一步是我下的，需要回退两步
								rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//回退棋子
								if(temp.eatChessIndex!=-1){//如果上一步吃了子，将被吃的子重新放回到棋盘
									resetChess(temp.eatChessIndex, temp.x, temp.y);//将被吃的棋子放回棋盘
								}
								temp = list.get(list.size()-1);
								list.remove(list.size()-1);

								rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//回退棋子
								if(temp.eatChessIndex!=-1){//如果上一步吃了子，将被吃的子重新放回到棋盘
									resetChess(temp.eatChessIndex, temp.x, temp.y);//将被吃的棋子放回棋盘
								}
							}else{//上一步是对方下的，需要回退一步

								rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//回退棋子
								if(temp.eatChessIndex!=-1){//如果上一步吃了子，将被吃的子重新放回到棋盘
									resetChess(temp.eatChessIndex, temp.x, temp.y);//将被吃的棋子放回棋盘
								}

							}

						}else{//假如我是黑方

							if(temp.index<16){//上一步是我下的，需要回退两步

								rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//回退棋子
								if(temp.eatChessIndex!=-1){//如果上一步吃了子，将被吃的子重新放回到棋盘
									resetChess(temp.eatChessIndex, temp.x, temp.y);//将被吃的棋子放回棋盘
								}

								temp = list.get(list.size()-1);
								list.remove(list.size()-1);

								rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//回退棋子
								if(temp.eatChessIndex!=-1){//如果上一步吃了子，将被吃的子重新放回到棋盘
									resetChess(temp.eatChessIndex, temp.x, temp.y);//将被吃的棋子放回棋盘
								}

							}else{//上一步是对方下的，需要回退一步

								rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//回退棋子
								if(temp.eatChessIndex!=-1){//如果上一步吃了子，将被吃的子重新放回到棋盘
									resetChess(temp.eatChessIndex, temp.x, temp.y);//将被吃的棋子放回棋盘
								}

							}

						}

						repaint();
					}

				}else if(array[0].equals("agree")){//对方同意悔棋

					JOptionPane.showMessageDialog(null, "对方同意了你的悔棋请求");
					Node temp = list.get(list.size()-1);//获取棋谱最后一步棋的信息
					list.remove(list.size()-1);//移除
					if(LocalPlayer==REDP){//假如我是红方

						if(temp.index>=16){//上一步是我下的，回退一步即可

							rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//回退棋子
							if(temp.eatChessIndex!=-1){//如果上一步吃了子，将被吃的子重新放回到棋盘
								resetChess(temp.eatChessIndex, temp.x, temp.y);//将被吃的棋子放回棋盘
							}
						}else{//上一步是对方下的，需要回退两步
							//第一次回退，此时回退到的状态是我刚下完棋轮到对方下棋的状态
							rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//回退棋子
							if(temp.eatChessIndex!=-1){//如果上一步吃了子，将被吃的子重新放回到棋盘
								resetChess(temp.eatChessIndex, temp.x, temp.y);//将被吃的棋子放回棋盘
							}

							temp = list.get(list.size()-1);
							list.remove(list.size()-1);
							//第二次回退，此时回退到的状态是我上一次刚轮到我下棋的状态
							rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//回退棋子
							if(temp.eatChessIndex!=-1){//如果上一步吃了子，将被吃的子重新放回到棋盘
								resetChess(temp.eatChessIndex, temp.x, temp.y);//将被吃的棋子放回棋盘
							}

						}

					}else{//假如我是黑方

						if(temp.index<16){//上一步是我下的，回退一步即可

							rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//回退棋子
							if(temp.eatChessIndex!=-1){//如果上一步吃了子，将被吃的子重新放回到棋盘
								resetChess(temp.eatChessIndex, temp.x, temp.y);//将被吃的棋子放回棋盘
							}
						}else{//上一步是对方下的，需要回退两步
							//第一次回退，此时回退到的状态是我刚下完棋轮到对方下棋的状态
							rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//回退棋子
							if(temp.eatChessIndex!=-1){//如果上一步吃了子，将被吃的子重新放回到棋盘
								resetChess(temp.eatChessIndex, temp.x, temp.y);//将被吃的棋子放回棋盘
							}

							temp = list.get(list.size()-1);
							list.remove(list.size()-1);
							//第二次回退，此时回退到的状态是我上一次刚轮到我下棋的状态
							rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//回退棋子
							if(temp.eatChessIndex!=-1){//如果上一步吃了子，将被吃的子重新放回到棋盘
								resetChess(temp.eatChessIndex, temp.x, temp.y);//将被吃的棋子放回棋盘
							}

						}


					}
					SetMyTurn(true);
					repaint();
				}else if(array[0].equals("refuse")){//对方拒绝悔棋

					JOptionPane.showMessageDialog(null, "对方拒绝了你的悔棋请求");					

				}



				//				System.out.println(new String(data));
				//s.send(dgp);
			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
