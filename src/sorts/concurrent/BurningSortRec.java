package sorts.concurrent;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class BurningSortRec extends Sort {
    public BurningSortRec(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Burning (Recursive)");
        this.setRunAllSortsName("Recursive Burning Sort");
        this.setRunSortName("Recursive Burning Sort");
        this.setCategory("Concurrent Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    private void comp(int[] array, int start, int end) {
        if(start != end && Reads.compareIndices(array, start, end, 0.5, true) == 1) {
            Writes.swap(array, start, end, 0.5, false, false);
        }
    }
    private void sort(int[] array, int start, int end, int depth) {
        Writes.recordDepth(depth);
        if(start == end)
            return;
        int mid = (start+end) / 2, g0 = mid-start;
        if(start == mid)
            return;
        Writes.recursion();
        this.sort(array, start, mid, depth + 1);
        Writes.recursion();
        this.sort(array, mid, end, depth + 1);
        for(int i=0; i<g0; i++) {
            this.comp(array, i+start, end-i-1);
        }
        Writes.recursion();
        this.sort(array, start, mid, depth + 1);
        Writes.recursion();
        this.sort(array, mid, end, depth + 1);
    }
    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        this.sort(array, 0, sortLength, 0);
    }
}