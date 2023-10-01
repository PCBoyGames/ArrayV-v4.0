package sorts.merge;

import java.util.BitSet;

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
public class NDShellMergeSort extends Sort {

    public NDShellMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Natural Disparity Shell Merge");
        this.setRunAllSortsName("Natural Disparity Shell Merge Sort");
        this.setRunSortName("Natural Disparity Shell Mergesort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected int findDisparity(int[] array, int a, int b) {
        int n = b - a;
        BitSet max = new BitSet(n);
        int maxIdx = 0;
        for (int i = 1; i < n; i++) {
            if (Reads.compareIndices(array, a + i, a + maxIdx, 0, false) > 0) {
                maxIdx = i;
                max.set(i);
            }
        }
        int i = n - 1;
        int p = 1;
        int j = n - 1;
        while (j >= 0 && i >= p) {
            while (!max.get(j) && j > 0)
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
            if (j + gap != i) Writes.write(array, j + gap, key, 1, true, false);
        }
        Highlights.clearAllMarks();
        return gap;
    }

    public void shellSort(int[] array, int a, int b) {
        double truediv = 3;
        int lastpar = b - a;
        int lastgap = b - a;
        while (true) {
            int par = findDisparity(array, a, b);
            int passpar = par;
            if (par >= lastpar)
                par = lastpar - (int) truediv;
            if (par / (int) truediv <= 1) {
                shellPass(array, a, b, 1, par, lastgap);
                break;
            }
            lastgap = shellPass(array, a, b, (int) ((par / (int) truediv) + par % (int) truediv), passpar, lastgap);
            if (lastpar - par <= Math.sqrt(lastpar))
                truediv *= 1.5;
            lastpar = par;
        }
    }

    protected int findRun(int[] array, int a, int b) {
        int i = a + 1;
        boolean dir;
        if (i < b) dir = Reads.compareIndices(array, i - 1, i++, 0.5, true) <= 0;
        else dir = true;
        while (i < b) {
            if (dir ^ Reads.compareIndices(array, i - 1, i, 0.5, true) <= 0)
                break;
            i++;
        }
        if (!dir)
            if (i - a < 4)
                Writes.swap(array, a, i - 1, 1.0, true, false);
            else
                Writes.reversal(array, a, i - 1, 1.0, true, false);
        Highlights.clearMark(2);
        return i;
    }

    public void mergeSort(int[] array, int a, int b) {
        int i, j, k;
        while (true) {
            i = findRun(array, a, b);
            if (i >= b)
                break;
            j = findRun(array, i, b);
            shellSort(array, a, j);
            Highlights.clearMark(2);
            if (j >= b)
                break;
            k = j;
            while (true) {
                i = findRun(array, k, b);
                if (i >= b)
                    break;
                j = findRun(array, i, b);
                shellSort(array, k, j);
                if (j >= b)
                    break;
                k = j;
            }
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        mergeSort(array, 0, sortLength);

    }

}
