package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

Coded for ArrayV by Ayako-chan
in collaboration with PCBoy

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Ayako-chan
 * @author PCBoy
 *
 */
public final class ExchangePapoSort extends BogoSorting {

    public ExchangePapoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Exchange Papo");
        this.setRunAllSortsName("Exchange Papo Sort");
        this.setRunSortName("Exchange Paposort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(8192);
        this.setBogoSort(true);
    }

    public void bogoCompSwap(int[] array, int a, int b) {
        if (BogoSorting.randBoolean()) {
            for (int i = a; i < b - 1; i++) {
                int j = BogoSorting.randInt(i, b);
                if (Reads.compareIndices(array, i, j, this.delay, true) > 0) {
                    Writes.swap(array, i, j, this.delay, true, false);
                }
            }
        } else {
            for (int i = b - 1; i > a; i--) {
                int j = BogoSorting.randInt(a, i);
                if (Reads.compareIndices(array, i, j, this.delay, true) < 0) {
                    Writes.swap(array, i, j, this.delay, true, false);
                }
            }
        }
    }

    public void sort(int[] array, int a, int b) {
        for (int i = a; i < b - 1; i++) {
            for (int j = b - 1; j > i; j--) {
                Highlights.markArray(3, i);
                Highlights.markArray(4, j);
                while (Reads.compareIndices(array, i, j, this.delay, true) > 0)
                    bogoCompSwap(array, i, j + 1);
            }
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);

    }

}
