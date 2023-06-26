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
public final class BidiBinaryQuasiPancakeSort extends Sort {

    public BidiBinaryQuasiPancakeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        setSortListName("Bidirectional Binary Quasi-Pancake");
        setRunAllSortsName("Bidirectional Binary Quasi-Pancake Sort");
        setRunSortName("Bidirectional Binary Quasi-Pancake Sort");
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
    
    public void insertSort(int[] array, int start, int end, double rSleep, double wSleep) {
        boolean invert = false;
        int l, r, m;
        for (int i = start + 1; i < end; i++) {
            if (invert ^ Reads.compareIndices(array, i - 1, i, rSleep, true) <= 0) {
                continue;
            }
            if (invert ^ Reads.compareIndices(array, start, i, rSleep, true) > 0) {
                Writes.reversal(array, start, i - 1, wSleep, true, false);
                invert = !invert;
                continue;
            }
            l = start + 1;
            r = i - 1;
            while (l < r) {
                m = l + (r - l) / 2;
                if (invert ^ Reads.compareIndices(array, m, i, rSleep, true) > 0) {
                    r = m;
                } else {
                    l = m + 1;
                }
            }
            if (l != i) {
                reversal(array, l, i - 1, wSleep, true, false);
                reversal(array, l, i, wSleep, true, false);
            }
        }
        if (invert) Writes.reversal(array, start, end - 1, wSleep, true, false);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        insertSort(array, 0, sortLength, 1.0, 0.25);

    }

}
