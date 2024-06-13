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

	private static class Turret implements Comparable<Turret>{
		int x, y, r, p;

		public Turret(int x, int y, int r, int p) {
			super();
			this.x = x;
			this.y = y;
			this.r = r;
			this.p = p;
		}

		@Override
		public int compareTo(Main.Turret o) {
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
	
	private static final int[] dx = {0,1,0,-1};
	public static int[] dy = new int[]{1, 0, -1, 0};
    public static int[] dx2 = new int[]{0, 0, 0, -1, -1, -1, 1, 1, 1};
    public static int[] dy2 = new int[]{0, -1, 1, 0, -1, 1, 0, -1, 1};
    
    private static int n,m,k;
    private static int turn;
    
    private static int[][] board;
    private static int[][] rec;
    
    private static boolean[][] vis;
    
    private static boolean[][] isActive;
    
    private static List<Turret> liveTurret = new ArrayList<>();
    
    public static int[][] backX;
    public static int[][] backY;
    
	
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
		m = stoi(st.nextToken());
		k = stoi(st.nextToken());
		
		board = new int[n][m];
		rec = new int[n][m];
		
		backX = new int[n][m];
		backY = new int[n][m];
		
		for(int i=0; i<n; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j=0; j<m; j++) {
				board[i][j] = stoi(st.nextToken());
			}
		}
		
		for(int tc =1; tc<=k; tc++) {
			liveTurret = new ArrayList<>();
			for(int i=0; i<n; i++) {
				for(int j=0; j<m; j++) {
					if(board[i][j] > 0) {
						liveTurret.add(new Turret(i,j,rec[i][j],board[i][j]));
					}
				}
			}
			
			if(liveTurret.size() <=1) {
				break;
			}
			
			init();
			
			awake(tc);
			
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
	
	private static void bombAttack() {
		Turret weakTurret = liveTurret.get(0);
        int sx = weakTurret.x;
        int sy = weakTurret.y;
        int pow = weakTurret.p;
    
        // 기존에 정렬된 가장 뒤 포탑이
        // 각성한 포탑을 제외한 포탑 중 가장 강한 포탑입니다.
        Turret strongTurret = liveTurret.get(liveTurret.size() - 1);
        int ex = strongTurret.x;
        int ey = strongTurret.y;
        
        for(int dir=0; dir<9; dir ++) {
        	int nx = (ex+dx2[dir] + n) %n;
        	int ny = (ey + dy2[dir] +m) %m;
        	
        	if(nx == sx && ny == sy) {
        		continue;
        	}
        	
        	if(nx == ex && ny == ey) {
        		board[nx][ny] -= pow;
        		
        	}
        	else {
        		board[nx][ny] -= pow/2;
        	}
        	
        	if(board[nx][ny] <0) {
        		board[nx][ny] = 0;
        	}
        	isActive[nx][ny] = true;
        }
	}
	
	// 레이저 공격을 진행합니다.
    public static boolean laserAttack() {
        // 기존에 정렬된 가장 앞선 포탑이
        // 각성한 포탑입니다.
        Turret weakTurret = liveTurret.get(0);
        int sx = weakTurret.x;
        int sy = weakTurret.y;
        int pow = weakTurret.p;
    
        // 기존에 정렬된 가장 뒤 포탑이
        // 각성한 포탑을 제외한 포탑 중 가장 강한 포탑입니다.
        Turret strongTurret = liveTurret.get(liveTurret.size() - 1);
        int ex = strongTurret.x;
        int ey = strongTurret.y;
    
        // bfs를 통해 최단경로를 관리해줍니다.
        Queue<Pair> q = new LinkedList<>();
        vis[sx][sy] = true;
        q.add(new Pair(sx, sy));
    
        // 가장 강한 포탑에게 도달 가능한지 여부를 canAttack에 관리해줍니다.
        boolean canAttack = false;
    
        while(!q.isEmpty()) {
            int x = q.peek().x;
            int y = q.peek().y;
            q.poll();
    
            // 가장 강한 포탑에게 도달할 수 있다면
            // 바로 멈춥니다.
            if(x == ex && y == ey) {
                canAttack = true;
                break;
            }
    
            // 각각 우, 하, 좌, 상 순서대로 방문하며 방문 가능한 포탑들을 찾고
            // queue에 저장해줍니다.
            for(int dir = 0; dir < 4; dir++) {
                int nx = (x + dx[dir] + n) % n;
                int ny = (y + dy[dir] + m) % m;
    
                // 이미 방문한 포탑이라면 넘어갑니다.
                if(vis[nx][ny]) 
                    continue;
    
                // 벽이라면 넘어갑니다.
                if(board[nx][ny] == 0) 
                    continue;
    
                vis[nx][ny] = true;
                backX[nx][ny] = x;
                backY[nx][ny] = y;
                q.add(new Pair(nx, ny));
            }
        }
    
        // 만약 도달 가능하다면 공격을 진행합니다.
        if(canAttack) {
            // 우선 가장 강한 포탑에게는 pow만큼의 공격을 진행합니다.
            board[ex][ey] -= pow;
            if(board[ex][ey] < 0) 
                board[ex][ey] = 0;
            isActive[ex][ey] = true;
    
            // 기존의 경로를 역추적하며
            // 경로 상에 있는 모든 포탑에게 pow / 2만큼의 공격을 진행합니다.
            int cx = backX[ex][ey];
            int cy = backY[ex][ey];
    
            while(!(cx == sx && cy == sy)) {
                board[cx][cy] -= pow / 2;
                if(board[cx][cy] < 0) 
                    board[cx][cy] = 0;
                isActive[cx][cy] = true;
    
                int nextCx = backX[cx][cy];
                int nextCy = backY[cx][cy];
    
                cx = nextCx;
                cy = nextCy;
            }
        }
    
        // 공격을 성공했는지 여부를 반환합니다.
        return canAttack;
    }
	
	private static void awake(int turn) {
		Collections.sort(liveTurret);
		
		Turret weakTurret = liveTurret.get(0);
		
		int x = weakTurret.x;
		int y = weakTurret.y;
		
		board[x][y] += (n+m);
		rec[x][y] = turn;
		weakTurret.p = board[x][y];
		weakTurret.r = turn;
		isActive[x][y] = true;
		
		liveTurret.set(0, weakTurret);
		
	}
	
	private static void init() {
		vis = new boolean[n][m];
		isActive = new boolean[n][m];
	}
	
}