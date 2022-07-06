package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class NTerrorismSort extends Sort {
    public NTerrorismSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("N-Terrorism");
        this.setRunAllSortsName("N-Terrorism Sort");
        this.setRunSortName("N-Terrorismsort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    private int recurse;
    private void terrorism0(int[] array, int start, int end, int max, int depth) {
        Writes.recursion();
        if (start >= max || start < 0 || end < 0) {
            return;
        }
        if (start != end && Reads.compareIndices(array, start, end, 0.005, true) < 0) {
            Writes.swap(array, start, end, 0.001, true, false);
        }
        Writes.recordDepth(depth++);
        for (int i=0; i<recurse; i++) {
            terrorism0(array, start+1, end, max, depth);
            terrorism0(array, start, end-1, max, depth);
            terrorism0(array, start+1, end-1, max-1, depth);
        }
    }
    private void terrorism1(int[] array, int start, int end, int max, int depth) {
        Writes.recursion();
        if (start >= max || start < 0 || end < 0) {
            return;
        }
        Writes.recordDepth(depth++);
        for (int i=0; i<recurse; i++) {
            terrorism1(array, start+1, end, max, depth);
            terrorism1(array, start, end-1, max, depth);
            terrorism1(array, start+1, end-1, max-1, depth);
        }
        terrorism0(array, start, end, max, depth);
    }
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        recurse = currentLength;
        terrorism1(array, 0, currentLength-1, currentLength, 0);
    }
}