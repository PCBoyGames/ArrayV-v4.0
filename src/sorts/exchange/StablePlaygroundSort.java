package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class StablePlaygroundSort extends Sort {
    public StablePlaygroundSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Stable Playground");
        this.setRunAllSortsName("Stable Playground Sort");
        this.setRunSortName("Stable Playground Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(512);
        this.setBogoSort(false);
    }

    protected int selectLowest(int[] array, int length) {
        int lowestindex = 0;
        for (int j = 0; j < length; j++) if (Reads.compareIndices(array, j, lowestindex, 0.005, true) == -1) lowestindex = j;
        return lowestindex;
    }

    protected int selectNext(int[] array, int length, int target) {
        int lowesthigh = -1;
        for (int right = 0; right < length; right++) {
            if (Reads.compareIndices(array, target, right, 0.005, true) < 0) {
                if (lowesthigh == -1) lowesthigh = right;
                else if (Reads.compareIndices(array, lowesthigh, right, 0.005, true) > 0) lowesthigh = right;
            }
        }
        return lowesthigh;
    }

    protected void chase(int[] array, int item, int target) {
        int dir = 0;
        int chase = 0;
        if (Math.abs(target - item) != 1) {
            if (target - item > 0) dir = 1;
            else dir = -1;
            chase = item;
            while (Math.abs(target - chase) != 1) {
                if (Reads.compareValues(array[chase], array[chase + dir]) != 0) Writes.swap(array, chase, chase + dir, 0.005, true, false);
                chase += dir;
            }
        }
    }

    protected void quit(int[] array, int bound, int item) {
        int pull = item;
        while (pull + 1 < bound) {
            if (Reads.compareValues(array[pull], array[pull + 1]) != 0) Writes.swap(array, pull, pull + 1, 0.005, true, false);
            pull++;
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int lasttarget = 0;
        int target = 0;
        for (int bound = currentLength; bound > 1; bound--) {
            lasttarget = selectLowest(array, bound);
            target = lasttarget;
            while (target != -1) {
                target = selectNext(array, bound, lasttarget);
                if (target != -1) {
                    chase(array, lasttarget, target);
                    lasttarget = target;
                } else quit(array, bound, lasttarget);
            }
        }
    }
}