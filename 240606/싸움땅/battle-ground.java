import java.io.*;
import java.util.*;

public class Main {

	static int N,M,K;
	
	//이거 priorityqueue로 바꿔보자
	static List<Integer>[][] gun;
	static int[][] people;
	static class Person{
		int number, power, gun, d, x, y;

		public Person(int number, int x, int y, int d, int power) {
			super();
			this.number = number;
			this.power = power;
			this.d = d;
			this.x = x;
			this.y = y;
		}

		
		
		
	}
	
	static Queue<Person> p;
	static int[] dx = {-1,0,1,0};
	static int[] dy = {0,1,0,-1};
	static int[] score;
	
	static int stoi(String s) {
		return Integer.parseInt(s);
	}
	
	static boolean isRange(int x, int y) {
		return 0<=x && x<N && 0<=y && y<N;
	} 
	
	
	public static void main(String[] args) throws IOException {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		StringBuilder sb = new StringBuilder();
		
		N = stoi(st.nextToken());
		M = stoi(st.nextToken());
		K = stoi(st.nextToken());
		score = new int[M+1];
		
		gun = new ArrayList[N][N];
		
		people = new int[N][N];
		
		for(int i=0; i<N; i++) {
			for(int j=0; j<N; j++) {
				gun[i][j] = new ArrayList<>();
			}
		}
		
		for(int i=0; i<N; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j=0; j<N; j++) {
				int g = stoi(st.nextToken());
				if(g!= 0) {
					gun[i][j].add(g);
				}
			}
		}
		
		p = new ArrayDeque<>();
		
		for(int i=1; i<=M; i++) {
			st = new StringTokenizer(br.readLine());
			int x = stoi(st.nextToken())-1;
			int y = stoi(st.nextToken())-1;
			int d = stoi(st.nextToken());
			int s = stoi(st.nextToken());
			people[x][y] = i;
			p.offer(new Person(i,x,y,d,s));
		}
		
		while(K-->0) {
			move_people();
		}
		
		for(int i=1; i<=M; i++) {
			sb.append(score[i]).append(" ");
		}
		System.out.println(sb);
	}
	
	private static void move_people() {
		for(int tc=0; tc<M; tc++) {
			Person player = p.poll();
			int nx = player.x + dx[player.d];
			int ny = player.y + dy[player.d];
			
			if(!isRange(nx,ny)) {
				player.d +=2;
				player.d %= 4;
				nx = player.x + dx[player.d];
				ny = player.y + dy[player.d];
			}
			if(people[nx][ny] == 0) {
				//해당 위치에 사람이 없는 경우
				if(gun[nx][ny].size() != 0) {
					//해당 위치에 총이 있는 경우
					int max = player.gun;
					int max_idx = -1;
					for(int i=0; i<gun[nx][ny].size(); i++) {
						int g = gun[nx][ny].get(i);
						if(max < g) {
							max = g;
							max_idx = i;
						}
					}
					if(max_idx != -1) {
						gun[nx][ny].remove(max_idx);
						if(player.gun!=0) {
							gun[nx][ny].add(player.gun);
						}
						player.gun = max;
					}
				}
				
				people[player.x][player.y] = 0;
				people[nx][ny] = player.number;
				player.x = nx;
				player.y = ny;
				p.offer(player);
			}
			
			else { //사람이 있는 경우
				
				int vs = people[nx][ny];
				people[player.x][player.y] = 0;
				player.x = nx;
				player.y = ny;
				fire(vs, player);
			}
			
			
		}
		
	}
	
	static void fire(int vs, Person now) {
    	int cnt = 0;
    	while(++cnt < M) { // 현재 플레이어가 잠깐 나와있으므로 M-1번 진행
    		Person v = p.poll();
    		if(v.number != vs) p.offer(v); // 대결상대가 아니면 다시 넣어주기
    		else {
    			if(v.power + v.gun < now.power + now.gun || (v.power + v.gun == now.power + now.gun && v.power < now.power)) {
    				// v가 졌음
    				score[now.number] += (now.power + now.gun) - (v.power + v.gun); // 점수 증가시키기
    				people[v.x][v.y] = now.number; // 해당 위치에는 now가 남음
    				
    				if(now.gun < v.gun) { // v가 해당 격자에서 가장 큰 총을 가지고 있을 것이므로 v하고만 비교하기
    					if(now.gun != 0) gun[now.x][now.y].add(now.gun);
    					now.gun = v.gun;
    				}
    				else
    					if(v.gun != 0)
    						gun[v.x][v.y].add(v.gun); // v가 총을 가지고 있었다면 총을 내려놓기
    				
    				v.gun = 0;
    				for(int d=0; d<4; d++) {
    					int nx = v.x + dx[(v.d + d) % 4];
    					int ny = v.y + dy[(v.d + d) % 4];
    					if(nx < 0 || ny < 0 || nx >= N || ny >= N || people[nx][ny] != 0) continue; // 격자에서 벗어나거나 사람이 있는 경우는 다른 격자 찾기
    					people[nx][ny] = v.number;
    					v.x = nx; v.y = ny;
    					v.d = (v.d + d) % 4; // 방향이 바뀌었으므로 좌표와 이동 방향 갱신
    					break;
    				}
    				
    				int max = 0; int max_idx = -1;
    				for(int i=0; i<gun[v.x][v.y].size(); i++) {
    					int g = gun[v.x][v.y].get(i);
    					if(max < g) {
    						max = g; max_idx = i;
    					}
    				}
    				if(max_idx != -1) {
    					gun[v.x][v.y].remove(max_idx);
    					v.gun = max;
    				}
    				p.offer(v); // v의 모든 정보가 갱신되었으므로 queue에 넣는다.
    			}
    			else {
    				// v가 이겼음
    				score[v.number] += (v.power + v.gun) - (now.power + now.gun);
    				
    				// v는 이미 칸의 가장 공격력이 높은 총을 가지고 있으므로 진 플레이어의 총만 비교
    				if(now.gun > v.gun) {
    					if(v.gun != 0) gun[v.x][v.y].add(v.gun);
    					v.gun = now.gun;
    				}
    				else if(now.gun != 0) 
    					gun[v.x][v.y].add(now.gun); // 진 플레이어 총 내려놓음
    				now.gun = 0;
    				p.offer(v); // v는 총의 정보를 제외하고 갱신할 것이 없으므로 queue에 넣어준다.
    				
    				for(int d=0; d<4; d++) {
    					int nx = now.x + dx[(now.d + d) % 4];
    					int ny = now.y + dy[(now.d + d) % 4];
    					if(nx < 0 || ny < 0 || nx >= N || ny >= N || people[nx][ny] != 0) continue;
    					people[nx][ny] = now.number;
    					now.x = nx; now.y = ny;
    					now.d = (now.d + d) % 4;
    					break;
    				}
    				int max = 0; int max_idx = -1;
    				for(int i=0; i<gun[now.x][now.y].size(); i++) {
    					int g = gun[now.x][now.y].get(i);
    					if(max < g) {
    						max = g; max_idx = i;
    					}
    				}
    				if(max_idx != -1) {
    					gun[now.x][now.y].remove(max_idx);
    					now.gun = max;
    				}
    			}
    		}
    	}
    	p.offer(now); // 마지막에 넣어주어야 원하는 순서 유지
    }

}