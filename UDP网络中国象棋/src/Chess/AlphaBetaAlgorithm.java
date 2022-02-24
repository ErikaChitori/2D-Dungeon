package Chess;

import java.util.ArrayList;
import AIChess.Rule;
class AlphaBetaNode {
	public Chess ch;
	public int[] from;
	public int[] to;
	public int value;
	
	public AlphaBetaNode(Chess ch, int[] from, int[] to) {
		this.ch = ch;
		this.from = from;
		this.to= to;
	}
}

public class AlphaBetaAlgorithm {
	public static final short REDP=1;
	public static final short BLACKP=0;
	private int depth=2;
	private ChessBoard Board;
	int[] BasicValue= {0,0,250,250,300,500,300};
	//棋子在相应棋盘位置的估值
	// 0
		int[][] JiangPosition = new int[][] {
			{0, 0, 0, 1, 5, 1, 0, 0, 0},
			{0, 0, 0, -8, -8, -8, 0, 0, 0},
			{0, 0, 0, -9, -9, -9, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0}
		};
		
		// 1
		int[][] ShiPosition = new int[][] {
			{0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 3, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0}
		};
		
		// 2
		int[][] XiangPosition = new int[][] {
			{0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0},
			{-2, 0, 0, 0, 3, 0, 0, 0, -2},
			{0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0}
		};
		
		// 3
		int[][] MaPosition = new int[][] {
			{0, -3, 2, 0, 2, 0, 2, -3, 0},
			{-3, 2, 4, 5, -10, 5, 4, 2, -3},
			{5, 4, 6, 7, 4, 7, 6, 4, 5},
			{4, 6, 10, 7, 10, 7, 10, 6, 4},
			{2, 10, 13, 14, 15, 14, 13, 10, 2},
			{2, 10, 13, 14, 15, 14, 13, 10, 2},
			{2, 12, 11, 15, 16, 15, 11, 12, 2},
			{5, 20, 12, 19, 12, 19, 12, 20, 5},
			{4, 10, 11, 15, 11, 15, 11, 10, 4},
			{2, 8, 15, 9, 6, 9, 15, 8, 2},
			{2, 2, 2, 8, 2, 8, 2, 2, 2}
		};
		
		// 4
		int[][] JuPosition = new int[][] {
			{-6, 6, 4, 12, 0, 12, 4, 6, -6},
			{5, 8, 6, 12, 0, 12, 6, 8, 5},
			{-2, 8, 4, 12, 12, 12, 4, 8, -2},
			{4, 9, 4, 12, 14, 12, 4, 9, 4},
			{8, 12, 12, 14, 15, 14, 12, 12, 8},
			{8, 11, 11, 14, 15, 14, 11, 11, 8},
			{6, 13, 13, 16, 16, 16, 13, 13, 6},
			{6, 8, 7, 14, 16, 14, 7, 8, 6},
			{6, 12, 9, 16, 33, 16, 9, 12, 6},
			{6, 8, 7, 13, 14, 13, 7, 8, 6}
		};
		
		// 5
		int[][] PaoPosition = new int[][] {
			{0, 0, 1, 3, 3, 3, 1, 0, 0},
			{0, 1, 2, 2, 2, 2, 2, 1, 0},
			{1, 0, 4, 3, 5, 3, 4, 0, 1},
			{0, 0, 0, 0, 0, 0, 0, 0, 0},
			{-2, 0, -2, 0, 6, 0, -2, 0, -2},
			{3, 0, 4, 0, 7, 0, 4, 0, 3},
			{10, 18, 22, 35, 40, 35, 22, 18, 10},
			{20, 27, 30, 40, 42, 40, 30, 27, 20},
			{20, 30, 45, 55, 55, 55, 45, 30, 20},
			{20, 30, 50, 65, 70, 65, 50, 30, 20},
			{0, 0, 0, 2, 4, 2, 0, 0, 0}
		};
		
		// 6
		int[][] BingPosition = new int[][] {
			{0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0},
			{-2, 0, -2, 0, 6, 0, -2, 0, -2},
			{3, 0, 4, 0, 7, 0, 4, 0, 3},
			{10, 18, 22, 35, 40, 35, 22, 18, 10},
			{20, 27, 30, 40, 42, 40, 30, 27, 20},
			{20, 30, 50, 65, 70, 65, 50, 30, 20},
			{0, 0, 0, 2, 4, 2, 0, 0, 0}
		};
		
