import java.io.*;
import java.util.*;

public class Main {

	private static class Fish implements Cloneable{
		int x, y, d;

		public Fish(int x, int y, int d) {
			super();
			this.x = x;
			this.y = y;
			this.d = d;
		}

		@Override
		protected Fish clone() throws CloneNotSupportedException {
			// TODO Auto-generated method stub
			return (Fish)super.clone();
		}
	}
	
	private static int M,S;
	private static final int fdx[] = {0,-1,-1,0,1,1,1,0,-1};
	private static final int fdy[] = {0,0,-1,-1,-1,0,1,1,1};
	
	private static final int dx[] = {0, -1,0,1,0};
	private static final int dy[] = {0, 0,-1,0,1};
	
	private static List<Fish>map[][];
	private static List<Fish> list;
	
	private static int smell[][];
	private static int sx, sy;
	private static int res;
	
	private static int[] result = new int[3];
	private static int[] shkMove = new int[3];
	private static int fishNum = Integer.MIN_VALUE;
	
	
	private static boolean isRange(int x, int y) {
		return 0<=x && x<4 && 0<=y && y<4;
	}
	
	private static int stoi(String s) {
		return Integer.parseInt(s);
	}
	

	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		M= stoi(st.nextToken());
		S =stoi(st.nextToken());
		
		smell = new int[4][4];
		
		map = new ArrayList[4][4];
		
		for(int i=0; i<4; i++) {
			for(int j=0; j<4; j++) {
				map[i][j] = new ArrayList<>();
			}
		}
		
		list = new ArrayList<>();
		
		st = new StringTokenizer(br.readLine());
		sx = stoi(st.nextToken())-1;
		sy = stoi(st.nextToken())-1;
		
		for(int i=0; i<M; i++) {
			st = new StringTokenizer(br.readLine());
			
			int r = stoi(st.nextToken())-1;
			int c = stoi(st.nextToken())-1;
			int d = stoi(st.nextToken());
			
			list.add(new Fish(r,c,d));
		}
		
		
		
		simulation();
	}
	
	private static void simulation() throws CloneNotSupportedException{
		while(S-->0) {
			List<Fish> copy = copy(list);
			
			for(int i=0; i<list.size(); i++) {
				Fish cur = list.get(i);
				cur = moveFish(cur);
			}
			
			setMap();
			
			fishNum = Integer.MIN_VALUE;
			sharkBackTracking(0);
			
			sharkMove();
			
			smellRemove();
			
			setCopyMap(copy);
			reset();
		}
		System.out.println(res);
	}
	
	private static void smellRemove() {
		for(int i=0; i<4; i++) {
			for(int j=0; j<4; j++) {
				if(smell[i][j] > 0) {
					smell[i][j] --;
				}
			}
		}
	}
	
	private static void reset() {
		list.clear();
		int cnt = 0;
		for(int i=0; i<4; i++) {
			for(int j=0; j<4; j++) {
				for(int k=0; k<map[i][j].size(); k++) {
					Fish cur = map[i][j].get(k);
					list.add(cur);
					cnt++;
				}
				map[i][j].clear();
			}
		}
		res = cnt;
	}
	
	private static void setCopyMap(List<Fish> copy) {
		for(int i=0; i<copy.size(); i++) {
			Fish cur = copy.get(i);
			map[cur.x][cur.y].add(cur);
		}
	}
	
	private static void sharkMove() {
		for(int i=0; i<3; i++) {
			sx += dx[shkMove[i]];
			sy += dy[shkMove[i]];
			if(map[sx][sy].size()>0) {
				smell[sx][sy] = 3;
				map[sx][sy].clear();
			}
		}
	}
	
	private static void sharkBackTracking(int idx) {
		if(idx == 3) {
			int fnum = checkFish();
			if(fnum == -1) {
				return;
			}
			
			if(fishNum < fnum) {
				fishNum = fnum;
				for(int i=0; i<3; i++) {
					shkMove[i] = result[i];
				}
			}
			return;
		}
		
		for(int i=1; i<=4; i++) {
			result[idx] = i;
			sharkBackTracking(idx+1);
		}
	}
	
	private static int checkFish() {
		boolean[][] visited = new boolean[4][4];
		int cnt = 0;
		int nx = sx;
		int ny = sy;
		for(int i=0; i<3; i++) {
			nx += dx[result[i]];
			ny += dy[result[i]];
			
			if(!isRange(nx,ny)) {
				cnt = -1;
				break;
			}
			if(visited[nx][ny]) {
				continue;
			}
			
			cnt+= map[nx][ny].size();
			visited[nx][ny] = true;
		}
		
		return cnt;
	}
	
	private static void setMap() {
		for(int i=0; i<list.size(); i++){
			Fish cur = list.get(i);
			map[cur.x][cur.y].add(cur);
		}
	}
	
	private static Fish moveFish(Fish cur) {
		int nx = 0; 
		int ny = 0;
		int cnt =0;
		for(int d=1; d<=8; d++) {
			nx = cur.x +fdx[cur.d];
			ny = cur.y + fdy[cur.d];
			
			if(isRange(nx,ny)) {
				if(smell[nx][ny] == 0 && !(nx == sx && ny == sy)) {
					cur.x = nx;
					cur.y = ny;
					break;
				}
			}
			cur.d++;
			if(cur.d>8) {
				cur.d =1;
			}
		}
		
		return cur;
	}
	
	private static List<Fish> copy(List<Fish> list) throws CloneNotSupportedException{
		List<Fish> tmp = new ArrayList<>();
		
		for(int i=0; i<list.size(); i++) {
			Fish cur =list.get(i);
			
			tmp.add(cur.clone());
		}
		
		return tmp;
	}
	
	
}