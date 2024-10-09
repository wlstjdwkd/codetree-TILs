import java.io.*;
import java.util.*;

public class Main {	
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private static StringTokenizer st;
	private static final int[] dx = {-1, 0, 1, 0};
	private static final int[] dy = {0, 1, 0, -1};
//	
//	static int[] dx = {-1,1,0,0,1,1,-1,-1};
//	static int[] dy = {0,0,1,-1,-1,1,1,-1};
	
	private static boolean isRange(int x, int y) {
		return 0 <= x && x<n && 0<= y && y<n;
	}
	
	private static int stoi(String s) {
		return Integer.parseInt(s);
	}
	
	private static class Pair{
		int x,y;

		public Pair(int x, int y) {
			super();
			this.x = x;
			this.y = y;
		}
		
		public boolean isSame(Pair p) {
			return this.x == p.x && this.y == p.y;
		}
	}
	
	private static int n,m,h,k;
	private static List<Integer>[][] hiders;
	private static List<Integer>[][] nextHiders;
	private static boolean[][] tree;

	private static int[][] seekNextDir;
	private static int[][] seekPrevDir;
	
	private static Pair seekPos;
	
	private static boolean forwardFacing = true;
	
	private static int ans;
	
	private static void init() throws IOException{
		st = new StringTokenizer(br.readLine());
		
		n = stoi(st.nextToken());
		m = stoi(st.nextToken());
		h = stoi(st.nextToken());
		k = stoi(st.nextToken());
		
		hiders = new ArrayList[n][n];
		nextHiders = new ArrayList[n][n];
		tree = new boolean[n][n];
		
		seekNextDir = new int[n][n];
		seekPrevDir = new int[n][n];
		
		for(int i=0; i<n; i++) {
			for(int j=0; j<n; j++) {
				hiders[i][j] = new ArrayList<>();
			}
		}
		
		while(m-->0) {
			st = new StringTokenizer(br.readLine());
			int x = stoi(st.nextToken());
			int y = stoi(st.nextToken());
			int d = stoi(st.nextToken());
			
			hiders[x-1][y-1].add(d);
		}
		
		while(h-->0) {
			st = new StringTokenizer(br.readLine());
			int x = stoi(st.nextToken());
			int y = stoi(st.nextToken());
			
			tree[x-1][y-1] = true;
		}
		
		seekPos = new Pair(n/2, n/2);
		
		int curX = n/2;
		int curY = n/2;
		int moveDir = 0;
		int moveNum = 1;
		
		while(curX>0 || curY>0) {
			for(int i=0; i<moveNum; i++) {
				seekNextDir[curX][curY] = moveDir;
				curX += dx[moveDir];
				curY += dy[moveDir];
				seekPrevDir[curX][curY] = (moveDir<2)?(moveDir+2):(moveDir-2);
				
				if(curX == 0 && curY == 0) {
					break;
				}
			}
			
			moveDir = (moveDir + 1) % 4;
			if(moveDir == 0 || moveDir == 2) {
				moveNum++;
			}
		}
	}
	
	
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		init();
		
		for(int t=1; t<=k; t++) {
			hiderMoveAll();
			
			seekerMove();
			
			getScore(t);
		}
		
		System.out.println(ans);
	}
	
	private static void getScore(int t) {
		int x = seekPos.x;
		int y = seekPos.y;
		
		int moveDir = getSeekerDir();
		
		for(int dist = 0; dist<3; dist++) {
			int nx = x+ dist*dx[moveDir];
			int ny = y+ dist * dy[moveDir];
			
			if(isRange(nx, ny)) {
				if(!tree[nx][ny]) {
					ans += t * hiders[nx][ny].size();
					
					hiders[nx][ny] = new ArrayList<>();
				}
			}
		}
	}
	
	private static void hiderMoveAll() {
		for(int i=0; i<n; i++) {
			for(int j=0; j<n; j++) {
				nextHiders[i][j] = new ArrayList<>();
			}
		}
		
		for(int i=0; i<n; i++) {
			for(int j=0; j<n; j++) {
				if(getDistance(i, j)<=3){
					for(int k=0; k<hiders[i][j].size(); k++) {
						hiderMove(i,j, hiders[i][j].get(k));
					}
				}
				else {
					for(int k=0; k<hiders[i][j].size(); k++) {
						nextHiders[i][j].add(hiders[i][j].get(k));
					}
				}
			}
		}
		
		for(int i=0; i<n; i++) {
			for(int j=0; j<n; j++) {
				hiders[i][j] = nextHiders[i][j];
			}
		}
	}
	
	private static void seekerMove() {
		int x = seekPos.x;
		int y = seekPos.y;
		
		int moveDir = getSeekerDir();
		
		seekPos = new Pair(x+dx[moveDir], y + dy[moveDir]);
		
		checkFacing();
	}
	
	private static void checkFacing() {
		if(seekPos.isSame(new Pair(0,0)) && forwardFacing) {
			forwardFacing = false;
		}
		
		if(seekPos.isSame(new Pair(n/2,n/2)) && !forwardFacing) {
			forwardFacing = true;
		}
	}
	
	private static int getSeekerDir() {
		int x = seekPos.x;
		int y = seekPos.y;
		int moveDir;
		if(forwardFacing) {
			moveDir = seekNextDir[x][y];
		}
		else {
			moveDir = seekPrevDir[x][y];
		}
		
		return moveDir;
	}
	
	private static void hiderMove(int x, int y ,int moveDir) {
		int[] dx2 = {0,0,1,-1};
		int[] dy2 = {-1,1,0,0};
		
		int nx = x+dx2[moveDir];
		int ny = y + dy2[moveDir];
		
		if(!isRange(nx, ny)) {
			moveDir = (moveDir<2) ? (1-moveDir) : (5-moveDir);
			nx = x+ dx[moveDir];
			ny = y+ dy[moveDir];
		}
		
		if(!new Pair(nx, ny).isSame(seekPos)) {
			nextHiders[nx][ny].add(moveDir);
		}
		else {
			nextHiders[x][y].add(moveDir);
		}
	}
	
	private static int getDistance(int x, int y) {
		return Math.abs(seekPos.x - x) + Math.abs(seekPos.y-y);
	}
	
}