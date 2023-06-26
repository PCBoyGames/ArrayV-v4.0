package sorts.misc;

import main.ArrayVisualizer;
import sorts.merge.NaturalRotateMergeSort;
import sorts.templates.Sort;
import utils.IndexedRotations;

/*

PORTED TO ARRAYV THEN IMPROVED BY PCBOYGAMES
BASED IN PART ON CODE FROM GEEKSFORGEEKS

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class InPlaceOptimizedSmartSafeStalinSortSwaps extends Sort {

    int segmentcount;

    public InPlaceOptimizedSmartSafeStalinSortSwaps(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("In-Place Optimized Smart Safe Stalin (Swaps)");
        this.setRunAllSortsName("In-Place Optimized Smart Safe Stalin Sort (Swaps)");
        this.setRunSortName("In-Place Optimized Smart Safe Stalinsort (Swaps)");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected boolean scan(int[] array, int collection, int currentLength) {
        for (int i = collection; i + 1 < currentLength; i++) if (Reads.compareIndices(array, i, i + 1, 0.1, true) > 0) return false;
        return true;
    }

    protected int stepDown(int[] array, int end) {
        for (int i = end - 1; i > 0; i--) for (int j = i - 1; j >= 0; j--) if (Reads.compareIndices(array, j, i, 0.025, true) > 0) return i + 1;
        return 0;
    }

    protected void checkSegments(int[] array, int end) {
        segmentcount = 0;
        for (int i = 0; i + 1 < end && segmentcount < 2; i++) if (Reads.compareIndices(array, i, i + 1, 0.01, true) > 0) segmentcount++;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        boolean pass = false;
        currentLength = stepDown(array, currentLength);
        boolean nochan = true;
        while (!pass) {
            pass = true;
            int collection = 1;
            nochan = true;
            for (int i = 1; i + 1 < currentLength; i++) {
                if (Reads.compareIndices(array, collection - 1, i, 0.5, true) <= 0) {
                    if (collection != i) Writes.swap(array, i, collection, 0.5, true, nochan = false);
                    pass = false;
                    collection++;
                }
            }
            if (nochan) {
                checkSegments(array, currentLength);
                if (segmentcount < 2) break;
            }
            if (pass) pass = scan(array, collection, currentLength);
            if (!pass) IndexedRotations.adaptable(array, 0, collection, currentLength, 0.25, true, false);
            currentLength--;
            if (currentLength - 1 > 0) {
                int cmp = Reads.compareIndices(array, currentLength - 1, currentLength, 1, true);
                while (cmp == 0 && currentLength - 1 >= 0) {
                    currentLength--;
                    if (currentLength - 1 >= 0) cmp = Reads.compareIndices(array, currentLength - 1, currentLength, 4, true);
                }
            }
            currentLength = stepDown(array, currentLength);
        }
        if (nochan) {
            NaturalRotateMergeSort merge = new NaturalRotateMergeSort(arrayVisualizer);
            merge.runSort(array, currentLength, 0);
        }
    }
}