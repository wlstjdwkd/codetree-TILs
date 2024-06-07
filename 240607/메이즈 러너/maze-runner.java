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
	
	private static int N,M,K;
	private static int[][] board;
	private static int[][] nextBoard;
	private static Pair[] traveler;
	
	private static Pair exits;
	
	private static int ans;
	
	private static int sx, sy, squareSize;
	
	
	private static int[] dx = {-1,1,0,0};
	private static int[] dy = {0,0,1,-1};
	
	private static boolean isRange(int x, int y) {
		return 0<=x && x<N && 0<=y && y<M;
	}
	
	private static int stoi(String s) {
		return Integer.parseInt(s);
	}
	
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = stoi(st.nextToken());
		M = stoi(st.nextToken());
		K = stoi(st.nextToken());
		
		board = new int[N+1][N+1];
		nextBoard = new int[N+1][N+1];
		
		traveler = new Pair[M+1];
		for(int i=1; i<=N; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j=1; j<=N; j++){
				board[i][j] = stoi(st.nextToken());
			}	
		}
		
		for(int i=1; i<=M; i++) {
			st = new StringTokenizer(br.readLine());
			int x = stoi(st.nextToken());
			int y = stoi(st.nextToken());
			traveler[i] = new Pair(x,y);
		}
		st = new StringTokenizer(br.readLine());
		int x = stoi(st.nextToken());
		int y = stoi(st.nextToken());
		exits = new Pair(x,y);
		
		while(K-->0) {
			moveAllTravelers();
			
			boolean isAllEscaped = true;
            for(int i = 1; i <= M; i++) {
                if(!(traveler[i].x == exits.x && traveler[i].y == exits.y)) {
                    isAllEscaped = false;
                }
            }

            // 만약 모든 사람이 출구로 탈출했으면 바로 종료합니다.
            if(isAllEscaped) break;
            
            findMinimunSquare();
            
            rotateSquare();
            
            rotateTravelerAndExit();
		}
		
		System.out.println(ans);
		System.out.println(exits.x + " " + exits.y);
		
	}
	
	private static void rotateTravelerAndExit() {
		for(int i=1; i<=M; i++) {
			int x = traveler[i].x;
			int y = traveler[i].y;
			
			if(sx <= x && x < sx + squareSize && sy <= y && y < sy + squareSize) {
                // Step 1. (sx, sy)를 (0, 0)으로 옮겨주는 변환을 진행합니다. 
                int ox = x - sx, oy = y - sy;
                // Step 2. 변환된 상태에서는 회전 이후의 좌표가 (x, y) -> (y, squareN - x - 1)가 됩니다.
                int rx = oy, ry = squareSize - ox - 1;
                // Step 3. 다시 (sx, sy)를 더해줍니다.
                traveler[i].x = rx + sx;
                traveler[i].y = ry + sy;
            }
		}
		
		int x = exits.x;
		int y = exits.y;
		if(sx <= x && x < sx + squareSize && sy <= y && y < sy + squareSize) {
            // Step 1. (sx, sy)를 (0, 0)으로 옮겨주는 변환을 진행합니다. 
            int ox = x - sx, oy = y - sy;
            // Step 2. 변환된 상태에서는 회전 이후의 좌표가 (x, y) -> (y, squareN - x - 1)가 됩니다.
            int rx = oy, ry = squareSize - ox - 1;
            // Step 3. 다시 (sx, sy)를 더해줍니다.
            exits.x = rx + sx;
            exits.y = ry + sy;
        }
	}
	
	private static void rotateSquare() {
		for(int x = sx; x<sx+squareSize; x++) {
			for(int y = sy; y<sy+squareSize; y++) {
				if(board[x][y] > 0) {
					board[x][y]--;
				}
			}
		}
		
		for(int x=sx; x<sx+squareSize; x++) {
			for(int y = sy; y<sy+squareSize; y++) {
				int ox = x-sx;
				int oy = y-sy;
				
				int rx = oy;
				int ry = squareSize-ox-1;
				
				nextBoard[rx+sx][ry+sy] = board[x][y];
				
			}
		}
		
		for(int x = sx; x<sx+squareSize; x++) {
			for(int y = sy; y<sy + squareSize; y++) {
				board[x][y] = nextBoard[x][y];
			}
		}
		
	}
	
	private static void findMinimunSquare() {
		for(int sz = 2; sz <=N; sz++) {//크기
			for(int x1 =1; x1<= N-sz+1;  x1++) {
				for(int y1=1; y1<=N-sz+1; y1++) {
					int x2 = x1+sz-1;
					int y2 =y1+sz-1;
					
					if(!(x1<= exits.x && exits.x <=x2 && y1<= exits.y && exits.y <=y2)) {
						continue;
					}
					
					boolean isTravelerIn = false;
					
					for(int l=1; l<=M; l++) {
						if(x1 <= traveler[l].x && traveler[l].x <= x2 && y1 <= traveler[l].y && traveler[l].y <= y2) {
                            // 출구에 있는 참가자는 제외합니다.
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
	
	private static void moveAllTravelers() {
		for(int i=1; i<=M; i++) {
			if(traveler[i].x == exits.x && traveler[i].y == exits.y) { // 이미 출구
				continue;
			}
			
			if(traveler[i].x != exits.x) { //행 먼저 확인
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
			
			if(traveler[i].y != exits.y) { //행이 같으면 열 확인
				int nx = traveler[i].x;
				int ny = traveler[i].y;
				
				if(exits.y > ny) {
					ny++;
				}
				else {
					ny--;
				}
				
				if(board[nx][ny] == 0) {
					traveler[i].x = nx;
					traveler[i].y = ny;
					ans++;
					continue;
				}
				
			}
		}
	}
		
	private static int getDistance(int x1, int y1, int x2, int y2) {
		return Math.abs(x1-x2) + Math.abs(y1-y2);
	}
	
}