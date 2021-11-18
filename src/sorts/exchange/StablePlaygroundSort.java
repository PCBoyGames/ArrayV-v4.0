package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class StablePlaygroundSort extends Sort {
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
        for (int j = 0; j < length; j++) {
            Highlights.markArray(2, j);
            Delays.sleep(0.005);
            if (Reads.compareValues(array[j], array[lowestindex]) == -1){
                lowestindex = j;
                Highlights.markArray(1, lowestindex);
                Delays.sleep(0.005);
            }
        }
        return lowestindex;
    }
    
    protected int selectNext(int[] array, int length, int target) {
        int lowesthigh = -1;
        int right = 0;
        while (right < length) {
            Highlights.markArray(1, target);
            Highlights.markArray(2, right);
            Delays.sleep(0.005);
            if (Reads.compareValues(array[target], array[right]) < 0) {
                if (lowesthigh == -1) lowesthigh = right;
                else {
                    Highlights.markArray(1, lowesthigh);
                    Highlights.markArray(2, right);
                    Delays.sleep(0.005);
                    if (Reads.compareValues(array[lowesthigh], array[right]) > 0) lowesthigh = right;
                }
            }
            right++;
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
        int bound = currentLength;
        int lasttarget = 0;
        int target = 0;
        while (bound > 1) {
            lasttarget = selectLowest(array, bound);
            target = lasttarget;
            while (target != -1) {
                target = selectNext(array, bound, lasttarget);
                if (target != -1) {
                    chase(array, lasttarget, target);
                    lasttarget = target;
                } else quit(array, bound, lasttarget);
            }
            bound--;
        }
    }
}