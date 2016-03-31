package minspantree;

public class Heap {
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
