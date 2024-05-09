package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Ported to ArrayV by Haruki
in collaboration with yuji

+-------------------------+
| yuji's Array Visualizer |
+-------------------------+

 */

/**
 * @author Haruki
 * @author yuji
 *
 */
public class PerfectHalverSelectionSort extends Sort {

    public PerfectHalverSelectionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Perfect Halver Selection");
        this.setRunAllSortsName("Perfect Halver Selection Sort");
        this.setRunSortName("Perfect Halver Selection Sort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    boolean compSwap(int[] array, int a, int b, double sleep) {
        if (Reads.compareIndices(array, a, b, sleep, true) > 0) {
            Writes.swap(array, a, b, sleep, false, false);
            return true;
        }
        return false;
    }

    public void sort(int[] array, int a, int b) {
        if (b - a < 4) {
            if (b - a == 2) compSwap(array, a, a + 1, 0.05);
            else if (b - a == 3) {
                compSwap(array, a, a + 1, 0.5);
                if (compSwap(array, b - 2, b - 1, 0.05))
                    compSwap(array, a, a + 1, 0.05);
            }
            return;
        }
        boolean anySwaps = true;
        int m = a + (b - a) / 2;
        while (anySwaps) {
            int minIdx = m, maxIdx = a;
            for (int i = a + 1; i < m; i++)
                if (Reads.compareIndices(array, i, maxIdx, 0.05, true) > 0)
                    maxIdx = i;
            for (int i = m + 1; i < b; i++)
                if (Reads.compareIndices(array, i, minIdx, 0.05, true) < 0)
                    minIdx = i;
            anySwaps = compSwap(array, maxIdx, minIdx, 0.05);
        }
        sort(array, a, m);
        sort(array, m, b);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);

    }

}