		public AlphaBetaNode search(ChessBoard board) {
			this.Board=board;
			ArrayList<AlphaBetaNode>allNextStep=new ArrayList<AlphaBetaNode>();
			int[][]map=Board.getMap();
			for(int i=0;i<10;i++)
				for(int j=0;j<9;j++) {
					//map=ChessBoard.getMap();
					if(map[i][j]!=-1)
					{
						Chess chess=Board.chess[map[i][j]];
						if(chess.player==BLACKP) {
							for(int[]next:Rule.getNextMove(chess,i,j)) {
								int[] pos= {i,j};
								AlphaBetaNode newNode=new AlphaBetaNode(chess,pos,next);
								allNextStep.add(newNode);
							}
						}
					}
				}
			AlphaBetaNode best=null;
			for(AlphaBetaNode n:allNextStep) {
				int toX=n.to[0],toY=n.to[1];
				Chess p;
				if(map[toX][toY]!=-1)
				p=Board.chess[map[toX][toY]];
				else p=null;
				if(p!=null&&(p.typeName=="将"||p.typeName=="帅"))
					return n;
			}
			long start=System.currentTimeMillis();
			for(AlphaBetaNode n:allNextStep) {
				int toX=n.to[0],toY=n.to[1];
				int temp1=map[toX][toY],temp2=map[n.from[0]][n.from[1]];
				//Chess eaten=ChessBoard.analyse(toX,toY);
				Board.updateMap(toX,toY,temp2);
				Board.updateMap(n.from[0],n.from[1],-1);
				if(temp1!=-1) {
					n.value+=100;
				}
				n.value=alpha_beta_search(depth,Integer.MIN_VALUE,Integer.MAX_VALUE,false);
				if(best==null||n.value>=best.value)best=n;
				Board.updateMap(toX,toY,temp1);
				Board.updateMap(n.from[0],n.from[1],temp2);
			}
			long finish=System.currentTimeMillis();
			System.out.println("Calculate Time: "+(finish-start)+"ms");
			System.out.println("From:("+best.from[0]+","+best.from[1]+")to("+best.to[0]+","+best.to[1]+")");
			return best;
		}
		private int[] getOppositePos(int[] pos) {
			int[] result=new int[] {9-pos[0],pos[1]};
			return result;
		}
		private int estimate_value(int piece) {
			return BasicValue[piece];
		}
		private int estimate_position(int piece,int[] pos) {
			switch(piece) {
			case 0:return JiangPosition[pos[0]][pos[1]];
			case 1:return ShiPosition[pos[0]][pos[1]];
			case 2:return XiangPosition[pos[0]][pos[1]];
			case 3:return MaPosition[pos[0]][pos[1]];
			case 4:return JuPosition[pos[0]][pos[1]];
			case 5:return PaoPosition[pos[0]][pos[1]];
			case 6:return BingPosition[pos[0]][pos[1]];
			default: return -1;
			}
		}
		
		private int estimate_myself(Chess chess) {
			if(chess.typeName=="将"||chess.typeName=="帅")return 0;
			int totalValue=0;
			ArrayList<int[]>next=Rule.getNextMove(chess,chess.x,chess.y);
			for(int []n:next) {
				int xx=n[0],yy=n[1];
				Chess p;
				if(ChessBoard.map[xx][yy]!=-1)
				p=ChessBoard.chess[ChessBoard.map[xx][yy]];
				else p=null;
				if(p!=null&&(p.typeName=="将"||p.typeName=="帅")) {
					totalValue+=9999;
					break;
				}
				if(p!=null&&p.typeName=="车") {
					totalValue+=500;
					break;
				}
				if(p!=null&&(p.typeName=="马"||p.typeName=="炮")) {
					totalValue+=100;
					break;
				}
				if(p!=null&&(p.typeName=="卒"||p.typeName=="兵")) {
					totalValue-=20;
					break;
				}
			}
			return totalValue;
		}
		
