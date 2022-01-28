package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Ayako-chan
in collaboration with aphitorite

-----------------------------
- Sorting Algorithm Scarlet -
-----------------------------

 */

/**
 * @author Ayako-chan
 * @author aphitorite
 *
 */
public final class BingoMergeSort extends Sort {

    public BingoMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        setSortListName("Bingo Merge");
        setRunAllSortsName("Bingo Merge Sort");
        setRunSortName("Bingo Mergesort");
        setCategory("Merge Sorts");
        setComparisonBased(true);
        setBucketSort(false);
        setRadixSort(false);
        setUnreasonablySlow(false);
        setUnreasonableLimit(0);
        setBogoSort(false);
    }

    //New Bingo Sort
    public void bingoSort(int[] array, int a, int b) {
        int length = b - a;
        for (int j = length, k = 1;;) {
            for (int i = k; i < j; i++)
                if (Reads.compareIndices(array, a + i, a + k - 1, 0.01, true) >= 0)
                    Writes.swap(array, a + k++, a + i, 0.02, true, false);
            if (k == j)
                break;
            if (Reads.compareIndices(array, a, a + k - 1, 0.01, true) == 0)
                while (k > 0)
                    Writes.swap(array, a + --j, a + --k, 0.02, true, false);
            else {
                Writes.swap(array, a + --j, a + --k, 1, true, false);
                while (Reads.compareIndices(array, a + --k, a + j, 0.01, true) == 0)
                    Writes.swap(array, a + --j, a + k, 0.02, true, false);
            }
            if (k == 0)
                k = 1;
        }
    }

    public void mergeSort(int[] array, int a, int b) {
        if(b - a < 2)
            return;
        int m = a + (b - a) / 2;
        mergeSort(array, a, m);
        mergeSort(array, m, b);
        bingoSort(array, a, b);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        mergeSort(array, 0, sortLength);

    }

}
