import java.io.*;
import java.util.*;

public class Main {
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private static StringTokenizer st;
//	private static final int[] dx = {-1, 0, 1, 0};
//	private static final int[] dy = {0, 1, 0, -1};
	
	private static final int[] dx = {-1,1,0,0,1,1,-1,-1};
	private static final int[] dy = {0,0,1,-1,-1,1,1,-1};
    
	private static boolean isRange(int x, int y) {
		return 0 <= x && x<n && 0<= y && y<n;
	}
	
	private static int stoi(String s) {
		return Integer.parseInt(s);
	}
	
	//=====================================================================
	
	private static int n,m,k,c,ans;
	private static class Point implements Comparable<Point>{
		int x, y;

		public Point(int x, int y) {
			super();
			this.x = x;
			this.y = y;
		}

		@Override
		public int compareTo(Point o) {
			// TODO Auto-generated method stub
			if(this.x == o.x) {
				return this.y - o.y;
			}
			return this.x - o.x;
		}
		
	}
	private static int[][] map, kill, tmp;
	
	private static Point choice;
	
	//=======================================================
	
	private static void init() throws IOException{
		st = new StringTokenizer(br.readLine());
		
		n = stoi(st.nextToken());
		m = stoi(st.nextToken());
		k = stoi(st.nextToken());
		c = stoi(st.nextToken())+1;
		
		map = new int[n][n];
		for(int i=0; i<n; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j=0; j<n; j++) {
				map[i][j] = stoi(st.nextToken());
			}
		}
		
		kill = new int[n][n];
		tmp = new int[n][n];
		
	}
	
	
	
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		init();
		
		for(int i=0; i<m; i++) {
			check();
			
			grow();
			
			breeding();
			
			selectMax();
			
			if(choice.x == -1) {
				continue;
			}
			
			killing();
		}
		System.out.println(ans);
	}
	
	private static void killing() {
		int x = choice.x;
		int y = choice.y;
		
		ans+= map[x][y];
		map[x][y] = -2;
		kill[x][y] = c;
		
		for(int p=4; p<8; p++) {
			for(int q= 1; q<=k; q++) {
				int nx = x+dx[p]*q;
				int ny = y + dy[p]*q;
				
				if(isRange(nx, ny)) {
					if(map[nx][ny]>=0) {
						ans += map[nx][ny];
						kill[nx][ny] = c;
						if(map[nx][ny] == 0) {
							map[nx][ny] = -2;
							break;
						}
						
						map[nx][ny] = -2;
						
					}
					else {
						if(map[nx][ny] == -2) {
							kill[nx][ny] = c;
						}
						break;
					}
				}
				else {
					break;
				}
			}
		}
	}
	
	private static void selectMax() {
		tmp = new int[n][n];
		
		int mx = -1;
		
		for(int i=0; i<n; i++) {
			for(int j=0; j<n; j++) {
				if(map[i][j]>0) {
					int cnt = map[i][j];
					for(int p=4; p<8; p++) {
						for(int q=1; q<=k; q++) {
							int nx = i+dx[p]*q;
							int ny = j+dy[p]*q;
							if(isRange(nx, ny)) {
								if(map[nx][ny]>0) {
									cnt+=map[nx][ny];
								}
								else {
									break;
								}
							}
							else {
								break;
							}
						}
						
					}
					
					tmp[i][j] = cnt;
					mx = Math.max(mx, cnt);
				}
			}
		}
		
		List<Point> list = new ArrayList<>();
		
		for(int i=0; i<n; i++) {
			for(int j=0;j<n; j++) {
				if(tmp[i][j] == mx) {
					list.add(new Point(i,j));
				}
			}
		}
		
		if(list.isEmpty()) {
			choice = new Point(-1,-1);
			return;
		}
		
		Collections.sort(list);
		choice = new Point(list.get(0).x, list.get(0).y);
	}
	
	private static void check() {
		for(int i=0; i<n; i++) {
			for(int j=0; j<n; j++) {
				if(kill[i][j]>0) {
					kill[i][j]--;
				}
				
				if(map[i][j] == -2 && kill[i][j] == 0) {
					map[i][j]=0;
				}
			}
		}
	}
	
	private static void breeding() {
		copy(tmp, map);
		for(int i=0; i<n; i++) {
			for(int j=0; j<n; j++) {
				if(map[i][j]>0) {
					int cnt = 0;
					for(int p=0; p<4; p++) {
						int nx = i+dx[p];
						int ny = j + dy[p];
						
						if(isRange(nx, ny)) {
							if(map[nx][ny] == 0) {
								cnt++;
							}
						}
					}
					
					for(int p=0; p<4; p++) {
						int nx = i+dx[p];
						int ny = j+dy[p];
						if(isRange(nx, ny)) {
							if(map[nx][ny] == 0) {
								tmp[nx][ny] += (map[i][j]/cnt);
							}
						}
					}
				}
			}
		}
		copy(map,tmp);
	}
	
	private static void copy(int[][] a, int[][] b) {
		for(int i=0; i<n; i++) {
			for(int j=0; j<n; j++) {
				a[i][j] = b[i][j];
			}
		}
	}
	
	private static void grow() {
		for(int i=0; i<n; i++) {
			for(int j=0; j<n; j++) {
				if(map[i][j] > 0) {
					int cnt = 0;
					for(int p=0; p<4; p++) {
						int nx = i+dx[p];
						int ny = j+dy[p];
						if(isRange(nx, ny)) {
							if(map[nx][ny] > 0) {
								cnt++;
							}
						}
					}
					
					map[i][j] += cnt;
				}
			}
		}
	}
}