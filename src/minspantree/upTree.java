package minspantree;

public class upTree {
	int[] upTreeArray;
	
	public upTree(){
		upTreeArray = new int[1000];
		for(int i = 0; i < proj3.nVertices; i++){
			upTreeArray[i] = -1;
		}
	}
	
	public void union(int a, int b){
		int countA = Math.abs(upTreeArray[a]);
		int countB = Math.abs(upTreeArray[b]);
		if(countA >= countB){
			upTreeArray[b] = a;
			upTreeArray[a] = -(countA + countB);
		}else{
			upTreeArray[a] = b;
			upTreeArray[b] = -(countA + countB);
		}
	}
	
	public int find(int i){
		while(upTreeArray[i] >= 0){
			i = upTreeArray[i];
		}
		return i;
	}
	

}
