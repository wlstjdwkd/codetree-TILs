import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {

	static class Pair implements Comparable<Pair>{
		int x,y;

		public Pair(int x, int y) {
			super();
			this.x = x;
			this.y = y;
		}

		@Override
		public int compareTo(Main.Pair o) {
			// TODO Auto-generated method stub
			if(this.y == o.y) {
				return o.x - this.x;
			}
			
			return this.y - o.y;
		}
	}
	
	static class Node implements Comparable<Node>{
		int x,y, score, rotate;

		public Node(int x, int y, int score, int rotate) {
			super();
			this.x = x;
			this.y = y;
			this.score = score;
			this.rotate = rotate;
		}

		@Override
		public int compareTo(Node o) {
			if (this.score == o.score) {
				if (this.rotate == o.rotate) {
					if (this.y == o.y) {
						return this.x - o.x; // 4. 행을 기준으로 오름차순
					}
					
					return this.y - o.y; // 3. 열을 기준으로 오름차순
				}
				
				return this.rotate - o.rotate; // 2. 각도 기준으로 오름차순
			}
			
			return o.score - this.score; // 1. 점수 기준으로 내림차순
		}
	}
	
	static int[] dx = {-1,1,0,0};
	static int[] dy = {0,0,-1,1};
	
	static int K,M;
	static int[][] map;
	static int[][] newMap;
	
	static List<Node> candidate;
	static Queue<Integer> bonus = new ArrayDeque<>();
	static PriorityQueue<Pair> remove;
	static int[] answer;
	
	static int stoi(String s) {
		return Integer.parseInt(s);
	}
	
	public static void main(String[] args) throws IOException {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		K = stoi(st.nextToken());
		M = stoi(st.nextToken());
		
		map = new int[5][5];
		answer = new int[K];
		
		for(int i=0; i<5; i++) {
			st = new StringTokenizer(br.readLine());
			
			for (int j = 0; j < 5; j++) {
				map[i][j] = stoi(st.nextToken());
				
			}
		}
		
		
		st = new StringTokenizer(br.readLine());
		
		for(int i=0; i<M; i++) {
			bonus.add(stoi(st.nextToken()));
		}
		
		for(int t=0; t<K; t++) {
			List<Node> candidate = new ArrayList<>();
			for(int cnt = 1; cnt<=3; cnt++) {
				for(int i=1; i<=3; i++) {
					for(int j=1; j<=3; j++) {
						rotateMap(i-1,j-1, cnt);
						
						int score = bfs(newMap);
						
						if(score>0) {
							candidate.add(new Node(i,j,score, cnt));
						}
					}
				}
			}
			
			if(candidate.isEmpty()) {
				break;
			}
			
			Collections.sort(candidate);
			
			Node best = candidate.get(0);
			int sx = best.x;
			int sy = best.y;
			
			rotateMap(sx-1, sy-1, best.rotate);
			map = newMap;
			
			int score = bfs(map);
			int sum = 0;
			
			while(score>0) {
				fillMap();
				sum+=score;
				
				score = bfs(map);
			}
			
			answer[t] = sum;
			
			for(int n : answer) {
				if(n==0) {
					break;
				}
				System.out.println(n + " ");
			}
			
		}
	}
	
	private static void rotateMap(int sx, int sy, int cnt) {
		newMap = new int[5][5];
		
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				newMap[i][j] = map[i][j];
			}
		}
		
		for (int i = sx; i < sx + 3; i++) {
			for (int j = sy; j < sy + 3; j++) {
				int ox = i - sx;
				int oy = j - sy;
				
				int rx = oy;
				int ry = 3 - ox - 1;
				
				if (cnt == 1) { // 90도 회전
					rx = oy;
					ry = 3 - ox - 1;
				} else if (cnt == 2) { // 180도 회전
					rx = 3 - ox - 1;
					ry = 3 - oy - 1;
				} else { // 270도 회전
					rx = 3 - oy - 1;
					ry = ox;
				}
				
				newMap[rx + sx][ry + sy] = map[i][j];
			}
		}
	}
	
	private static void fillMap() {
		while(!remove.isEmpty()) {
			Pair cur = remove.poll();
			
			map[cur.x][cur.y] = bonus.poll();
		}
	}
	
	private static int bfs(int[][] arr) {
		boolean[][] visited = new boolean[5][5];
		Queue<Pair> q = new ArrayDeque<>();
		
		remove = new PriorityQueue<>();
		
		for(int i=0; i<5; i++) {
			for(int j=0; j<5; j++) {
				if(!visited[i][j]) {
					q.add(new Pair(i,j));
					visited[i][j] = true;
					
					List<Pair> list = new ArrayList<>();
					
					int cnt = 1;
					list.add(new Pair(i,j));
					
					while(!q.isEmpty()) {
						Pair cur = q.poll();
						
						for(int d=0; d<4; d++) {
							int nx = cur.x + dx[d];
							int ny = cur.y + dy[d];
							
							if(!isRange(nx,ny) || visited[nx][ny]) {
								continue;
							}
							
							if(arr[nx][ny] == arr[i][j]) {
								q.add(new Pair(nx,ny));
								visited[nx][ny] = true;
								
								cnt++;
								list.add(new Pair(nx,ny));
							}
							
						}
					}
					
					if(cnt>=3) {
						remove.addAll(list);
					}
				}
			}
		}
		
		return remove.size();
	}
	
	private static boolean isRange(int x, int y) {
		return 0<=x && x<5 && 0<=y && y<5;
	}

}