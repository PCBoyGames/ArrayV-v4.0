package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Flanlaina
in collaboration with Meme Man

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Flanlaina
 * @author Meme Man
 *
 */
public class ScuteSortIterative extends Sort {
    public ScuteSortIterative(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Scute (Iterative)");
        this.setRunAllSortsName("Iterative Scute Sort");
        this.setRunSortName("Iterative Scutesort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    // Easy patch to avoid self-reversals and the "reversals can be done in a single
    // swap" notes.
    void reversal(int[] array, int a, int b, double sleep, boolean mark, boolean aux) {
        if (b <= a) return;
        if (b - a >= 3) Writes.reversal(array, a, b, sleep, mark, aux);
        else Writes.swap(array, a, b, sleep, mark, aux);
    }

    boolean compSwap(int[] array, int a, int b) {
        if (Reads.compareIndices(array, a, b, 0.001, true) > 0) {
            reversal(array, a, b, 0.001, true, false);
            return true;
        }
        return false;
    }
    
    public void sort(int[] array, int a, int b) {
        int currentLength = b - a;
        int n = 1;
        for (; n < currentLength; n *= 2) ;
        for (boolean s = true; s;) {
            s = false;
            for (int k = 0; k < n / 2; k++)
                for (int j = n; j > 1 && k < j - 1 - k; j /= 2)
                    for (int i = a; i + j - 1 - k < b; i += j)
                        s |= this.compSwap(array, i + k, i + j - 1 - k);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);
    }
}
