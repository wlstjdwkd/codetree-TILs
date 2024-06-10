import java.io.*;
import java.util.*;

public class Main {

	private static class Cell implements Comparable<Cell>{
		int friendCnt, blankCnt, x,y;

		public Cell(int friendCnt, int blankCnt, int x, int y) {
			super();
			this.friendCnt = friendCnt;
			this.blankCnt = blankCnt;
			this.x = x;
			this.y = y;
		}

		@Override
		public int compareTo(Main.Cell o) {
			// TODO Auto-generated method stub
			if(o.friendCnt == this.friendCnt) {
				if(o.blankCnt == this.blankCnt) {
					if(this.x == o.x) {
						return this.y - o.y;
					}
					return this.x - o.x;
				}
				return o.blankCnt - this.blankCnt;
			}
			return o.friendCnt - this.friendCnt;
		}
	}
	private static final int DIR_NUM = 4;
	private static int n;
	
	private static int[] targetNum;
	
	private static final int[] dx = {-1,1,0,0};
	private static final int[] dy = {0,0,-1,1};
	
	private static int[][] rides;
	
	private static boolean isRange(int x, int y) {
		return 1<=x && x<=n && 1<=y && y<=n;
	}
	
	private static PriorityQueue<Cell> pq;
	
	private static int stoi(String s) {
		return Integer.parseInt(s);
	}
	
	private static boolean[][] friends;
	
	private static void move(int num) {
		pq = new PriorityQueue<>();
		
		for(int i=1; i<=n; i++) {
			for(int j=1; j<=n; j++) {
				if(rides[i][j] == 0) {
					Cell curr = getCurCell(num, i,j);
					pq.add(curr);
				}
			}
		}
		
		Cell bestCell = pq.poll();
		int x = bestCell.x;
		int y = bestCell.y;
		
		rides[x][y] = num;
	}
	
	private static Cell getCurCell(int num, int x, int y) {
		int friendCnt = 0;
		int blankCnt = 0;
		for(int i=0; i<DIR_NUM; i++) {
			int nx = x + dx[i];
			int ny = y + dy[i];
			
			if(!isRange(nx, ny)) {
				continue;
			}
			
			if(rides[nx][ny] == 0) {
				blankCnt++;
			}
			else if(isFriend(num, rides[nx][ny])) {
				friendCnt++;
			}
		}
		
		return new Cell(friendCnt, blankCnt, x, y);
	}
	
	private static boolean isFriend(int num1, int num2) {
		return friends[num1][num2];
	}
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		n = stoi(st.nextToken());
		targetNum = new int[n*n+1];
		
		friends = new boolean[n*n+1][n*n+1];
		
		rides = new int[n+1][n+1];
		
		for(int i=1; i<= n*n; i++) {
			st = new StringTokenizer(br.readLine());
			targetNum[i] = stoi(st.nextToken());
			for(int j=1; j<=4; j++) {
				int friendNum = stoi(st.nextToken());
				
				friends[targetNum[i]][friendNum] = true;
			}
		}
		
		for(int i=1; i<= n*n; i++) {
			move(targetNum[i]);
		}
		
		System.out.println(getTotalScore());
	}
	
	private static int getScore(int x, int y) {
		int cnt = 0;
		for(int i=0; i<DIR_NUM; i++) {
			int nx = x+dx[i];
			int ny = y+dy[i];
			if(isRange(nx,ny)) {
				if(isFriend(rides[x][y], rides[nx][ny])) {
					cnt++;
				}
				
			}
		}
		
		return (int) Math.pow(10, cnt-1);
	}
	
	private static int getTotalScore() {
		int score = 0;
		for(int i=1; i<=n; i++) {
			for(int j=1; j<=n; j++) {
				score += getScore(i,j);
			}
		}
		return score;
	}
	
	
}