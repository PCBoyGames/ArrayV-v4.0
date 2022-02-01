package sorts.misc;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/* An algorithm created by fungamer2 that works similar to the 2-opt algorithm for the Travelling Salesman Problem
Time Complexity: O(n^3) */
public final class TwoOptSort extends Sort {

    public TwoOptSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("2-Opt");
        this.setRunAllSortsName("2-Opt Sort");
        this.setRunSortName("2-Opt Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(2048);
        this.setBogoSort(false);
    }

    public int stabVal(int val) { //Required to make the sort work on Stability Checking
        return arrayVisualizer.doingStabilityCheck() ? arrayVisualizer.getStabilityValue(val) : val;
    }

    public int getDist(int[] array, int length) {
        int dist = 0;
        for (int i = 0; i < length - 1; i++) {
            Reads.addComparison();
            Writes.startLap();
            int amount = Math.abs(stabVal(array[i + 1]) - stabVal(array[i]));
            Writes.stopLap();
            dist += amount;
            Highlights.markArray(1, i);
            Highlights.markArray(2, i + 1);
            Delays.sleep(0.01);
        }
        return dist;
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        boolean done = false;
        while (!done) {
            done = true;
            int bestDist = getDist(array, sortLength);
            for (int i = 0; i < sortLength - 1; i++) {
                for (int j = i + 1; j < sortLength; j++) {
                    Writes.reversal(array, i, j, 0.02, true, false);
                    int dist = getDist(array, sortLength);
                    if (dist < bestDist) {
                        bestDist = dist;
                        done = false;
                    } else {
                        Writes.reversal(array, i, j, 0.02, true, false);
                    }
                }
            }
        }
        Writes.reversal(array, 0, sortLength - 1, 1, true, false);
    }
}
