package sorts.esoteric;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

final public class RedHerringSort extends BogoSorting {

	// Improved Horror Sort
    public RedHerringSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Red Herring");
        this.setRunAllSortsName("Red Herring Sort");
        this.setRunSortName("Red Herringsort");
        this.setCategory("Esoteric Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    private int CPO2LT(int l) {
    	int z=1;
    	while(z < l)
    		z<<=1;
    	return z>>1;
    }
    private void bitComp(int[] arr, int a, int b, boolean d) throws StackOverflowError {
    	if(d == (Reads.compareValues(arr[a], arr[b]) == 1)) {
    		Writes.swap(arr, a, b, 1, true, false);
    	}
    }
    private void bitMerge(int[] arr, int a, int b, boolean d) throws StackOverflowError {
    	if(b < 2)
    		return;
    	int m = CPO2LT(b);
    	for(int i=0; i<b-m; i++) {
    		this.bitComp(arr, a+i, a+m+i, d);
    	}
    	if(m < 1)
    		return;
    	this.bitMerge(arr, a, b-m, d);
    	this.bitMerge(arr, a+b-m, m, d);
    	
    	return;
    }
    private void bitBitMerge(int[] arr, int a, int b, boolean d) throws StackOverflowError {
    	if(b < 2)
    		return;
    	int m = CPO2LT(b);

    	if(m < 1)
    		return;

    	this.bitMerge(arr, a, b, d);

    	this.bitBitMerge(arr, a, b-m, !d);
    	this.bitBitMerge(arr, a+b-m, m, d);

    	this.bitMerge(arr, a, b, !d);

    	this.bitBitMerge(arr, a, b-m, d);
    	this.bitBitMerge(arr, a+b-m, m, d);
    	
    	return;
    }
    private void Terrible_L1(int[] arr, int a, int l, int m) {
    	if(l<2 || a>m || a+l>=m)
    		return;
    	boolean k = Math.abs((l-a)%2) > 0;
    	
    	this.bitMerge(arr, a, l, k);
    	
    	this.Terrible_L1(arr, a, l-1, m);
    	this.Terrible_L1(arr, a+1, l, m);
    	
    	this.bitBitMerge(arr, a, l, false);
    }
    private void Terrible_L2(int[] arr, int a, int l, int m) {
    	if(l<2 || a>m || a+l>=m)
    		return;
    	boolean k = Math.abs((l-a)%2) > 0;
    	
    	this.Terrible_L1(arr, a, l, m);
    	
    	while(l>1) {
    		this.Terrible_L2(arr, a, l-1, m);
        	this.Terrible_L2(arr, a+1, l--, m);
    	}
    	
    	this.bitMerge(arr, a, l, !k);
    }
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
    	this.Terrible_L2(array, 0, currentLength, currentLength+1);
    	// this.bitBitMerge(array, 0, currentLength, false);
    }
}