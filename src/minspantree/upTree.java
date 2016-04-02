package minspantree;

public class upTree {
	Vertex[] upTreeArray;
	
	public upTree(){
		upTreeArray = new Vertex[1000];
		//for(int i = 0; i < proj3.nVertices; i++){
			//upTreeArray[i] = -1;
		//}
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
