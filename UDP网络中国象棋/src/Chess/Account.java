package Chess;
import java.awt.event.ActionEvent;
import javax.swing.JRootPane;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import UserData.MySQLManager;
public class Account extends JFrame{
	Connection conn=MySQLManager.getConnection();
	
	JButton btnLogin;
	protected JTabbedPane jtab;
	JFrame mainFrame;
	boolean f=false;
	public Account(JFrame mainFrame) {
		this.mainFrame=mainFrame;
		this.setSize(500,350);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				if(f==false)
				mainFrame.setVisible(true);
			}
		});
		jtab=new JTabbedPane(JTabbedPane.TOP);
		JPanel panelLogin=new PanelLogin();
		JPanel panelRegister=new PanelRegister();
		jtab.add("��¼",panelLogin);
		jtab.add("ע��",panelRegister);
		this.add(jtab);
		this.setVisible(true);
	}
	class PanelLogin extends JPanel{
		PanelLogin(){
			this.setLayout(null);
			JLabel labelAccount=new JLabel("�˺ţ�");
			labelAccount.setBounds(80,40,50,30);
			this.add(labelAccount);
			JTextField textAccount=new JTextField();
			textAccount.setBounds(130,40,250,30);
			this.add(textAccount);
			JLabel labelPass=new JLabel("����:");
			labelPass.setBounds(80,80,50,30);
			this.add(labelPass);
			JPasswordField textPass=new JPasswordField();
			textPass.setBounds(130,80,250,30);
			this.add(textPass);
			btnLogin=new JButton("��¼");
			btnLogin.setBounds(180, 130, 100, 30);
			btnLogin.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if(textAccount.getText().isEmpty() || String.valueOf(textPass.getPassword()).isEmpty()) {
						JOptionPane.showMessageDialog(textAccount, "�˺ź����벻��Ϊ��", "��¼ʧ��", JOptionPane.WARNING_MESSAGE);
					}
					else {
						String flag=MySQLManager.verify(textAccount.getText(),String.valueOf(textPass.getPassword()));
						if(flag!="") {
							//mainFrame.dispose();
							Menu.loginChess(1,flag);
							f=true;
							Account.this.dispose();
						}
						else {
							JOptionPane.showMessageDialog(textAccount, "�˺Ż��������", "��¼ʧ��", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			});
			this.add(btnLogin);
		}
	}
	class PanelRegister extends JPanel{
		PanelRegister(){
			this.setLayout(null);
			JLabel labelName=new JLabel("�ǳ�:");
			labelName.setBounds(80,40,50,30);
			this.add(labelName);
			JTextField textName=new JTextField();
			textName.setBounds(130,40,250,30);
			this.add(textName);
			JLabel labelAccount=new JLabel("�˺�:");
			labelAccount.setBounds(80,80,50,30);
			this.add(labelAccount);
			JTextField textAccount=new JTextField();
			textAccount.setBounds(130,80,250,30);
			this.add(textAccount);
			JLabel labelPass=new JLabel("����:");
			labelPass.setBounds(80,120,50,30);
			this.add(labelPass);
			JPasswordField textPass=new JPasswordField();
			textPass.setBounds(130,120,250,30);
			this.add(textPass);
			JButton btnRegister=new JButton("ע��");
			btnRegister.setBounds(180, 160, 100, 30);
			btnRegister.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if(textAccount.getText().isEmpty() || String.valueOf(textPass.getPassword()).isEmpty()) {
						JOptionPane.showMessageDialog(textAccount, "�˺š����벻��Ϊ��", "ע��ʧ��", JOptionPane.WARNING_MESSAGE);
					}
					else {
						if(MySQLManager.isExist(textName.getText(),textAccount.getText())) {
							JOptionPane.showMessageDialog(textAccount, "�ǳƻ��˺��Ѵ���", "ע��ʧ��", JOptionPane.ERROR_MESSAGE);
						}else {
							MySQLManager.add(textName.getText(),textAccount.getText(),String.valueOf(textPass.getPassword()));
							JOptionPane.showMessageDialog(textAccount, "ע��ɹ�", "ע��ɹ�", JOptionPane.INFORMATION_MESSAGE);
						}
					}
				}
			});
			this.add(btnRegister);
		}
	}
}
