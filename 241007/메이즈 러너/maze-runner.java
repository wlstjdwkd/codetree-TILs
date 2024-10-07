import java.io.*;
import java.util.*;

public class Main {
	
	
	
	private static final int[] dx = {-1,0,1,0};
	private static final int[] dy = {0,1,0,-1};
	
//	private static boolean isRange(int x, int y) {
//		return 0 <= x && x<N && 0<= y && y<N;
//	}
	
	private static int stoi(String s) {
		return Integer.parseInt(s);
	}
	
	
	private static class Pair{
		int r,c;

		public Pair(int r, int c) {
			super();
			this.r = r;
			this.c = c;
		}
		
	}
	private static int[][] board;
	private static int[][] nextBoard;
	private static Pair[] traveler;
	private static Pair exits;
	
	private static int sx, sy, squareSize;
	
	private static int n,m,k;
	private static int ans;
	
	
	
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		n = stoi(st.nextToken());
		m= stoi(st.nextToken());
		k = stoi(st.nextToken());
		
		board = new int[n+1][n+1];
		nextBoard = new int[n+1][n+1];
		traveler = new Pair[m+1];
		
		for(int i=1; i<=n; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j=1; j<=n; j++) {
				board[i][j] = stoi(st.nextToken());
			}
		}
		
		for(int i=1; i<=m; i++) {
			st = new StringTokenizer(br.readLine());
			int x = stoi(st.nextToken());
			int y = stoi(st.nextToken());
			
			traveler[i] = new Pair(x,y);
		}
		
		st = new StringTokenizer(br.readLine());
		int x = stoi(st.nextToken());
		int y = stoi(st.nextToken());
		
		exits = new Pair(x,y);
		
		while(k-->0) {
			moveAllTraveler();
			
			if(isFinish()) {
				break;
			}
			
			findMinimumSquare();
			
			rotateSquare();
			
			rotateTravelerAndExit();
		}
		
		System.out.println(ans);
		System.out.println(exits.r + " " + exits.c);
	}
	
	private static void findMinimumSquare() {
		for(int sz = 2; sz<=n; sz++) {
			for(int x1 = 1; x1<=n-sz+1; x1++) {
				for(int y1 = 1; y1<=n-sz+1; y1++) {
					int x2 = x1 + sz -1;
					int y2 = y1 + sz -1;
					
					if(!(x1 <= exits.r && exits.r <= x2 && y1 <= exits.c && exits.c <= y2)) {
						continue;
					}
					
					boolean isTravelerIn = false;
					for(int l=1; l<=m; l++) {
						if(x1 <= traveler[l].r && traveler[l].r <=x2 && y1<=traveler[l].c && traveler[l].c <= y2) {
							if(!(traveler[l].r == exits.r && traveler[l].c == exits.c)) {
								isTravelerIn = true;
								break;
							}
						}
					}
					
					if(isTravelerIn) {
						sx = x1;
						sy = y1;
						squareSize = sz;
						
						return;
					}
				}
			}
		}
	}
	
	private static void rotateSquare() {
		for(int x=sx; x<sx+squareSize; x++) {
			for(int y=sy; y<sy+squareSize; y++) {
				if(board[x][y] > 0) {
					board[x][y]--;
				}
			}
		}
		
		for(int x = sx; x<sx+squareSize; x++) {
			for(int y=sy; y<sy+squareSize; y++) {
				int ox = x-sx;
				int oy = y-sy;
				
				int rx = oy;
				int ry = squareSize - ox -1;
				
				nextBoard[sx+rx][sy+ry] = board[x][y];
			}
		}
		
		for(int x = sx; x<sx+squareSize; x++) {
			for(int y=sy; y<sy+squareSize; y++) {
				board[x][y] = nextBoard[x][y];
			}
		}
	}
	
	private static void rotateTravelerAndExit() {
		for(int i=1; i<=m; i++) {
			int x = traveler[i].r;
			int y = traveler[i].c;
			
			if(sx <= x && x< sx+squareSize && sy<=y && y<sy+squareSize) {
				int ox = x-sx;
				int oy = y-sy;
				
				int rx = oy;
				int ry = squareSize - ox -1;
				
				traveler[i].r = rx+sx;
				traveler[i].c = ry + sy;
			}
		}
		
		if(sx<= exits.r && exits.r < sx+ squareSize && sy <= exits.c && exits.c < sy+squareSize) {
			int ox = exits.r - sx;
			int oy = exits.c - sy;
			
			int rx = oy;
			int ry = squareSize - ox -1;
			
			exits.r = rx+ sx;
			exits.c = ry + sy;
		}
	}
	
	private static boolean isFinish() {
		boolean isAllEscaped = true;
		for(int i=1; i<=m; i++) {
			if(!(traveler[i].r == exits.r && traveler[i].c == exits.c)) {
				isAllEscaped = false;
				break;
			}
		}
		
		return isAllEscaped;
	}
	
	private static void moveAllTraveler() {
		for(int i=1; i<=m; i++) {
			if(traveler[i].r == exits.r && traveler[i].c == exits.c) {
				continue;
			}
			
			if(traveler[i].r != exits.r) {
				int nr = traveler[i].r;
				int nc = traveler[i].c;
				if(exits.r> nr) {
					nr++;
				}
				else {
					nr--;
				}
				
				if(board[nr][nc] == 0) {
					traveler[i].r = nr;
					traveler[i].c = nc;
					ans++;
					continue;
				}
			}
			
			if(traveler[i].c != exits.c) {
				int nr = traveler[i].r;
				int nc = traveler[i].c;
				if(exits.c > nc) {
					nc++;
				}
				else {
					nc--;
				}
				
				if(board[nr][nc] == 0) {
					traveler[i].r = nr;
					traveler[i].c = nc;
					ans++;
					continue;
				}
			}
		}
	}
	
	
}