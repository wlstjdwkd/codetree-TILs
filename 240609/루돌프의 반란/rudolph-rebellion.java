import java.io.*;
import java.util.*;

public class Main {

	private static class Node implements Comparable<Node>{
		int n,x,y,d;

		public Node(int x, int y) {
			super();
			this.x = x;
			this.y = y;
		}
		
		public Node(int n, int x, int y, int d) {
			this.n = n;
			this.x = x;
			this.y = y;
			this.d = d;
		}

		@Override
		public int compareTo(Node o) {
			if (this.d == o.d) {
				if (this.x == o.x) {
					return o.y - this.y;
				}
				
				return o.x - this.x;
			}
			
			return this.d - o.d;
		}
	}
	
	private static final int[] dx = {-1,0,1,0};
	private static final int[] dy = {0,1,0,-1};
	
	private static int N,M,P,C,D;
	
	private static int[][] map;
	private static Node[] santa;
	private static int[] stun;
	private static int[] score;
	private static boolean[] dead;
	
	private static int rx, ry;
	
	private static boolean isRange(int x, int y) {
		return 0<=x && x<N && 0<=y && y<N;
	}
	
	private static int stoi(String s) {
		return Integer.parseInt(s);
	}
	
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        P = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());
        D = Integer.parseInt(st.nextToken());
        
        map = new int[N][N];
        santa = new Node[P + 1];
        stun = new int[P + 1];
        score = new int[P + 1];
        dead = new boolean[P + 1];
        
		
        st = new StringTokenizer(br.readLine());
        
        rx = Integer.parseInt(st.nextToken()) - 1;
        ry = Integer.parseInt(st.nextToken()) - 1;
        
        map[rx][ry] = -1;
        
        for (int i = 1; i <= P; i++) {
        	st = new StringTokenizer(br.readLine());
        	
        	int n = Integer.parseInt(st.nextToken());
        	int x = Integer.parseInt(st.nextToken()) - 1;
        	int y = Integer.parseInt(st.nextToken()) - 1;
        	
        	santa[n] = new Node(x, y);
        	map[x][y] = n;
        }
        
        
        while(M-->0) {
        	if(isFinish()) {
        		break;
        	}
        	
        	moveRudolph();
        	moveSanta();
        	
        	addScore();
        	decreaseStun();
        }
        
        for(int i=1; i<=P; i++) {
        	System.out.print(score[i] + " ");
        }
	}
	
	private static void decreaseStun() {
		for(int i=1; i<=P; i++) {
			if(stun[i] >0) {
				stun[i]--;
			}
		}
	}
	
	private static void addScore() {
		for(int i=1; i<=P; i++) {
			if(!dead[i]) {
				score[i]++;
			}
		}
	}
	
	private static void moveSanta() {
		for(int i=1; i<=P; i++) {
			if(dead[i] || stun[i] >0) {
				continue;
			}
			Node cur = santa[i];
			
			int min = getDistance(cur.x, cur.y);
			
			int dir = -1;
			
			for(int d=0; d<4; d++) {
				int nx = cur.x + dx[d];
				int ny = cur.y + dy[d];
				
				if(isRange(nx,ny)){
					if(map[nx][ny] > 0) {
						continue;
					}
					
					int dist = getDistance(nx, ny);
					if(dist<min) {
						dir = d;
						min = dist;
					}
				}
			}
			
			if(dir!= -1) {
				map[cur.x][cur.y] = 0;
				
				cur.x += dx[dir];
				cur.y += dy[dir];
				
				if(rx == cur.x && ry == cur.y) {
					score[i] +=D;
					stun[i] = 2;
					
					int nx = cur.x + (-dx[dir] *D);
					int ny = cur.y + (-dy[dir] * D);
					
					interaction(i, nx, ny, -dx[dir], -dy[dir]);
				}
				
				else {
					map[cur.x][cur.y] = i;
				}
			}
		}
	}
	
	private static void moveRudolph() {
		Node s = findSanta();
		
		int moveX = 0;
		int moveY = 0;
		map[rx][ry] = 0;
		
		if(rx<s.x) {
			moveX = 1;
		}
		else if(rx>s.x) {
			moveX = -1;
		}
		
		if(ry < s.y) {
			moveY = 1;
		}
		else if(ry>s.y) {
			moveY = -1;
		}
		
		rx += moveX;
		ry += moveY;
		
		map[rx][ry] = -1;
		
		if(rx == s.x && ry == s.y) {
			score[s.n] +=C;
			stun[s.n] =2;
			
			int nx = s.x + moveX *C;
			int ny = s.y + moveY*C;
			
			interaction(s.n, nx, ny, moveX, moveY);
		}
	}
	
	private static void interaction(int n, int x, int y, int moveX, int moveY) {
		if(isRange(x,y)) {
			if(map[x][y] > 0) {
				interaction(map[x][y], x+moveX, y+moveY,moveX, moveY);
			}
			
			map[x][y] = n;
			santa[n] = new Node(x,y);
		}
		else {
			dead[n] = true;
		}
	}
	
	private static Node findSanta() {
		PriorityQueue<Node> pq = new PriorityQueue<>();
		
		for(int i=1; i<=P; i++) {
			if(dead[i]) {
				continue;
			}
			
			Node s = santa[i];
			int d = getDistance(s.x, s.y);
			pq.add(new Node(i, s.x, s.y, d));
		}
		return pq.poll();
	}
	
	private static boolean isFinish() {
		for(int i=1; i<=P; i++) {
			if(!dead[i]) {
				return false;
			}
		}
		return true;
	}
	
	private static int getDistance(int x, int y) {
		return (x-rx) * (x-rx) + (y-ry) * (y-ry);
	}
	
	
}