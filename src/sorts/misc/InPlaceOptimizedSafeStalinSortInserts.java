package sorts.misc;

import main.ArrayVisualizer;
import sorts.merge.NaturalRotateMergeSort;
import sorts.templates.Sort;
import utils.IndexedRotations;


// #3 of Distray's Pop The Top Lineup, modified
final public class InPlaceOptimizedSafeStalinSortInserts extends Sort {
    public InPlaceOptimizedSafeStalinSortInserts(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("In-Place Optimized Safe Stalin (Inserts)");
        this.setRunAllSortsName("In-Place Optimized Safe Stalin Sort (Inserts)");
        this.setRunSortName("In-Place Optimized Safe Stalinsort (Inserts)");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(2048);
        this.setBogoSort(false);
    }

    private int buildStalinRuns(int[] array, int start, int end) {
        Highlights.clearAllMarks();
        int runs = 0;
        for (int i=start; i<end; i++) {
            for (int j=i+1; j<end; j++) {
                if (Reads.compareValues(array[j], array[i]) >= 0) {
                    if (++i != j) Writes.insert(array, j, i, 0.5, true, false);
                }
            }
            runs++;
        }
        return runs;
    }

    private int getRun(int[] array, int start, int end) {
        int left = start;
        while (left < end && Reads.compareIndices(array, left, left+1, 0.1, true) <= 0) {
            left++;
        }
        return left;
    }


    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int runs;
        do {
            runs = buildStalinRuns(array, 0, currentLength);
            if (runs < 3) break;
            int left = getRun(array, 0, currentLength), right = getRun(array, left+1, currentLength);
            int mid = left;
            while (mid > 0 && Reads.compareIndices(array, mid, right, 1, true) >= 0) {
                mid--;
            }
            IndexedRotations.adaptable(array, 0, left+1, currentLength, 1, true, false);
            currentLength -= left-mid;
        } while (runs > 2);
        if (runs == 2) {
            NaturalRotateMergeSort merge = new NaturalRotateMergeSort(arrayVisualizer);
            merge.runSort(array, currentLength, 0);
        }
    }
}
