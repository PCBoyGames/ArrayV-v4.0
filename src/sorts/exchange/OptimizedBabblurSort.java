package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class OptimizedBabblurSort extends Sort {

    boolean swaps = true;
    boolean swapshere = false;

    public OptimizedBabblurSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Optimized Babblur");
        this.setRunAllSortsName("Optimized Babblur Sort");
        this.setRunSortName("Optimized Babblur Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(512);
        this.setBogoSort(false);
    }

    protected void babblur(int[] array, int start, int end) {
        boolean track = true;
        while (track && end - start > 1) {
            track = false;
            for (int i = start; i + 1 < end && !track; i++) if (Reads.compareIndices(array, i, i + 1, 0.001, true) > 0) track = true;
            end--;
        }
        if (end - start == 1) {
            if (Reads.compareIndices(array, start, start + 1, 0.001, true) > 0) {
                Writes.swap(array, start, start + 1, 0.001, swaps = true, false);
                swapshere = true;
            }
            return;
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int start = 0;
        int end = currentLength;
        int nextend = currentLength;
        while (swaps) {
            swaps = false;
            boolean startfound = false;
            for (int i = start - 1 > 0 ? start - 1 : 0; i < end - 1; i++) {
                swapshere = false;
                babblur(array, i, end);
                if (swaps && !startfound) {
                    start = i;
                    startfound = true;
                }
                if (swapshere) nextend = i + 1;
            }
            end = nextend;
        }
    }
}