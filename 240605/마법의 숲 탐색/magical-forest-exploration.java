import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {

	static int R,C,K, score, rMax;
	static int[][] m, gArr;
	static int[] di= {-1,0,1,0},
			dj = {0,1,0,-1};
	static boolean[] v;
	static int stoi(String s) {
		return Integer.parseInt(s);
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		R = stoi(st.nextToken());
		C = stoi(st.nextToken());
		K = stoi(st.nextToken());
		
		score = 0;
		initMap();
		gArr = new int[K][];
		
		for (int k = 0; k < K; k++) {
			st = new StringTokenizer(br.readLine());
			int c = stoi(st.nextToken())-1;
			int e = stoi(st.nextToken());
			
			rMax = 0;
			v = new boolean[K];
			drop(k, -2, c, e);
		}
		System.out.println(score);
	}
	
	static void drop(int idx, int ci, int cj, int e) {
		while(true) {
			if(ci == R-2) {
				break;
			}
			
			if ((ci == -2 && m[ci + 2][cj] == -1) || (m[ci + 2][cj] == -1 && m[ci + 1][cj - 1] == -1 && m[ci + 1][cj + 1] == -1)) {
                ci += 1;
                continue;
            }
			
			// go left
            if (cj >= 2) {
                if ((ci == -2 && m[ci + 2][cj - 1] == -1) || ((ci == -1) && m[ci + 2][cj - 1] == -1 && m[ci + 1][cj - 1] == -1 && m[ci + 1][cj - 2] == -1) || (m[ci + 2][cj - 1] == -1 && m[ci + 1][cj - 1] == -1 && m[ci + 1][cj - 2] == -1 && m[ci][cj - 2] == -1)) {
                    ci += 1;
                    cj -= 1;
                    e = (e + 3) % 4;
                    continue;
                }
            }

            // go right
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
		gArr[idx] = new int[] {ci,cj,e};
		
		moveGolem(idx);
		score += rMax;
		
	}
	
	static void moveGolem(int idx) {
		v[idx] = true;
		
		int ri = gArr[idx][0] + 2;
		rMax = ri > rMax ? ri : rMax;
		
		int e = gArr[idx][2];
		ri = gArr[idx][0] + di[e];
		int rj = gArr[idx][1] + dj[e];
		
		for(int d = 0; d<4; d++) {
			int ni = ri+di[d];
			int nj = rj+dj[d];
			if(ni<0 || ni>=R || nj <0 || nj >=C) continue;
			if(m[ni][nj] == -1 || v[m[ni][nj]]) continue;
			
			moveGolem(m[ni][nj]);
		}
		
	}
	
	
	static void initMap() {
		m = new int[R][C];
		for(int r = 0; r<R; r++) {
			Arrays.fill(m[r], -1);
		}
	}

}