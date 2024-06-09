import java.io.*;
import java.util.*;

public class Main {

	private static int R,C,K, score, rMax;
	private static int[][] m;
	private static Golem[] golem;
	
	private static final int[] di = {-1,0,1,0}, dj = {0,1,0,-1};
	private static boolean[] v;
	
	private static boolean isRange(int x, int y) {
		return 0<=x && x<R && 0<=y && y<C;
	}
	
	private static int stoi(String s) {
		return Integer.parseInt(s);
	}
	
	private static class Golem {
		int x, y, dir;

		public Golem(int x, int y, int dir) {
			super();
			this.x = x;
			this.y = y;
			this.dir = dir;
		}
		
	}
	
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		R = stoi(st.nextToken());
		C = stoi(st.nextToken());
		K =stoi(st.nextToken());
		
		score = 0;
		initMap();
		golem = new Golem[K];
		
		for(int k=0; k<K; k++) {
			st = new StringTokenizer(br.readLine());
			int c = stoi(st.nextToken())-1;
			int e = stoi(st.nextToken());
			
			rMax = 0;
			v = new boolean[K];
			drop(k, -2, c, e);
		}
		System.out.println(score);
	}
	
	private static void drop(int idx, int ci, int cj, int e) {
		while(ci < R-2) {
			//내려오는거
			if ((ci == -2 && m[ci + 2][cj] == -1) || (m[ci + 2][cj] == -1 && m[ci + 1][cj - 1] == -1 && m[ci + 1][cj + 1] == -1)) {
				ci++;
				continue;
			}
			
			//왼쪽 구르기
			if(cj>=2) {
				if ((ci == -2 && m[ci + 2][cj - 1] == -1) || ((ci == -1) && m[ci + 2][cj - 1] == -1 && m[ci + 1][cj - 1] == -1 && m[ci + 1][cj - 2] == -1) || (m[ci + 2][cj - 1] == -1 && m[ci + 1][cj - 1] == -1 && m[ci + 1][cj - 2] == -1 && m[ci][cj - 2] == -1)) {
					ci++;
					cj--;
					e = (e+3)%4;
					continue;
				}
			}
			
			if (cj < C - 2) {
                if ((ci == -2 && m[ci + 2][cj + 1] == -1) || ((ci == -1) && m[ci + 2][cj + 1] == -1 && m[ci + 1][cj + 1] == -1 && m[ci + 1][cj + 2] == -1) || (m[ci + 2][cj + 1] == -1 && m[ci + 1][cj + 1] == -1 && m[ci + 1][cj + 2] == -1 && m[ci][cj + 2] == -1)) {
                    ci += 1;
                    cj += 1;
                    e = (e + 1) % 4;
                    continue;
                }
            }
			
			break;
		}
		
		if(ci<=0) {
			initMap();
			return;
		}
		
		m[ci][cj] = m[ci - 1][cj] = m[ci][cj + 1] = m[ci + 1][cj] = m[ci][cj - 1] = idx;
		golem[idx] = new Golem(ci, cj, e);
		
		moveGolem(idx);
		score += rMax;
	}
	
	private static void moveGolem(int idx) {
		v[idx] = true;
		
		Golem curGol = golem[idx];
		
		int ri = curGol.x +2;
		rMax = ri > rMax ? ri : rMax;
		
		int e = curGol.dir;
		ri = curGol.x + di[e];
		int rj = curGol.y + dj[e];
		
		for(int d = 0; d<4; d++) {
			int ni = ri+di[d];
			int nj = rj + dj[d];
			
			if(isRange(ni,nj)) {
				if(m[ni][nj] == -1 || v[m[ni][nj]]) {
					continue;
				}
				
				moveGolem(m[ni][nj]);
			}
		}
	}
	
	private static void initMap() {
		m = new int[R][C];
		for(int i=0; i<R; i++) {
			for(int j=0; j<C; j++) {
				m[i][j] = -1;
			}
		}
	}
	
	
	
}