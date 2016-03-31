package minspantree;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Scanner;

public class proj3 {
	private static Heap heap;
	private static upTree uptree;
	public static int nVertices;
	public static proj3 pr;

	public static void main(String[] args) throws FileNotFoundException {
		pr = new proj3();
		heap = new Heap();
		nVertices = 0;
		File file = new File("files\\graph1-input.txt");
		Scanner sc = new Scanner(file);
		
		while(sc.hasNextLine()){
			String line = sc.nextLine();
			if(!line.trim().equals("-1")){
				Scanner sc1 = new Scanner(line);
				int v1 = sc1.nextInt();
				int v2 = sc1.nextInt();
				double w = sc1.nextDouble();
				Edge e = new Edge(v1, v2, w);
				heap.insert(e);
				if(nVertices < (Math.max(v1,  v2) + 1)){
					nVertices = Math.max(v1,  v2) + 1;
				}
			}
			
		}
		
		for(int i = 0; i < heap.getSize(); i++){
			System.out.println(heap.getElement(i).toString());
		}
		
		uptree = new upTree();
		LinkedList<Edge> mst = pr.kruskalMST();
		for(Edge e:mst){
			System.out.println(e.toString());
		}

	}
	
	public LinkedList<Edge> kruskalMST(){
		LinkedList<Edge> mst = new LinkedList<Edge>();
		int components = nVertices;
		while(components > 1){
			Edge e = heap.deleteMin();
			int v1 = e.getVertex1().getIndex();
			int v2 = e.getVertex2().getIndex();
			int u1 = uptree.find(v1);
			int u2 = uptree.find(v2);
			if(u1 != u2){
				uptree.union(u1, u2);
				mst.add(e);
				
			}
			components--;
		}
		Collections.sort(mst,new EdgeComparator());
		return mst;
	}
	
	class EdgeComparator implements Comparator<Edge>{

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

	

}
