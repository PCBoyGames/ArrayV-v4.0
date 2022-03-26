package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import utils.IndexedRotations;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class OptimizedGoalkeeperSort extends Sort {
    public OptimizedGoalkeeperSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Optimized Goalkeeper");
        this.setRunAllSortsName("Optimized Goalkeeper Sort");
        this.setRunSortName("Optimized Goalkeeper Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(4096);
        this.setBogoSort(false);
    }
    
    protected int score(int[] array, int bound) {
        int next = bound;
        boolean found = false;
        while (next > 0 && !found) {
            for (int i = 0; i < next && !found; i++) if (Reads.compareIndices(array, i, next, 0.125, true) > 0) found = true;
            if (!found) next--;
        }
        return next;
    }
    
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int bound = currentLength - 1;
        int set = 0;
        bound = score(array, bound);
        while (bound > 0) {
            if (Reads.compareIndices(array, set, set + 1, 0.125, true) > 0) {
                if (set > 0) {
                    Highlights.clearAllMarks();
                    IndexedRotations.neon(array, 0, set, bound + 1, 0.25, true, false);
                    Highlights.clearAllMarks();
                }
                set = 0;
                boolean found = false;
                int goal;
                for (goal = 2; goal <= bound && !found; goal++) if (Reads.compareIndices(array, 0, goal, 0.125, true) < 0) found = true;
                if (!found) {
                    Highlights.markArray(2, bound);
                    Writes.insert(array, 0, bound, 0.125, true, false);
                    bound--;
                    bound = score(array, bound);
                }
                else {
                    Highlights.markArray(2, goal - 1);
                    Writes.insert(array, 0, goal - 2, 0.125, true, false);
                }
            } else set++;
        }
    }
}