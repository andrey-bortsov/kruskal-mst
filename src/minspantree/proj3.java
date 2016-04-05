package minspantree;
/*********************************************************************************
 *********************************************************************************
 **                                                                             **
 ** This program calculates and outputs a minimum spanning tree using as input  **
 ** a set of weighted edges and numbered vertices of a graph. The program has   **
 ** five inner classes: Vertex, Edge, Heap, upTree, and AdjacencyList.          **
 **                                                                             **
 ** Author: Andrey Bortsov                                                      **
 ** Date: 4-2-2016                                                              **
 **                                                                             **
 *********************************************************************************
 *********************************************************************************/

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
	public static final int MAX_NODES = 1000; // maximum nodes in graph
	public static final int MAX_EDGES = 5000; // maximum edges in graph

	public static void main(String[] args) {
		pr = new proj3();
		heap = pr.new Heap();
		nVertices = 0;
		uptree = pr.new upTree();
		al = pr.new AdjacencyList();
		
		Scanner sc = new Scanner(System.in);
		
		// read input line by line until -1 encountered
		while(sc.hasNextLine()){
			String line = sc.nextLine();
			if(!line.trim().equals("-1")){
				Scanner sc1 = new Scanner(line);
				int v1 = sc1.nextInt();
				int v2 = sc1.nextInt();
				double w = sc1.nextDouble();
				Edge e = pr.new Edge(v1, v2, w);                   // construct edge object
				heap.insert(e);                                    // insert edge into heap
				uptree.makeSet(e.getVertex1());                    // make set from first vertex and insert into uptree 
				uptree.makeSet(e.getVertex2());                    // make set from second vertex and insert into uptree 
				if(nVertices < (Math.max(v1,  v2) + 1)){           // update node counter
					nVertices = Math.max(v1,  v2) + 1;
				}			
				al.add(e.getVertex1().getIndex(), e.getVertex2()); // add first vertex into adjacency list
				al.add(e.getVertex2().getIndex(), e.getVertex1()); // add second vertex into adjacency list
			}
		}
		
		// print all edges from heap
		for(int i = 0; i < heap.getSize(); i++){
			System.out.println(heap.getElement(i).toString());
		}
		
		// compute and print MST
		LinkedList<Edge> mst = pr.kruskalMST();
		for(Edge e:mst){
			System.out.println(e.toString());
		}
		
		// print adjacency list in sorted order
		al.sort();
		for(int i = 0; i < nVertices; i++){
			System.out.println(al.toString(i));
		}

	}
	
	/**************************************************************************
	 **************************************************************************
	 ** 
	 ** This function, when applied to a heap of edges, returns a collection 
	 ** of edges that constitute a minimum spanning tree for the graph.
	 ** It uses an upTree to keep track of joined edges.
	 ** @return Linked List of MST edges
	 ** 
	 ************************************************************************** 
	 **************************************************************************/
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
		Collections.sort(mst,new EdgeComparator()); // sort edges in MST before printing
		return mst;
	}
	
	/**
	 * Helper class that provides a method to compare two edges
	 * by their vertices values which is used to sort edges in the 
	 * kruskalMST() method.
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
		
		// return formatted string with two vertex names
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
		int index;       // vertex number (also serves as key and value)
		Vertex parent;   // link to parent in uptree
		int count;       // count of notes in uptree (only if root)
		
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

		// compare two vertex objects by their keys/values
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
			heapArray = new Edge[MAX_EDGES];
			size = 0;
		}
		
		public void insert(Edge e){
			heapArray[size] = e;
			size++;
			this.upHeap(size - 1);
		}
		
		// delete and return root of heap with minimum weight
		public Edge deleteMin(){
			Edge min = heapArray[0];
			size--;
			swap(0, size);
			downHeap(0);
			return min;
		}
		
		// restore heap order after insertion
		public void upHeap(int pos){
			if(pos > 0){
				if(heapArray[pos].getWeight() < heapArray[(pos - 1)/2].getWeight()){
					swap(pos, (pos - 1)/2);
					upHeap((pos - 1)/2);
				}
			}
		}
		
		// restore heap order after deleting root
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
			upTreeArray = new Vertex[MAX_NODES];
		}
		
		// return root vertex of new uptree after union of two trees
		public Vertex union(Vertex a, Vertex b){
			int countA = Math.abs(a.getCount());
			int countB = Math.abs(b.getCount());
			if(countA >= countB){
				b.setParent(a);
				b.setCount(0);
				a.setCount(-(countA + countB)); // update count of new uptree
				return a;
			}else{
				a.setParent(b);
				a.setCount(0);
				b.setCount(-(countA + countB)); // update count of new uptree
				return b;
			}
		}
		
		// return root vertex of uptree containing vertex
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
		
		@SuppressWarnings("unchecked")
		public AdjacencyList(){
			adjacencyListArray = (LinkedList<Vertex>[]) new LinkedList[MAX_NODES];
		}
		
		// add vertex to array of vertex lists at specified position
		public void add(int pos, Vertex v){
			if(adjacencyListArray[pos] == null){
				adjacencyListArray[pos] = new LinkedList<Vertex>();
				adjacencyListArray[pos].add(v);
			}else{
				adjacencyListArray[pos].add(v);
			}
		}
		
		// sort all vertex lists in array
		public void sort(){
			int pos = 0;
			while(adjacencyListArray[pos] != null){
				adjacencyListArray[pos].sort(null);
				pos++;
			}
		}
		
		// return string of adjacent vertices names
		public String toString(int pos){
			String str = "";
			for(Vertex v: adjacencyListArray[pos]){
				str = str + String.format("%4d", v.getIndex()) + " ";
			}
			return str;
		}

	}

}
