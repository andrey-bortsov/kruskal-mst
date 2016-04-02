package minspantree;

public class Vertex implements Comparable{
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
