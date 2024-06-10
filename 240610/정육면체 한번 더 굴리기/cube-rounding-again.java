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
			return this.x == p.x && this.y ==p.y;
		}
	}
	
	private static class Dice{
		int u,f,r;

		public Dice(int u, int f, int r) {
			super();
			this.u = u;
			this.f = f;
			this.r = r;
		}
		
	}
	
	private static int n,m;
	
	private static int x,y;
	private static int moveDir;
	
	private static final int[] dx = {0,1,0,-1};
	private static final int[] dy = {1,0,-1,0};
	
	private static Queue<Pair> q = new ArrayDeque<>();
	private static boolean[][] visited;
	
	private static int[][] grid;
	
	private static int u =1, f=2, r=3;
	
	private static int ans;
	
	private static boolean isRange(int x, int y) {
		return 0<=x && x<n && 0<=y && y<n;
	}
	
	private static int stoi(String s) {
		return Integer.parseInt(s);
	}
	
	private static void simulate() {
		int nx = x+dx[moveDir];
		int ny = y + dy[moveDir];
		if(!isRange(nx,ny)) {
			moveDir = (moveDir<2)? (moveDir+2) : (moveDir-2);
			nx = x +dx[moveDir];
			ny = y + dy[moveDir];
		}
		
		x = nx;
		y = ny;
		
		ans += getScore();
		
		Dice dice = new Dice(-1,-1,-1);
		if(moveDir == 0) // 오른쪽
            dice = new Dice(7 - r, f, u);
        else if(moveDir == 1) // 아래쪽
            dice = new Dice(7 - f, u, r);
        else if(moveDir == 2) // 왼쪽
            dice = new Dice(r, f, 7 - u);
        else if(moveDir == 3) // 위쪽
            dice = new Dice(f, 7 - u, r);
		
		u = dice.u;
		f = dice.f;
		r = dice.r;
		
		int bottom = 7-u;
		
		if(bottom > grid[x][y]) {
			moveDir = (moveDir +1) %4;
		}
		
		else if(bottom < grid[x][y]) {
			moveDir = (moveDir-1 + 4) %4;
		}
		
	}
	
	private static int getScore() {
		int targetNum = grid[x][y];
		
		visited = new boolean[n][n];
		
		visited[x][y] = true;
		q.add(new Pair(x,y));
		int score = 0;
		while(!q.isEmpty()) {
			Pair cur = q.poll();
			int curX = cur.x;
			int curY = cur.y;
			score += targetNum;
			
			for(int i=0; i<4; i++) {
				int newX = curX + dx[i];
				int newY = curY +dy[i];
				
				if(isRange(newX, newY)) {
					if(!visited[newX][newY]) {
						if(targetNum == grid[newX][newY]) {
							q.add(new Pair(newX, newY));
							visited[newX][newY] = true;
						}
					}
				}
			}
		}
		
		return score;
		
	}

	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		n = stoi(st.nextToken());
		m = stoi(st.nextToken());
		
		grid = new int[n][n];
		
		
		for(int i=0; i<n; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j=0; j<n; j++) {
				grid[i][j] = stoi(st.nextToken());
			}
		}
		
		while(m-->0) {
			simulate();
		}
		
		System.out.println(ans);
	}
	
	
}