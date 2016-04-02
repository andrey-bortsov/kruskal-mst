package minspantree;

import java.util.LinkedList;

public class AdjacencyList {
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
			str = str + " " + v.getIndex();
		}
		return str;
	}

}
