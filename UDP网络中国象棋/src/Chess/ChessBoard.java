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
	
	private void initMap() {//���̳�ʼ��
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
						JOptionPane.showConfirmDialog(null, "�췽Ӯ��","��ʾ",JOptionPane.DEFAULT_OPTION);
					}
					else if(index4==16) {
						JOptionPane.showConfirmDialog(null, "�ڷ�Ӯ��","��ʾ",JOptionPane.DEFAULT_OPTION);
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
							JOptionPane.showConfirmDialog(null, "�췽Ӯ��","��ʾ",JOptionPane.DEFAULT_OPTION);
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
		message="�����ڵȴ�����״̬��";
		
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(isMyTurn==false) {
					message="���ڸöԷ�����";
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
						message="���޷��ٿضԷ�����";
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
							message="�����Ϲ���";
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
						list.add(new Node(index1,x2,y2,x1,y1,index2));//��¼�ҷ�������Ϣ
						if(index2 == 0){//���Ե����ǽ�
							message = "�췽Ӯ��";
							JOptionPane.showConfirmDialog(null, "�췽Ӯ��","��ʾ",JOptionPane.DEFAULT_OPTION);
							//send
							send("succ"+"|"+"�췽Ӯ��"+"|");
							return ;
						}
						if(index2 == 16){//���Ե�����˧
							message = "�ڷ�Ӯ��";
							JOptionPane.showConfirmDialog(null, "�ڷ�Ӯ��","��ʾ",JOptionPane.DEFAULT_OPTION);
							//send
							send("succ"+"|"+"�ڷ�Ӯ��"+"|");
							return ;
						}
						SetMyTurn(false);//�öԷ���
					}else{//���ܳ���
						message = "���ܳ���";
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
		if(b)message="�ֵ���������";
		else message="�Է�����˼��";
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
		case 0:name="��";break;
		case 1:;
		case 2:name="ʿ";break;
		case 3:;
		case 4:name="��";break;
		case 5:;
		case 6:name="��";break;
		case 7:;
		case 8:name="��";break;
		case 9:;
		case 10:name="��";break;
		case 11:;
		case 12:;
		case 13:;
		case 14:;
		case 15:name="��";break;
		case 16:name="˧";break;
		case 17:;
		case 18:name="��";break;
		case 19:;
		case 20:name="��";break;
		case 21:;
		case 22:name="��";break;
		case 23:;
		case 24:name="��";break;
		case 25:;
		case 26:name="��";break;
		case 27:;
		case 28:;
		case 29:;
		case 30:;
		case 31:name="��";break;
		}
		
		chess[index]=new Chess(temp,name,x,y);
		map[x][y]=index;
	}
	
	public void startJoin(String ip,int otherPort,int receivePort) {
		flag=true;
		this.otherPort=otherPort;
		this.receivePort=receivePort;
		this.ip=ip;
		System.out.println("�����ӵ�"+ip);
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
		//�ڷ�
		chess[0] = new Chess(BLACKP,"��",0,4);
		map[0][4] = 0;
		chess[1] = new Chess(BLACKP,"ʿ",0,3);
		map[0][3] = 1;
		chess[2] = new Chess(BLACKP,"ʿ",0,5);
		map[0][5] = 2;
		chess[3] = new Chess(BLACKP,"��",0,2);
		map[0][2] = 3;
		chess[4] = new Chess(BLACKP,"��",0,6);
		map[0][6] = 4;
		chess[5] = new Chess(BLACKP,"��",0,1);
		map[0][1] = 5;
		chess[6] = new Chess(BLACKP,"��",0,7);
		map[0][7] = 6;
		chess[7] = new Chess(BLACKP,"��",0,0);
		map[0][0] = 7;
		chess[8] = new Chess(BLACKP,"��",0,8);
		map[0][8] = 8;
		chess[9] = new Chess(BLACKP,"��",2,1);
		map[2][1] = 9;
		chess[10] = new Chess(BLACKP,"��",2,7);
		map[2][7] = 10;
		for(int i=0;i<5;i++){
			chess[11+i] = new Chess(BLACKP,"��",3,i*2);
			map[3][i*2] = 11+i;
		}
		
		//�췽
		chess[16] = new Chess(REDP,"˧",9,4);
		map[9][4] = 16;
		chess[17] = new Chess(REDP,"��",9,3);
		map[9][3] = 17;
		chess[18] = new Chess(REDP,"��",9,5);
		map[9][5] = 18;
		chess[19] = new Chess(REDP,"��",9,2);
		map[9][2] = 19;
		chess[20] = new Chess(REDP,"��",9,6);
		map[9][6] = 20;
		chess[21] = new Chess(REDP,"��",9,1);
		map[9][1] = 21;
		chess[22] = new Chess(REDP,"��",9,7);
		map[9][7] = 22;
		chess[23] = new Chess(REDP,"��",9,0);
		map[9][0] = 23;
		chess[24] = new Chess(REDP,"��",9,8);
		map[9][8] = 24;
		chess[25] = new Chess(REDP,"��",7,1);
		map[7][1] = 25;
		chess[26] = new Chess(REDP,"��",7,7);
		map[7][7] = 26;

		for(int i=0;i<5;i++){
			chess[27+i] = new Chess(REDP,"��",6,i*2);
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
//			InetAddress ia = InetAddress.getLocalHost();//��ȡ������ַ
			InetAddress ia = InetAddress.getByName(ip );//��ȡĿ���ַ		
			System.out.println("�������ӵ�ip��"+ip);
			DatagramPacket dgp = new DatagramPacket(buffer, buffer.length,ia,otherPort);
			s.send(dgp);
			System.out.println("������Ϣ:"+str);
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
			System.out.println("���ǿͻ��ˣ��Ұ󶨵Ķ˿���"+receivePort);
			DatagramSocket s = new DatagramSocket(receivePort);
			byte[] data = new byte[100];
			DatagramPacket dgp = new DatagramPacket(data, data.length);
			while(flag==true){
				s.receive(dgp);
				String strData = new String(data);
				String[] array = new String[6];
				array = strData.split("\\|");
				if(array[0].equals("join")){//�Ծֱ����룬���Ǻڷ�
					LocalPlayer = BLACKP;
					startNewGame(LocalPlayer);
					if(LocalPlayer==REDP){
						SetMyTurn(true);
					}else{
						SetMyTurn(false);
					}
					//���������ɹ���Ϣ
					send("conn|");

				}else if(array[0].equals("conn")){//�ҳɹ�������˵ĶԾ֣������ɹ������Ǻ췽
					LocalPlayer = REDP;
					startNewGame(LocalPlayer);
					if(LocalPlayer==REDP){
						SetMyTurn(true);
					}else{
						SetMyTurn(false);
					}

				}else if(array[0].equals("succ")){
					if(array[1].equals("�ڷ�Ӯ��")){
						if(LocalPlayer==REDP)
							JOptionPane.showConfirmDialog(null, "�ڷ�Ӯ�ˣ���������¿�ʼ","������",JOptionPane.DEFAULT_OPTION);
						else
							JOptionPane.showConfirmDialog(null, "�ڷ�Ӯ�ˣ���������¿�ʼ","��Ӯ��",JOptionPane.DEFAULT_OPTION);						
					}
					if(array[1].equals("�췽Ӯ��")){
						if(LocalPlayer==REDP)
							JOptionPane.showConfirmDialog(null, "�췽Ӯ�ˣ���������¿�ʼ","��Ӯ��",JOptionPane.DEFAULT_OPTION);
						else
							JOptionPane.showConfirmDialog(null, "�췽Ӯ�ˣ���������¿�ʼ","������",JOptionPane.DEFAULT_OPTION);						
					}
					message = "��������¿���";
					GameClient.buttonStart.setEnabled(true);//���Ե����ʼ��ť��
					//
				}else if(array[0].equals("move")){
					//�Է���������Ϣ��move|����������|x|y|oldX|oldY|������������
					System.out.println("������Ϣ:"+array[0]+"|"+array[1]+"|"+array[2]+"|"+array[3]+"|"+array[4]+"|"+array[5]+"|"+array[6]+"|");
					int index = Short.parseShort(array[1]);
					x2 = Short.parseShort(array[2]);
					y2 = Short.parseShort(array[3]);
					//					String z = array[4];//�Է��ϲ������������Ϣ
					//					message = x2 + ":" +y2;
					int oldX = Short.parseShort(array[4]);//�����ƶ�ǰ��������
					int oldY = Short.parseShort(array[5]);//�����ƶ�ǰ��������
					int eatChessIndex = Short.parseShort(array[6]);//���Ե�����������
					list.add(new Node(index,x2,y2,oldX,oldY,eatChessIndex));//��¼������Ϣ
					message = "�Է�������\""+chess[index].typeName+"\"�ƶ�����("+x2+","+y2+")\n���ڸ�������";
					Chess c = chess[index];
					x1 = c.x;
					y1 = c.y;

					index = map[x1][y1];

					int index2 = map[x2][y2];
					map[x1][y1] = -1;
					map[x2][y2] = index;
					chess[index].setPos(x2, y2);
					if(index2!=-1){// ��������ӣ���ȡ�±��Ե�������
						chess[index2] = null;
					}
					repaint();
					isMyTurn = true;
				}else if(array[0].equals("quit")){
					JOptionPane.showConfirmDialog(null, "�Է��˳��ˣ���Ϸ������","��ʾ",JOptionPane.DEFAULT_OPTION);
					message = "�Է��˳��ˣ���Ϸ������";
					GameClient.buttonStart.setEnabled(true);//���Ե����ʼ��ť��
				}else if(array[0].equals("lose")){
					JOptionPane.showConfirmDialog(null, "��ϲ�㣬�Է������ˣ�","��Ӯ��",JOptionPane.DEFAULT_OPTION);
					SetMyTurn(false);
					GameClient.buttonStart.setEnabled(true);//���Ե����ʼ��ť��
				}else if(array[0].equals("ask")){//�Է��������

					String msg = "�Է�������壬�Ƿ�ͬ�⣿";
					int type = JOptionPane.YES_NO_OPTION;
					String title = "�������";
					int choice = 0;
					choice = JOptionPane.showConfirmDialog(null, msg,title,type);
					if(choice==1){//��,�ܾ�����
						send("refuse|");
					}else if(choice == 0){//��,ͬ�����
						send("agree|");
						message = "ͬ���˶Է��Ļ��壬�Է�����˼��";
						SetMyTurn(false);//�Է�����

						Node temp = list.get(list.size()-1);//��ȡ�������һ�������Ϣ
						list.remove(list.size()-1);//�Ƴ�
						if(LocalPlayer==REDP){//�������Ǻ췽

							if(temp.index>=16){//��һ�������µģ���Ҫ��������
								rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//��������
								if(temp.eatChessIndex!=-1){//�����һ�������ӣ������Ե������·Żص�����
									resetChess(temp.eatChessIndex, temp.x, temp.y);//�����Ե����ӷŻ�����
								}
								temp = list.get(list.size()-1);
								list.remove(list.size()-1);

								rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//��������
								if(temp.eatChessIndex!=-1){//�����һ�������ӣ������Ե������·Żص�����
									resetChess(temp.eatChessIndex, temp.x, temp.y);//�����Ե����ӷŻ�����
								}
							}else{//��һ���ǶԷ��µģ���Ҫ����һ��

								rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//��������
								if(temp.eatChessIndex!=-1){//�����һ�������ӣ������Ե������·Żص�����
									resetChess(temp.eatChessIndex, temp.x, temp.y);//�����Ե����ӷŻ�����
								}

							}

						}else{//�������Ǻڷ�

							if(temp.index<16){//��һ�������µģ���Ҫ��������

								rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//��������
								if(temp.eatChessIndex!=-1){//�����һ�������ӣ������Ե������·Żص�����
									resetChess(temp.eatChessIndex, temp.x, temp.y);//�����Ե����ӷŻ�����
								}

								temp = list.get(list.size()-1);
								list.remove(list.size()-1);

								rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//��������
								if(temp.eatChessIndex!=-1){//�����һ�������ӣ������Ե������·Żص�����
									resetChess(temp.eatChessIndex, temp.x, temp.y);//�����Ե����ӷŻ�����
								}

							}else{//��һ���ǶԷ��µģ���Ҫ����һ��

								rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//��������
								if(temp.eatChessIndex!=-1){//�����һ�������ӣ������Ե������·Żص�����
									resetChess(temp.eatChessIndex, temp.x, temp.y);//�����Ե����ӷŻ�����
								}

							}

						}

						repaint();
					}

				}else if(array[0].equals("agree")){//�Է�ͬ�����

					JOptionPane.showMessageDialog(null, "�Է�ͬ������Ļ�������");
					Node temp = list.get(list.size()-1);//��ȡ�������һ�������Ϣ
					list.remove(list.size()-1);//�Ƴ�
					if(LocalPlayer==REDP){//�������Ǻ췽

						if(temp.index>=16){//��һ�������µģ�����һ������

							rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//��������
							if(temp.eatChessIndex!=-1){//�����һ�������ӣ������Ե������·Żص�����
								resetChess(temp.eatChessIndex, temp.x, temp.y);//�����Ե����ӷŻ�����
							}
						}else{//��һ���ǶԷ��µģ���Ҫ��������
							//��һ�λ��ˣ���ʱ���˵���״̬���Ҹ��������ֵ��Է������״̬
							rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//��������
							if(temp.eatChessIndex!=-1){//�����һ�������ӣ������Ե������·Żص�����
								resetChess(temp.eatChessIndex, temp.x, temp.y);//�����Ե����ӷŻ�����
							}

							temp = list.get(list.size()-1);
							list.remove(list.size()-1);
							//�ڶ��λ��ˣ���ʱ���˵���״̬������һ�θ��ֵ��������״̬
							rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//��������
							if(temp.eatChessIndex!=-1){//�����һ�������ӣ������Ե������·Żص�����
								resetChess(temp.eatChessIndex, temp.x, temp.y);//�����Ե����ӷŻ�����
							}

						}

					}else{//�������Ǻڷ�

						if(temp.index<16){//��һ�������µģ�����һ������

							rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//��������
							if(temp.eatChessIndex!=-1){//�����һ�������ӣ������Ե������·Żص�����
								resetChess(temp.eatChessIndex, temp.x, temp.y);//�����Ե����ӷŻ�����
							}
						}else{//��һ���ǶԷ��µģ���Ҫ��������
							//��һ�λ��ˣ���ʱ���˵���״̬���Ҹ��������ֵ��Է������״̬
							rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//��������
							if(temp.eatChessIndex!=-1){//�����һ�������ӣ������Ե������·Żص�����
								resetChess(temp.eatChessIndex, temp.x, temp.y);//�����Ե����ӷŻ�����
							}

							temp = list.get(list.size()-1);
							list.remove(list.size()-1);
							//�ڶ��λ��ˣ���ʱ���˵���״̬������һ�θ��ֵ��������״̬
							rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//��������
							if(temp.eatChessIndex!=-1){//�����һ�������ӣ������Ե������·Żص�����
								resetChess(temp.eatChessIndex, temp.x, temp.y);//�����Ե����ӷŻ�����
							}

						}


					}
					SetMyTurn(true);
					repaint();
				}else if(array[0].equals("refuse")){//�Է��ܾ�����

					JOptionPane.showMessageDialog(null, "�Է��ܾ�����Ļ�������");					

				}



				//				System.out.println(new String(data));
				//s.send(dgp);
			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
