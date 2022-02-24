package Chess;
import javax.swing.*;
import AIChess.StartAIChess;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
public class Menu extends JPanel{
	private static JLabel message;
	protected static int id=0;
	public static int ff=0;
	public static void loginChess(int x,String name) {
		JFrame frame=new JFrame();
		frame.setSize(500, 400);
		frame.setLocationRelativeTo(null);
		frame.setTitle("�й�����");
		frame.setResizable(false);
		frame.setLayout(null);
		if(x==0)
		message=new JLabel("�˺�:δ��¼");
		else {
			message=new JLabel("�˺ţ�"+name);
		}
		message.setBounds(5,5,400,30);
		frame.add(message);
		
		JButton btn1=new JButton("������ս");
		JButton btn2=new JButton("�˻���ս");
		JButton btn3=new JButton("�˺Ź���");
		JButton btn4=new JButton("���ֲ���/��ͣ");
		btn1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(x==0) {
					String s1="�Ƿ�򿪵�½���棿";
					String s2="δ��¼";
					if(JOptionPane.showConfirmDialog(frame,s1,s2,JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
						frame.setVisible(false);
						new Account(frame);
					}
				}else {
					frame.setVisible(false);
					Music.stop();
					if(ff==0)
					Music.play2();
					new GameClient();
				}
			}
			
		});
		btn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				Music.stop();
				if(ff==0)
				Music.play2();
				new StartAIChess();
			}
		});
		btn3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				new Account(frame);
			}
		});
		btn4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(ff==0) {
					ff=1;
					Music.stop();
				}else {
					ff=0;
					Music.play1();
				}
			}
		});
		btn4.setBounds(frame.getWidth()/2-75,250,150,30);
		frame.add(btn4);
		btn3.setBounds(frame.getWidth()/2-75,210,150,30);
		frame.add(btn3);
		btn2.setBounds(frame.getWidth()/2-75,170,150,30);
		frame.add(btn2);
		btn1.setBounds(frame.getWidth()/2-75,130,150,30);
		frame.add(btn1);
		frame.setVisible(true);	
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
