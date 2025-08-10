package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

Coded for ArrayV by Flanlaina

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Flanlaina
 *
 */
public class StacklessStablePermutationSort extends BogoSorting {
    public StacklessStablePermutationSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Stackless Stable Permutation");
        this.setRunAllSortsName("Stackless Stable Permutation Sort");
        this.setRunSortName("Stackless Stable Permutation Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(11);
        this.setBogoSort(false);
    }

    int compare(int[] array, int[] key, int ofs, int a, int b, double sleep, boolean mark) {
        int cmp = Reads.compareIndices(array, ofs + a, ofs + b, sleep, mark);
        if (cmp == 0) return Reads.compareOriginalIndices(key, a, b, 0.0, false);
        return cmp;
    }

    protected boolean isSorted(int[] array, int[] key, int start, int end) {
        for (int i = 0; i < end - start - 1; ++i) {
            if (compare(array, key, start, i, i + 1, delay, true) > 0) {
                //if (markLast) Highlights.markArray(3, i + 1);
                return false;
            }
        }
        return true;
    }

    void dualSwap(int[] array, int[] idx, int o, int a, int b) {
        if (a == b) return;
        Writes.swap(array, o + a, o + b, 0, true, false);
        Writes.swap(idx, a, b, delay, false, true);
    }
    
    void dualReversal(int[] array, int[] idx, int o, int a, int b) {
        int i = a, j = b;
        while (i < j) {
            dualSwap(array, idx, o, i, j);
            i++;
            j--;
        }
    }
    
    void nextPermutation(int[] array, int[] idx, int a, int n) {
        if (n < 2) return;
        int i = n - 1;
        while (i > 0) {
            if (idx[i - 1] < idx[i]) {
                break;
            }
            i--;
        }
        if (i == 0) {
            dualReversal(array, idx, a, 0, n - 1);
            return;
        }
        int j = n - 1;
        while (j > i - 1) {
            if (idx[j] > idx[i - 1]) {
                break;
            }
            j--;
        }
        dualSwap(array, idx, a, i - 1, j);
        dualReversal(array, idx, a, i, n - 1);
    }

    public void sort(int[] array, int a, int b) {
        int n = b - a;
        int[] idx = Writes.createExternalArray(n);
        for (int i = 0; i < n; i++) {
            Writes.write(idx, i, i, delay, false, true);
        }
        while (!isSorted(array, idx, a, b)) {
            nextPermutation(array, idx, a, n);
        }
        Writes.deleteExternalArray(idx);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);
    }
}
