import java.io.*;
import java.util.*;

public class Main {
	
	private static final int[] dx = {-1, 0, 0, 1};
	private static final int[] dy = {0, -1, 1, 0};
	
	
	private static boolean isRange(int x, int y) {
		return 0 <= x && x<n && 0<= y && y<n;
	}
	
	private static int stoi(String s) {
		return Integer.parseInt(s);
	}
	
	
	
	private static class Node implements Comparable<Node>{
		int x,y,dir,dist;
		
		

		public Node(int x, int y, int dist) {
			super();
			this.x = x;
			this.y = y;
			this.dist = dist;
		}

		public Node(int x, int y) {
			super();
			this.x = x;
			this.y = y;
		}

		public Node(int x, int y, int dir, int dist) {
			super();
			this.x = x;
			this.y = y;
			this.dir = dir;
			this.dist = dist;
		}

		@Override
		public int compareTo(Node o) {
			if (this.dist == o.dist) {
				if (this.x == o.x) {
					return this.y - o.y;
				}
				
				return this.x - o.x;
			}
			
			return this.dist - o.dist;
		}
		
		public boolean isSame(Node node) {
			return this.x == node.x && this.y == node.y;
		}
		
		
	}
	
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private static StringTokenizer st;
	
	private static int n,m;
	private static int[][] map;
	
	private static Node[] person;
	private static Node[] store;
	
	private static int time;
	
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		init();
		
		time = 1;
		
		while(true) {
			moveStore();
			
			if(time<=m) {
				moveBasecamp(store[time-1]);
			}
			
			if(isFinish()) {
				break;
			}
			
			time++;
		}
		
		System.out.println(time);
	}
	
	private static boolean isFinish() {
		for(int i=0; i<m; i++) {
			if(!person[i].isSame(store[i])) {
				return false;
			}
		}
		
		return true;
	}
	
	private static void moveBasecamp(Node start) {
		PriorityQueue<Node> pq = new PriorityQueue<>();
		boolean[][] visited = new boolean[n][n];
		
		pq.add(new Node(start.x, start.y, 0));
		
		visited[start.x][start.y] = true;
		
		while(!pq.isEmpty()) {
			Node cur = pq.poll();
			if(map[cur.x][cur.y] == 1) {
				map[cur.x][cur.y] = 2;
				person[time-1] = cur;
				return;
			}
			
			for(int i=0; i<4; i++) {
				int nx = cur.x + dx[i];
				int ny = cur.y +dy[i];
				
				if(isRange(nx, ny)) {
					if(!visited[nx][ny]) {
						if(map[nx][ny] != 2) {
							pq.add(new Node(nx,ny, cur.dist+1));
							visited[nx][ny] = true;
						}
					}
				}
			}
		}
	}
	
	private static void moveStore() {
		for(int i=0; i<m; i++) {
			Node start = person[i];
			Node end = store[i];
			
			if(isRange(start.x, start.y)) {
				if(!start.isSame(end)) {
					int dir = findDir(start, end);
					
					start.x += dx[dir];
					start.y += dy[dir];
				}
			}
		}
		
		for(int i=0; i<m; i++) {
			if(person[i].isSame(store[i])) {
				map[person[i].x][person[i].y] = 2;
			}
		}
	}
	
	private static int findDir(Node start, Node end) {
		PriorityQueue<Node> pq = new PriorityQueue<>();
		boolean[][] visited = new boolean[n][n];
		
		pq.add(new Node(start.x, start.y, -1, 0));
		visited[start.x][start.y] = true;
		
		while(!pq.isEmpty()) {
			Node cur = pq.poll();
			
			if(cur.isSame(end)) {
				return cur.dir;
			}
			
			for(int d = 0; d<4; d++) {
				int nx =cur.x + dx[d];
				int ny = cur.y +dy[d];
				
				if(isRange(nx, ny)) {
					if(!visited[nx][ny]) {
						
						if(map[nx][ny] !=2) {
							visited[nx][ny] = true;
							if(cur.dir == -1) {
								pq.add(new Node(nx,ny,d, cur.dist+1));
							}
							else {
								pq.add(new Node(nx,ny,cur.dir, cur.dist+1));
							}
						}
						
						
					}
				}
			}
		}
		
		return 0;
	}
	
	
	private static void init() throws IOException{
		st = new StringTokenizer(br.readLine());
		
		n = stoi(st.nextToken());
		m = stoi(st.nextToken());
		
		map = new int[n][n];
		person = new Node[m];
		store = new Node[m];
		
		for(int i=0; i<n; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j=0; j<n; j++) {
				map[i][j] = stoi(st.nextToken());
			}
		}
		
		for(int i=0; i<m; i++) {
			st = new StringTokenizer(br.readLine());
			
			int x = stoi(st.nextToken())-1;
			int y = stoi(st.nextToken())-1;
			
			store[i] = new Node(x,y);
			person[i] = new Node(-1,-1);
		}
	}
	
	
}