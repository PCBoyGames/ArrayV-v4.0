package sorts.misc;

import main.ArrayVisualizer;
import sorts.insert.BlockInsertionSortNeon;
import sorts.templates.Sort;
import utils.IndexedRotations;


// #3 of Distray's Pop The Top Lineup
final public class InPlaceOptimizedSafeStalinSort extends Sort {
    public InPlaceOptimizedSafeStalinSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("In-Place Optimized Safe Stalin");
        this.setRunAllSortsName("In-Place Optimized Safe Stalin Sort");
        this.setRunSortName("In-Place Optimized Safe Stalinsort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(2048);
        this.setBogoSort(false);
    }

    private int buildStalinRuns(int[] array, int start, int end) {
        int runs = 0;
        for(int i=start; i<end; i++) {
            for(int j=i+1; j<end; j++) {
                if(Reads.compareValues(array[j], array[i]) >= 0) {
                    if(++i != j) Writes.swap(array, j, i, 1, true, false);
                }
            }
            runs++;
        }
        return runs;
    }

    private int getRun(int[] array, int start, int end) {
        int left = start;
        while(left < end && Reads.compareIndices(array, left, left+1, 0.1, true) <= 0) {
            left++;
        }
        return left;
    }


    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int runs;
        do {
            runs = buildStalinRuns(array, 0, currentLength);
            if(runs < 3) break;
            int left = getRun(array, 0, currentLength), right = getRun(array, left+1, currentLength);
            int mid = left;
            while(mid > 0 && Reads.compareIndices(array, mid, right, 1, true) >= 0) {
                mid--;
            }
            IndexedRotations.neon(array, 0, left+1, currentLength, 1, true, false);
            currentLength -= left-mid;
        } while(runs > 2);
        if(runs == 2) {
            BlockInsertionSortNeon neon = new BlockInsertionSortNeon(arrayVisualizer);
            neon.insertionSort(array, 0, currentLength);
        }
    }
}
