package sorts.bogo;

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
public class StacklessDeterministicBogoSort extends BogoSorting {
    public StacklessDeterministicBogoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Stackless Deterministic Bogo");
        this.setRunAllSortsName("Stackless Deterministic Bogo Sort");
        this.setRunSortName("Stackless Deterministic Bogosort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(11);
        this.setBogoSort(false); // It is deterministic
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
        while (!isRangeSorted(array, a, b)) {
            nextPermutation(array, idx, a, n);
        }
        Writes.deleteExternalArray(idx);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);
    }
}
