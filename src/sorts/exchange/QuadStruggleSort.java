package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Ayako-chan

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

A really cursed mix between Bubble and Quad Stooge.

 */

/**
 * @author Ayako-chan
 *
 */
public final class QuadStruggleSort extends Sort {

    public QuadStruggleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Quad Struggle");
        this.setRunAllSortsName("Quad Struggle Sort");
        this.setRunSortName("Quad Strugglesort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    boolean compSwap(int[] array, int a, int b) {
        if (Reads.compareIndices(array, a, b, 0.125, true) > 0) {
            Writes.swap(array, a, b, 0.125, false, false);
            return true;
        }
        return false;
    }
    
    public void sort(int[] array, int a, int b) {
        if (b - a < 2) return;
        boolean anySwaps = true;
        while (anySwaps) {
            anySwaps = false;
            if (b - a == 2)
                anySwaps |= compSwap(array, a, a + 1);
            else if (b - a == 3) {
                for (int i = a + 1; i < b; i++)
                    anySwaps |= compSwap(array, i - 1, i);
            } else {
                for (int i = a; i + 3 < b; i++) {
                    anySwaps |= compSwap(array, i, i + 1);
                    anySwaps |= compSwap(array, i + 2, i + 3);
                    anySwaps |= compSwap(array, i + 1, i + 2);
                    anySwaps |= compSwap(array, i, i + 1);
                    anySwaps |= compSwap(array, i + 2, i + 3);
                    anySwaps |= compSwap(array, i + 1, i + 2);
                }
            }
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);

    }

}
