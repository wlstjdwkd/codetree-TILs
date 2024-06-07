import java.io.*;
import java.util.*;

public class Main {

	
	private static int l,n,q;
	private static int[][] info;
	private static int[] bef_k;
	private static int[] r;
	private static int[] c;
	private static int[] h;
	private static int[] w;
	private static int[] k;
	
	private static int[] nr;
	private static int[] nc;
	
	private static int[] dmg;
	private static boolean[] is_moved;
	
	
	private static int[] dx = {-1,0,1,0};
	private static int[] dy = {0,1,0,-1};
	
	private static boolean isRange(int x, int y) {
		return 0<=x && x<l && 0<=y && y<l;
	}
	
	private static int stoi(String s) {
		return Integer.parseInt(s);
	}
	
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		l = stoi(st.nextToken());
		n = stoi(st.nextToken());
		q = stoi(st.nextToken());
		
		info = new int[l+1][l+1];
		r = new int[n+1];
		c = new int[n+1];
		h = new int[n+1];
		w = new int[n+1];
		k = new int[n+1];
		bef_k = new int[n+1];
		
		nr = new int[n+1];
		nc = new int[n+1];
		dmg = new int[n+1];
		
		is_moved = new boolean[n+1];
		
		
		for(int i=1; i<=l; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j=1; j<=l; j++) {
				info[i][j] = stoi(st.nextToken());
			}
		}
		
		for(int i=1; i<=n; i++) {
			st = new StringTokenizer(br.readLine());
			r[i] = stoi(st.nextToken());
			c[i] = stoi(st.nextToken());
			h[i] = stoi(st.nextToken());
			w[i] = stoi(st.nextToken());
			k[i] = stoi(st.nextToken());
			bef_k[i] = k[i];
		}
		
		for(int i=1; i<=q; i++) {
			st = new StringTokenizer(br.readLine());
			int idx = stoi(st.nextToken());
			int dir = stoi(st.nextToken());
			movePiece(idx, dir);
		}
		
		long ans = 0;
		for(int i=1; i<=n; i++) {
			if(k[i] > 0) {
				ans += bef_k[i] - k[i];
			}
		}
		System.out.println(ans);
	}
	
	private static void movePiece(int idx, int dir) {
		if(k[idx] <=0) {
			return;
		}
		
		if(tryMovement(idx, dir)) {
			for(int i=1; i<=n; i++) {
				r[i] = nr[i];
				c[i] = nc[i];
				k[i] -= dmg[i];
			}
		}
	}
	
	private static boolean tryMovement(int idx, int dir) {
		Queue<Integer> q = new ArrayDeque<>();
		boolean is_pos = true;
		
		for(int i=1; i<=n; i++) {
			dmg[i] = 0;
			is_moved[i] = false;
			nr[i] = r[i];
			nc[i] = c[i];
		}
		
		q.add(idx);
		is_moved[idx] = true;
		
		while(!q.isEmpty()) {
			int x =q.poll();
			
			nr[x] += dx[dir];
			nc[x] += dy[dir];
			
			if(!isRange(nr[x], nc[x])) {
				return false;
			}
			
			for(int i = nr[x]; i<= nr[x] + h[x] -1; i++) {
				for(int j=nc[x]; j<= nc[x] + w[x]-1; j++) {
					if(info[i][j] == 1) {
						dmg[x]++;
					}
					if(info[i][j] == 2) {
						return false;
					}
				}
			}
			
			for(int i=1; i<=n; i++) {
				if(is_moved[i] || k[i] <=0) {
					continue;
				}
				if(r[i] > nr[x] + h[x] -1 || nr[x] > r[i] + h[i] -1) {
					continue;
				}
				if(c[i] > nc[x] + w[x] - 1 || nc[x] > c[i] + w[i] - 1) 
                    continue;
				
				is_moved[i] = true;
				q.add(i);
			}
		}
		
		dmg[idx] = 0;
		return true;
	}
	
}