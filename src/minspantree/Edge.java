package minspantree;

public class Edge {
	private Vertex vertex1;
	private Vertex vertex2;
	private double weight;
	private Edge next;
	
	public Edge(int v1, int v2, double w){
		if(v1 < v2){
			vertex1 = new Vertex(v1);
		    vertex2 = new Vertex(v2);
		}else{
			vertex1 = new Vertex(v2);
		    vertex2 = new Vertex(v1);
		}
		weight = w;
		next = null;
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
			return " " + vertex1.getIndex() + " " + vertex2.getIndex();
		}else{
			return " " + vertex2.getIndex() + " " + vertex1.getIndex();
		}
	}

}
