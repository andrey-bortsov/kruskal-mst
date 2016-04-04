package minspantree;
/*********************************************************************************
 *********************************************************************************
 **
 ** This program calculates and outputs a minimum spanning tree using as input a set 
 ** of weighted edges and numbered vertices of a graph. The program has five inner
 ** classes: Vertex, Edge, Heap, upTree, and AdjacencyList.
 ** 
 ** Author: Andrey Bortsov
 ** Date: 4-2-2016
 ** 
 *********************************************************************************
 ********************************************************************************/
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Scanner;

public class proj3 {
	private static Heap heap;
	private static upTree uptree;
	private static int nVertices;
	private static proj3 pr;
	private static AdjacencyList al;

	public static void main(String[] args) throws FileNotFoundException {
		pr = new proj3();
		heap = pr.new Heap();
		nVertices = 0;
		uptree = pr.new upTree();
		al = pr.new AdjacencyList();
		
		File file = new File("files\\graph1-input.txt");
		Scanner sc = new Scanner(file);
		
		while(sc.hasNextLine()){
			String line = sc.nextLine();
			if(!line.trim().equals("-1")){
				Scanner sc1 = new Scanner(line);
				int v1 = sc1.nextInt();
				int v2 = sc1.nextInt();
				double w = sc1.nextDouble();
				Edge e = pr.new Edge(v1, v2, w);
				heap.insert(e);
				uptree.makeSet(e.getVertex1());
				uptree.makeSet(e.getVertex2());
				if(nVertices < (Math.max(v1,  v2) + 1)){
					nVertices = Math.max(v1,  v2) + 1;
				}
				
				al.add(e.getVertex1().getIndex(), e.getVertex2());
				al.add(e.getVertex2().getIndex(), e.getVertex1());
			}
			
		}
		
		for(int i = 0; i < heap.getSize(); i++){
			System.out.println(heap.getElement(i).toString());
		}
		
		uptree = pr.new upTree();
		LinkedList<Edge> mst = pr.kruskalMST();
		for(Edge e:mst){
			System.out.println(e.toString());
		}
		al.sort();
		for(int i = 0; i < nVertices; i++){
			System.out.println(al.toString(i));
		}

	}
	
	/**
	 * This function, when applied to a heap of edges, returns a collection 
	 * of edges that constitute a minimum spanning tree for the graph.
	 * It uses an upTree to keep track of joined edges.
	 * @return Linked List of MST edges
	 */
	public LinkedList<Edge> kruskalMST(){
		LinkedList<Edge> mst = new LinkedList<Edge>();
		int components = nVertices;
		while(components > 1){
			Edge e = heap.deleteMin();
			Vertex v1 = e.getVertex1();
			Vertex v2 = e.getVertex2();
			Vertex u1 = uptree.find(v1);
			Vertex u2 = uptree.find(v2);
			if(u1.getIndex() != u2.getIndex()){
				uptree.union(u1, u2);
				mst.add(e);
				
			}
			components--;
		}
		Collections.sort(mst,new EdgeComparator());
		return mst;
	}
	
	/**
	 * Helper class that provides a method to compare two edges
	 * by their vertices values.
	 */
	private class EdgeComparator implements Comparator<Edge>{
		
		/**
		 * Returns -1 if edge e2 vertices are greater than e1 vertices.
		 * Returns 1 otherwise.
		 */
		@Override
		public int compare(Edge e1, Edge e2) {
			if(e1.getVertex1().getIndex() < e2.getVertex1().getIndex()){
				return -1;
			}else if(e1.getVertex1().getIndex() > e2.getVertex1().getIndex()){
				return 1;
			}else if(e1.getVertex2().getIndex() < e2.getVertex2().getIndex()){
				return -1;
			}else{
				return 1;
		    }
	    }
	}

	
	/**************************************************************************
	 **************************************************************************
	 ** 
	 ** EDGE is used to store information about two vertices and edge weight. 
	 **
	 **************************************************************************
	 **************************************************************************/
	public class Edge {
		private Vertex vertex1;
		private Vertex vertex2;
		private double weight;

		public Edge(int v1, int v2, double w){
			if(v1 < v2){
				vertex1 = new Vertex(v1);
			    vertex2 = new Vertex(v2);
			}else{
				vertex1 = new Vertex(v2);
			    vertex2 = new Vertex(v1);
			}
			weight = w;
		}
		
		public Vertex getVertex1(){
			return vertex1;
		}
		
		public Vertex getVertex2(){
			return vertex2;
		}
		
		public double getWeight(){
			return weight;
		}
		
		public String toString(){
			if(vertex1.getIndex() < vertex2.getIndex()){
				return String.format("%4d%5d", vertex1.getIndex(), vertex2.getIndex());
			}else{
				return String.format("%4d%5d", vertex2.getIndex(), vertex1.getIndex());
			}
		}

	}

