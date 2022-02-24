package Chess;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import Chess.ChessBoard;
import Chess.GameClient;
public class GameClient extends JFrame{
	static ChessBoard gamePanel = new ChessBoard();
	static JButton buttonGiveIn = new JButton("����");
	static JButton buttonStart = new JButton("��ʼ");
	JButton buttonAskRegret = new JButton("�������");
	JTextField textIp = new JTextField("127.0.0.1");//IP
	JTextField textPort = new JTextField("3004");//�Է��˿�
	public static final short REDPLAYER = 1;
	public static final short BLACKPLAYER = 0;
	
	public GameClient(){
		 JPanel panelBottom = new JPanel(new FlowLayout());
		 panelBottom.add(new JLabel("����Է�IP:"));
		 panelBottom.add(textIp);
		 panelBottom.add(new JLabel("����Է��˿�:"));
		 panelBottom.add(textPort);
		 panelBottom.add(buttonGiveIn);
		 panelBottom.add(buttonAskRegret);
		 panelBottom.add(buttonStart);
		 this.getContentPane().setLayout(new BorderLayout());
		 this.getContentPane().add(gamePanel,BorderLayout.CENTER);
		 this.getContentPane().add(panelBottom,BorderLayout.SOUTH);
		 this.setSize(610,730);
		 this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 this.setTitle("�й�����ͻ���");
		 this.setVisible(true);
		 buttonGiveIn.setEnabled(false);
		 buttonAskRegret.setEnabled(false);
		 buttonStart.setEnabled(true);
		 setVisible(true);
		 this.addWindowListener(new WindowAdapter() {//���ڹر��¼�
			 public void windowClosing(WindowEvent e){
				 try{
					 gamePanel.send("quit|");
					 System.exit(0);
				 }catch(Exception ex){
					 ex.printStackTrace();
				 }
			 }
		});
		 		 
		 buttonGiveIn.addMouseListener(new MouseAdapter() {//�����¼�
			 public void mouseClicked(MouseEvent e){
				 try{
					 gamePanel.send("lose|");//����������Ϣ
				 }catch(Exception ex){
					 ex.printStackTrace();
				 }
			 }
		});
		 
		buttonAskRegret.addMouseListener(new MouseAdapter() {
			 public void mouseClicked(MouseEvent e){
				 if(gamePanel.list.size()==0){
					 JOptionPane.showMessageDialog(null, "���ܻ���");
					 return ;
				 }
				 
				 if(gamePanel.list.size()==1){
					int flag = gamePanel.LocalPlayer==REDPLAYER?REDPLAYER:BLACKPLAYER;
					if(flag==REDPLAYER){//������Ǻ췽���ж���һ���ǲ��ǶԷ��µģ�����ǣ����ܻ���
						if(gamePanel.list.get(0).index<16){
							 JOptionPane.showMessageDialog(null, "���ܻ���");
							 return ;
						}
					}else{
						if(gamePanel.list.get(0).index>=16){
							 JOptionPane.showMessageDialog(null, "���ܻ���");
							 return ;
						}
					}
					
				 }
				 
				 gamePanel.send("ask|");//���������������
				 
			 }
		});

		 
		 buttonStart.addMouseListener(new MouseAdapter() {
			 public void mouseClicked(MouseEvent e){
				 String ip = textIp.getText();
				 int otherPort = Integer.parseInt(textPort.getText());
				 int receivePort;
				 if(otherPort == 3003){
					 receivePort = 3004;                            
				 }else{
					 receivePort = 3003;
				 }
				 gamePanel.startJoin(ip, otherPort, receivePort);
				 buttonGiveIn.setEnabled(true);
				 buttonAskRegret.setEnabled(true);
				 buttonStart.setEnabled(false);
			 }
		});
		 
	}
	
	
	
	
	
}
