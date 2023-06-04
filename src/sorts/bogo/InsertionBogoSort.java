package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

Ported to ArrayV by Ayako-chan
in collaboration with yuji

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Ayako-chan
 * @author yuji
 *
 */
public final class InsertionBogoSort extends BogoSorting {

    public InsertionBogoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Insertion Bogo");
        this.setRunAllSortsName("Insertion Bogo Sort");
        this.setRunSortName("Insertion Bogosort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(4096);
        this.setBogoSort(true);
    }

    public void sort(int[] array, int a, int b) {
        while (!this.isRangeSorted(array, a, b, false, true)) {
            boolean anyChange = false;
            while (!anyChange) {
                int i = BogoSorting.randInt(a, b);
                int t = array[i];
                int j = i - 1;
                while (j >= a && Reads.compareValues(array[j], t) > 0) {
                    Writes.write(array, j + 1, array[j], this.delay, true, false);
                    j--;
                }
                if (j + 1 < i) {
                    Writes.write(array, j + 1, t, this.delay, true, false);
                    anyChange = true;
                }
            }
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);

    }

}