	/**************************************************************************
	 **************************************************************************
	 **
	 ** VERTEX is used to store vertex index (=name), and also its parent
	 ** (used in the up-tree) and count (only if root in up-tree).
	 *
	 **************************************************************************
	 **************************************************************************/
	private class Vertex implements Comparable{
		int index;
		Vertex parent;
		int count;
		
		public Vertex(int i){
			index = i;
			parent = null;
			count = 0;
		}
		
		public int getIndex(){
			return index;
		}
		
		public void setParent(Vertex p){
			parent = p;
		}
		
		public Vertex getParent(){
			return parent;
		}
		
		public int getCount(){
			return count;
		}
		
		public void setCount(int c){
			count = c;
		}

		@Override
		public int compareTo(Object u) {
			if(this.getIndex() > ((Vertex) u).getIndex()){
				return 1;
			}else{
			    return -1;
			}
		}

	}

	
	/**************************************************************************
	 ************************************************************************** 
	 **
	 ** HEAP data structure is used to store weighed edges ordered by their 
	 ** weight. This class provides insert() and deleteMin() operations used 
	 ** by other classes of this program. 
	 **
	 **************************************************************************
	 **************************************************************************/
	private class Heap {
		private Edge[] heapArray;
		private int size;
		
		public Heap(){
			heapArray = new Edge[5000];
			size = 0;
		}
		
		public void insert(Edge e){
			heapArray[size] = e;
			size++;
			this.upHeap(size - 1);
		}
		
		public Edge deleteMin(){
			Edge min = heapArray[0];
			size--;
			swap(0, size);
			downHeap(0);
			return min;
		}
		
		public void upHeap(int pos){
			if(pos > 0){
				if(heapArray[pos].getWeight() < heapArray[(pos - 1)/2].getWeight()){
					swap(pos, (pos - 1)/2);
					upHeap((pos - 1)/2);
				}
			}
		}
		
		public void downHeap(int pos){
			int i = 0;
			if ((2*pos + 2) < size){
				if(heapArray[2*pos + 2].getWeight() <= heapArray[2*pos + 1].getWeight()){
					i = 2*pos + 2;
				}else{
					i = 2*pos + 1;
				}
			}else if((2*pos + 1) < size){
				i = 2*pos + 1;
			}
			if(i > 0 & heapArray[pos].getWeight() > heapArray[i].getWeight()){
				swap(pos, i);
				downHeap(i);
			}
		}
		
		private void swap(int a, int b){
			Edge temp = heapArray[a];
			heapArray[a] = heapArray[b];
			heapArray[b] = temp;
		}
		
		public int getSize(){
			return size;
		}
		
		public Edge getElement(int i){
			return heapArray[i];
		}

	}

	
	
	/*******************************************************************
	 *******************************************************************
	 *
	 * UP-TREE class defining an object used to perform set operations
	 * 'union' and 'find' on a set of balanced uptrees.
	 * 
	 *******************************************************************
	 *******************************************************************/
	private class upTree {
		Vertex[] upTreeArray;
		
		public upTree(){
			upTreeArray = new Vertex[1000];
		}
		
		public Vertex union(Vertex a, Vertex b){
			int countA = Math.abs(a.getCount());
			int countB = Math.abs(b.getCount());
			if(countA >= countB){
				b.setParent(a);
				b.setCount(0);
				a.setCount(-(countA + countB));
				return a;
			}else{
				a.setParent(b);
				a.setCount(0);
				b.setCount(-(countA + countB));
				return b;
			}
		}
		
		public Vertex find(Vertex v){
			while(v.getParent() != null){
				v = v.getParent();
			}
			return v;
		}
		
		public void makeSet(Vertex v){
			int pos = v.getIndex();
			upTreeArray[pos] = v;
		}
		

	}

	
	
	
	/******************************************************************
	 ******************************************************************
	 *
	 * ADJACENCY LIST - used to construct an adjacency list and
	 * print it out as a list of vertices adjacent to one.
	 *
	 ******************************************************************
	 ******************************************************************/
	private class AdjacencyList {
		LinkedList<Vertex>[] adjacencyListArray;
		
		public AdjacencyList(){
			adjacencyListArray = new LinkedList[1000];
		}
		
		public void add(int pos, Vertex v){
			if(adjacencyListArray[pos] == null){
				adjacencyListArray[pos] = new LinkedList<Vertex>();
				adjacencyListArray[pos].add(v);
			}else{
				adjacencyListArray[pos].add(v);
			}
		}
		
		public void sort(){
			int pos = 0;
			while(adjacencyListArray[pos] != null){
				adjacencyListArray[pos].sort(null);
				pos++;
			}
		}
		
		public String toString(int pos){
			String str = "";
			for(Vertex v: adjacencyListArray[pos]){
				str = str + v.getIndex() + " ";
			}
			return str;
		}

	}


}
