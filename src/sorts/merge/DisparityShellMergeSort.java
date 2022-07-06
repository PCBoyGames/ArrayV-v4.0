package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Ayako-chan
in collaboration with Control and mg-2018

-----------------------------
- Sorting Algorithm Scarlet -
-----------------------------

 */

/**
 * @author Ayako-chan
 * @author Control
 * @author mg-2018
 *
 */
public final class DisparityShellMergeSort extends Sort {

    public DisparityShellMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Disparity Shell Merge");
        this.setRunAllSortsName("Disparity Shell Merge Sort");
        this.setRunSortName("Disparity Shell Mergesort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    final int WLEN = 3;

    public static int nextGap(int g) {
        int c = (g + 1) / 2 - (g + 1) / 18 - 1;
        if (c < 4)
            return 1;
        do {
            int a = c, b = g;
            while (b > 0) {
                int t = b;
                b = a % b;
                a = t;
            }
            if (a == 1)
                break;
            else
                c++;
        } while (true);
        return c;
    }

    protected boolean getBit(int[] bits, int idx) {
        int b = (bits[idx >> WLEN]) >> (idx & ((1 << WLEN) - 1)) & 1;
        return b == 1;
    }

    protected void flag(int[] bits, int idx) {
        Writes.write(bits, idx >> WLEN, bits[idx >> WLEN] | (1 << (idx & ((1 << WLEN) - 1))), 0, false, true);
    }

    protected int findDisparity(int[] array, int a, int b) {
        int n = b - a;
        int[] max = new int[((n - 1) >> WLEN) + 1];
        Writes.changeAllocAmount(max.length);
        int maxIdx = 0;
        for (int i = 1; i < n; i++) {
            if (Reads.compareIndices(array, a + i, a + maxIdx, 0, false) > 0) {
                maxIdx = i;
                flag(max, i);
            }
        }
        int i = n - 1;
        int p = 1;
        int j = n - 1;

        while (j >= 0 && i >= p) {
            while (!getBit(max, j) && j > 0)
                j--;
            maxIdx = j;
            while (Reads.compareIndices(array, a + i, a + maxIdx, 0, false) > 0 && i >= p)
                i--;
            if (Reads.compareIndices(array, a + i, a + j, 0, false) <= 0 && p < i - j)
                p = i - j;
            j--;
        }
        Writes.changeAllocAmount(-max.length);
        return p;
    }

    public boolean shellPass(int[] array, int a, int b, int g, double sleep) {
        boolean anywrite = false;
        for (int i = a + g; i < b; i++) {
            if (Reads.compareIndices(array, i - g, i, sleep, true) > 0) {
                int t = array[i], j = i;
                anywrite = true;
                Highlights.clearMark(2);
                do {
                    Writes.write(array, j, array[j - g], sleep, true, false);
                    j -= g;
                } while (j - g >= a && Reads.compareValues(array[j - g], t) > 0);
                Writes.write(array, j, t, sleep, true, false);
            }
        }
        return anywrite;
    }

    public void shellSort(int[] array, int a, int b) {
        int g = b - a;
        do {
            int p = findDisparity(array, a, b);
            if (p == 1)
                break;
            g = nextGap(p);
            if (!shellPass(array, a, b, g, 0.75)) {
                g = nextGap(g);
                shellPass(array, a, b, g, 0.75);
            }
        } while (g != 1);
    }

    public void mergeSort(int[] array, int a, int b) {
        int mRun = b - a;
        for (; mRun >= 32; mRun = (mRun + 1) / 2);
        int i;
        for (i = a; i+mRun<b; i+=mRun)
            shellPass(array, i, i + mRun, 1, 0.5);
        shellPass(array, i, b, 1, 0.5);
        for (int j = mRun; j < (b - a); j *= 2) {
            for (i = a; i + 2 * j <= b; i += 2 * j)
                shellSort(array, i, i + 2 * j);
            if (i + j < b)
                shellSort(array, i, b);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        mergeSort(array, 0, sortLength);

    }

}
