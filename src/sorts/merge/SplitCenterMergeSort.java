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
        int way = 1;
        int i = start;
        int swapless = 0;
        int runs = 0;
        int first = start;
        int nextfirst = start;
        int last = start + len - 1;
        int nextlast = start + len - 1;
        boolean anyswaps = false;
        while (swapless < 2 && runs < len) {
            anyswaps = false;
            i = (int) Math.floor(len / 2) + start;
            while (i < last && i >= first) {
                if (Reads.compareIndices(array, i, i + 1, 0.01, true) > 0) {
                    Writes.swap(array, i, i + 1, 0.01, true, false);
                    anyswaps = true;
                    if (way == 1) nextlast = i + 1;
                    else nextfirst = i + 1;
                }
                i += way;
            }
            if (way == 1) last = nextlast;
            else first = nextfirst;
            way *= -1;
            if (!anyswaps) {
                swapless++;
            } else {
                swapless = 0;
            }
            runs++;
        }
        if (len <= 4) {
            int c = 1;
            int s;
            int f = start + (len / 2);
            boolean a = false;
            for (int j = start + len - 1; j > 0; j -= c) {
                if (f - 1 < start) {
                    s = start;
                } else {
                    s = f - 1;
                }
                a = false;
                c = 1;
                for (int k = s; k < j; k++) {
                    if (Reads.compareIndices(array, k, k + 1, 0.025, true) > 0) {
                        Writes.swap(array, k, k + 1, 0.075, true, false);
                        if (!a) {
                            f = k;
                        }
                        a = true;
                        c = 1;
                    } else {
                        c++;
                    }
                }
            }
        }
    }

    protected void alternatemethod(int[] array, int currentLength) {
        int way = 1;
        int i = 1;
        int swapless = 0;
        int runs = 1;
        boolean anyswaps = false;
        while (swapless < 2 && runs < currentLength) {
            anyswaps = false;
            i = (int) Math.floor(currentLength / 2);
            while (i < currentLength && i > 0) {
                if (Reads.compareIndices(array, i - 1, i, 0.005, true) > 0) {
                    Writes.swap(array, i - 1, i, 0.005, true, false);
                    anyswaps = true;
                }
                i += way;
            }
            way *= -1;
            if (!anyswaps) {
                swapless++;
            } else {
                swapless = 0;
            }
            runs++;
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int len = 2;
        int index = 0;
        while (len < currentLength) {
            index = 0;
            while (index + len - 1 < currentLength) {
                method(array, index, len);
                index += len;
            }
            len *= 2;
        }
        if (len == currentLength) method(array, 0, currentLength);
        else alternatemethod(array, currentLength);
    }
}