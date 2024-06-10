import java.io.*;
import java.util.*;

public class Main {

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
	private static int[][] seekRevDir;
	
	private static Pair seekPos;
	
	private static boolean forwardFacing = true;
	
	private static int ans;
	
	private static boolean isRange(int x, int y) {
		return 0<=x && x<n && 0<=y && y<n;
	}
	
	private static int stoi(String s) {
		return Integer.parseInt(s);
	}
	

	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		n = stoi(st.nextToken());
		m= stoi(st.nextToken());
		h = stoi(st.nextToken());
		k = stoi(st.nextToken());
		
		hiders = new ArrayList[n][n];
		nextHiders = new ArrayList[n][n];
		tree = new boolean[n][n];
		
		seekNextDir = new int[n][n];
		seekRevDir = new int[n][n];
		
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
		
		seekPos = new Pair(n/2,n/2);
		
		initializeSeekerPath();
		
		for(int t=1; t<=k; t++) {
			simulate(t);
		}
		
		System.out.println(ans);
		
	}
	
	private static int getDistance(int x, int y) {
		return Math.abs(seekPos.x -x) + Math.abs(seekPos.y - y);
	}
	
	private static void hiderMoveAll() {
		for(int i=0; i<n; i++) {
			for(int j=0; j<n; j++) {
				nextHiders[i][j] = new ArrayList<>();
			}
		}
		
		for(int i=0; i<n; i++) {
			for(int j =0; j<n; j++) {
				if(getDistance(i, j)<=3) {
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
				hiders[i][j] = new ArrayList<>(nextHiders[i][j]);
			}
		}
	}
	
	private static void hiderMove(int x, int y, int moveDir) {
		int[] dx = new int[] {0,0,1,-1};
		int[] dy = new int[] {-1,1,0,0};
		
		int nx = x+ dx[moveDir];
		int ny = y + dy[moveDir];
		
		if(!isRange(nx, ny)) {
			moveDir = (moveDir < 2) ? (1-moveDir) : (5-moveDir);
			nx = x + dx[moveDir];
			ny = y +dy[moveDir];
		}
		
		if(!new Pair(nx, ny).isSame(seekPos)) {
			nextHiders[nx][ny].add(moveDir);
		}
		else {
			nextHiders[x][y].add(moveDir);
		}
		
	}
	
	private static void seekerMove() {
		int x = seekPos.x;
		int y =seekPos.y;
		
		int[] dx = new int[] {-1,0,1,0};
		int[] dy = new int[] {0,1,0,-1};
		
		int moveDir = getSeekerDir();
		
		seekPos = new Pair(x+dx[moveDir], y + dy[moveDir]);
		
		checkFacing();
	}
	
	private static void checkFacing() {
		if(seekPos.isSame(new Pair(0,0)) && forwardFacing) {
			forwardFacing = false;
		}
		if(seekPos.isSame(new Pair(n/2, n/2)) && !forwardFacing) {
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
			moveDir = seekRevDir[x][y];
		}
		return moveDir;
	}
	
	private static void simulate(int t) {
		 // 도망자가 움직입니다.
        hiderMoveAll();
    
        // 술래가 움직입니다.
        seekerMove();
        
        // 점수를 얻습니다.
        getScore(t);
	}
	
	private static void getScore(int t) {
		// 상우하좌 순서대로 넣어줍니다.
        int[] dx = new int[]{-1, 0, 1,  0};
        int[] dy = new int[]{0 , 1, 0, -1};
        
        int x = seekPos.x;
        int y = seekPos.y;
        
        int moveDir = getSeekerDir();
        
        for(int dist = 0; dist <3; dist++) {
        	int nx = x+ dist * dx[moveDir];
        	int ny = y + dist * dy[moveDir];
        	
        	if(isRange(nx,ny)) {
        		if(!tree[nx][ny]) {
        			ans += t*hiders[nx][ny].size();
        			
        			hiders[nx][ny] = new ArrayList<>();
        		}
        	}
        }
	}
	
	private static void initializeSeekerPath() {
		int[] dx = new int[] {-1,0,1,0};
		int[] dy = new int[] {0,1,0,-1};
		
		int curX = n/2;
		int curY = n/2;
		int moveDir = 0;
		int moveNum = 1;
		
		while(curX>0 || curY>0) {
			for(int i=0; i<moveNum; i++) {
				seekNextDir[curX][curY] = moveDir;
				curX += dx[moveDir];
				curY += dy[moveDir];
				seekRevDir[curX][curY] = (moveDir<2)?(moveDir + 2) : (moveDir - 2);
				
				if(curX == 0 && curY == 0) {
					break;
				}
			}
				
			moveDir = (moveDir +1) % 4;
			if(moveDir == 0 || moveDir == 2) {
				moveNum++;
			}
			
		}
	}
	
	
}