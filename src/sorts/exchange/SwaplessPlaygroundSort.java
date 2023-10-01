package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class SwaplessPlaygroundSort extends Sort {
    public SwaplessPlaygroundSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Swapless Playground");
        this.setRunAllSortsName("Swapless Playground Sort");
        this.setRunSortName("Swapless Playground Sort");
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
        Writes.insert(array, item, target - item > 0 ? target - 1 : target + 1, 0.005, true, false);
    }

    protected void quit(int[] array, int bound, int item) {
        Writes.insert(array, item, bound - 1, 0.005, true, false);
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