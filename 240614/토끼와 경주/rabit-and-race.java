import java.io.*;
import java.util.*;

public class Main {
	
	private static class Pair implements Comparable<Pair>{
		int x,y;

		public Pair(int x, int y) {
			super();
			this.x = x;
			this.y = y;
		}

		@Override
		public int compareTo(Main.Pair o) {
			// TODO Auto-generated method stub
			if((o.x+o.y) == (this.x+ this.y)) {
				if(o.x == this.x) {
					return o.y - this.y;
				}
				return o.x - this.x;
			}
			return (o.x+o.y) - (this.x+this.y);
		}
	}
	
	private static class Rabbit implements Comparable<Rabbit>{
		int x,y,j,id,d;
		long score;
		boolean runned;

		public Rabbit(int x, int y, int j, int id, int d) {
			super();
			this.x = x;
			this.y = y;
			this.j = j;
			this.id = id;
			this.d = d;
		}

		@Override
		public int compareTo(Main.Rabbit o) {
			// TODO Auto-generated method stub
			if(this.j == o.j) {
				if((this.x +this.y) == (o.x + o.y)) {
					if(this.x == o.x) {
						return this.y - o.y;
					}
					return this.x - o.x;
				}
				return (this.x + this.y) - (o.x+o.y);
			}
			return this.j - o.j;
		}
		
	}

	private static int n,m,p;
	private static Pair[] point;
	
	private static long totalSum;
	
	
	static int stoi(String s) {
		return Integer.parseInt(s);
	}
	
//	static boolean isRange(int x, int y) {
//		return 0<=x && x<N && 0<=y && y<N;
//	} 
	
	private static List<Rabbit> rabbit = new ArrayList<>();
	
	private static final int[] dx = {-1,0,1,0};
	private static final int[] dy = {0,-1,0,1};
	
	public static void main(String[] args) throws IOException {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		int q = stoi(st.nextToken());
		while(q-->0) {
			st = new StringTokenizer(br.readLine());
			int query = stoi(st.nextToken());
			
			switch(query) {
			case 100:
				init(st);
				break;
			case 200:
				startRound(st);
				break;
			case 300:
				powerUp(st);
				break;
			case 400:
				print();
				break;
			}
		}
    }
	
	private static void print() {
		long ans = 0;
		for(int i=0; i<rabbit.size(); i++) {
			ans = Math.max(ans, rabbit.get(i).score + totalSum);
		}
		System.out.println(ans);
	}
	
	private static void powerUp(StringTokenizer st) {
		int id = stoi(st.nextToken());
		int t = stoi(st.nextToken());
		
		for(int i=0; i<rabbit.size(); i++) {
			if(rabbit.get(i).id == id) {
				rabbit.get(i).d *=t;
				break;
			}
		}
	}
	
	public static Pair getUpPair(int x, int y, int dis) {
        dis %= 2 * (n - 1);

        if (dis >= x - 1) {
            dis -= (x - 1);
            x = 1;
        } else {
            x -= dis;
            dis = 0;
        }

        if (dis >= n - x) {
            dis -= (n - x);
            x = n;
        } else {
            x += dis;
            dis = 0;
        }

        x -= dis;

        return new Pair(x, y);
    }

    public static Pair getDownPair(int x, int y, int dis) {
        dis %= 2 * (n - 1);

        if (dis >= n - x) {
            dis -= (n - x);
            x = n;
        } else {
            x += dis;
            dis = 0;
        }

        if (dis >= x - 1) {
            dis -= (x - 1);
            x = 1;
        } else {
            x -= dis;
            dis = 0;
        }

        x += dis;

        return new Pair(x, y);
    }

    public static Pair getLeftPair(int x, int y, int dis) {
        dis %= 2 * (m - 1);

        if (dis >= y - 1) {
            dis -= (y - 1);
            y = 1;
        } else {
            y -= dis;
            dis = 0;
        }

        if (dis >= m - y) {
            dis -= (m - y);
            y = m;
        } else {
            y += dis;
            dis = 0;
        }

        y -= dis;

        return new Pair(x, y);
    }

    public static Pair getRightPair(int x, int y, int dis) {
        dis %= 2 * (m - 1);

        if (dis >= m - y) {
            dis -= (m - y);
            y = m;
        } else {
            y += dis;
            dis = 0;
        }

        if (dis >= y - 1) {
            dis -= (y - 1);
            y = 1;
        } else {
            y -= dis;
            dis = 0;
        }

        y += dis;

        return new Pair(x, y);
    }
	
	private static void startRound(StringTokenizer st) {
		int k = stoi(st.nextToken());
		int bonus = stoi(st.nextToken());
		
		
		for(int i=0; i<rabbit.size(); i++) {
			rabbit.get(i).runned = false;
		}
		
		Collections.sort(rabbit);
		
		for(int i=0; i<k; i++) {
			Rabbit curRabbit = rabbit.get(0);
			int x = curRabbit.x;
			int y = curRabbit.y;
			int d = curRabbit.d;
			curRabbit.runned = true;
			curRabbit.j++;
			PriorityQueue<Pair> pq = new PriorityQueue<>();
			
			pq.add(getUpPair(x, y, d));
            pq.add(getDownPair(x, y, d));
            pq.add(getLeftPair(x, y, d));
            pq.add(getRightPair(x, y, d));
			
			Pair pair = pq.poll();
			curRabbit.x = pair.x;
			curRabbit.y = pair.y;
			
			curRabbit.score -= (pair.x + pair.y);
			totalSum += (pair.x+pair.y);
			
			
			Collections.sort(rabbit);
			
		}
		
		for(int i=0; i<rabbit.size(); i++) {
			Rabbit curRabbit = rabbit.get(i);
			if(curRabbit.runned) {
				curRabbit.score += bonus;
			}
		}
	}
	
	private static void init(StringTokenizer st) {
		n = stoi(st.nextToken());
		m = stoi(st.nextToken());
		p = stoi(st.nextToken());
		
		for(int i=1; i<=p; i++) {
			int id = stoi(st.nextToken());
			int d = stoi(st.nextToken());
			rabbit.add(new Rabbit(1,1,0,id,d));
			
		}
	}

}