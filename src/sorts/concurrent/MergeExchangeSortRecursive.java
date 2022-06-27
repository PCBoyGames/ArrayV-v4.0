package sorts.concurrent;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public final class MergeExchangeSortRecursive extends Sort {

    public MergeExchangeSortRecursive(ArrayVisualizer arrayVisualizer)  {
        super(arrayVisualizer);

        this.setSortListName("Merge-Exchange (Recursive)");
        this.setRunAllSortsName("Batcher's Merge-Exchange Sort");
        this.setRunSortName("Recursive Merge-Exchange Sort");
        this.setCategory("Concurrent Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
	
	private int end;
	
	private void compSwap(int[] array, int a, int b) {
		if(b < this.end && Reads.compareIndices(array, a, b, 0.5, true) > 0)
			Writes.swap(array, a, b, 0.5, true, false);
	}
	
	private void merge(int[] array, int p, int n, int g, int g1) {
		if(n == 2) {
			this.compSwap(array, p, p+g);
			return;
		}
		this.merge(array, p,          n/2, g, 2*g1+2);
		this.merge(array, p+(g1+2)*g, n/2, g, 2*g1+2);
		
		for(int i = 2; i < n; i += 2)
			this.compSwap(array, p+((i-1)+((i-1)/2)*g1)*g, p+(i+(i/2)*g1)*g);
	}
	
	private void sort(int[] array, int p, int n, int g) {
		if(n > 2) {
			this.sort(array, p,   n/2, g*2);
			this.sort(array, p+g, n/2, g*2);
		}
		this.merge(array, p, n, g, 0);
	}

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
		this.end = length;
		int n = 1 << (32-Integer.numberOfLeadingZeros(length-1));
		this.sort(array, 0, n, 1);
    }
}
