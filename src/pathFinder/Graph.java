package pathFinder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Stack;

public class Graph {
	
	private int V;
	private LinkedList<Squares>[] adj;
	private int E;
	private Squares[] edgeTo;
	private boolean[] visited;
	private LinkedList<Squares> seq;
	private Stack<Squares> path;
	private int distTo[];
	private IndexMinPQ<Integer> pq;
	private Squares[] vertices;
	
	public Graph(int V) {
		this.V = V;
		adj = (LinkedList<Squares>[])new LinkedList[V];
		edgeTo = new Squares[V];
		vertices = new Squares[V];
		for(int i = 0; i < V; i++) {
			adj[i] = new LinkedList<Squares>();
		}
		this.E = 0;
	}
	
	public int V() {
		return V;
	}
	
	public int E() {
		return E;
	}
	
	public LinkedList<Squares> getSeq(){
		return seq;
	}
	
	public Stack<Squares> getPath(){
		return path;
	}
	
	public void setVertices(Squares[] v) {
		vertices = v;
	}
	
	public void addEdge(Squares v, Squares w) {
		adj[v.getVal()].add(w);
		adj[w.getVal()].add(v);
		E++;
	}
	
	public LinkedList<Squares>[] adj(){
		return adj;
	}
	
	public void removeVertices(Squares x) {
		E -= adj[x.getVal()].size();
		adj[x.getVal()].clear();
		for(int i = 0; i < V; i++) {
			for(int j = 0; j < adj[i].size(); j++) {
				if(x.equals(adj[i].get(j)))
					adj[i].remove(adj[i].get(j));
			}
		}
		
	}

	//DFS without recursion
	public void DFS(Squares v, Squares dest) {
		seq = new LinkedList<Squares>();
		
		visited = new boolean[V];
		
		Stack<Squares> stack = new Stack<Squares>();
		
		stack.push(v);
		
		while(!stack.empty()) {
			
			Squares tmp = stack.pop();
			seq.add(tmp);
			
			if(visited[tmp.getVal()] == false) {
				visited[tmp.getVal()] = true;
			}
			
			if(tmp == dest) {
				if(hasPathTo(dest)) {
					path = pathTo(v, dest);
				}else {
					path = new Stack<Squares>();
				}
				return;
			}
			

			
			for(Squares s: adj[tmp.getVal()]) {
				if(!visited[s.getVal()]) {
					edgeTo[s.getVal()] = tmp;
					stack.push(s);
				}
			}
		}
	}
	
	
	public void BFS(Squares v, Squares dest) {
		seq = new LinkedList<Squares>();
		visited = new boolean[V];
		
		
		LinkedList<Squares> queue = new LinkedList<Squares>();
		
		visited[v.getVal()] = true;
		queue.add(v);
		
		while(queue.size() != 0) {
			Squares tmp = queue.poll();
			seq.add(tmp);
			
			if(tmp == dest) {
				if(hasPathTo(dest)) {
					path = pathTo(v, dest);
				}else {
					path = new Stack<Squares>();
				}
				return;
			}
				
			
			for(Squares s : adj[tmp.getVal()]) {
				if(!visited[s.getVal()]) {
					visited[s.getVal()] = true;
					edgeTo[s.getVal()] = tmp; 
					queue.add(s);
				}
			}
		}
		

	}
	
	public void A_Star(Squares v, Squares dest) {
		pq = new IndexMinPQ<Integer>(V);
		seq = new LinkedList<Squares>();
		visited = new boolean[V];
		
		for(int i = 0; i < V; i++) {visited[i] = false;}
		
		pq.insert(v.getVal(), 0);
		
		visited[v.getVal()] = true;
		
		int g_score [] = new int[V];
		int f_score [] = new int[V];
		
		for(int i = 0; i < V; i++) {
			g_score[i] = Integer.MAX_VALUE;
			f_score[i] = Integer.MAX_VALUE;
		}
		g_score[v.getVal()] = 0;
		f_score[v.getVal()] = h(v, dest);
		
		while(!pq.isEmpty()) {
			int tmp = pq.delMin();
			seq.add(vertices[tmp]);
			
			if(tmp == dest.getVal()) {
				if(hasPathTo(dest)) {
					path = pathTo(v, dest);
				}else {
					path = new Stack<Squares>();
				}
				return;
			}
			
			for(Squares s: adj[tmp]) {
				int temp_g_score = g_score[tmp] + 1;
				
				
				if(temp_g_score < g_score[s.getVal()]) {
					seq.add(s);
					edgeTo[s.getVal()] = vertices[tmp];
					g_score[s.getVal()] = temp_g_score;
					f_score[s.getVal()] = temp_g_score + h(s, dest);
					
					if(!pq.contains(s.getVal())) {
						pq.insert(s.getVal(), f_score[s.getVal()]);
						visited[s.getVal()] = true; 
					}
				}
			}
		}
	}
	
	private int h(Squares v, Squares dest) {
		return Math.abs(v.getX() - dest.getX()) + Math.abs(v.getY() - dest.getY());
	}
	
	public void Dijkstra(Squares v, Squares dest) {
		pq = new IndexMinPQ<Integer>(V);
		
		seq = new LinkedList<Squares>();
		visited = new boolean[V];
		
		distTo = new int[V];
		
		for(int i = 0; i < V; i++) {
			distTo[i] = Integer.MAX_VALUE;
		}
		distTo[v.getVal()] = 0;
		
		pq.insert(v.getVal(), 0);
		visited[v.getVal()] = true;
		seq.add(v);
		
		
		while(!pq.isEmpty()) {
			int tmp = pq.delMin();
			seq.add(vertices[tmp]);
			if(tmp == dest.getVal()) {
				if(hasPathTo(dest)) {
					path = pathTo(v, dest);
				}else {
					path = new Stack<Squares>();
				}
				return;
			}
			relax(tmp);
		}
		
		if(hasPathTo(dest)) {
			path = pathTo(v, dest);
		}else {
			path = new Stack<Squares>();
		}
	}
	
	private void relax(int v) {
		for(Squares s: adj[v]) {
			if(distTo[s.getVal()] > distTo[v] + 1) {
				distTo[s.getVal()] = distTo[v] + 1;
				edgeTo[s.getVal()] = vertices[v];
				
				if(pq.contains(s.getVal())) 
					pq.changeKey(s.getVal(), distTo[s.getVal()]);
				else {
					pq.insert(s.getVal(), distTo[s.getVal()]);
					visited[s.getVal()] = true;
					
				}
			}
		}
	}
	
	private boolean hasPathTo(Squares w) {
		return visited[w.getVal()];
	}
	
	private Stack<Squares> pathTo(Squares v, Squares dest){
		Stack<Squares> path = new Stack<Squares>();
		
		for(Squares x = dest; x != v; x = edgeTo[x.getVal()]) {
			path.push(x);
		}
		path.push(v);
		return path;
 	}
}