		public int estimate(ChessBoard Board) {
			int[][] totalValue=new int[2][3];
			int[][] map=ChessBoard.getMap();
			for(int i=0;i<10;i++)
				for(int j=0;j<9;j++) {
					Chess piece;
					int[] pos= {i,j};
					if(map[i][j]!=-1) {
						piece=ChessBoard.chess[map[i][j]];
						switch(piece.typeName) {
						case "将":
							{
								totalValue[0][0]+=estimate_value(0);
								totalValue[0][1]+=estimate_position(0,pos);
							}break;
						case "帅":
						{
							totalValue[1][0]+=estimate_value(0);
							totalValue[1][1]+=estimate_position(0,getOppositePos(pos));
						}break;
						case "士":
						{
							totalValue[0][0]+=estimate_value(1);
							totalValue[0][1]+=estimate_position(1,pos);
						}break;
						case "仕":
						{
							totalValue[1][0]+=estimate_value(1);
							totalValue[1][1]+=estimate_position(1,getOppositePos(pos));
						}break;
						case "象":
						{
							totalValue[0][0]+=estimate_value(2);
							totalValue[0][1]+=estimate_position(2,pos);
						}break;
						case "相":
						{
							totalValue[1][0]+=estimate_value(2);
							totalValue[1][1]+=estimate_position(2,getOppositePos(pos));
						}break;
						case "马":
							if(piece.player==0)
						{
							totalValue[0][0]+=estimate_value(3);
							totalValue[0][1]+=estimate_position(3,pos);
						}
							else{
							totalValue[1][0]+=estimate_value(3);
							totalValue[1][1]+=estimate_position(3,getOppositePos(pos));
						}break;
						case "车":
							if(piece.player==0)
							{
							totalValue[0][0]+=estimate_value(4);
							totalValue[0][1]+=estimate_position(4,pos);
							}
							else{
							totalValue[1][0]+=estimate_value(4);
							totalValue[1][1]+=estimate_position(4,getOppositePos(pos));
							}break;
						case "炮":
							if(piece.player==0)
							{
							totalValue[0][0]+=estimate_value(5);
							totalValue[0][1]+=estimate_position(5,pos);
							}
							else{
							totalValue[1][0]+=estimate_value(5);
							totalValue[1][1]+=estimate_position(5,getOppositePos(pos));
							}break;
						
						case "卒":
						{
							totalValue[0][0]+=estimate_value(6);
							totalValue[0][1]+=estimate_position(6,pos);
						}break;
						case "兵":
						{
							totalValue[1][0]+=estimate_value(6);
							totalValue[1][1]+=estimate_position(6,getOppositePos(pos));
						}break;
					}
					totalValue[0][2]+=estimate_myself(piece);
					totalValue[1][2]+=estimate_myself(piece);
					}
				}
				int red=totalValue[1][0]+totalValue[1][1]+totalValue[1][2];
				int black=totalValue[0][0]+totalValue[0][1]+totalValue[0][2];
				int result_value=black-red;
				return result_value;
		}
		private int alpha_beta_search(int depth,int alpha,int beta,boolean isMax) {
			if(depth==0)return estimate(Board);
			int[][]map=Board.getMap();
			int f1=0,f2=0;
			for(int i=0;i<10;i++)
				for(int j=0;j<9;j++)
				if(map[i][j]==0)f1=1;
				else if(map[i][j]==16)f2=1;
			if(f1==0||f2==0)return estimate(Board);
			
			ArrayList<AlphaBetaNode>allNextStep=new ArrayList<AlphaBetaNode>();
			for(int i=0;i<10;i++)
				for(int j=0;j<9;j++) {
					if(map[i][j]!=-1) {
						Chess piece=ChessBoard.chess[map[i][j]];
						if((piece.player==0&&isMax)||(piece.player==1&&!isMax)) {
							for(int[] next:Rule.getNextMove(piece,piece.x,piece.y)) {
								int[]pos= {i,j};
								AlphaBetaNode newNode=new AlphaBetaNode(piece,pos,next);
								allNextStep.add(newNode);
							}
						}
					}
				}
			
			for(AlphaBetaNode n:allNextStep) {
				int toX=n.to[0],toY=n.to[1];
				int frX=n.from[0],frY=n.from[1];
				int temp1=map[toX][toY],temp2=map[frX][frY];
				Board.updateMap(toX,toY,temp2);
				Board.updateMap(frX,frY,-1);
				if(isMax) {
					alpha=Math.max(alpha,alpha_beta_search(depth-1,alpha,beta,false));
				}
				else beta=Math.min(beta,alpha_beta_search(depth-1,alpha,beta,true));
				Board.updateMap(toX,toY,temp1);
				Board.updateMap(frX,frY,temp2);
				if(beta<=alpha)break;
			}
			return isMax?alpha:beta;
		}
}
