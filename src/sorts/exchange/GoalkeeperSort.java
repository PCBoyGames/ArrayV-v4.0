package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class GoalkeeperSort extends Sort {
    
    public GoalkeeperSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Goalkeeper");
        this.setRunAllSortsName("Goalkeeper Sort");
        this.setRunSortName("Goalkeeper Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(false);
    }
    
    protected int score(int[] array, int bound) {
        int next = bound;
        boolean found = false;
        while (next > 0 && !found) {
            for (int i = 0; i < next && !found; i++) if (Reads.compareIndices(array, i, next, 0.05, true) > 0) found = true;
            if (!found) next--;
        }
        return next;
    }
    
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int bound = currentLength - 1;
        bound = score(array, bound);
        while (bound > 0) {
            if (Reads.compareIndices(array, 0, 1, 0.05, true) > 0) {
                boolean found = false;
                int goal;
                for (goal = 2; goal <= bound && !found; goal++) if (Reads.compareIndices(array, 0, goal, 0.05, true) < 0) found = true;
                if (!found) {
                    Highlights.markArray(2, bound);
                    Writes.insert(array, 0, bound, 0.05, true, false);
                    bound--;
                    bound = score(array, bound);
                }
                else {
                    Highlights.markArray(2, goal - 1);
                    Writes.insert(array, 0, goal - 2, 0.05, true, false);
                }
            } else {
                Highlights.markArray(2, bound);
                Writes.insert(array, 0, bound, 0.05, true, false);
            }
        }
    }
}