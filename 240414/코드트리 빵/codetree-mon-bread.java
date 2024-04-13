import java.io.*;
import java.util.*;

public class Main {
	private static int N,M;
	private static int[][] map;
	private static int[][] store;
	private static boolean[][] v;
	private static Queue<int[]> people;
	private static int[] dx = {-1,0,0,1};
	private static int[] dy = {0,-1,1,0};
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		map = new int[N][N];
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < N; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		store = new int[M][2];
		v = new boolean[N][N];
		for (int i = 0; i < M; i++) {
			st = new StringTokenizer(br.readLine());
			store[i][0] = Integer.parseInt(st.nextToken())-1;
			store[i][1] = Integer.parseInt(st.nextToken())-1;
		}
		
		people = new ArrayDeque<>();
		int cnt = 0;
		while(true) {
			int size = people.size();
			if (size == 0 && cnt!=0) {
				break;
			}
			
			while(size-- >0) {
				int[] t = people.poll();
				int[] add = bfs(t[0], t[1], t[2]);
				if (add[1] == store[t[0]][0] && add[2] == store[t[0]][1]) {
					v[add[1]][add[2]] = true;
				}
				else {
					people.add(add);
				}
			}
			
			if (cnt<M) {
				people.add(find_basecamp(cnt, store[cnt][0], store[cnt][1]));
			}
			cnt++;
		}
		System.out.println(cnt);
	}
	
	private static int[] bfs(int n, int x,int y) {
		Queue<int[]> q = new ArrayDeque<>();
		q.offer(new int[] {x,y,-1,-1});
		boolean[][] visited = new boolean[N][N];
		visited[x][y] = true;
		while(!q.isEmpty()) {
			int[] now = q.poll();
			if (now[0] == store[n][0] && now[1] == store[n][1]) {
				return new int[] {n, now[2], now[3]};
			}
			
			for (int d = 0; d < 4; d++) {
				int nx = now[0] + dx[d];
				int ny = now[1] + dy[d];
				if(nx<0 || ny<0 || nx>= N || ny >=N ||visited[nx][ny] || v[nx][ny]) {
					continue;
				}
				
				visited[nx][ny] = true;
				if(now[2] == -1) {
					q.offer(new int[] {nx,ny,nx,ny});
				}
				else {
					q.offer(new int[] {nx,ny,now[2], now[3]});
				}
			}
		}
		
		return null;
	}
	
	private static int[] find_basecamp(int n, int x, int y) {
		Queue<int[]>q = new ArrayDeque<>();
		q.offer(new int[] {x,y});
		boolean[][] visited = new boolean[N][N];
		visited[x][y] = true;
		while(!q.isEmpty()) {
			int[] now = q.poll();
			
			if (map[now[0]][now[1]] == 1) {
				v[now[0]][now[1]] = true;
				return new int[] {n,now[0],now[1]};
			}
			
			for (int d = 0; d < 4; d++) {
				int nx = now[0] + dx[d];
				int ny = now[1] + dy[d];
				if(nx<0 || ny<0 || nx>= N || ny >=N ||visited[nx][ny] || v[nx][ny]) {
					continue;
				}
				visited[nx][ny] = true;
				q.add(new int[] {nx,ny});
			}
		}
		return null;
	}
}