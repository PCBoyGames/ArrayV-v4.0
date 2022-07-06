package sorts.merge;

import main.ArrayVisualizer;
import utils.IndexedRotations;
import sorts.insert.BinaryInsertionSort;
import sorts.templates.Sort;


final public class OptimizedShoveMergeSort extends Sort {
    public OptimizedShoveMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Optimized Shove Merge");
        this.setRunAllSortsName("Optimized Shove Merge Sort");
        this.setRunSortName("Optimized Shove Mergesort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    BinaryInsertionSort b;
    private void merge(int[] array, int start, int mid, int end) {
        int m = end;
        while (start < mid && mid < m) {
            int c = Reads.compareIndices(array, start, mid, 0.1, true);
            if (c == 0) c = -1;
            if (c == -1) {
                int l = start, d;
                do {
                    l++;
                    d = Reads.compareIndices(array, l, mid, 0.1, true);
                } while (l < mid && c == d || d == 0);
                IndexedRotations.juggling(array, start, l, end, 0.1, true, false);
                m -= l-start;
                mid -= l-start;
            } else {
                int r = mid, d;
                do {
                    r++;
                    d = Reads.compareIndices(array, start, r, 0.1, true);
                } while (r < m && c == d);
                IndexedRotations.juggling(array, mid, r, end, 0.1, true, false);
                m -= r-mid;
            }
        }
        if (start < mid)
            IndexedRotations.juggling(array, start, mid, end, 0.1, true, false);
        if (mid < m)
            IndexedRotations.juggling(array, start, m, end, 0.1, true, false);
    }
    private void sort(int[] array, int start, int end) {
        int mid = start+(end-start)/2;
        if (start == mid)
            return;
        //if (end-start < 32) {
        //    b.customBinaryInsert(array, start, end, 0.25);
        //    return;
        //}
        this.sort(array, start, mid);
        this.sort(array, mid, end);
        this.merge(array, start, mid, end);
    }
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        b = new BinaryInsertionSort(arrayVisualizer);
        this.sort(array, 0, length);
    }
}