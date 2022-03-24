package sorts.merge;

import main.ArrayVisualizer;
import utils.IndexedRotations;
import sorts.insert.BinaryInsertionSort;
import sorts.templates.Sort;


final public class OnlinePDMSort extends Sort {
    public OnlinePDMSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Online PDM");
        this.setRunAllSortsName("Online Pattern-Defeating Merge Sort");
        this.setRunSortName("Online PDMsort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    private void head(int[] array, int[] tmp, int start, int mid, int end) {
    	if(start >= mid || mid >= end)
    		return;
    	if(Reads.compareIndices(array, start, end-1, 5, true) > 0) {
    		IndexedRotations.cycleReverse(array, start, mid, end, 1, true, false);
    		return;
    	}
    	Writes.arraycopy(array, start, tmp, 0, mid-start, 0.5, true, true);
    	int[][] table = new int[][] {tmp, array};
    	int[] ptrs = new int[] {0, mid},
    		  vals = new int[] {mid-start, end-mid};
    	int cmp, to = start;
    	while(vals[0] > 0 && vals[1] > 0) {
    		cmp = -(Reads.compareValues(array[ptrs[1]], tmp[ptrs[0]]) >> 31);
    		//Highlights.markArray(2, ptrs[cmp], table[cmp]);
    		Writes.write(array, to++, table[cmp][ptrs[cmp]++], 1, true, false);
    		--vals[cmp];
    	}
    	while(vals[0] > 0) {
    		Writes.write(array, to++, tmp[ptrs[0]++], 1, true, false);
    		--vals[0];
    	}
    }
    private void tail(int[] array, int[] tmp, int start, int mid, int end) {
    	if(start >= mid || mid >= end)
    		return;
    	if(Reads.compareIndices(array, start, end-1, 5, true) > 0) {
    		IndexedRotations.cycleReverse(array, start, mid, end, 1, true, false);
    		return;
    	}
    	Writes.arraycopy(array, mid, tmp, 0, end-mid, 0.5, true, true);
    	int[][] table = new int[][] {array, tmp};
    	int[] ptrs = new int[] {mid-1, (end-mid)-1},
    		  vals = new int[] {mid-start, end-mid};
    	int cmp, to = end;
    	while(vals[0] > 0 && vals[1] > 0) {
    		cmp = -((Reads.compareValues(array[ptrs[0]], tmp[ptrs[1]])-1) >> 31);
    		//Highlights.markArray(2, ptrs[cmp], table[cmp]);
    		Writes.write(array, --to, table[cmp][ptrs[cmp]--], 1, true, false);
    		--vals[cmp];
    	}
    	while(vals[1] > 0) {
    		Writes.write(array, --to, tmp[ptrs[1]--], 1, true, false);
    		--vals[1];
    	}
    }
    int sig(int a, int b, int d) {
    	return ((a + b) + d * Math.abs(a - b)) / 2;
    }
    private void segRev(int[] array, int start, int end) {
        int i = start;
        int left;
        int right;
        while (i < end) {
            left = i;
            while (Reads.compareIndices(array, i, i + 1, 0.25, true) == 0 && i < end) i++;
            right = i;
            if (left != right) {
                if (right - left < 3) Writes.swap(array, left, right, 0.75, true, false);
                else Writes.reversal(array, left, right, 0.75, true, false);
            }
            i++;
        }
    }
    private int findRun(int[] array, int start, int end) {
    	if(start >= end - 1)
    		return start + 1;
    	int cmp = -Reads.compareIndices(array, start++, start, 1, true),
    		k = start - 1, d;
    	boolean lUniq = false;
    	if(cmp==0) {cmp++; lUniq=true;}
    	do {
    		d = Reads.compareIndices(array, start++, start, 1, true);
    		lUniq = lUniq || d == 0;
    	} while(start < end && d != cmp);
    	int m = (start - k) / 2,
    		q = sig(k, start-1, -cmp);
    	for(int i=0; i<m; i++) {
    		Writes.swap(array, k+i, q+cmp*i, 1, true, false);
    	}
    	if(lUniq&&cmp==-1)
    		segRev(array, k, start-1);
    	return start;
    }
    public int ms(int[] array, int[] tmp, int start, int end, int depthRun, int depthOverall) {
    	if(start >= end)
    		return start;
    	Writes.recursion();
    	Writes.recordDepth(depthOverall++);
    	if(depthRun < 2) {
    		return findRun(array, start, end);
    	}
    	int l = ms(array, tmp, start, end, depthRun/2, depthOverall),
    		r = ms(array, tmp, l, end, depthRun/2, depthOverall);
    	if(r > end && l >= end)
    		return l;
    	else if(r > end)
    		r=end;
    	if(l-start<r-l) {
    		head(array, tmp, start, l, r);
    	} else {
    		tail(array, tmp, start, l, r);
    	}
    	return r;
    }
    public void sort(int[] array, int start, int end) {
    	int[] tmp = Writes.createExternalArray((end-start+1)/2);
    	ms(array, tmp, start, end, end-start, 0);
    	Writes.deleteExternalArray(tmp);
    }
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
    	sort(array, 0, length);
    }
}