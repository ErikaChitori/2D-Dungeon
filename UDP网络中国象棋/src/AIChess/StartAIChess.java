package AIChess;
import Chess.ChessBoard;
import Chess.Chess;
import Chess.AlphaBetaAlgorithm;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
public class StartAIChess extends JFrame{
	static ChessBoard gamePanel=new ChessBoard(true);
	static JButton buttonStart=new JButton("开始");
	public static final short REDPLAYER = 1;
	public static final short BLACKPLAYER = 0;
	public StartAIChess() {
		JPanel panelBottom=new JPanel(new FlowLayout());
		panelBottom.add(buttonStart);
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(gamePanel,BorderLayout.CENTER);
		this.getContentPane().add(panelBottom,BorderLayout.SOUTH);
		this.setSize(610,730);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("中国象棋人机对战");
		this.setVisible(true);
		buttonStart.setEnabled(true);
		setVisible(true);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					System.exit(0);
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		buttonStart.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				gamePanel.startNewGameWithAI(REDPLAYER);
			}
		});
	}
}
