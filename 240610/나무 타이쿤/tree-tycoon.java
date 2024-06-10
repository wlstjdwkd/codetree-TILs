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
	
	private static final int DIR_NUM = 8;
	
	private static int n,m;
	
	private static int[][] height;
	
	private static boolean[][] fertilizer;
	private static boolean[][] nextFert;
	
	// 문제에서 주어진 순서대로 → ↗ ↑ ↖ ← ↙ ↓ ↘
    public static int[] dx = new int[]{0, -1, -1, -1,  0,  1, 1, 1};
    public static int[] dy = new int[]{1,  1,  0, -1, -1, -1, 0, 1};
	
	
	private static boolean isRange(int x, int y) {
		return 0<=x && x<n && 0<=y && y<n;
	}
	
	private static int stoi(String s) {
		return Integer.parseInt(s);
	}
	
	
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		n = stoi(st.nextToken());
		m = stoi(st.nextToken());
		
		height = new int[n][n];
		fertilizer = new boolean[n][n];
		
		for(int i=0; i<n; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j=0; j<n; j++) {
				height[i][j] = stoi(st.nextToken());
			}
		}
		
		initFertilizer();
		
		while(m-->0) {
			st = new StringTokenizer(br.readLine());
			int d = stoi(st.nextToken());
			int p = stoi(st.nextToken());
			
			simulate(d-1, p);
		}
		
		System.out.println(getScore());
	}
	
	private static int getScore() {
		int sum = 0;
		for(int i=0; i<n; i++) {
			for(int j=0; j<n; j++) {
				sum += height[i][j];
			}
		}
		return sum;
	}
	
	private static void move(int d, int p) {
		nextFert = new boolean[n][n];
		
		for(int i=0; i<n; i++) {
			for(int j=0; j<n; j++) {
				if(fertilizer[i][j]) {
					int nx = (i + dx[d] *p + n) %n;
					int ny = (j + dy[d] * p + n) % n;
					
					nextFert[nx][ny] = true;
				}
			}
		}
		
		for(int i=0; i<n; i++) {
			for(int j=0; j<n; j++) {
				fertilizer[i][j] = nextFert[i][j];
			} 
		}
	}
	
	private static void grow() {
		for(int i=0; i<n; i++) {
			for(int j=0; j<n; j++) {
				if(fertilizer[i][j]) {
					height[i][j]++;
				}
			}
		}
	}
	
	private static void diagonalGrow() {
		for(int i=0; i<n; i++) {
			for(int j=0; j<n; j++) {
				if(fertilizer[i][j]) {
					int cnt = getDiagCnt(i,j);
					height[i][j] +=cnt;
				}
			}
		}
	}
	
	private static int getDiagCnt(int x, int y) {
		int cnt = 0;
		for(int i=1; i<DIR_NUM; i+=2) {
			int nx = x+dx[i];
			int ny = y + dy[i];
			
			if(isRange(nx, ny) && height[nx][ny] >=1) {
				cnt++;
			}
		}
		return cnt;
	}
	
	private static void determineFert() {
		for(int i=0; i<n; i++) {
			for(int j=0; j<n; j++) {
				if(fertilizer[i][j]) {
					fertilizer[i][j] = false;
				}
				
				else if(height[i][j] >=2) {
					fertilizer[i][j] = true;
					height[i][j] -=2;
				}
			}
		}
	}
	
	private static void simulate(int d, int p) {
		move(d,p);
		
		grow();
		
		diagonalGrow();
		
		determineFert();
	}
	
	private static void initFertilizer() {
		for(int i=n-2; i<n; i++) {
			for(int j=0; j<2; j++) {
				fertilizer[i][j] = true;
			}
		}
	}
	
	
}