package sorts.select;

import main.ArrayVisualizer;
import sorts.insert.InsertionSort;
import sorts.templates.Sort;

final public class PureFibHeapSort extends Sort {
    public PureFibHeapSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Pure Fibonacci Heap");
        this.setRunAllSortsName("Pure Fibonacci Heap Sort");
        this.setRunSortName("Pure Fibonacci Heapsort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    private int[] FIB = new int[46]; // fib(47) > Integer.MAX_VALUE
    
    public void genFib() {
    	FIB[0]=FIB[1]=1;
    	for(int i=2; i<FIB.length; i++) {
    		FIB[i]=FIB[i-2]+FIB[i-1];
    	}
    }
    
    public void sift(int[] array, int a, int b) {
    	byte order = 2;
    	int n=b-1;
    	if(order < 2) return;
    	while(true) {
    		int f=FIB[order-2],
    			j=b-f;
    		if(j<a)
    			return;
    		else {
    			int l = j,
    				m = b-FIB[order-1];
    			if(m<a)m=a;
    			for(int i=m; i<j; i++) {
    				if(i>=a && Reads.compareIndices(array, l, i, 1, true) > 0)
    					l = i;
    			}
    			Writes.swap(array, l, n, 1, true, false);
    			n=l;
    			order++;
    		}
    	}
    }
    
    private void fibHeapify(int[] array, int start, int end) {
    	genFib();
    	int j=1;
    	while(FIB[j]<=end-start)
    		j++;
    	for(int i=start+1; i<=end; i++) {
    		sift(array, start, i);
    	}
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
    	fibHeapify(array, 0, length);
    	for(int i=0; i<length; i++) {
    		Writes.swap(array, i, length-1, 1, true, false);
    		sift(array, i+1, length);
    	}
    	InsertionSort i = new InsertionSort(arrayVisualizer);
    	i.customInsertSort(array, 0, length, 1, false);
    }
}