package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class BabblurSort extends Sort {

    boolean swaps = true;

    public BabblurSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Babblur");
        this.setRunAllSortsName("Babblur Sort");
        this.setRunSortName("Babblur Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(512);
        this.setBogoSort(false);
    }

    protected void babblur(int[] array, int start, int end, int depth) throws Exception {
        Writes.recordDepth(depth);
        if (end - start == 1) {
            if (Reads.compareIndices(array, start, start + 1, 0.001, true) > 0) Writes.swap(array, start, start + 1, 0.001, swaps = true, false);
            return;
        }
        boolean track = false;
        for (int i = start; i + 1 < end && !track; i++) if (Reads.compareIndices(array, i, i + 1, 0.001, true) > 0) track = true;
        if (track) {
            Writes.recursion();
            try {babblur(array, start, end - 1, depth + 1);} catch (Exception e) {}
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        while (swaps) {
            swaps = false;
            for (int i = 0; i < currentLength - 1; i++) try {babblur(array, i, currentLength, 0);} catch (Exception e) {}
        }
    }
}