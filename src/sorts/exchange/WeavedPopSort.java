package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class WeavedPopSort extends Sort {
    public WeavedPopSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Weaved Pop");
        this.setRunAllSortsName("Weaved Pop Sort");
        this.setRunSortName("Weaved Pop Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int gap = 4;
        int start = 0;
        int dir = -1;
        boolean sorted = false;
        boolean finalized = false;
        while (!finalized) {
            sorted = false;
            int begin = start;
            int end = currentLength;
            while (!sorted) {
                sorted = true;
                boolean beginfound = false;
                int last = end;
                for (int i = begin - gap > start ? begin - gap : start; i + gap < end; i += gap) {
                    if (Reads.compareIndices(array, i, i + gap, 0.025, true) == dir) {
                        if (!beginfound) {
                            beginfound = true;
                            begin = i;
                        }
                        last = i + gap;
                        Writes.swap(array, i, i + gap, 0.025, true, false);
                        sorted = false;
                    }
                }
                end = last;
            }
            start++;
            dir *= -1;
            if (start == gap) {
                start = 0;
                if (gap == 1) finalized = true;
                else {
                    gap /= 2;
                    if (gap == 1) {
                        for (int i = 0, j = currentLength - 2 - (currentLength) % 2; i < j; i += 2, j -= 2) Writes.swap(array, i, j, 1, true, false);
                        dir = 1;
                    }
                    else dir = -1;
                }
            }
        }
    }
}