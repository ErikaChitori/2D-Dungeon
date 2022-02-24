package AIChess;
import java.util.ArrayList;
import Chess.Chess;
import Chess.ChessBoard;
public class Rule {
	private static int x,y;
	private static char player;
	private static Chess chess;
	private static int [][] map=ChessBoard.getMap();
	public static ArrayList<int[]>getNextMove(Chess chess,int x,int y){
		Rule.x=x;
		Rule.y=y;
		Rule.chess=chess;
		switch(chess.typeName) {
		case "将":
			return Jiang();
		case "帅":
			return Jiang();
		case "士":
			return Shi();
		case "仕":
			return Shi();
		case "马":
			return Ma();
		case "车":
			return Ju();
		case "炮":
			return Pao();
		case "卒":
			return Bing();
		case "兵":
			return Bing();
		case "象":
			return Xiang();
		case "相":
			return Xiang();
			default:
				return null;
		}
	}
	private static ArrayList<int[]>Ma(){
		ArrayList<int[]>moves=new ArrayList<int[]>();
		int[][]target=new int[][] {{1,-2},{2,-1},{2,1},{1,2},{-1,2},{-2,1},{-2,-1},{-1,-2}};
		int [][]obstacle=new int[][] {{0,-1},{1,0},{1,0},{0,1},{0,1},{-1,0},{-1,0},{0,-1}};
		for(int i=0;i>target.length;i++) {
			int nx=x+target[i][0],ny=y+target[i][1];
			int ox=x+obstacle[i][0],oy=y+obstacle[i][1];
			if(map[ox][oy]!=-1)continue;
			if(nx<0||nx>=10||ny<0||ny>=9)continue;
			if(map[nx][ny]==-1) {
				int[]xx=new int[] {nx,ny};
				moves.add(xx);
			}
			else {
				Chess nchess=ChessBoard.chess[map[nx][ny]];
				if(nchess.player!=chess.player) {
					int[]xx=new int[] {nx,ny};
					moves.add(xx);
				}
			}
		}
		return moves;
	}
	
