import java.io.*;
import java.util.*;

public class Main {
	
	private static class Shark{
		int r,c,d;

		public Shark(int r, int c, int d) {
			super();
			this.r = r;
			this.c = c;
			this.d = d;
		}
		
	}
	
	private static int N,M,K;
	private static int[][] resttime;
	private static int[][] smell;
	private static int[][][] priority;
	private static final int[] dr = {0,-1,1,0,0};
	private static final int[] dc = {0,0,0,-1,1};
	
	private static Shark[] shark;
	
	
	static int stoi(String s) {
		return Integer.parseInt(s);
	}
	
	static boolean isRange(int x, int y) {
		return 1<=x && x<=N && 1<=y && y<=N;
	} 
	
	
	public static void main(String[] args) throws IOException {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N= stoi(st.nextToken());
		M = stoi(st.nextToken());
		K = stoi(st.nextToken());
		
		resttime = new int[N+1][N+1];
		smell = new int[N+1][N+1];
		priority = new int[M+1][5][4];
		shark = new Shark[M+1];
		
		for(int i=1; i<=N; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j=1; j<=N; j++) {
				int n = stoi(st.nextToken());
				
				if(n>0) {
					resttime[i][j] = K;
					smell[i][j] = n;
					shark[n] = new Shark(i,j,0);
				}
			}
		}
		
		st = new StringTokenizer(br.readLine());
		for(int i=1; i<=M; i++) {
			shark[i].d = stoi(st.nextToken());
		}
		
		for(int i=1; i<=M; i++) {
			for(int j=1; j<=4; j++) {
				st = new StringTokenizer(br.readLine());
				for(int k=0; k<4; k++) {
					priority[i][j][k] = stoi(st.nextToken());
				}
			}
		}
		
		System.out.println(solve());
	}
	
	private static int solve() {
		int time = 0;
		
		while(true) {
			int count = 0;
			for(int m=1; m<=M; m++) {
				if(shark[m] != null) {
					count++;
				}
			}
			
			if(count == 1 && shark[1] != null) {
				return time;
			}
			
			if(time>=1000) {
				return -1;
			}
			
			int[][] tmp = new int[N+1][N+1];
			
			for(int m = 1; m<=M; m++) {
				if(shark[m] != null) {
					moveShark(tmp, m);
				}
			}
			
			
			for(int i=1; i<=N; i++) {
				for(int j=1; j<=N;j ++) {
					if(resttime[i][j] > 0) {
						resttime[i][j]--;
					}
					
					if(resttime[i][j] == 0) {
						smell[i][j] = 0;
					}
				}
			}
			
			for(int i=1; i<=N; i++) {
				for(int j=1; j<=N; j++) {
					if(tmp[i][j] >0) {
						resttime[i][j] = K;
						smell[i][j] = tmp[i][j];
					}
				}
			}
			time++;
		}
	}
	
	private static void moveShark(int[][] tmp, int m) {
		int nr = 0;
		int nc = 0;
		int d = 0;
		boolean flag = false;
		
		for(int i=0; i<4; i++) {
			d = priority[m][shark[m].d][i];
			nr = shark[m].r +dr[d];
			nc = shark[m].c + dc[d];
			
			if(isRange(nr,nc)) {
				if(smell[nr][nc] == 0) {
					flag = true;
					break;
				}
			}
		}
		
		if(!flag) {
			for(int i=0; i<4; i++) {
				d = priority[m][shark[m].d][i];
				nr = shark[m].r +dr[d];
				nc = shark[m].c + dc[d];
				
				if(isRange(nr,nc)) {
					if(smell[nr][nc] == m) {
						break;
					}
				}
			}
		}
		
		if(tmp[nr][nc] == 0) {
			tmp[nr][nc] = m;
			shark[m].r = nr;
			shark[m].c = nc;
			shark[m].d = d;
		}
		else {
			shark[m] = null;
		}
	}

}