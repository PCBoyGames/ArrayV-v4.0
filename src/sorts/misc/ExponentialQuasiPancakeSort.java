package sorts.misc;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Ayako-chan
in collaboration with Project Nayuki and fungamer2

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Ayako-chan
 * @author Project Nayuki
 * @author fungamer2
 *
 */
public final class ExponentialQuasiPancakeSort extends Sort {

    public ExponentialQuasiPancakeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        setSortListName("Exponential Quasi-Pancake");
        setRunAllSortsName("Exponential Quasi-Pancake Sort");
        setRunSortName("Exponential Quasi-Pancake Sort");
        setCategory("Miscellaneous Sorts");
        setComparisonBased(true);
        setBucketSort(false);
        setRadixSort(false);
        setUnreasonablySlow(false);
        setUnreasonableLimit(0);
        setBogoSort(false);
    }
    
    // Easy patch to avoid self-reversals and the "reversals can be done in a single
    // swap" notes.
    void reversal(int[] array, int a, int b, double sleep, boolean mark, boolean aux) {
        if (b <= a) return;
        if (b - a >= 3) Writes.reversal(array, a, b, sleep, mark, aux);
        else Writes.swap(array, a, b, sleep, mark, aux);
    }
    
    protected int expSearch(int[] array, int a, int b, int val, double sleep) {
        int i = 1;
        while (b - i >= a && Reads.compareValues(val, array[b - i]) < 0)
            i *= 2;
        int a1 = Math.max(a, b - i + 1), b1 = b - i / 2;
        while (a1 < b1) {
            int m = a1 + (b1 - a1) / 2;
            Highlights.markArray(2, m);
            Delays.sleep(sleep);
            if (Reads.compareValues(val, array[m]) < 0)
                b1 = m;
            else
                a1 = m + 1;
        }
        return a1;
    }
    
    public void sort(int[] array, int a, int b, double rSleep, double wSleep) {
        for (int i = a + 1; i < b; i++) {
            int j = expSearch(array, a, i, array[i], rSleep);
            if (j != i) {
                reversal(array, j, i - 1, wSleep, true, false);
                reversal(array, j, i, wSleep, true, false);
            }
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength, 1.0, 0.25);

    }

}
