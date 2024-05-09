package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Harumi
extending code by PCBoy and mariam-ts

+-------------------------+
| mariam-ts' Sort-O-Matic |
+-------------------------+

 */

/**
 * @author Harumi
 * @author PCBoy
 * @author mariam-ts
 *
 */
public class BrokenGnomePopSort extends Sort {

    public BrokenGnomePopSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Broken Gnome Pop");
        this.setRunAllSortsName("Broken Gnome Pop Sort");
        this.setRunSortName("Broken Gnome Popsort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public void brokenGnome(int[] array, int a, int b, boolean dir) {
        int cmp = dir ? 1 : -1;
        boolean sorted = false;
        while (!sorted) {
            sorted = true;
            for (int i = a + 1; i < b; i++) {
                int j = i;
                while (j > a && Reads.compareIndices(array, j - 1, j, 0.05, true) * cmp > 0) {
                    Writes.swap(array, i, j - 1, 0.05, true, false);
                    j--;
                    sorted = false;
                }
            }
        }
    }

    public void popSort(int[] array, int a, int b) {
        int quarter = (b - a) / 4, half = (b - a) / 2;
        brokenGnome(array, a, a + quarter, false);
        brokenGnome(array, a + quarter, a + half, true);
        brokenGnome(array, a + half, b - quarter, false);
        brokenGnome(array, b - quarter, b, true);
        brokenGnome(array, a, a + half, false);
        brokenGnome(array, a + half, b, true);
        brokenGnome(array, a, b, true);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        popSort(array, 0, sortLength);

    }

}
