package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class IterativePopSort extends Sort {
    public IterativePopSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Optimized Iterative Pop");
        this.setRunAllSortsName("Optimized Iterative Pop Sort");
        this.setRunSortName("Optimized Iterative Pop Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void bubble(int[] array, int start, int end, int dir) {
        int c = 1;
        int s;
        int f = 1;
        for (int j = end - 1; j > 0; j -= c) {
            if (f - 1 < start) s = start;
            else s = f - 1;
            boolean a = false;
            c = 1;
            for (int i = s; i < j; i++) {
                if (Reads.compareIndices(array, i, i + 1, 0.025, true) == dir) {
                    Writes.swap(array, i, i + 1, 0.075, true, false);
                    if (!a) f = i;
                    a = true;
                    c = 1;
                } else c++;
            }
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        for (int len = 2; len < currentLength; len *= 2) {
            int index = 0;
            int dir = -1;
            for (; index + len <= currentLength; index += len, dir *= -1) bubble(array, index, index + len, dir);
            if (index != currentLength) bubble(array, index, currentLength, dir);
        }
        bubble(array, 0, currentLength, 1);
    }
}