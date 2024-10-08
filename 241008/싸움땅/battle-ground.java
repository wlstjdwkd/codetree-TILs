import java.io.*;
import java.util.*;

public class Main {
	
	private static final int[] dx = {-1, 0, 1, 0};
	private static final int[] dy = {0, 1, 0, -1};
	
	
	private static boolean isRange(int x, int y) {
		return 0 <= x && x<N && 0<= y && y<N;
	}
	
	private static int stoi(String s) {
		return Integer.parseInt(s);
	}
	
	
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private static StringTokenizer st;
	private static int N,M,K;
	private static PriorityQueue<Integer>[][] gun;
	private static int[][] people;
	private static class Person{
		int number, power, gun, d, x, y, score;

		public Person(int number, int x, int y, int d, int power, int score) {
			super();
			this.number = number;
			this.power = power;
			this.d = d;
			this.x = x;
			this.y = y;
			this.score = score;
		}
		
	}
	
	private static List<Person> p;
//	private static int[] score;
	
	private static void init() throws IOException{
		st = new StringTokenizer(br.readLine());
		
		N = stoi(st.nextToken());
		M = stoi(st.nextToken());
		K= stoi(st.nextToken());
		
		gun = new PriorityQueue[N][N];
		
		people = new int[N][N];
		
		for(int i=0; i<N; i++) {
			for(int j=0; j<N; j++) {
				gun[i][j] = new PriorityQueue<>(Comparator.reverseOrder());
			}
		}
		
		for(int i=0; i<N; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j=0; j<N; j++) {
				int g = stoi(st.nextToken());
				if(g != 0) {
					gun[i][j].add(g);
				}
			}
		}
		
		p = new ArrayList<>();
		
		for(int i=1; i<=M; i++) {
			st = new StringTokenizer(br.readLine());
			int x = stoi(st.nextToken())-1;
			int y = stoi(st.nextToken())-1;
			int d = stoi(st.nextToken());
			int s = stoi(st.nextToken());
			people[x][y] = i;
			p.add(new Person(i,x,y,d,s,0));
		}
	}
	
	
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		init();
		
		while(K-->0) {
			movePeople();
		}
		
		StringBuilder sb = new StringBuilder();
		for(int i=1; i<=M; i++) {
			sb.append(p.get(i-1).score).append(" ");
		}
		System.out.println(sb);
	}
	
	private static void movePeople() {
		for(int tc = 0; tc<M; tc++) {
			Person player = p.get(tc);
			int nx = player.x + dx[player.d];
			int ny = player.y + dy[player.d];
			
			if(!isRange(nx,ny)) {
				player.d += 2;
				player.d %= 4;
				nx = player.x + dx[player.d];
				ny = player.y + dy[player.d];
			}
			
			if(people[nx][ny] == 0) {
				if(!gun[nx][ny].isEmpty()) {
					int max = player.gun;
					int newGun = gun[nx][ny].peek();
					if(max < newGun) {
						gun[nx][ny].poll();
						if(player.gun!=0) {
							gun[nx][ny].add(player.gun);
						}
						player.gun = newGun;
					}
				}
				
				people[player.x][player.y] = 0;
				people[nx][ny] = player.number;
				player.x = nx;
				player.y = ny;
			}
			
			else {
				int vs = people[nx][ny];
				people[player.x][player.y] = 0;
				player.x = nx;
				player.y = ny;
				fire(vs-1, player);
			}
		}
	}
	
	private static void fire(int vs, Person now) {
		int cnt = 0;
		Person v = p.get(vs);
		if(v.power + v.gun < now.power + now.gun || (v.power + v.gun == now.power + now.gun && v.power< now.power)) {
			//v가 진거
			p.get(now.number-1).score += (now.power + now.gun) - (v.power + v.gun);
			people[v.x][v.y] = now.number;
			
			if(now.gun < v.gun) {
				if(now.gun!= 0) {
					gun[now.x][now.y].add(now.gun);
				}
				now.gun = v.gun;
			}
			else {
				if(v.gun != 0) {
					gun[v.x][v.y].add(v.gun);
				}
			}
				
			v.gun = 0;
			for(int d = 0; d<4; d++) {
				int nx = v.x + dx[(v.d + d) % 4];
				int ny = v.y + dy[(v.d + d) % 4];
				
				if(isRange(nx, ny)) {
					if(people[nx][ny] == 0) {
						people[nx][ny] = v.number;
						v.x = nx;
						v.y = ny;
						v.d = (v.d + d)%4;
						break;
					}
				}
			}
					
			int max = 0;
			if(!gun[v.x][v.y].isEmpty()) {
				max = gun[v.x][v.y].peek();
				gun[v.x][v.y].poll();
			}
			
			v.gun = max;
				
			
		}
			
		else {
			p.get(v.number-1).score += (v.power + v.gun) - (now.power + now.gun);
			
			if(now.gun > v.gun) {
				if(v.gun != 0) {
					gun[v.x][v.y].add(v.gun);
				}
				v.gun = now.gun;
			}
			
			else if (now.gun != 0) {
				gun[v.x][v.y].add(now.gun);
			}
			now.gun = 0;
			
			for(int d=0; d<4; d++) {
				int nx = now.x + dx[(now.d + d) % 4];
				int ny = now.y + dy[(now.d + d) % 4];
				if(nx < 0 || ny < 0 || nx >= N || ny >= N || people[nx][ny] != 0) continue;
				people[nx][ny] = now.number;
				now.x = nx; now.y = ny;
				now.d = (now.d + d) % 4;
				break;
			}
			int max = 0;
			if (!gun[now.x][now.y].isEmpty()) {
				max = gun[now.x][now.y].peek();
				gun[now.x][now.y].poll();
			}
			now.gun = max;
		}
		
	}
	
	
}