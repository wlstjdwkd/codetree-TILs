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
		
	}
	
	private static int[][] board;
	
	private static int[][] nextBoard;
	
	private static Pair[] traveler;
	private static Pair exits;
	
	private static int sx, sy, squareSize;
	
	private static int n,m,k;
	
	private static int ans;
	
//	private static boolean isRange(int x, int y) {
//		return 0<=x && x<l && 0<=y && y<l;
//	}
	
	private static int stoi(String s) {
		return Integer.parseInt(s);
	}
	
	
	public static void main(String[] args) throws IOException {
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
			
			boolean isAllEscaped = true;
			for(int i=1; i<=m; i++) {
				if(!(traveler[i].x == exits.x && traveler[i].y == exits.y)) {
					isAllEscaped = false;
				}
			}
			
			if(isAllEscaped) break;
			
			findMinimumSquare();
			
			rotateSquare();
			
			rotateTravelerAndExit();
		}
		
		System.out.print( ans + "\n");
		System.out.println(exits.x + " " + exits.y);
	}
	
	private static void rotateTravelerAndExit() {
		for(int i=1; i<=m; i++) {
			int x = traveler[i].x;
			int y = traveler[i].y;
			
			if(sx<= x && x< sx+squareSize && sy <= y && y<sy+squareSize) {
				int ox = x-sx;
				int oy = y - sy;
				
				int rx = oy;
				int ry = squareSize - ox -1;
				
				traveler[i].x = rx+sx;
				traveler[i].y = ry+sy;
			}
		}
		
		int x = exits.x;
		int y = exits.y;
		
		if(sx<= x && x< sx+squareSize && sy <=y && y < sy+squareSize) {
			int ox = x-sx;
			int oy = y-sy;
			int rx = oy;
			int ry = squareSize-ox-1;
			
			exits.x = rx+sx;
			exits.y = ry+sy;
		}
	}
	
	private static void rotateSquare() {
		for(int x = sx; x<sx+squareSize; x++) {
			for(int y = sy; y<sy+squareSize; y++) {
				if(board[x][y] >0) {
					board[x][y]--;
				}
			}
		}
		
		
		for(int x = sx; x<sx+squareSize; x++) {
			for(int y=sy; y<sy+squareSize; y++) {
				int ox = x-sx;
				int oy = y -sy;
				
				int rx = oy;
				int ry = squareSize - ox -1;
				
				nextBoard[sx+rx][sy+ry] = board[x][y];
				
			}
		}
		
		for(int x = sx; x<sx+squareSize; x++) {
			for(int y= sy; y<sy+squareSize; y++) {
				board[x][y] = nextBoard[x][y];
			}
		}
	}
	
	private static void findMinimumSquare() {
		for(int sz = 2; sz<=n; sz++) {
			for(int x1=1; x1<=n -sz+1; x1++) {
				for(int y1=1; y1<=n-sz+1; y1++) {
					int x2 = x1 + sz-1;
					int y2 = y1 + sz -1;
					
					// 만약 출구가 해당 정사각형 안에 없다면 스킵합니다.
                    if(!(x1 <= exits.x && exits.x <= x2 && y1 <= exits.y && exits.y <= y2)) {
                        continue;
                    }
                    
                    boolean isTravelerIn = false;
                    for(int l = 1; l<=m; l++) {
                    	if(x1 <= traveler[l].x && traveler[l].x <= x2 && y1 <= traveler[l].y && traveler[l].y <= y2) {
                    		if(!(traveler[l].x == exits.x && traveler[l].y == exits.y)) {
                    			isTravelerIn = true;
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
	
	private static void moveAllTraveler() {
		for(int i=1; i<=m; i++) {
			if(traveler[i].x == exits.x && traveler[i].y == exits.y) {
				continue;
			}
			
			if(traveler[i].x != exits.x) {
				int nx = traveler[i].x;
				int ny = traveler[i].y;
				if(exits.x > nx) {
					nx++;
				}
				else {
					nx--;
				}
				
				if(board[nx][ny] == 0) {
					traveler[i].x = nx;
					traveler[i].y = ny;
					ans++;
					continue;
				}
			}
			
			if(traveler[i].y != exits.y) {
                int nx = traveler[i].x;
                int ny = traveler[i].y;
    
                if(exits.y > ny) ny++;
                else ny--;
    
                // 벽이 없다면 행을 이동시킬 수 있습니다.
                // 이 경우 열을 이동시킵니다.
                if(board[nx][ny] == 0) {
                    traveler[i].x = nx;
                    traveler[i].y = ny;
                    ans++;
                    continue;
                }
            }
		}
	}
	
}