	private static ArrayList<int[]>Ju(){
		ArrayList<int[]>moves=new ArrayList<int[]>();
		int[] yOffsets = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
        int[] xOffsets = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        for(int offset:yOffsets) {
        	int nx=x,ny=y+offset;
        	if(ny<0||ny>=9)break;
        	if(map[nx][ny]==-1) {
        		int[]xx=new int[] {nx,ny};
				moves.add(xx);
        	}
        	else {
        		Chess nchess=ChessBoard.chess[map[nx][ny]];
				if(nchess.player!=chess.player) {
					int[]xx=new int[] {nx,ny};
					moves.add(xx);
				}
				else break;
        	}
        }
        for(int offset:yOffsets) {
        	int nx=x,ny=y-offset;
        	if(ny<0||ny>=9)break;
        	if(map[nx][ny]==-1) {
        		int[]xx=new int[] {nx,ny};
				moves.add(xx);
        	}
        	else {
        		Chess nchess=ChessBoard.chess[map[nx][ny]];
				if(nchess.player!=chess.player) {
					int[]xx=new int[] {nx,ny};
					moves.add(xx);
				}
				else break;
        	}
        }
        for(int offset:xOffsets) {
        	int nx=x+offset,ny=y;
        	if(nx<0||nx>=10)break;
        	if(map[nx][ny]==-1) {
        		int[]xx=new int[] {nx,ny};
				moves.add(xx);
        	}
        	else {
        		Chess nchess=ChessBoard.chess[map[nx][ny]];
				if(nchess.player!=chess.player) {
					int[]xx=new int[] {nx,ny};
					moves.add(xx);
				}
				else break;
        	}
        }
        for(int offset:xOffsets) {
        	int nx=x-offset,ny=y;
        	if(nx<0||nx>=10)break;
        	if(map[nx][ny]==-1) {
        		int[]xx=new int[] {nx,ny};
				moves.add(xx);
        	}
        	else {
        		Chess nchess=ChessBoard.chess[map[nx][ny]];
				if(nchess.player!=chess.player) {
					int[]xx=new int[] {nx,ny};
					moves.add(xx);
				}
				else break;
        	}
        }
        return moves;
	}
	private static ArrayList<int[]>Pao(){
		ArrayList<int[]>moves=new ArrayList<int[]>();
		int[] yOffsets = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
        int[] xOffsets = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        boolean rr=false,ll=false,uu=false,dd=false;
        for(int offset:yOffsets) {
        	int nx=x,ny=y+offset;
        	if(ny<0||ny>=9)break;
        	boolean e=false;
        	if(map[nx][ny]==-1)e=true;
        	if(!rr) {
        		if(e) {
        			int[]xx=new int[] {nx,ny};
    				moves.add(xx);
        		}
        		else rr=true;
        	}
        	else if(!e) {
        		Chess nchess=ChessBoard.chess[map[nx][ny]];
        		if(nchess.player!=chess.player) {
					int[]xx=new int[] {nx,ny};
					moves.add(xx);
				}
        		break;
        	}
        }
        for(int offset:yOffsets) {
        	int nx=x,ny=y-offset;
        	if(ny<0||ny>=9)break;
        	boolean e=false;
        	if(map[nx][ny]==-1)e=true;
        	if(!ll) {
        		if(e) {
        			int[]xx=new int[] {nx,ny};
    				moves.add(xx);
        		}
        		else ll=true;
        	}
        	else if(!e) {
        		Chess nchess=ChessBoard.chess[map[nx][ny]];
        		if(nchess.player!=chess.player) {
					int[]xx=new int[] {nx,ny};
					moves.add(xx);
				}
        		break;
        	}
        }
        for(int offset:xOffsets) {
        	int nx=x-offset,ny=y;
        	if(nx<0||nx>=10)break;
        	boolean e=false;
        	if(map[nx][ny]==-1)e=true;
        	if(!uu) {
        		if(e) {
        			int []xx=new int[] {nx,ny};
        			moves.add(xx);
    				}
        		else uu=true;
        		}else if(!e) {
        		Chess nchess=ChessBoard.chess[map[nx][ny]];
        		if(nchess.player!=chess.player) {
					int[]xx=new int[] {nx,ny};
					moves.add(xx);
        		}
        		break;
        	}
        }	
        for(int offset:xOffsets) {
        	int nx=x+offset,ny=y;
        	if(nx<0||nx>=10)break;
        	boolean e=false;
        	if(map[nx][ny]==-1)e=true;
        	if(!dd) {
        		if(e) {
        			int []xx=new int[] {nx,ny};
        			moves.add(xx);
    				}
        		else dd=true;
        		}else if(!e) {
        		Chess nchess=ChessBoard.chess[map[nx][ny]];
        		if(nchess.player!=chess.player) {
					int[]xx=new int[] {nx,ny};
					moves.add(xx);
        		}
        		break;
        	}
        }	
		return moves;
	}
	private static ArrayList<int[]>Xiang(){
		ArrayList<int[]>moves=new ArrayList<int[]>();
		int[][] target=new int[][] {{-2,-2},{2,-2},{-2,2},{2,2}};
		int[][] obstacle=new int[][] {{-1,-1},{1,-1},{-1,1},{1,1}};
		for(int i=0;i<target.length;i++) {
			int nx=x+target[i][0],ny=y+target[i][1];
			int ox=x+obstacle[i][0],oy=y+obstacle[i][1];
			if(nx<0||nx>=10||ny<0||ny>=9)continue;
			if(nx>4&&chess.player==0)continue;
			if(nx<5&&chess.player==1)continue;
			if(map[ox][oy]==-1) {
				if(map[nx][ny]==-1) {
					int []xx=new int[] {nx,ny};
        			moves.add(xx);
				}
				else {
					Chess nchess=ChessBoard.chess[map[nx][ny]];
	        		if(nchess.player!=chess.player) {
						int[]xx=new int[] {nx,ny};
						moves.add(xx);
	        		}
				}
			}
		}
		return moves;
	}
	private static ArrayList<int[]>Shi(){
		ArrayList<int[]> moves=new ArrayList<int[]>();
		int[][] target=new int[][] {{-1,-1},{1,1},{-1,1},{1,-1}};
		for(int[] aTarget:target) {
			int nx=x+aTarget[0],ny=y+aTarget[1];
			if(nx<0||nx>=10||ny<0||ny>=9)continue;
			if((nx>2||ny<3||ny>5)&&chess.player==0)continue;
			if((nx<7||ny<3||ny>5)&&chess.player==1)continue;
			if(map[nx][ny]==-1) {
				int []xx=new int[] {nx,ny};
    			moves.add(xx);
			}
			else {
				Chess nchess=ChessBoard.chess[map[nx][ny]];
        		if(nchess.player!=chess.player) {
					int[]xx=new int[] {nx,ny};
					moves.add(xx);
        		}
			}
		}
		return moves;
	}
	private static ArrayList<int[]>Jiang(){
		ArrayList<int[]>moves=new ArrayList<int[]>();
		int[][]target=new int[][] {{0,1},{0,-1},{1,0},{-1,0}};
		for(int[] aTarget:target) {
			int nx=x+aTarget[0],ny=y+aTarget[1];
			if(nx<0||nx>=10||ny<0||ny>=9)continue;
			if((nx>2||ny<3||ny>5)&&chess.player==0)continue;
			if((nx<7||ny<3||ny>5)&&chess.player==1)continue;
			if(map[nx][ny]==-1) {
				int []xx=new int[] {nx,ny};
    			moves.add(xx);
			}else {
				Chess nchess=ChessBoard.chess[map[nx][ny]];
        		if(nchess.player!=chess.player) {
					int[]xx=new int[] {nx,ny};
					moves.add(xx);
        		}
			}
		}
		boolean flag = true;
		int ox=-1,oy=-1;
		if(chess.player==1) {
			int f=0;
			for(int i=0;i<10;i++)
			{
			 for(int j=0;j<9;j++)
				 if(map[i][j]==0) {
					 ox=i;oy=j;
					 f=1;break;
				 }
			 if(f==1)break;
			}
		}
		else {
			int f=0;
			for(int i=0;i<10;i++)
			{
			 for(int j=0;j<9;j++)
				 if(map[i][j]==16) {
					 ox=i;oy=j;
					 f=1;break;
				 }
			 if(f==1)break;
			}
		}
		if(oy==y) {
			for(int i=Math.min(ox,x)+1;i<Math.max(ox,x);i++)
			{
				Chess nchess;
				if(map[x][y]!=-1)
				nchess=ChessBoard.chess[map[x][y]];
				else nchess=null;
				if(nchess!=null) {
					flag=false;
					break;
				}
			}
			if(flag) {
					int[]xx=new int[] {ox,oy};
					moves.add(xx);
			}
		}
		return moves;
	}
	private static ArrayList<int[]>Bing(){
		ArrayList<int[]>moves=new ArrayList<int[]>();
		int[][]targetU=new int[][] {{0,1},{0,-1},{-1,0}};
		int[][]targetD=new int[][] {{0,1},{0,-1},{1,0}};
		if(chess.player==1) {
			if(x>4) {
				int nx=x-1,ny=y;
				if(map[nx][ny]==-1) {
					int[]xx=new int[] {nx,ny};
					moves.add(xx);
				}else {
					Chess nchess=ChessBoard.chess[map[nx][ny]];
	        		if(nchess.player!=chess.player) {
						int[]xx=new int[] {nx,ny};
						moves.add(xx);
	        		}
				}
			}else {
				for(int[] aTarget:targetU) {
					int nx=x+aTarget[0],ny=y+aTarget[1];
					if(nx<0||nx>=10||ny<0||ny>=9)continue;
					if(map[nx][ny]==-1) {
						int[]xx=new int[] {nx,ny};
						moves.add(xx);
					}
					else {
						Chess nchess=ChessBoard.chess[map[nx][ny]];
		        		if(nchess.player!=chess.player) {
							int[]xx=new int[] {nx,ny};
							moves.add(xx);
		        		}
					}
				}
			}
		}
		if(chess.player==0) {
			if(x<5) {
				int nx=x+1,ny=y;
				if(map[nx][ny]==-1) {
					int[]xx=new int[] {nx,ny};
					moves.add(xx);
				}else {
					Chess nchess=ChessBoard.chess[map[nx][ny]];
	        		if(nchess.player!=chess.player) {
						int[]xx=new int[] {nx,ny};
						moves.add(xx);
	        		}
				}
			}else {
				for(int[] aTarget:targetD) {
					int nx=x+aTarget[0],ny=y+aTarget[1];
					if(nx<0||nx>=10||ny<0||ny>=9)continue;
					if(map[nx][ny]==-1) {
						int[]xx=new int[] {nx,ny};
						moves.add(xx);
					}
					else {
						Chess nchess=ChessBoard.chess[map[nx][ny]];
		        		if(nchess.player!=chess.player) {
							int[]xx=new int[] {nx,ny};
							moves.add(xx);
		        		}
					}
				}
			}
		}
		return moves;
	}
}
