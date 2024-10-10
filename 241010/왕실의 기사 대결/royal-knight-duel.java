import java.io.*;
import java.util.*;

public class Main {
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private static StringTokenizer st;
	private static final int[] dx = {-1,0,1,0};
	private static final int[] dy = {0,1,0,-1};
	
//    public static int[] dx = new int[]{0, -1, -1, -1,  0,  1, 1, 1};
//    public static int[] dy = new int[]{1,  1,  0, -1, -1, -1, 0, 1};
	
	private static boolean isRange(int x, int y) {
		return 0 <= x && x<l && 0<= y && y<l;
	}
	
	private static int stoi(String s) {
		return Integer.parseInt(s);
	}
	
	//=====================================================================
	
	private static int l,n,q;
	private static class Knight{
		int r,c,h,w,k,bef_k;

		public Knight(int r, int c, int h, int w, int k, int bef_k) {
			super();
			this.r = r;
			this.c = c;
			this.h = h;
			this.w = w;
			this.k = k;
			this.bef_k = bef_k;
		}
		
	}
	
	private static int[][] info;
	private static int[] nr;
	private static int[] nc;
	private static int[] dmg;
	private static boolean[] is_moved;
	
	private static Knight[] knights;
	
	//=======================================================
	
	private static void init() throws IOException{
		st = new StringTokenizer(br.readLine());
		
		l = stoi(st.nextToken());
		n = stoi(st.nextToken());
		q = stoi(st.nextToken());
		
		nr = new int[n+1];
		nc = new int[n+1];
		is_moved = new boolean[n+1];
		dmg = new int[n+1];
		
		knights = new Knight[n+1];
		info = new int[l+1][l+1];
		
		for(int i=1; i<=l; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j=1; j<=l; j++) {
				info[i][j] = stoi(st.nextToken());
			}
		}
		
		for(int i=1; i<=n; i++) {
			st = new StringTokenizer(br.readLine());
			
			int r = stoi(st.nextToken());
			int c = stoi(st.nextToken());
			int h = stoi(st.nextToken());
			int w = stoi(st.nextToken());
			int k = stoi(st.nextToken());
			int bef_k =k;
			knights[i] = new Knight(r,c,h,w,k,bef_k);
		}
	}
	
	
	
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		init();
		
		for(int i=1; i<=q; i++) {
			st= new StringTokenizer(br.readLine());
			int idx = stoi(st.nextToken());
			int dir = stoi(st.nextToken());
			movePiece(idx, dir);
		}
		
		long ans = 0;
		
		for(int i=1; i<=n; i++) {
			if(knights[i].k>0) {
				ans += knights[i].bef_k - knights[i].k;
			}
		}
		System.out.println(ans);
	}
	
	private static void movePiece(int idx, int dir) {
		if(knights[idx].k <=0) {
			return;
		}
		
		if(tryMovement(idx,dir)) {
			for(int i=1; i<=n; i++) {
				knights[i].r = nr[i];
				knights[i].c = nc[i];
				knights[i].k -= dmg[i];
			}
		}
	}
	
	private static boolean tryMovement(int idx, int dir) {
		Queue<Integer> q = new ArrayDeque<>();
		
		for(int i=1; i<=n; i++) {
			dmg[i] = 0;
			nr[i] = knights[i].r;
			nc[i] = knights[i].c;
			is_moved[i] = false;
		}
		
		is_moved[idx] = true;
		q.add(idx);
		while(!q.isEmpty()) {
			int cur = q.poll();
			
			nr[cur] += dx[dir];
			nc[cur] += dy[dir];
			
			if(nr[cur] < 1 || nc[cur]<1 || nr[cur] + knights[cur].h -1 > l || nc[cur]+knights[cur].w -1 > l) {
				return false;
			}
			
			for(int i=nr[cur]; i<= nr[cur] + knights[cur].h -1; i++) {
				for(int j=nc[cur]; j<= nc[cur] + knights[cur].w -1; j++) {
					if(info[i][j] == 1) {
						dmg[cur]++;
					}
					else if(info[i][j] == 2) {
						return false;
					}
				}
			}
			
			for(int i=1; i<=n; i++) {
				if(!is_moved[i]) {
					if(knights[i].k>0) {
						if(knights[i].r > nr[cur] + knights[cur].h-1 ||nr[cur] > knights[i].r+knights[i].h-1) {
							continue;
						}
						
						if(knights[i].c > nc[cur] + knights[cur].w -1 || nc[cur] > knights[i].c + knights[i].w -1) {
							continue;
						}
						
						is_moved[i] = true;
						q.add(i);
					}
				}
			}
		}
		
		dmg[idx] = 0;
		return true;
	}
}