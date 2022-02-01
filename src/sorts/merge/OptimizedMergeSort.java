package sorts.merge;

import main.ArrayVisualizer;
import sorts.insert.InsertionSort;
import sorts.templates.Sort;

final public class OptimizedMergeSort extends Sort {
    public OptimizedMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Optimized Merge");
        this.setRunAllSortsName("Optimized Merge Sort");
        this.setRunSortName("Optimized Mergesort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    InsertionSort inserter;
    private boolean ceilLogOdd(int v) {
        int l = 0;
        while(v > 0) {
            v /= 2; l++;
        }
        return l % 2 == 1;
    }
    private boolean merge(int[] array, int[] tmp, int start, int mid, int end, boolean reverse) {
        int t = start, l = start, r = mid;
        int[] from = reverse ? tmp : array,
              to = reverse ? array : tmp;
        if(Reads.compareValues(from[mid-1], from[mid]) <= 0) {
            Writes.arraycopy(from, start, to, start, end-start, 1, true, !reverse);
            return false;
        }
        if(Reads.compareValues(from[start], from[end-1]) >= 0) {
            Writes.arraycopy(from, start, to, start+(end-mid), mid-start, 1, true, !reverse);
            Writes.arraycopy(from, mid, to, start, end-mid, 1, true, !reverse);
            return false;
        }
        while(l < mid && r < end) {
            Highlights.markArray(1, l);
            Highlights.markArray(2, r);
            if(Reads.compareValues(from[l], from[r]) <= 0) {
                Writes.write(to, t++, from[l++], 1, true, !reverse);
            } else {
                Writes.write(to, t++, from[r++], 1, true, !reverse);
            }
        }
        while(l < mid)
            Writes.write(to, t++, from[l++], 1, true, !reverse);
        while(r < end)
            Writes.write(to, t++, from[r++], 1, true, !reverse);
        return true;
    }
    public boolean mergeSort(int[] array, int[] tmp, int start, int end, boolean aux) {
        int mid = start + (end - start) / 2;
        if(end-start < 32 && !aux) {
            this.inserter.customInsertSort(array, start, end, 0.1, false);
            return false;
        }
        this.mergeSort(array, tmp, start, mid, !aux);
        this.mergeSort(array, tmp, mid, end, !aux);
        return this.merge(array, tmp, start, mid, end, !aux);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        boolean doCopy = ceilLogOdd(length);
        int[] tmp = Writes.createExternalArray(length);
        this.inserter = new InsertionSort(arrayVisualizer);
        if(this.mergeSort(array, tmp, 0, length, doCopy) && doCopy) {
            Writes.arraycopy(tmp, 0, array, 0, length, 1, true, false);
        }
    }
}