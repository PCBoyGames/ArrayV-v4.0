package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

Coded for ArrayV by Flanlaina
in collaboration with gooflang

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Flanlaina
 * @author gooflang
 *
 */
public class RecursiveExchangeBomoSort extends BogoSorting {
    public RecursiveExchangeBomoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Recursive Exchange Bomo");
        this.setRunAllSortsName("Recursive Exchange Bomo Sort");
        this.setRunSortName("Recursive Exchange Bomosort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(4096);
        this.setBogoSort(true);
    }

    public void pull(int[] array, int a, int b) {
        if (a < b) {
            for (int i = a; i < b; i++) {
                if (Reads.compareIndices(array, i, i + 1, delay, true) > 0) {
                    Writes.swap(array, i, i + 1, delay, true, false);
                }
            }
        } else {
            for (int i = a; i > b; i--) {
                if (Reads.compareIndices(array, i, i - 1, delay, true) < 0) {
                    Writes.swap(array, i, i - 1, delay, true, false);
                }
            }
        }
    }

    public void recShuffle(int[] array, int a, int b, int d) {
        if (b - a < 2) return;
        Writes.recordDepth(d++);
        int r1 = randInt(a, b);
        int r2 = randInt(a, b);
        pull(array, r1, r2);
        int m = (b - a) / 2;
        Writes.recursion();
        recShuffle(array, a, a+m, d);
        Writes.recursion();
        recShuffle(array, a + m, b, d);
    }

    public void recBomo(int[] array, int a, int b) {
        while (!isRangeSorted(array, a, b)) recShuffle(array, a, b, 0);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        recBomo(array, 0, sortLength);
    }
}
