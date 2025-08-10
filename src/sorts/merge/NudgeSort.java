package sorts.merge;

import main.ArrayVisualizer;
import sorts.hybrid.BismuthSort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class NudgeSort extends BismuthSort {
    public NudgeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Nudge");
        this.setRunAllSortsName("Nudge Sort");
        this.setRunSortName("Nudgesort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public void nudgeMerge(int[] array, int start, int end) {
        colors = false;
        int len = 1;
        for (; len < end - start; len *= 2) {
            int i = start;
            for (; i + 2 * len <= end; i += 2 * len) {
                if (len == 1) {if (Reads.compareIndices(array, i, i + 1, 1, true) > 0) Writes.swap(array, i, i + 1, 1, true, false);}
                else giveUpFair(array, i, i + len, i + 2 * len, true, true, false, 0);
            }
            if (i + len <= end) giveUpFair(array, i, i + len, end, true, true, false, 0);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int base) {
        nudgeMerge(array, 0, currentLength);
    }
}