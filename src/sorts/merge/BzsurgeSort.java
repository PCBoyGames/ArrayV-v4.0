package sorts.merge;

import main.ArrayVisualizer;
import sorts.insert.BinaryInsertionSort;
import sorts.templates.Sort;


final public class BzsurgeSort extends Sort {
    public BzsurgeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Bzsurge");
        this.setRunAllSortsName("Bzsurge Sort");
        this.setRunSortName("Bzsurgesort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    private boolean merge(int[] array, int[] tmp, int start, int mid, int end) {
        int l = start, r = mid, t = start;
        while(l < mid && r < end) {
            Highlights.markArray(1, l);
            Highlights.markArray(2, r);
            if(Reads.compareValues(array[l], array[r]) <= 0) {
                Writes.write(tmp, t++, array[l++], 1, true, true);
            } else {
                Writes.write(tmp, t++, array[r++], 1, true, true);
            }
        }
        if(l == r) {
            return true;
        }
        while(l < mid) {
            Writes.write(tmp, t++, array[l++], 1, true, true);
        }
        while(r < end) {
            Writes.write(tmp, t++, array[r++], 1, true, true);
        }
        Writes.arraycopy(tmp, start, array, start, end-start, 0.5, true, false);
        return false;
    }
    private boolean surge(int[] array, int[] tmp, int start, int end) {
        int mid = start+(end-start)/2;
        if(start == mid)
            return true;
        if(end-start < 32) {
            BinaryInsertionSort b = new BinaryInsertionSort(arrayVisualizer);
            b.customBinaryInsert(array, start, end, 0.25);
            return true;
        }
        boolean c = this.merge(array, tmp, start, mid, end),
                l = this.surge(array, tmp, start, mid),
                r = this.surge(array, tmp, mid, end);
        return c && l && r;
    }
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int[] t = Writes.createExternalArray(length);
        while(!this.surge(array, t, 0, length));
    }
}