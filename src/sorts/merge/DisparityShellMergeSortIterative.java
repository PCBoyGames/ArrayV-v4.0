package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Ayako-chan
in collaboration with Control, mg-2018 and PCBoy

-----------------------------
- Sorting Algorithm Scarlet -
-----------------------------

 */

/**
 * @author Ayako-chan
 * @author Control
 * @author mg-2018
 * @author PCBoy
 *
 */
public final class DisparityShellMergeSortIterative extends Sort {

    public DisparityShellMergeSortIterative(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Disparity Shell Merge (Iterative)");
        this.setRunAllSortsName("Iterative Disparity Shell Merge Sort");
        this.setRunSortName("Iterative Disparity Shell Mergesort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    final int WLEN = 3;

    protected boolean getBit(int[] bits, int idx) {
        int b = (bits[idx >> WLEN]) >> (idx & ((1 << WLEN) - 1)) & 1;
        return b == 1;
    }

    protected void flag(int[] bits, int idx) {
        Writes.write(bits, idx >> WLEN, bits[idx >> WLEN] | (1 << (idx & ((1 << WLEN) - 1))), 0, false, true);
    }

    // Unlike PCBoy's version, this is NOT cheating.
    protected int findDisparity(int[] array, int a, int b) {
        int n = b - a;
        int[] max = new int[((n - 1) >> WLEN) + 1];
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
        return p;
    }

    protected int shellPass(int[] array, int a, int b, int gap, int par, int lastgap) {
        if (gap >= lastgap) return lastgap;
        if (gap == lastgap - 1 && gap != 1) return lastgap;
        lastgap = gap;
        for (int i = a + gap; i < b; i++) {
            int key = array[i];
            int j = i - gap;
            while (j >= a && Reads.compareValues(key, array[j]) < 0) {
                Writes.write(array, j + gap, array[j], 1, true, false);
                j -= gap;
            }
            if (j + gap < i) Writes.write(array, j + gap, key, 1, true, false);
        }
        Highlights.clearAllMarks();
        return gap;
    }

    protected void insertSort(int[] array, int a, int b) {
        for (int i = a + 1; i < b; i ++) {
            int t = array[i];
            int j = i - 1;
            while (j >= a && Reads.compareValues(array[j], t) > 0) {
                Writes.write(array, j + 1, array[j], 0.5, true, false);
                j--;
            }
            if (j + 1 < i)
                Writes.write(array, j + 1, t, 0.5, true, false);
        }
    }

    public void shellSort(int[] array, int a, int b) {
        double truediv = 3;
        int lastpar = b - a;
        int lastgap = b - a;
        while (true) {
            int par = findDisparity(array, a, b);
            int passpar = par;
            if (par >= lastpar) par = lastpar - (int) truediv;
            if (par / (int) truediv <= 1) {
                shellPass(array, a, b, 1, par, lastgap);
                break;
            }
            lastgap = shellPass(array, a, b, (int) ((par / (int) truediv) + par % (int) truediv), passpar, lastgap);
            if (lastpar - par <= Math.sqrt(lastpar)) truediv *= 1.5;
            lastpar = par;
        }
    }

    public void mergeSort(int[] array, int a, int b) {
        int mRun = b - a;
        for (; mRun >= 32; mRun = (mRun + 1) / 2);
        int i;
        for (i = a; i+mRun<b; i+=mRun)
            insertSort(array, i, i + mRun);
        insertSort(array, i, b);
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
