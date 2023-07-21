package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class SplitCenterMergeSort extends Sort {
    public SplitCenterMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Split Center Merge");
        this.setRunAllSortsName("Split Center Merge Sort");
        this.setRunSortName("Split Center Mergesort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void method(int[] array, int start, int len) {
        for (int runs = 0, way = 1, swapless = 0, first = start, nextfirst = start, last = start + len - 1, nextlast = start + len - 1; swapless < 2 && runs < len; runs++, way *= -1) {
            boolean anyswaps = false;
            for (int i = (int) Math.floor(len / 2) + start; i < last && i >= first; i += way) {
                if (Reads.compareIndices(array, i, i + 1, 0.01, true) > 0) {
                    Writes.swap(array, i, i + 1, 0.01, anyswaps = true, false);
                    if (way == 1) nextlast = i + 1;
                    else nextfirst = i + 1;
                }
            }
            if (way == 1) last = nextlast;
            else first = nextfirst;
            if (!anyswaps) swapless++;
            else swapless = 0;
        }
        if (len <= 4) {
            for (int c = 1, j = start + len - 1, s, f = start + (len / 2); j > 0; j -= c) {
                if (f - 1 < start) s = start;
                else s = f - 1;
                boolean a = false;
                c = 1;
                for (int k = s; k < j; k++) {
                    if (Reads.compareIndices(array, k, k + 1, 0.025, true) > 0) {
                        if (!a) f = k;
                        Writes.swap(array, k, k + 1, 0.075, a = true, false);
                        c = 1;
                    } else c++;
                }
            }
        }
    }

    protected void alternatemethod(int[] array, int currentLength) {
        for (int runs = 0, way = 1, swapless = 0; swapless < 2 && runs < currentLength; runs++, way *= -1) {
            boolean anyswaps = false;
            for (int i = (int) Math.floor(currentLength / 2); i < currentLength && i > 0; i += way) if (Reads.compareIndices(array, i - 1, i, 0.005, true) > 0) Writes.swap(array, i - 1, i, 0.005, anyswaps = true, false);
            if (!anyswaps) swapless++;
            else swapless = 0;
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int len = 2;
        while (len < currentLength) {
            for (int index = 0; index + len - 1 < currentLength; index += len) method(array, index, len);
            len *= 2;
        }
        if (len == currentLength) method(array, 0, currentLength);
        else alternatemethod(array, currentLength);
    }
}