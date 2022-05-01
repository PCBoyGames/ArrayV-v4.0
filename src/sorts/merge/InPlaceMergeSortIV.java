package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import utils.IndexedRotations;

// Distray, 4
final public class InPlaceMergeSortIV extends Sort {
    public InPlaceMergeSortIV(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("In-Place Merge IV");
        this.setRunAllSortsName("In-Place Merge Sort IV");
        this.setRunSortName("In-Place Merge Sort IV");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
	
    // aphi, In-Place Merge Sort III
    private int binSearchR(int[] array, int a, int b, int k) {
    	while(a < b) {
    		int m = a + (b - a) / 2;
    		if(Reads.compareIndices(array, m, k, 1, true) > 0) {
    			b = m;
    		} else {
    			a = m + 1;
    		}
    	}
    	return a;
    }
    private int binSearchL(int[] array, int a, int b, int k) {
    	while(a < b) {
    		int m = a + (b - a) / 2;
    		if(Reads.compareIndices(array, m, k, 1, true) >= 0) {
    			b = m;
    		} else {
    			a = m + 1;
    		}
    	}
    	return a;
    }
    // shifts as much of [start...mid] to [mid...end] as possible (modulus for fragments)
    private void maxShift(int[] array, int start, int mid, int end) {
    	int gap = mid - start;
    	for(int i=0; i<Math.min(end-mid, gap); i++) {
    		int j=start+i, tmp=array[j];
    		for(j+=gap; j<end; j+=gap) {
    			Writes.write(array, j-gap, array[j], 1, true, false);
    		}
			Writes.write(array, j-gap, tmp, 1, true, false);
    	}
    }
	private void inPlaceMerge4(int[] array, int a, int m, int b) {
		if(a >= m || m >= b || Reads.compareIndices(array, m-1, m, 1, true) <= 0) // avoid unnecessary merging with sorted check
			return;
		while(a < m && m < b) {
			a = binSearchR(array, a, m, m); // find first number higher than array[m] on left side
			if(a == m) return; // break if unnecessary
			int t = m; // temporarily store m
			m = binSearchL(array, m, b, a); // find first number higher than array[a] on right side
			while(a < t) {
				int w = m - t - 1;
				w -= w % (t - a);
				maxShift(array, a, t, m); // shift as much of a as possible to the right side
				a+=w;
				t+=w;
				int j = binSearchL(array, m, b, m - 1); // only work on what has to be merged between shifted left and current right side
				inPlaceMerge4(array, t, m, j); // recursive merge
				if(m - t <= t - a) { // avoid rotations if unneeded
					a += m - t;
					m = j;
				} else {
					IndexedRotations.juggling(array, a += m - t, t = m, m = j, 1, true, false);
					break; // and iterate on remaining parts once rotations are needed
				}
			}
		}
	}
    
    public void IPM4(int[] array, int start, int end) {
    	for(int i=1; i<end-start; i*=2) {
			for(int j=start; j<end; j+=2*i) {
				if(j+i>=end)
					break;
				if(j+2*i<end)
					inPlaceMerge4(array,j,j+i,j+2*i);
				else
					inPlaceMerge4(array,j,j+i,end);
			}
		}
    }
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
    	IPM4(array, 0, length);
    }
}