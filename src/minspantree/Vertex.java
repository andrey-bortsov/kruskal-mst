package minspantree;

public class Vertex {
	int index;
	Vertex parent;
	
	public Vertex(int i){
		index = i;
		parent = null;
	}
	
	public int getIndex(){
		return index;
	}
	
	public void setParent(Vertex p){
		parent = p;
	}
	
	public Vertex getParent(Vertex p){
		return p;
	}

}
