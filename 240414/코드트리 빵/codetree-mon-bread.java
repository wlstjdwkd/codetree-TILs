import java.util.*;
import java.io.*;

public class Main {
	static int N, M;
	static int[][] map;
	static int[][] store;
	static boolean[][] v;
	static ArrayDeque<int[]> people;
	static int[] dx = {-1, 0, 0, 1}, dy = {0, -1, 1, 0};
    public static void main(String args[]) throws Exception{
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(bf.readLine(), " ");
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        map = new int[N][N];
        for(int i=0; i<N; i++) {
        	st = new StringTokenizer(bf.readLine(), " ");
        	for(int j=0; j<N; j++)
        		map[i][j] = Integer.parseInt(st.nextToken());
        }
        store = new int[M][2];
        v = new boolean[N][N];
        for(int i=0; i<M; i++){
        	st = new StringTokenizer(bf.readLine(), " ");
        	store[i][0] = Integer.parseInt(st.nextToken())-1;
        	store[i][1] = Integer.parseInt(st.nextToken())-1;
        }
        people = new ArrayDeque<>();
        int cnt = 0;
        while(true) {
        	int size = people.size();
        	if(size == 0 && cnt!=0) break;
        	while(size-- > 0) {
        		int[] t = people.poll();
        		int[] add = bfs(t[0], t[1], t[2]);
        		if(add[1] == store[t[0]][0] && add[2] == store[t[0]][1]) // 편의점 도착
        			v[add[1]][add[2]] = true;
        		else people.offer(add);
        	}
        	if(cnt < M)
        		people.offer(find_basecamp(cnt, store[cnt][0], store[cnt][1]));
        	cnt++;
        }
        System.out.println(cnt);
    }
    static int[] bfs(int n, int x, int y) {
    	ArrayDeque<int[]> q = new ArrayDeque<>();
    	q.offer(new int[] {x, y, -1, -1});
    	boolean[][] visited = new boolean[N][N];
    	visited[x][y] = true;
    	while(!q.isEmpty()) {
    		int[] now = q.poll();
    		if(now[0] == store[n][0] && now[1] == store[n][1]) {
    			return new int[] {n, now[2], now[3]};
    		}
    		for(int d=0; d<4; d++) {
    			int nx = now[0] + dx[d];
    			int ny = now[1] + dy[d];
    			if(nx < 0 || ny < 0 || nx >= N || ny >= N || visited[nx][ny] || v[nx][ny]) continue;
    			visited[nx][ny] = true;
    			if(now[2] == -1) {
    				q.offer(new int[] {nx, ny, nx, ny});
    			}
    			else
    				q.offer(new int[] {nx, ny, now[2], now[3]});
    		}
     	}
    	return null; // number+이동한 곳의 좌표를 리턴!
    }
    static int[] find_basecamp(int n, int x, int y) { // @param : 편의점의 위치 - 가장 가까운 base camp 찾기
//    	System.out.println(x+", "+y);
    	ArrayDeque<int[]> q = new ArrayDeque<>();
    	q.offer(new int[] {x, y});
    	boolean[][] visited = new boolean[N][N];
    	visited[x][y] = true;
    	while(!q.isEmpty()) {
    		int[] now = q.poll();
    		
    		if(map[now[0]][now[1]] == 1) {
    			v[now[0]][now[1]] = true;
    			return new int[] {n, now[0], now[1]};
    		}
    		for(int d=0; d<4; d++) {
    			int nx = now[0] + dx[d];
    			int ny = now[1] + dy[d];
    			if(nx < 0 || ny < 0 || nx >= N || ny >= N || visited[nx][ny] || v[nx][ny]) continue;
    			visited[nx][ny] = true;
    			q.offer(new int[] {nx, ny});
    		}
     	}
    	return null; // number+해당 베이스캠프의 좌표를 리턴!
    }
}