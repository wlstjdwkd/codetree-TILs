import java.io.*;
import java.util.*;

public class Main {
	
	private static final int[] dx = {0,1,0,-1};
	private static final int[] dy = {1, 0, -1, 0};
	
	private static final int[] dx2 = {0, 0, 0, -1, -1, -1, 1, 1, 1};
    private static final int[] dy2 = {0, -1, 1, 0, -1, 1, 0, -1, 1};
	
    private static int n,m,k;
    
    private static int[][] board;
    private static int[][] rec;
    
    private static boolean[][] vis;
    private static boolean[][] isActive;
    
    private static List<Turret> liveTurret = new ArrayList<>();
    
//	private static boolean isRange(int x, int y) {
//		return 0 <= x && x<l && 0<= y && y<l;
//	}
	
	private static int stoi(String s) {
		return Integer.parseInt(s);
	}
	
	private static class Pair{
		int x, y;

		public Pair(int x, int y) {
			super();
			this.x = x;
			this.y = y;
		}
		
	}
	
	private static class Turret implements Comparable<Turret>{
		int x,y,r,p;

		public Turret(int x, int y, int r, int p) {
			super();
			this.x = x;
			this.y = y;
			this.r = r;
			this.p = p;
		}

		@Override
		public int compareTo(Turret o) {
			// TODO Auto-generated method stub
			if(this.p == o.p) {
				if(this.r == o.r) {
					if((o.x+o.y) == (this.x+this.y)) {
						return o.y - this.y;
					}
					return (o.x+o.y) - (this.x+this.y);
				}
				return o.r - this.r;
			}
			return this.p - o.p;
		}
		
		
	}
	
	
	
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private static StringTokenizer st;
	
	
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		init();
		
		for(int tc=1; tc<=k; tc++) {
			liveTurret = new ArrayList<>();
			for(int i=0; i<n; i++) {
				for(int j=0; j<m; j++) {
					if(board[i][j]>0) {
						liveTurret.add(new Turret(i,j,rec[i][j], board[i][j]));
					}
				}
			}
			
			if(liveTurret.size()<=1) {
				break;
			}
			
			vis = new boolean[n][m];
			isActive = new boolean[n][m];
			
			selectAttack(tc);
			
			if(!laserAttack()) {
				bombAttack();
			}
			
			reverse();
		}
		
		int ans = 0;
		for(int i=0; i<n; i++) {
			for(int j=0; j<m; j++) {
				ans = Math.max(ans,  board[i][j]);
			}
		}
		System.out.println(ans);
		
	}
	
	private static void reverse() {
		for(int i=0; i<n; i++) {
			for(int j=0; j<m; j++) {
				if(isActive[i][j]) {
					continue;
				}
				
				if(board[i][j] == 0) {
					continue;
				}
				
				board[i][j]++;
			}
		}
	}
	
	private static boolean laserAttack() {
		Turret weakTurret = liveTurret.get(0);
		
		int sx = weakTurret.x;
		int sy = weakTurret.y;
		
		Turret strongTurret = liveTurret.get(liveTurret.size()-1);
		int ex = strongTurret.x;
		int ey = strongTurret.y;
		
		Queue<Pair> q = new ArrayDeque<>();
		q.add(new Pair(sx, sy));
		vis[sx][sy] = true;
		Pair[][] come = new Pair[n][m];
		
		boolean canAttack = false;
		
		while(!q.isEmpty()) {
			Pair pair = q.poll();
			int x = pair.x;
			int y = pair.y;
			if(x == ex && y == ey) {
				canAttack = true;
				break;
			}
			
			for(int dir = 0; dir<4; dir++) {
				int nx = (x+dx[dir] + n) %n;
				int ny = (y+dy[dir] + m) % m;
				
				if(!vis[nx][ny]) {
					if(board[nx][ny] != 0) {
						vis[nx][ny] = true;
						q.add(new Pair(nx,ny));
						come[nx][ny] = new Pair(x,y);
					}
				}
			}
		}
		
		if(canAttack) {
			int x = ex;
			int y =ey;
			while(x!=sx || y!= sy) {
				int power = board[sx][sy]/2;
				if(x == ex && y == ey) {
					power = board[sx][sy];
				}
				
				board[x][y] -= power;
				if(board[x][y] < 0) {
					board[x][y] = 0;
				}
				
				isActive[x][y] = true;
				
				Pair pair = come[x][y];
				x= pair.x;
				y = pair.y;
			}
		}
		
		return canAttack;
	}
	
	private static void bombAttack() {
		Turret weakTurret = liveTurret.get(0);
		int sx = weakTurret.x;
		int sy = weakTurret.y;
		int pow = weakTurret.p;
		
		Turret strongTurret = liveTurret.get(liveTurret.size()-1);
		int ex = strongTurret.x;
		int ey = strongTurret.y;
		
		for(int dir = 0; dir<9; dir++) {
			int nx = (ex + dx2[dir] + n) % n;
			int ny = (ey + dy2[dir] + m) % m;
			
			if(nx == sx && ny == sy) {
				continue;
			}
			
			if(nx == ex && ny == ey) {
				board[nx][ny] -= pow;
			}
			else {
				board[nx][ny] -= pow/2;
			}
			
			if(board[nx][ny] < 0) {
				board[nx][ny] = 0;
			}
			isActive[nx][ny] = true;
		}
	}
	
	private static void selectAttack(int turn) {
		Collections.sort(liveTurret);
		
		Turret weakTurret = liveTurret.get(0);
		
		int x = weakTurret.x;
		int y = weakTurret.y;
		
		board[x][y] += (n+m);
		rec[x][y] = turn;
		weakTurret.p = board[x][y];
		weakTurret.r = turn;
		isActive[x][y] = true;
	}
	
	private static void init() throws IOException{
		st = new StringTokenizer(br.readLine());
		n = stoi(st.nextToken());
		m = stoi(st.nextToken());
		k = stoi(st.nextToken());
		
		board = new int[n][m];
		rec = new int[n][m];
		
		for(int i=0; i<n; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j=0; j<m; j++) {
				board[i][j] = stoi(st.nextToken());
			}
		}
		
	}
	
	
	
	
}