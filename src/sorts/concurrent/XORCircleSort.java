package sorts.concurrent;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

final public class XORCircleSort extends BogoSorting {
    public XORCircleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Circle (XOR)");
        this.setRunAllSortsName("XOR Circle Sort");
        this.setRunSortName("Circlesort (XOR Method)");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    private void comp(int[] array, int start, int end, boolean dir, int max) {
        if(end < max && start < max && start != end && dir == (Reads.compareIndices(array, start, end, 0.1, true) == 1)) {
            Writes.swap(array, start, end, 0.5, false, false);
        }
    }
    private int xLess(int length) {
        int z = 1;
        while(z <= length)
            z<<=1;
        return z>>1;
    }
    private void xorPass(int[] array, int start, int end) {
        int x = this.xLess(end-start);
        for(int i=start; i<end; i++) {
            for(int j=x; j>0; j/=2) {
                if((i & j) == 0) {
                    this.comp(array, i, i^j, true, end);
                } else {
                    this.comp(array, i, i^j, false, end);
                }
            }
        }
    }
    private void circlePass(int[] array, int start, int end) {
        int m = start + ((end - start) / 2);
        for(int i=0; i<m-start; i++) {
            this.comp(array, start+i, end-i-1, true, end);
        }
    }
    private void circleSort(int[] array, int start, int end) {
        int k = end-start;
        while(k > 0 && !isRangeSorted(array, start, end)) {
            for(int i=start; i<end; i+=k) {
                this.xorPass(array, i, i+k);
                this.circlePass(array, i, i+k);
            }
            for(int i=start+(k/2); i<end-(k/2); i+=k) {
                this.circlePass(array, i, i+k);
                this.xorPass(array, i, i+k);
            }
            k /= 2;
        }
    }
    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        this.circleSort(array, 0, sortLength);
    }
}